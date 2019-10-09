package main.java.util;

import main.java.model.InputValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SearchManager {
    private InputValidator inputValidator;
    private ArrayList<File> requiredFiles;
    private ConcurrentHashMap<File, Integer[]> indexLine = new ConcurrentHashMap<>();
    private HashMap<File, ArrayList<Integer>> newMap = new HashMap<>();
    private HashMap<File, String> mapMap = new HashMap<>();

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        requiredFiles = new ArrayList<>();
    }

    public ArrayList<File> search(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(inputValidator.getFileMask())) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();


                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();

                    }
                    String everything = sb.toString();
                    mapMap.put(file, everything);

                    if (everything.contains(inputValidator.getSearchText())) {
                        requiredFiles.add(file);
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        int i = everything.indexOf(inputValidator.getSearchText());
                        arrayList.add(i);
                        int j = everything.indexOf(inputValidator.getSearchText(), i+1);

                        while (!(j == -1)) {
                            arrayList.add(j);

                            System.out.println(j);
                            j = everything.indexOf(inputValidator.getSearchText(), j+1);
                        }
                        newMap.put(file, arrayList);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Not found");
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

    public ArrayList<Integer> getEntries2(File file) {
        return newMap.get(file);
    }

    public String getText(File file){
        return mapMap.get(file);
    }
}
