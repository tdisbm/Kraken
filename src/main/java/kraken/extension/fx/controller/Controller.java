package kraken.extension.fx.controller;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kraken.unit.Container;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class Controller {
    private Container container = new Container();

    private ControllerSwitcher switcher = null;

    private String template = null;

    private JsonNode options;

    private Stage stage;

    private Scene scene;


    final public void setTemplate(String template) {
        this.template = template;
    }

    final public <T> T get(String definition) {
        return this.container.get(definition);
    }

    final public <T> HashMap<String, T> get(String ext, String ...definitions) {
        if (definitions == null) {
            return this.container.getByExtensionRoot(ext);
        }

        HashMap<String, T> collection = new HashMap<>();
        T current;

        for (String definition : definitions) {
            if ((current = this.container.get(ext + "." + definition)) != null) {
                collection.putIfAbsent(definition, current);
            }
        }

        return collection;
    }

    public void init() {
        // to override
    }

    final public void switchController(Controller controller) {
        try {
            this.switcher.load(controller).show();
        } catch (Exception ignored) {}
    }

    final public Controller setOptions(JsonNode options) {
        this.options = options;

        return this;
    }

    final public Controller setStage(Stage stage) {
        this.stage = stage;
        return  this;
    }

    final public JsonNode getOptions() {
        return this.options;
    }

    final String getTemplate() {
        return this.template;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public Controller setScene(Scene scene) {
        this.scene = scene;

        return this;
    }
}
