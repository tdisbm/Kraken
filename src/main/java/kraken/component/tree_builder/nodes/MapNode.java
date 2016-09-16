package kraken.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kraken.component.tree_builder.chain.SupportChain;

public class MapNode extends Node
{
    public MapNode(String name) {
        super(name);
    }

    @Override
    public void buildSupportChain(SupportChain chain) {
        chain.add(ObjectNode.class);
    }

    public Object linearize(Object complex) {
        return complex;
    }
}
