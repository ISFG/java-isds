package cz.abclinuxu.datoveschranky.common;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Utils {

    private static final int BUFFER_SIZE = 4 * 4096;

    public static String readResourceAsString(Class<?> clazz, String resourceFile) {
        try {
            InputStream is = clazz.getResourceAsStream(resourceFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } catch (IOException ioe) {
            String message = String.format("IO chyba pri cteni zdroje %s.", resourceFile);
            throw new DataBoxException(message);
        }
    }

    public static void close(Closeable... closeUs) {
        IOException lastException = null;
        for (Closeable closeMe : closeUs) {
            try {
                closeMe.close();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        if (lastException != null) {
            throw new RuntimeException("Chyba pri zavirani.", lastException);
        }
    }

    public static void copy(InputStream source, OutputStream dest) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        int read;
        while ((read = source.read(bytes)) != -1) {
            dest.write(bytes, 0, read);
        }
    }
}
