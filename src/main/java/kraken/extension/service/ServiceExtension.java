package kraken.extension.service;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.DependencyNode;
import kraken.component.tree_builder.nodes.InstanceNode;
import kraken.unit.Extension;

import java.util.LinkedHashMap;

public class ServiceExtension extends Extension {
    @java.lang.Override
    public void map(Object definition, JsonNode prototype) throws Exception {}

    @java.lang.Override
    public TreeBuilder buildPrototype(TreeBuilder builder) {
        return builder
            .setRoot("services")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }

    public LinkedHashMap<String, Object> registerSystemDefaults(LinkedHashMap<String, Object> defaults) {
        defaults.put("container", this.getContainer());

        return defaults;
    }
}
