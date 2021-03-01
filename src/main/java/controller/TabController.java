package controller;

import javafx.scene.control.Button;

class TabController {

    private TabController(){}

    static void activeButtons(Button prev, Button next, Button all) {
        prev.disableProperty().setValue(false);
        next.disableProperty().setValue(false);
        all.disableProperty().setValue(false);
    }
}