package kraken.extension.scene;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.unit.Container;
import kraken.unit.Extension;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.ArrayNode;
import kraken.component.tree_builder.nodes.InstanceNode;
import kraken.component.tree_builder.nodes.MapNode;
import kraken.component.tree_builder.nodes.StringNode;
import kraken.extension.scene.controller.Controller;

import java.lang.reflect.Field;

public class FXExtension extends Extension
{
    private FXBridge bridge = new FXBridge();

    private Controller main;

    @Override
    public void map(Object definition, JsonNode prototype) throws Exception {
        if (!(definition instanceof Controller)) {
            throw new Exception("Controller definition must implement " + Controller.class.getName());
        }

        if (prototype.get("template") == null) {
            throw new Exception("Fatal Error: Controller must have template!");
        }

        Controller controller = (Controller) definition;
        JsonNode options;

        controller.setTemplate(prototype.get("template").asText());

        if ((options = prototype.get("options")) != null) {
            for (JsonNode option : options) {
                if (option.asText().equals("main") && this.main == null) {
                    this.main = controller;
                }
            }
        }

        controller.setOptions(options);
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) {
        return treeBuilder.setRoot("controllers")
            .addChild(new InstanceNode("class"))
            .addChild(new StringNode("template"))
            .next(new MapNode("options"))
                .addChild(new ArrayNode(null))
            .end()
            .next(new MapNode("config"))
                .addChild(new ArrayNode(null))
            .end()
        .end();
    }

    public void done() {
        try {
            Field c = this.getClass().getSuperclass().getDeclaredField("container");
            c.setAccessible(true);

            this.bridge.setContainer((Container) c.get(this));
            this.bridge.up(this.main);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
