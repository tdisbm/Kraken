package kraken.extension.parameter;


import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.nodes.DependencyNode;
import kraken.unit.Extension;
import kraken.component.tree_builder.TreeBuilder;


public class ParameterExtension extends Extension
{
    @Override
    public void map(Object definition, JsonNode prototype) {}

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) {
        return new TreeBuilder(new DependencyNode("parameters"));
    }
}
