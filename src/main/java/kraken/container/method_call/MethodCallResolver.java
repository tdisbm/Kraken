package kraken.container.method_call;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import kraken.component.tree_builder.nodes.MethodInvokeNode;
import kraken.component.tree_builder.nodes.Node;
import kraken.component.util.instance.MethodInvoker;
import kraken.container.ContainerResolver;
import kraken.unit.Container;
import kraken.unit.Extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodCallResolver extends ContainerResolver {
    public static final int INVOKE_RECORD_METHOD_NAME = 0;
    public static final int INVOKE_RECORD_METHOD_ARGS = 1;

    @Override
    public void resolve() {
        Map<String, Map<String, Object>> definitionMap = findRawDefinitions();
        Map<String, ArrayList<Node>> callNodes = findMethodCallNodes();

        ArrayList<Node> currentCallNodes;

        for(Map.Entry<String, Map<String, Object>> definitions : definitionMap.entrySet()) {
            currentCallNodes = callNodes.get(definitions.getKey());

            for (Map.Entry<String, Object> definition : definitions.getValue().entrySet()) {
                callMethods(definition, currentCallNodes);
            }
        }
    }

    private Map<String, Map<String, Object>> findRawDefinitions() {
        Container container = getContainer();
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();

        for (Extension ext : getExtensions()) {
            if (ext.getTreeBuilder().getRunner().hasNode(MethodInvokeNode.class)) {
                String rootName = ext.getTreeBuilder().getRootName();
                result.put(rootName, container.getRawByExtensionRoot(rootName));
            }
        }

        return result;
    }

    private Map<String, ArrayList<Node>> findMethodCallNodes() {
        Map<String, ArrayList<Node>> result = new LinkedHashMap<>();

        for (Extension ext : getExtensions()) {
            if (ext.getTreeBuilder().getRunner().hasNode(MethodInvokeNode.class)) {
                String rootName = ext.getTreeBuilder().getRootName();
                result.put(rootName, ext.getTreeBuilder().getRunner().findByClass(MethodInvokeNode.class, null));
            }
        }

        return result;
    }

    private void callMethods(Map.Entry<String, Object> rawDefinition, ArrayList<Node> callNodes) {
        Object instance = getContainer().get(rawDefinition.getKey());
        Container container = getContainer();

        if (instance == null) {
            return;
        }

        ObjectNode raw = (ObjectNode) rawDefinition.getValue();
        JsonNode node;

        for (Node callNode : callNodes) {
            node = raw.findValue(callNode.getName());
            if (node == null) {
                continue;
            }

            if (node instanceof ArrayNode) {
                Iterator i = node.iterator();
                Iterator j;

                ArrayNode invokeRecord;
                ValueNode rawArg;

                String methodName;

                ArrayList<Object> args = new ArrayList<>();

                while (i.hasNext()) {
                    invokeRecord = (ArrayNode) i.next();
                    args.clear();
                    if (invokeRecord.get(INVOKE_RECORD_METHOD_NAME) instanceof TextNode) {
                        methodName = invokeRecord.get(INVOKE_RECORD_METHOD_NAME).asText();

                        if (invokeRecord.get(INVOKE_RECORD_METHOD_ARGS) != null) {
                            j = invokeRecord.get(INVOKE_RECORD_METHOD_ARGS).iterator();
                            while(j.hasNext()) {
                                rawArg = (ValueNode) j.next();
                                args.add(null != container.get(rawArg.asText())
                                    ? container.get(rawArg.asText())
                                    : rawArg
                                );
                            }
                        }

                        try {
                            MethodInvoker.invoke(instance, methodName, args);
                        } catch (NoSuchMethodException e) {
                            System.out.println(String.format("Fatal error: Method '%s' does't exist in '%s'",
                                methodName,
                                instance.getClass().getName()
                            ));
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }
}
