package main.java.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import main.java.model.InputValidator;

import java.io.File;
import java.util.ArrayList;

class TreeController {
    private TreeItem<String> root;

    TreeController(InputValidator inputValidator, TreeView<String> fileTree) {
        root = new TreeItem<>(inputValidator.getFolder().getName());
        fileTree.setRoot(root);
    }

    void findFilesInFolder(ArrayList<File> files) {
        //files.forEach(this::addFile);
        for (File file : files) {
            TreeItem<String> item = new TreeItem<>(file.getName());
            findFilesInFolderWithRecursion(item, file);
        }
    }

    private void findFilesInFolderWithRecursion(TreeItem<String> current, File file) {
        TreeItem<String> parent = new TreeItem<>(file.getParentFile().getName());
        parent.getChildren().add(current);
        if (root.toString().equals(parent.toString())) {
            root.getChildren().add(current);
        } else {
            findFilesInFolderWithRecursion(parent, file.getParentFile());
        }
    }

}
