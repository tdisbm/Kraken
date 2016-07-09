package kraken.container.dependency_injection;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.*;
import kraken.unit.Container;
import kraken.unit.Extension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

class DependencyTreeBuilder
{
    private TreeBuilder tree;

    private static final String ROOT_NAME = "dependency_linker";

    DependencyTreeBuilder(LinkedList<Extension> extensions) {
        if (extensions == null) {
            return;
        }

        Node mainRoot = new ArrayNode(ROOT_NAME);

        for (Extension extension : extensions) {
            TreeBuilder builder = extension.getTreeBuilder();
            mainRoot.addChild(builder.getRoot().setParent(mainRoot));
        }

        this.tree = new TreeBuilder(mainRoot);
    }

    final TreeBuilder build(Container container) {
        if (this.tree == null || this.tree.getRunner() == null) {
            return null;
        }

        ArrayList<Node> dependencies = this.tree.getRunner().findByClass(
            DependencyNode.class,
            null
        );
        ArrayList<Node> instances = this.tree.getRunner().findByClass(
            InstanceNode.class,
            null
        );

        if (instances.size() == 0 || dependencies.size() == 0) {
            return null;
        }

        for (Node node : dependencies) {
            linkReferences(node, container);
        }

        for (Node node : instances) {
            linkReferences(node, container);
        }

        return this.tree;
    }

    private void linkReferences(Node node, Container container) {
        LinkedHashMap<String, Object> definitions = container.getByExtensionRoot(node.getRoot().getName());
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        JsonNode current;

        for (Map.Entry<String, Object> def : definitions.entrySet()) {
            try {
                current = ((JsonNode) def.getValue()).get(node.getName());
            } catch (Exception e) {
                current = null;
            }

            if (node.supports(current)) {
                result.put(def.getKey(), node.linearize(current));
            }
        }

        node.setValue(result);
    }
}
