package kraken.extension.fx.controller;


import com.fasterxml.jackson.databind.JsonNode;
import kraken.unit.Container;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ControllerSwitcher
{
    private Container container;

    private Stage stage;

    private Map<Integer, Scene> scenes = new HashMap<>();

    public ControllerSwitcher(Stage stage)
    {
        this.stage = stage;
    }

    public Stage load(Controller controller) throws Exception {
        this.stage.setScene(fetchScene(controller));
        this.fetchOptions(controller);

        return this.stage;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    private Scene fetchScene(Controller controller) throws Exception {
        int id = controller.hashCode();

        if (this.scenes.containsKey(id)) {
            return this.scenes.get(id);
        }

        FXMLLoader loader = new FXMLLoader(new File(controller.getTemplate()).toURI().toURL());

        Scene scene = new Scene(loader.load());
        Controller ctr;

        if (null == (ctr = loader.getController())) {
            System.out.format("Fatal Error: No controller was specified in scene '%s'", controller.getTemplate());
            System.exit(1);
        }

        this.copy(controller, ctr);
        ctr.init();
        ctr.setScene(scene).setStage(this.stage);
        this.scenes.putIfAbsent(id, scene);

        return scene;
    }

    private void copy(Controller environment, Controller fx) {
        try {
            Field s = fx.getClass().getSuperclass().getDeclaredField("switcher");
            Field c = fx.getClass().getSuperclass().getDeclaredField("container");

            s.setAccessible(true);
            s.set(fx, this);

            c.setAccessible(true);
            c.set(fx, container);

            fx.setTemplate(environment.getTemplate());
            fx.setOptions(environment.getOptions());
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    private void fetchOptions(Controller controller) {
        JsonNode options = controller.getOptions();

        if (options == null) {
            return;
        }

        for (JsonNode option : options) {
            if (option.asText().equals("fullscreen") && !this.stage.isFullScreen()) {
                this.stage.setFullScreen(true);
            }
        }
    }
}
