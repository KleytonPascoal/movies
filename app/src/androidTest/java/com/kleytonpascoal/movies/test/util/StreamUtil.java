package com.kleytonpascoal.movies.test.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kleyton on 22/05/17.
 */

public class StreamUtil {

    public static String getStringFromStream(InputStream inputStream) throws IOException {
        final BufferedInputStream bufferInputStream = new BufferedInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int result = bufferInputStream.read();
        while (result != -1) {
            outputStream.write((byte) result);
            result = bufferInputStream.read();
        }
        return outputStream.toString("UTF-8");
    }
}
