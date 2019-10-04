package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import main.java.model.InputValidator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.MAX_PRIORITY;

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
    private TextField filePath_field;

    @FXML
    private TreeView<String> fileTree;

    @FXML
    private TabPane tabPane;

    @FXML
    void initialize() {
        Thread.currentThread().setPriority(MAX_PRIORITY);
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

    @FXML
    void search() {
        InputValidator inputValidator = new InputValidator();
        try {
            inputValidator.setFileMask(fileMask_field);
            inputValidator.setSearchText(searchText_field);
            inputValidator.setFolder(filePath_field);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        String[] files = inputValidator.getFolder().list(new ExtensionFilter(inputValidator.getFileMask()));
        for (String file : files) {
            System.out.println(file);
        }
        */

        TreeController treeController = new TreeController(inputValidator, fileTree);
        treeController.findFilesInFolder();
    }
}
