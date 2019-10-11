package util;

import model.InputValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchManager {
    private InputValidator inputValidator;
    private ArrayList<File> requiredFiles;
    private HashMap<File, ArrayList<Integer>> entries = new HashMap<>();

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        requiredFiles = new ArrayList<>();
    }

    public ArrayList<File> search(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(inputValidator.getFileMask())) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                    String everything = readFile(br);
                    if (everything.contains(inputValidator.getSearchText())) {
                        requiredFiles.add(file);
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        int i = everything.indexOf(inputValidator.getSearchText());
                        arrayList.add(i);
                        int j = everything.indexOf(inputValidator.getSearchText(), i + 1);

                        while (!(j == -1)) {
                            arrayList.add(j);
                            j = everything.indexOf(inputValidator.getSearchText(), j + 1);
                        }
                        entries.put(file, arrayList);
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

    public static String readFile(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb.toString();
    }

    public ArrayList<Integer> getEntries(File file) {
        return entries.get(file);
    }
}
