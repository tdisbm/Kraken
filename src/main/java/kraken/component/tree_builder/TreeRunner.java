package kraken.component.tree_builder;

import kraken.component.tree_builder.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TreeRunner
{
    private TreeBuilder builder;

    TreeRunner(TreeBuilder builder) {
        this.builder = builder;
    }

    final public boolean hasNode(Class<?> clazz) {
        ArrayList<Node> result;
        result = findByClass(clazz, null);

        return result.size() > 0;
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

        for (LinkedHashMap aMap : map) {
            merged.putAll(aMap);
        }

        return merged;
    }

    private ArrayList<LinkedHashMap> linearizeValues(ArrayList<Node> nodes) {
        ArrayList<LinkedHashMap> collection = new ArrayList<>();

        for (Node node : nodes) {
            collection.add((LinkedHashMap) node.getValue());
        }

        return collection;
    }
}
