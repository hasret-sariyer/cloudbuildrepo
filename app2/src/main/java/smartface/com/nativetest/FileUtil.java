package smartface.com.nativetest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by smartface on 26.07.2018.
 */

public class FileUtil {

    public static void copyStream(InputStream in, OutputStream out) throws IOException{
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }
}
