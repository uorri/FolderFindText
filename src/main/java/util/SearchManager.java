package main.java.util;

import main.java.model.InputValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SearchManager {
    private InputValidator inputValidator;
    private ArrayList<File> requiredFiles;

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        requiredFiles = new ArrayList<>();
    }

    public ArrayList<File> search(File file) {
        if (file.isFile()) {
            try {
                if (file.getName().endsWith(inputValidator.getFileMask())) {
                    BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                    while (reader.read() != -1) {
                        String line = reader.readLine();
                        if (line.contains(inputValidator.getSearchText())) {
                            requiredFiles.add(file);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] folderEntries = file.listFiles();
            if (folderEntries != null) {
                for (File file1 : folderEntries) {
                    search(file1);
                }
            } else System.out.println("Not found");
        }
        return requiredFiles;
    }
}
