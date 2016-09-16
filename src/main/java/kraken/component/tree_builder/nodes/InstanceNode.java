package kraken.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import kraken.component.tree_builder.chain.SupportChain;

public class InstanceNode extends Node
{
    public InstanceNode(String name) {
        super(name);
    }

    @Override
    public void buildSupportChain(SupportChain chain) {
        chain
            .add(TextNode.class)
        ;
    }

    public Object linearize(Object complex) {
        return ((JsonNode) complex).textValue();
    }
}
