package kraken.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.node.TextNode;
import kraken.component.tree_builder.chain.SupportChain;

public class StringNode extends Node
{
    public StringNode(String name) {
        super(name);
    }

    @Override
    public SupportChain buildSupportChain(SupportChain chain) {
        return chain.add(String.class).add(StringNode.class).add(TextNode.class);
    }

    public Object linearize(Object complex) {
        return complex;
    }
}