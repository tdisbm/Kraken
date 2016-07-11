package kraken.unit;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.extension.Configurator;
import kraken.component.tree_builder.TreeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;


public abstract class Extension
{
    private Container container;

    private TreeBuilder treeBuilder = new TreeBuilder();

    private Configurator configurator = new Configurator();

    private boolean __mapped__ = false;

    public Extension() {
        this.treeBuilder = this.buildPrototype(this.treeBuilder);

        if (null == this.treeBuilder) {
            System.out.println("Fatal Error: Extension '" + this.getClass() + "' prototype is not defined");
            System.exit(1);
        }
    }

    final public Extension setContainer(Container container) {
        this.container = container;

        return this;
    }

    final public TreeBuilder getTreeBuilder() {
        return this.treeBuilder;
    }

    final void mapping() {
        if (this.__mapped__ || null == this.container) {
            return;
        }

        LinkedHashMap<String, ?> definitions;

        if (null == (definitions = this.findOwn())) {
            return;
        }


        for (Map.Entry<String, ?> definition : definitions.entrySet()) {
            try {
                this.map(
                        this.container.get(definition.getKey()),
                        (JsonNode) this.configurator.get(definition.getKey())
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        this.done();
        this.__mapped__ = true;
    }

    final String getRootName() {
        return this.treeBuilder.getRootName();
    }

    final Configurator getConfigurator() {
        return this.configurator;
    }

    final String wrap(String definitionLocator) {
        return this.getRootName() + "." +definitionLocator;
    }

    public void done() {}

    private LinkedHashMap<String, Object> findOwn() {
        return this.container.getByExtensionRoot(this.getRootName());
    }

    public abstract void map(Object definition, JsonNode prototype) throws Exception;

    public abstract TreeBuilder buildPrototype(TreeBuilder builder);
}
