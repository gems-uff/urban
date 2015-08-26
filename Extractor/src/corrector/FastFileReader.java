package corrector;


import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FastFileReader implements Closeable {
    private RandomAccessFile in;
    private FileChannel inChannel;

    public FastFileReader(File target) throws FileNotFoundException {
        in = new RandomAccessFile(target, "r");
        inChannel = in.getChannel();
    }
    public FastFileReader(String target) throws FileNotFoundException {
        in = new RandomAccessFile(target, "r");
        inChannel = in.getChannel();
    }

    public StringBuilder readLines() throws IOException {
        StringBuilder fileBuffer = new StringBuilder();
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        buffer.load();
        fileBuffer.append(StandardCharsets.UTF_8.decode(buffer));
        return fileBuffer;
    }

    @Override
    public void close() throws IOException {
        this.inChannel.close();
        this.in.close();
    }
}
