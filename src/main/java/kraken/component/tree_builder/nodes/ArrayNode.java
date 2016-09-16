package kraken.component.tree_builder.nodes;

import kraken.component.tree_builder.chain.SupportChain;

public class ArrayNode extends Node
{
    public ArrayNode(String name) {
        super(name);
    }

    @Override
    public void buildSupportChain(SupportChain chain) {
        chain
            .add(com.fasterxml.jackson.databind.node.ArrayNode.class)
        ;
    }

    public Object linearize(Object complex) {
        return complex;
    }
}
