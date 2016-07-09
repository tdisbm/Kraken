package environment.extension.scene.controller;

import com.fasterxml.jackson.databind.JsonNode;
import environment.unit.Container;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;


public abstract class Controller extends VBox implements Initializable {
    private Container container = new Container();

    private ControllerSwitcher switcher = null;

    private String template = null;

    private JsonNode options;

    @FXML
    private Button switch_controller;


    final public void setTemplate(String template) {
        this.template = template;
    }

    final public Object get(String definition) {
        return this.container.get(definition);
    }

    final public Object get(String ext, String ...definitions) {
        if (definitions == null) {
            return this.container.getByExtensionRoot(ext);
        }

        Map<String, Object> collection = new HashMap<>();
        Object current;

        for (String definition : definitions) {
            if ((current = this.container.get(ext + "." + definition)) != null) {
                collection.putIfAbsent(definition, current);
            }
        }

        return collection;
    }

    final public void switchController(Controller controller) {
        try {
            this.switcher.load(controller).show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    final public Controller setOptions(JsonNode options) {
        this.options = options;

        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (switch_controller != null) {
            switch_controller.setOnAction(event ->
                this.switchController((Controller) this.get(((Button) event.getTarget()).getId()))
            );
        }
    }

    final public JsonNode getOptions() {
        return this.options;
    }

    final String getTemplate() {
        return this.template;
    }
}
