package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class StreamUtils {
    private static final int BUFFER_SIZE = 1024;

    public static String toString(InputStream inputStream) throws IOException {
        final char[] buffer = new char[BUFFER_SIZE];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
