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
    private SearchManager sm;
    private File currentFile;
    private Map<String, File> openedFiles = new HashMap<>();
    private Map<Tab, TextArea> openedTextArea = new HashMap<>();
    private static Logger log = Logger.getLogger(Controller.class.getName());
    private InputValidator inputValidator;


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
            sm = new SearchManager(inputValidator);
            ArrayList<File> files = sm.search(inputValidator.getFolder());
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
                            StringBuilder sb = new StringBuilder();
                            String line = br.readLine();

                            while (line != null) {
                                sb.append(line);
                                sb.append(System.lineSeparator());
                                line = br.readLine();
                            }
                            String everything = sb.toString();
                            textArea.appendText(everything);
                        } catch (IOException io) {
                            log.info("Unable to read file");
                        }

                        tab.setContent(textArea);
                        openedTextArea.put(tab, textArea);
                        tabPane.getTabs().add(tab);
                        tabPane.getSelectionModel().select(tab);
                        TabController tabController = new TabController();
                        tabController.disableButtons(selectPrevButton, selectNextButton, selectAllButton);
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
                System.out.println(currentFile.getAbsoluteFile());
                log.info("File: " + currentFile.getAbsoluteFile() + " was selected.");
            }
        }
    }

    @FXML
    public void selectPrev() {
        int currentIndex = textArea.getCaretPosition();
        ArrayList<Integer> list = sm.getEntries2(currentFile);
        for (Integer index : list) {
            int infelicity = index - 3;
            if (infelicity < currentIndex) {
                textArea.positionCaret(infelicity);
            }
        }
    }

    @FXML
    public void selectNext() {
        int currentIndex = textArea.getCaretPosition();
        ArrayList<Integer> list = sm.getEntries2(currentFile);
        for (Integer index : list) {
            int infelicity = index - 3;
            if (infelicity > currentIndex) {
                textArea.positionCaret(infelicity);
                break;
            }
        }
    }

    @FXML
    public void selectAll() {
        textArea.selectAll();
    }
}
