package environment.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import environment.component.tree_builder.chain.SupportChain;

public class MapNode extends Node
{
    public MapNode(String name) {
        super(name);
    }

    @Override
    public SupportChain buildSupportChain(SupportChain chain) {
        return chain.add(ObjectNode.class);
    }

    public Object linearize(Object complex) {
        return complex;
    }
}
