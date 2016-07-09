package environment.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import environment.component.tree_builder.chain.SupportChain;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

public class InstanceNode extends Node
{
    public InstanceNode(String name) {
        super(name);
    }

    @Override
    public SupportChain buildSupportChain(SupportChain chain) {
        return chain.add(StringNode.class).add(TextNode.class);
    }

    public Object linearize(Object complex) {
        return ((JsonNode) complex).textValue();
    }
}
