package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import main.java.model.InputValidator;
import main.java.util.SearchManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private Button selectPrevButton;

    @FXML
    private Button selectNextButton;

    @FXML Button selectAllButton;

    private TreeController treeController;

   private TextArea textArea;

    private SearchManager sm;
    private InputValidator inputValidator;
    private File currentFile;



    @FXML
    void initialize() {
        Thread.currentThread().setPriority(MAX_PRIORITY);
    }


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
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeController = new TreeController(inputValidator, fileTree);
        sm = new SearchManager(inputValidator);

        ArrayList<File> files = sm.search(inputValidator.getFolder());
        treeController.findFilesInFolder(files);



    }

    @FXML
    public void mouseClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<String> item = fileTree.getSelectionModel().getSelectedItem();
            currentFile = treeController.getFile(item);
            if (currentFile.isFile()) {

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
                }

                tab.setContent(textArea);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                TabController tabController = new TabController();
                tabController.disableButtons(selectPrevButton, selectNextButton, selectAllButton);

            }
        }
    }

    @FXML
    public void selectPrev(){
        textArea.positionCaret(10);
        /*LinkedList li = sm.getEntries(currentFile);
        System.out.println(li);*/
    }

    /*
    public void selectNext2(){
        LinkedList li = sm.getEntries(currentFile);
        int i = (int) li.getFirst();
        textArea.positionCaret(i);

        System.out.println(li);
    }
    */

    @FXML
    public void selectNext(){
        int currentIndex = textArea.getCaretPosition();
        ArrayList<Integer> list = sm.getEntries2(currentFile);

        for (Integer index: list){
            if (index-3 > currentIndex){
                int i = index - 3;
                textArea.positionCaret(i);
                System.out.println("I set: " + i);
                break;
            }
        }
    }





}
