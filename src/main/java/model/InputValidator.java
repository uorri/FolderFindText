package model;

import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class InputValidator {
    private String fileMask;
    private String searchText;
    private File folder;

    public void setFileMask(TextField fileMask) throws IOException {
        if (fileMask.getText().isEmpty())
            throw new IOException("Filemask is wrong!");
        else
            this.fileMask = fileMask.getText();
    }

    public String getFileMask() {
        return fileMask;
    }

    public void setSearchText(TextField searchText) throws IOException {
        if (searchText.getText().isEmpty())
            throw new IOException("Search text is empty!");
        else
            this.searchText = searchText.getText();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setFolder(TextField filePath) throws IOException {
        File tFolder = new File(filePath.getText());
        if (!tFolder.isDirectory() || (!tFolder.exists()))
            throw new IOException("Filepath is wrong!");
        else
            this.folder = tFolder;
    }

    public File getFolder() {
        return folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputValidator that = (InputValidator) o;
        return Objects.equals(fileMask, that.fileMask) &&
                Objects.equals(searchText, that.searchText) &&
                Objects.equals(folder, that.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileMask, searchText, folder);
    }
}
