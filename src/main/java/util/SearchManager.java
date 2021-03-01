package util;

import model.InputValidator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchManager {
    private final InputValidator inputValidator;
    private static final Logger log = Logger.getLogger(SearchManager.class.getName());

    public SearchManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    public List<File> searchFiles(File file) {
        final String extension = inputValidator.getFileMask();
        List<File> requiredFiles = null;
        try (Stream<Path> walk = Files.walk(file.toPath())) {
            requiredFiles = walk
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith("." + extension))
                    .map(Path::toFile)
                    .filter(f -> searchNeededText(readFile(f)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return requiredFiles;
    }

    public String readFile(File file) {
        String content = "";

        try (Stream<String> lines = Files.lines(file.toPath(), Charset.defaultCharset())) {
            content = lines
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return content;
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

    private boolean searchNeededText(String content) {
        return content.contains(inputValidator.getSearchText().toLowerCase());
    }
}
