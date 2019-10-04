package main.java.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import main.java.model.InputValidator;

import java.io.File;

class TreeController {
    private TreeItem<String> root;
    private File[] folderEntries;

    TreeController(InputValidator inputValidator, TreeView<String> fileTree) {
        root = new TreeItem<>(inputValidator.getFolder().getName());
        fileTree.setRoot(root);
        folderEntries = inputValidator.getFolder().listFiles();
    }

    void findFilesInFolder(){
        findUsingRecursion(root, folderEntries);
    }

    private void findUsingRecursion(TreeItem<String> root, File[] folderEntries){
        for (File entry : folderEntries){
            TreeItem<String> item = new TreeItem<>(entry.getName());
            if (entry.isDirectory()){
                File[] files = entry.listFiles();
                if (files != null && files.length != 0) {
                    root.getChildren().add(item);
                    findUsingRecursion(item, files);
                }
            }
            else
                root.getChildren().add(item);
        }
    }


}
