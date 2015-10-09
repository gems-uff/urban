/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.models.LineBoundingBox;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schettino
 */
public class Constants {

    public static final int INDEX_TIME = 0;
    public static final int INDEX_BUS_NUMBER = 1;
    public static final int INDEX_LINE = 2;
    public static final int INDEX_LATITUDE = 3;
    public static final int INDEX_LONGITUDE = 4;
    public static final int INDEX_SPEED = 5;

    public static final String KEY_TIME = "time";
    public static final String KEY_BUS_NUMBER = "bus_number";
    public static final String KEY_LINE = "line";
    public static final String KEY_POSITION = "position";
    public static final String KEY_SPEED = "speed";

    public static final String[] COLUMNS = {"DATAHORA", "ORDEM", "LINHA",
        "LATITUDE", "LONGITUDE", "VELOCIDADE"};

    public static final int STATUS_STARTED = 1;
    public static final int STATUS_FINISHED_SUCCESSFULLY = 2;
    public static final int STATUS_FINISHED_WITH_ERRORS = 3;

    public static final String MSG_ERROR_COLUMNS = "'File columns in invalid format'";

    public static final String KEY_COLUMNS = "COLUMNS";
    public static final String KEY_DATA = "DATA";

    public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String JSON_DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";

    public static List<LineBoundingBox> getGarageList() {
        ArrayList<LineBoundingBox> list = new ArrayList<LineBoundingBox>();
        
        list.add(new LineBoundingBox(-22.9544153295,-22.9482408665, -43.351812174, -43.3470113616));
        list.add(new LineBoundingBox(-22.9589005678,-22.9566382548,-43.3483789465, -43.343578134));
        list.add(new LineBoundingBox(-22.9312269185,-22.9283512713,-43.2558105489, -43.2529623847));
        list.add(new LineBoundingBox(-22.9216221766,-22.9195568336,-43.2580206892, -43.2555158476));
        list.add(new LineBoundingBox(-22.9069174524,-22.9052274349,-43.2617114088, -43.2601507048));
        list.add(new LineBoundingBox(-22.9035078632,-22.901165516,-43.1976817152, -43.1958957057));
        list.add(new LineBoundingBox(-22.8762967294,-22.8734409429,-43.2429359457, -43.2400663237));
        list.add(new LineBoundingBox(-22.871749519,-22.8696746832,-43.249984791, -43.2488210539));
        list.add(new LineBoundingBox(-22.8686776068,-22.8677148965, -43.2574306032, -43.2568945031));
        list.add(new LineBoundingBox(-22.8583221216,-22.8571714971,-43.2454786798, -43.24397162));
        list.add(new LineBoundingBox(-22.8591031356,-22.8582540498,-43.2454250356, -43.2446689944));
        list.add(new LineBoundingBox(-22.8165646907,-22.8139970868,-43.1893132231, -43.1871624331));
        list.add(new LineBoundingBox(-22.9029659491,-22.9017123839,-43.2997558614, -43.2973583083));
        list.add(new LineBoundingBox(-22.9034304508, -22.9021768898, -43.3153985044,-43.3127220015));
        list.add(new LineBoundingBox(-22.8900580933, -22.888122413,-43.2937369844,-43.291028295));
        list.add(new LineBoundingBox(-22.8688451553,-22.8677697779,-43.2938657305,-43.2905776839));
        list.add(new LineBoundingBox(-22.8377813149,-22.8360629797,-43.2867095968,-43.2845802645));
        list.add(new LineBoundingBox(-22.8180043282,-22.8162264073,-43.3026848337,-43.3009310107));
        list.add(new LineBoundingBox(-22.8053552793,-22.8039236716,-43.3116541406,-43.308891807));
        list.add(new LineBoundingBox(-22.8114773271,-22.8086014771, -43.3442805311,-43.3411748747));
        list.add(new LineBoundingBox(-22.8770335404,-22.87608977,-43.3310304186,-43.3292551379));
        list.add(new LineBoundingBox(-22.8812741646,-22.8797965303,-43.359011223,-43.3564849238));
        list.add(new LineBoundingBox(-22.8770434433,-22.8759859596,-43.3691982528,-43.367793117));
        list.add(new LineBoundingBox(-22.8672420992,-22.8654778954,-43.3574930927,-43.3548219542));
        list.add(new LineBoundingBox(-22.8610436099,-22.8599912436,-43.3569378754,-43.354639564));
        list.add(new LineBoundingBox(-22.8606481604,-22.8567880957,-43.3717329404,-43.370217834));
        list.add(new LineBoundingBox(-22.8122137982,-22.8103967885,-43.3734280965,-43.3716930489));
        list.add(new LineBoundingBox(-22.8161102888,-22.8144762238,-43.3874184987,-43.3853079419));
        list.add(new LineBoundingBox(-22.8184491137,-22.8153365908,-43.3954651257,-43.39223877));
        list.add(new LineBoundingBox(-22.8965008221,-22.8941431832,-43.4753413102,-43.4732736687));
        list.add(new LineBoundingBox(-22.8852526286,-22.8827965529,-43.4967775246,-43.493937407));
        list.add(new LineBoundingBox(-22.8977856414,-22.8919292521,-43.5336203477,-43.5277546982));
        list.add(new LineBoundingBox(-22.9216856411,-22.9156048344,-43.6094517609,-43.606890593));
        list.add(new LineBoundingBox(-22.9210729658,-22.9183718076,-43.6463374993,-43.6427249055));

        return list;
    }

}
