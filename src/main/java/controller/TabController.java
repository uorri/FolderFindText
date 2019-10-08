package main.java.controller;

import javafx.scene.control.Button;

class TabController {

    void disableButtons(Button prev, Button next, Button all) {
        prev.disableProperty().setValue(false);
        next.disableProperty().setValue(false);
        all.disableProperty().setValue(false);
    }
}