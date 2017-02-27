package kraken.extension.service;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.DependencyNode;
import kraken.component.tree_builder.nodes.InstanceNode;
import kraken.component.tree_builder.nodes.MethodInvokeNode;
import kraken.unit.Extension;

public class ServiceExtension extends Extension {
    @java.lang.Override
    public void map(Object definition, JsonNode prototype) throws Exception {}

    @java.lang.Override
    public TreeBuilder buildPrototype(TreeBuilder builder) {
        return builder
            .setRoot("services")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
            .addChild(new MethodInvokeNode("calls"))
        .end();
    }
}
