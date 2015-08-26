package corrector;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pedro
 */
public class Corrector {

    private File pastaRotas;
    private Pattern patternPontosRota = Pattern.compile("(-\\d{2}\\.?\\d*)\",\"(-\\d{2}\\.?\\d*)");
    private Pattern patternNomeRota = Pattern.compile("(?<=linha)(\\w+)(?=-)");
    private FileFilter filtroRota = (file -> {
        return file.isFile() && file.getName().endsWith(".csv");
    });
    private Map<String, LineString> outputRotas = new HashMap<>();
    private Map<String, BoundingBox> linhasParaCaixas = new HashMap<>();

    public Corrector(File pastaRotasE) {
        this.pastaRotas = pastaRotasE;
    }

    /*
     Monta as rotas na variável outputRotas.
     */
    public void processaRotas() {

        /*
         Abre pasta (pastaRotas) de rotas e lê uma a uma
         */
        Arrays.asList(pastaRotas.listFiles(filtroRota)).parallelStream().forEach(arqRota -> {
            Matcher matcher = patternNomeRota.matcher(arqRota.getName());
            if (matcher.find()) {
                String nome = matcher.group(1);
                try (FastFileReader ffr = new FastFileReader(arqRota)) {
                    /*
                     Lê o arquivo e cria a rota
                     */
                    String pontosDaRota = ffr.readLines().toString();
                    List<GeodesicSample> listaDePontos = new ArrayList<>();
                    matcher = patternPontosRota.matcher(pontosDaRota);
                    while (matcher.find()) {
                        listaDePontos.add(new GeodesicSample(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), 0));
                    }
                    LineString rota = new SimpleLineString(listaDePontos);
                    outputRotas.put(nome, rota);
                    /* 
                     Gera bounding box que engloba todos os pontos da rota
                     */
                    ResponsiveBoundingBox box = new ResponsiveBoundingBox(rota.segmentsOf().stream().findAny().get().begin());
                    box.addAll(rota);
                    linhasParaCaixas.put(nome, box);

                } catch (FileNotFoundException err) {
                    System.err.println("Arquivo não encontrado.");
                } catch (IOException err) {
                    System.err.println(err.getMessage());
                }
            }
        });

    }

    /*
     Projeta os pontos nas suas rotas se estiverem perto o suficiente.
     Argumentos: routeError - > distância aceitável para fazer a projeção.
     boxError - > distância aceitável para ser considerado dentro do Rio de Janeiro.
     Sobrescreve a amostra passada como argumento, projetando-a na rota.
     */
    public void alinharDados(double routeError, double boxError, List<Object> sample) {

        GeodesicCalculator calc = GeodesicCalculator.CALCULATOR;
        if (!sample.get(1).toString().isEmpty() && !sample.get(2).toString().isEmpty()) {
            GeodesicSample ping = new GeodesicSample((Double) sample.get(3), (Double) sample.get(4), (Double) sample.get(5));
            String nomeDaRota;
            if (sample.get(2) instanceof Double) {
                nomeDaRota = String.format(Locale.ENGLISH, "%d", (int) Math.round((Double) sample.get(2)));
            } else {
                nomeDaRota = sample.get(2).toString();
            }

            LineString rota = outputRotas.get(nomeDaRota);
            BoundingBox box = linhasParaCaixas.get(nomeDaRota);
            if (box != null && calc.closeEnough(box, ping, boxError)) {
                if (calc.crossTrackDistance(rota, ping) < routeError) {
                    ping = calc.projectionOn(rota, ping);
                    sample.set(3, Math.toDegrees(ping.lat()));
                    sample.set(4, Math.toDegrees(ping.lng()));
                }
            }

        }

    }

}
