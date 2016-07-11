package kraken.component.tree_builder;

import kraken.component.tree_builder.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
        map.forEach(new Consumer<LinkedHashMap>() {
            @Override
            public void accept(LinkedHashMap linkedHashMap) {
                merged.putAll(linkedHashMap);
            }
        });

        return merged;
    }

    private ArrayList<LinkedHashMap> linearizeValues(ArrayList<Node> nodes) {
        return nodes.stream()
            .map(new Function<Node, LinkedHashMap>() {
                @Override
                public LinkedHashMap apply(Node node) {
                    return (LinkedHashMap) node.getValue();
                }
            })
            .collect(Collectors.toCollection(new Supplier<ArrayList<LinkedHashMap>>() {
                @Override
                public ArrayList<LinkedHashMap> get() {
                    return new ArrayList<>();
                }
            }));
    }
}
