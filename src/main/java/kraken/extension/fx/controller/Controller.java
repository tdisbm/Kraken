package kraken.extension.fx.controller;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.stage.Stage;
import kraken.unit.Container;
import javafx.fxml.Initializable;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller extends Stage implements Initializable {
    private Container container = new Container();

    private ControllerSwitcher switcher = null;

    private String template = null;

    private JsonNode options;


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
        } catch (Exception ignored) {}
    }

    final public Controller setOptions(JsonNode options) {
        this.options = options;

        return this;
    }

    final public JsonNode getOptions() {
        return this.options;
    }

    final String getTemplate() {
        return this.template;
    }
}
