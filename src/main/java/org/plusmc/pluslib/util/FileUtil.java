package org.plusmc.pluslib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * A utility class for file operations.
 */
@SuppressWarnings("unused")
public class FileUtil {

    /**
     * Reads the data from a zip file.
     *
     * @param file     The zip file.
     * @param fileName The name of the file in the zip file.
     * @return The data.
     */
    public static byte[] readZip(File file, String fileName) {
        byte[] result;
        try {
            ZipFile zipFile = new ZipFile(file);
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(fileName));
            result = inputStream.readAllBytes();
            inputStream.close();
            zipFile.close();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * Writes the data to a zip file.
     *
     * @param file     The zip file.
     * @param fileName The name of the file in the zip file.
     * @param data     The data.
     */
    public static void writeZip(File file, String fileName, byte[] data) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            zipOutputStream.write(data);
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the data from a file.
     *
     * @param file The file.
     * @return The data.
     */
    public static byte[] readData(File file) {
        byte[] result;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            result = fileInputStream.readAllBytes();
            fileInputStream.close();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * Writes the data to a file.
     *
     * @param file The file.
     * @param data The data.
     */
    public static void writeData(File file, byte[] data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception ignored) {
        }
    }
}