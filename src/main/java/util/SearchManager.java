package util;

import model.InputValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchManager {
    private InputValidator inputValidator;
    private ArrayList<File> requiredFiles;
    private static Logger log = Logger.getLogger(SearchManager.class.getName());

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        requiredFiles = new ArrayList<>();
    }

    public List<File> searchFiles(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(inputValidator.getFileMask())) {
                lookForNeededText(file);
            }
        } else {
            recursivelySearchFiles(file);
        }
        return requiredFiles;
    }

    private void lookForNeededText(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String everything = readFile(br);
            String searchText = inputValidator.getSearchText().toLowerCase();
            if (everything.contains(searchText)) {
                requiredFiles.add(file);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private void recursivelySearchFiles(File file) {
        File[] folderEntries = file.listFiles();
        if (folderEntries != null) {
            for (File f : folderEntries) {
                searchFiles(f);
            }
        }
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

        while (j != -1) {
            entries.add(j);
            j = text.indexOf(searchText, j + 1);
        }

        return entries;
    }
}
