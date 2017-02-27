package kraken.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kraken.component.tree_builder.chain.SupportChain;

public class MethodInvokeNode extends Node {
    public MethodInvokeNode(String name) {
        super(name);
    }

    @Override
    public void buildSupportChain(SupportChain chain) {
        chain
            .add(ObjectNode.class)
            .add(com.fasterxml.jackson.databind.node.ArrayNode.class)
        ;
    }

    @Override
    public Object simplify(Object complex) {
        return null;
    }
}
