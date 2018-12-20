package hiberspring.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtilImpl implements FileUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        BufferedReader reader
                = new BufferedReader(new FileReader(filePath));

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
