package kraken.component.tree_builder.nodes;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.*;
import kraken.component.tree_builder.chain.SupportChain;

import java.util.ArrayList;

public class DependencyNode extends Node
{
    public DependencyNode(String name)
    {
        super(name);
    }

    public void buildSupportChain(SupportChain chain) {
        chain
            .add(ObjectNode.class)
            .add(StringNode.class)
            .add(DoubleNode.class)
            .add(ArrayNode.class)
            .add(IntNode.class)
        ;
    }

    public Object simplify(Object complex) {
        ArrayList<Object> result = new ArrayList<>();

        if (complex instanceof ArrayNode) {
            for (int i = 0, n = ((ArrayNode) complex).size(); i < n; i++) {
                result.add(((ArrayNode) complex).get(i).asText());
            }
        }

        if (complex instanceof ObjectNode) {
            for (int i = 0, n = ((ObjectNode) complex).size(); i < n; i++) {
                result.add(((ObjectNode) complex).get(i));
            }
        }

        if (complex instanceof IntNode) {
            result.add(((IntNode) complex).intValue());
        }

        if (complex instanceof TextNode) {
            result.add(((TextNode) complex).asText());
        }

        if (complex instanceof DoubleNode) {
            result.add(((DoubleNode) complex).doubleValue());
        }

        return result;
    }
}
