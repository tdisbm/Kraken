package kraken.extension.service;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.DependencyNode;
import kraken.component.tree_builder.nodes.InstanceNode;
import kraken.unit.Extension;

public class ServiceExtension extends Extension {
    @Override
    public void map(Object definition, JsonNode prototype) throws Exception {}

    @Override
    public TreeBuilder buildPrototype(TreeBuilder builder) {
        return builder.setRoot("sensors")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }
}
