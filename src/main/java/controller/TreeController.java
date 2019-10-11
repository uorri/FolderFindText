package controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.InputValidator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

class TreeController {
    private Map<TreeItem<String>, File> files = new HashMap<>();
    private Map<File, TreeItem<String>> folders = new HashMap<>();


    TreeController(InputValidator inputValidator, TreeView<String> fileTree) {
        TreeItem<String> root = new TreeItem<>(inputValidator.getFolder().getName());
        fileTree.setRoot(root);
        folders.put(inputValidator.getFolder(), fileTree.getRoot());
    }

    void createTree(File file, TreeItem<String> childItem){
        TreeItem<String> currentItem = new TreeItem<>(file.getName());
        TreeItem<String> parentItem;
        File parentFile = file.getParentFile();
        if (childItem != null){
            currentItem.getChildren().add(childItem);
        }
        if (folders.containsKey(parentFile)){
            parentItem = folders.get(parentFile);
            parentItem.getChildren().add(currentItem);
        } else {
            createTree(parentFile, currentItem);
        }
        if (file.isFile()) files.put(currentItem, file);
        else folders.put(file, currentItem);
    }

    File getFile(TreeItem<String> item) {
        return files.get(item);
    }
}

