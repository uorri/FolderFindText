package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import model.InputValidator;
import util.SearchManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Controller {

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
    private Button selectPrevButton;

    @FXML
    private Button selectNextButton;

    @FXML
    Button selectAllButton;

    private TreeController treeController;
    private TextArea textArea;
    private File currentFile;
    private Map<String, File> openedFiles = new HashMap<>();
    private Map<Tab, TextArea> openedTextArea = new HashMap<>();
    private static Logger log = Logger.getLogger(Controller.class.getName());
    private InputValidator inputValidator;
    private SearchManager searchManager;


    @FXML
    void openFolder() {
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
        inputValidator = new InputValidator();
        try {
            inputValidator.setFileMask(fileMask_field);
            inputValidator.setSearchText(searchText_field);
            inputValidator.setFolder(filePath_field);
            treeController = new TreeController(inputValidator, fileTree);
            searchManager = new SearchManager(inputValidator);
            ArrayList<File> files = searchManager.search(inputValidator.getFolder());
            files.forEach(file -> treeController.createTree(file, null));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<String> item = fileTree.getSelectionModel().getSelectedItem();
            if (item != null) {
                if (!item.getValue().equals(inputValidator.getFolder().getName())) {
                    currentFile = treeController.getFile(item);
                    if (currentFile != null && currentFile.isFile()) {
                        openedFiles.put(currentFile.getName(), currentFile);
                        log.info("File: " + currentFile.getAbsoluteFile() + " was opened.");
                        final Tab tab = new Tab(item.getValue());
                        textArea = new TextArea();

                        try (BufferedReader br = new BufferedReader(new FileReader(currentFile.getAbsoluteFile()))) {
                            String everything = searchManager.readFile(br);
                            textArea.appendText(everything);
                        } catch (IOException io) {
                            log.info("Unable to read file");
                        }

                        tab.setContent(textArea);
                        openedTextArea.put(tab, textArea);
                        tabPane.getTabs().add(tab);
                        tabPane.getSelectionModel().select(tab);
                        TabController.activeButtons(selectPrevButton, selectNextButton, selectAllButton);
                    }
                }
            }
        }
    }

    @FXML
    public void mouseClickTab(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            Tab selected = tabPane.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textArea = openedTextArea.get(selected);
                currentFile = openedFiles.get(selected.getText());
                log.info("File: " + currentFile.getAbsoluteFile() + " was selected.");
            }
        }
    }

    @FXML
    public void selectPrev() {
        int currentIndex = textArea.getCaretPosition();
        String text = textArea.getText();
        List<Integer> entries = searchManager.getEntries(text);
        entries.stream().filter(x -> x < currentIndex).forEach(x -> textArea.positionCaret(x));
    }

    @FXML
    public void selectNext() {
        int currentIndex = textArea.getCaretPosition();
        String text = textArea.getText();
        List<Integer> entries = searchManager.getEntries(text);

        for (Integer index : entries) {
            if (index > currentIndex) {
                textArea.positionCaret(index);
                break;
            }
        }
    }

    @FXML
    public void selectAll() {
        textArea.selectAll();
    }
}
