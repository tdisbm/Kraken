package kraken;

import kraken.container.ContainerResolver;
import kraken.container.dependency_injection.DependencyResolver;
import kraken.container.method_call.MethodCallResolver;
import kraken.unit.Container;
import kraken.unit.Extension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Register {
    private LinkedList<InputStream> resources;

    private LinkedList<Extension> extensions;

    private LinkedList<ContainerResolver> resolvers;

    private Container container;

    Register() {
        this.container = Container.getInstance();
        this.resources = new LinkedList<>();
        this.extensions = new LinkedList<>();
        this.resolvers = new LinkedList<>();

        this.registerResolvers();
    }

    final Register registerResource(Object resource) {
        InputStream temp = null;

        try {
            if (resource instanceof File) {
                temp = new FileInputStream((File) resource);
            }

            if (resource instanceof InputStream) {
                temp = (InputStream) resource;
            }

            if (temp != null) {
                this.resources.add(temp);
            }
        } catch (FileNotFoundException ignored) {}

        return this;
    }

    final Register registerResolver(ContainerResolver resolver) {
        resolver.setContainer(this.container);
        this.resolvers.add(resolver);

        return this;
    }

    final Register registerExtension(Extension extension) {
        extension.setContainer(this.container);
        this.extensions.add(extension);

        LinkedHashMap<String, Object> systemDefaults = extension.registerSystemDefaults(new LinkedHashMap<>());
        List<ContainerResolver> resolvers = extension.registerResolvers(new LinkedList<>());
        List<Object> resources = extension.registerResources(new LinkedList<>());

        for (Object resource: resources) {
            this.registerResource(resource);
        }

        for (ContainerResolver resolver: resolvers) {
            this.registerResolver(resolver);
        }

        for (Map.Entry def: systemDefaults.entrySet()) {
            this.container.set(extension.getTreeBuilder().getRootName() + "." + def.getKey(), def.getValue());
        }

        return this;
    }

    final public LinkedList<Extension> getExtensions() {
        return this.extensions;
    }

    final public LinkedList<ContainerResolver> getResolvers() {
        return this.resolvers;
    }

    final public LinkedList<InputStream> getResources() {
        return this.resources;
    }

    final Container getContainer() {
        return this.container;
    }

    private void registerResolvers(){
        this
        .registerResolver(new DependencyResolver())
        .registerResolver(new MethodCallResolver())
        ;
    }
}
