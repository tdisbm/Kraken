package environment.container.dependency_injection;

import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.unit.Container;
import environment.container.ContainerResolver;

import java.util.*;
import java.util.stream.Collectors;

import static environment.component.util.instance.InstanceLoader.getClasses;
import static environment.component.util.instance.InstanceLoader.newInstance;

public class DependencyResolver extends ContainerResolver {

    @Override
    public void resolve() {
        Container container = this.getContainer();
        TreeBuilder builder = new DependencyTreeBuilder(this.getExtensions())
            .build(container);

        assert builder != null;
        LinkedHashMap<String, ?> definitionMap = builder.getRunner().getValues(DependencyNode.class);
        LinkedHashMap<String, ?> referenceMap = builder.getRunner().getValues(InstanceNode.class);

        this.createInstances(referenceMap, definitionMap, container);
    }

    private void createInstances(
        LinkedHashMap<String, ?> referenceMap,
        LinkedHashMap<String, ?> definitionMap,
        Container container
    ) {
        LinkedHashMap<String, Class<?>> classes = getClasses(referenceMap);
        LinkedHashMap<String, Object> loaded = new LinkedHashMap<>();
        List<Integer> loadStack = new DependencyMatrix(
            definitionMap,
            referenceMap,
            container
        ).topologicalOrder();

        ArrayList<String> argumentList;
        ArrayList<Object> arguments = new ArrayList<>();

        String[] instanceList = classes.keySet().toArray(new String[0]);
        Object instance;

        for (int i : loadStack) {
            argumentList = (ArrayList<String>) definitionMap.get(instanceList[i]);

            if (argumentList != null)
            arguments.addAll(argumentList.stream().map(argument ->
                null != loaded.get(argument)
                ? loaded.get(argument)
                : container.hasExtension(argument)
                ? container.get(argument)
                : argument).collect(Collectors.toList())
            );

            if (null == (instance = newInstance(classes.get(instanceList[i]), arguments))) {
                throw new Error("Can't create instance for definition " + instanceList[i]);
            }

            loaded.put(instanceList[i], instance);
            arguments.clear();
        }

        this.push(container, loaded);
    }

    private void push(Container container, LinkedHashMap<String, Object> instances) {
        for (Map.Entry<String, Object> obj : instances.entrySet()) {
            container.set(obj.getKey(), obj.getValue());
        }
    }

}
