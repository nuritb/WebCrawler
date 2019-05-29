package utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileUtils {

    public static final String OUTPUT_FILE_NAME = "output.txt";

    public static void writeToOutputFile(String url, int depth, double ratio) {
        String result = String.join("\t", url, String.valueOf(depth), String.valueOf(ratio));
        System.out.println(result);
        writeToFile(result, OUTPUT_FILE_NAME, true);
    }

    public static void writeToFile(String content, String fileName, boolean append) {
        try {
            String validFileName = replaceInvalidCharacters(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(new java.io.FileWriter(validFileName, append));
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("Failed to write to file " + fileName);
        }
    }

    //TODO: replace each invalid character with unique value to avoid file names collisions
    private static String replaceInvalidCharacters(String url) {
        return url.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}
