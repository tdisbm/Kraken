package kraken.component.tree_builder.nodes;

import kraken.component.tree_builder.chain.SupportChain;

public class ArrayNode extends Node
{
    public ArrayNode(String name) {
        super(name);
    }

    @Override
    public SupportChain buildSupportChain(SupportChain chain) {
        return chain.add(ArrayNode.class);
    }

    public Object linearize(Object complex) {
        return complex;
    }
}
