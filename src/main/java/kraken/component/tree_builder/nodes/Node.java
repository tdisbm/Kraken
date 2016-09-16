package kraken.component.tree_builder.nodes;

import kraken.component.tree_builder.chain.SupportChain;

import java.util.ArrayList;

public abstract class Node
{
    private Node root;

    private String name;

    private Object value;

    private ArrayList<Node> children = new ArrayList<>();

    private ArrayList<Node> parents = new ArrayList<>();

    private SupportChain supportChain = new SupportChain();

    Node(String name) {
        this.buildSupportChain(this.supportChain);
        this.name = name;
    }

    final public Node setParent(Node parents) {
        this.parents.add(0, parents);

        return this;
    }

    final public Node addParent(Node parent) {
        this.parents.add(parent);

        return this;
    }

    final public Node getParent() {
        try {
            return this.parents.get(0);
        } catch (Exception ignored) {}

        return null;
    }

    final public Node getParent(int i) {
        return this.parents.get(i);
    }

    final public Node addChild(Node child) {
        this.children.add(child);

        return this;
    }

    final public Node getChild(int i) {
        return this.children.get(i);
    }

    final public Node setRoot(Node root) {
        this.root = root;

        return this;
    }

    final public Node getRoot() {
        return this.root;
    }

    final public ArrayList<Node> getChildren() {
        return this.children;
    }

    final public String getName() {
        return this.name;
    }

    final public Object getValue() {
        return value;
    }

    final public Node setValue(Object value) {
        this.value = value;

        return this;
    }

    final public boolean supports(Object value) {
        return this.supportChain != null && this.supportChain.supports(value);
    }

    public abstract void buildSupportChain (SupportChain chain);

    public abstract Object linearize(Object complex);
}
