package environment.resolver.container;

import environment.unit.Extension;
import environment.unit.Container;

import java.util.LinkedList;

public abstract class ContainerResolver
{
    private LinkedList<Extension> extensions;

    private Container container;

    public abstract void resolve();

    final public ContainerResolver setExtensions(LinkedList<Extension> extensions) {
        this.extensions = extensions;

        return this;
    }

    final protected LinkedList<Extension> getExtensions() {
        return extensions;
    }

    final public ContainerResolver setContainer(Container container) {
        this.container = container;

        return this;
    }

    final protected Container getContainer() {
        return container;
    }
}
