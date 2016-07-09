package test.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import environment.extension.scene.controller.Controller;


public class DashboardController extends Controller {

    @FXML
    protected Button test;

    public void doSomething() {
        this.switchController((Controller) this.get("controllers.help_me"));
    }
}
