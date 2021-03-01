package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import model.InputValidator;
import util.SearchManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class Controller {

    @FXML
    private TextField fileMaskField;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField filePathField;

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
    private final Map<String, File> openedFiles = new HashMap<>();
    private final Map<Tab, TextArea> openedTextArea = new HashMap<>();
    private static final Logger log = Logger.getLogger(Controller.class.getName());
    private InputValidator inputValidator;
    private SearchManager searchManager;


    @FXML
    void openFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose file folder");
        File currentFolder = new File(filePathField.getText());
        if (currentFolder.exists()) {
            chooser.setInitialDirectory(currentFolder);
        }
        File newFolder = chooser.showDialog(null);
        if (newFolder != null) {
            filePathField.setText(newFolder.toString());
        }
    }

    @FXML
    void search() {
        inputValidator = new InputValidator();
        try {
            inputValidator.setFileMask(fileMaskField);
            inputValidator.setSearchText(searchTextField);
            inputValidator.setFolder(filePathField);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        treeController = new TreeController(inputValidator, fileTree);
        searchManager = new SearchManager(inputValidator);
        List<File> files = searchManager.searchFiles(inputValidator.getFolder());
        files.forEach(file -> treeController.createTree(file, null));
    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<String> item = fileTree.getSelectionModel().getSelectedItem();
            if (item != null && !item.getValue().equals(inputValidator.getFolder().getName())) {
                currentFile = treeController.getFile(item);
                if (currentFile != null && currentFile.isFile()) {
                    openedFiles.put(currentFile.getName(), currentFile);
                    log.info("File: " + currentFile.getAbsoluteFile() + " was opened.");
                    displayFileContent(item);
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

        Optional<Integer> first = entries.stream().filter(x -> x > currentIndex).findFirst();
        first.ifPresent(i -> textArea.positionCaret(i));
    }

    @FXML
    public void selectAll() {
        textArea.selectAll();
    }

    private void displayFileContent(TreeItem<String> item) {
        final Tab tab = new Tab(item.getValue());
        textArea = new TextArea();
        appendText();
        tab.setContent(textArea);
        openedTextArea.put(tab, textArea);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        TabController.activeButtons(selectPrevButton, selectNextButton, selectAllButton);
    }

    private void appendText() {
        String everything = searchManager.readFile(currentFile);
        textArea.appendText(everything);
    }
}
