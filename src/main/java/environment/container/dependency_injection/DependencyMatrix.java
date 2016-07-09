package environment.container.dependency_injection;

import environment.unit.Container;

import java.util.*;

import static environment.component.util.graph.TopologicalSort.topologicalSort;

class DependencyMatrix
{
    private List<Integer>[] matrix;

    DependencyMatrix(
        LinkedHashMap<String, ?> definitionMap,
        LinkedHashMap<String, ?> referenceMap,
        Container container
    ) {
        this.create(
            referenceMap,
            definitionMap,
            container
        );

        this.validate();
    }

    List<Integer> topologicalOrder() {
        List<Integer> loadStackIndexes = topologicalSort(this.matrix);
        Collections.reverse(loadStackIndexes);

        return loadStackIndexes;
    }

    private void create(
        LinkedHashMap<String, ?> referenceMap,
        LinkedHashMap<String, ?> definitionMap,
        Container container
    ) {
        ArrayList<String> references = new ArrayList<>(referenceMap.keySet());
        generate(referenceMap.size());

        for (Map.Entry<String, ?> definition : definitionMap.entrySet()) {
            for (String reference : (ArrayList<String>) definition.getValue()) {
                if (!container.has(reference) && container.hasExtension(reference)) {
                    System.out.format(
                        "Fatal error: '%s' has dependency to non existent definition '%s'",
                        definition.getKey(),
                        reference
                    );
                    System.exit(1);
                }

                if (references.indexOf(reference) != -1) {
                    matrix[references.indexOf(definition.getKey())].add(
                        references.indexOf(reference)
                    );
                }
            }
        }
    }

    private void generate(int size) {
        matrix = new List[size];

        for (int i = 0; i < size; i++) {
            matrix[i] = new ArrayList<>();
        }
    }

    private void validate() {
        int[][] mirror = new int[matrix.length][matrix.length];
        int iterator = -1;

        for (List<Integer> i : matrix) {
            iterator++;
            for (int j : i) {
                mirror[iterator][j] = 1;
            }
        }

        for (int i = 0; i < mirror.length; i++) {
            if (mirror[i][i] == 1) {
                System.out.println("Fatal error: Definition can't be dependent by self");
                System.exit(1);
            }

            for (int j = 0; j < mirror[i].length; j++) {
                if (mirror[i][j] == 1 && mirror[j][i] == 1) {
                    System.out.println("Fatal error: Circular dependency detected");
                    System.exit(1);
                }
            }
        }
    }
}
