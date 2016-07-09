package kraken.component.tree_builder;


import kraken.component.tree_builder.nodes.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class TreeBuilder
{
    private Node root;

    private Node current;

    private TreeRunner runner = new TreeRunner(this);

    public TreeBuilder() {}

    public TreeBuilder(Node root) {
        root.setRoot(root);

        this.root = root;
        this.current = root;
    }

    final public TreeBuilder setRoot(String name) {
        if (this.root != null) {
            System.out.println("Warning: root is already defined");
            return this;
        }

        this.root = new MapNode(name);
        this.current = this.root;

        return this;
    }

    @Contract("null -> fail")
    @NotNull
    final public TreeBuilder addChild(Node node) {
        this.current.addChild(node);
        node.setRoot(this.root)
            .setParent(this.current);

        return this;
    }

    final public TreeBuilder end() {
        Node next = this.current.getParent();

        if (next == null && this.current != this.root) {
            System.out.println("Fatal Error: Unexpected end of builder");
            System.exit(1);
        }

        this.current = next == this.root ? this.root : next;

        return this;
    }

    final public TreeBuilder next(Node next) {
        next.setRoot(this.root);
        next.setParent(this.current);

        this.current.addChild(next);
        this.current = next;

        return this;
    }

    final public TreeRunner getRunner()
    {
        return this.runner;
    }

    final public Node getRoot() {
        return this.root;
    }

    final public String getRootName() {
        if (this.root.getName() == null || this.root.getName().isEmpty()) {
            System.out.println("Fatal Error: Extension empty root is not allowed!");
            Thread.dumpStack();
            System.exit(1);
        }

        return this.root.getName();
    }
}
