package kraken.component.tree_builder;

import kraken.component.tree_builder.nodes.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class TreeRunner
{
    private TreeBuilder builder;

    TreeRunner(TreeBuilder builder) {
        this.builder = builder;
    }

    final public ArrayList<Node> findByClass(Class<?> clazz, Node from) {
        ArrayList<Node> list = new ArrayList<>();
        Node current = from == null ? this.builder.getRoot() : from;

        if (this.builder.getRoot() == null || current.getChildren() == null) {
            return list;
        }

        if (current.getClass().getName().equals(clazz.getName())) {
            list.add(current);
        }

        for (Node node : current.getChildren()) {
            list.addAll(this.findByClass(clazz, node));
        }

        return list;
    }

    final public LinkedHashMap<String, ?> getValues(Class<?> clazz) {
        return this.mergeMaps(this.linearizeValues(
            this.findByClass(clazz, null)
        ));
    }

    private LinkedHashMap<String, ?> mergeMaps(ArrayList<LinkedHashMap> map) {
        LinkedHashMap<String, ?> merged = new LinkedHashMap<>();
        map.forEach(merged::putAll);

        return merged;
    }

    private ArrayList<LinkedHashMap> linearizeValues(ArrayList<Node> nodes) {
        return nodes.stream()
            .map(node -> (LinkedHashMap) node.getValue())
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
