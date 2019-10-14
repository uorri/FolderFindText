package util;

import model.InputValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchManager {
    private InputValidator inputValidator;
    private ArrayList<File> requiredFiles;

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        requiredFiles = new ArrayList<>();
    }

    public ArrayList<File> search(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(inputValidator.getFileMask())) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                    String everything = readFile(br);
                    String searchText = inputValidator.getSearchText().toLowerCase();
                    if (everything.contains(searchText)) {
                        requiredFiles.add(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            File[] folderEntries = file.listFiles();
            if (folderEntries != null) {
                for (File file1 : folderEntries) {
                    search(file1);
                }
            }
        }
        return requiredFiles;
    }

    public String readFile(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb.toString();
    }

    public List<Integer> getEntries(String text) {
        List<Integer> entries = new ArrayList<>();
        String searchText = inputValidator.getSearchText().toLowerCase();
        int i = text.toLowerCase().indexOf(searchText);
        entries.add(i);
        int j = text.toLowerCase().indexOf(searchText, i + 1);

        while (!(j == -1)) {
            entries.add(j);
            j = text.indexOf(searchText, j + 1);
        }

        return entries;
    }
}
