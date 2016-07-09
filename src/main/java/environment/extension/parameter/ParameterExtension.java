package environment.extension.parameter;


import com.fasterxml.jackson.databind.JsonNode;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;


public class ParameterExtension extends Extension
{
    @Override
    public void map(Object definition, JsonNode prototype) {}

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) {
        return new TreeBuilder(new DependencyNode("parameters"));
    }
}
