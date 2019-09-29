package main.java;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField fileMask_field;

    @FXML
    private TextField searchText_field;

    @FXML
    private Button searchButton;

    @FXML
    private TextField filePath_field;

    @FXML
    private TreeView<?> fileTree;

    @FXML
    private TabPane tabPane;

    @FXML
    void initialize() {
        searchButton.setOnAction(event -> {
            String filePath = filePath_field.getText().trim();
            String fileMask = fileMask_field.getText().trim();
            String searchText= searchText_field.getText().trim();

            if (!filePath.equals("") || !fileMask.equals(""))
                 search(filePath, fileMask, searchText);
            else
                System.out.println("Error!");
        });
    }

    private void search(String filePath, String fileMask, String searchText) {
    }

    @FXML
    void openFolder(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Chose file folder");
        File currentFolder = new File(filePath_field.getText());
        if (currentFolder.exists())
            chooser.setInitialDirectory(currentFolder);
        File newFolder = chooser.showDialog(null);
        if (newFolder != null)
            filePath_field.setText(newFolder.toString());
    }
}
