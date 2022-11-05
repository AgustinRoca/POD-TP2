package ar.edu.itba.client.utils;

import java.nio.file.Paths;

public abstract class FileUtils {
    public static String formatFilePath(String path, String filename, String extension) {
        return Paths.get(path, filename + "." + extension).toString();
    }
}
