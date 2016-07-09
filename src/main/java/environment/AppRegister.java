package environment;

import environment.resolver.container.dependency_injection.*;
import environment.unit.Container;
import environment.unit.Extension;
import environment.resolver.container.ContainerResolver;

import java.io.File;
import java.util.LinkedList;

public class AppRegister {
    private LinkedList<File> resources;

    private LinkedList<Extension> extensions;

    private LinkedList<ContainerResolver> resolvers;

    private Container container;

    AppRegister() {
        this.container = new Container();
        this.resources = new LinkedList<>();
        this.extensions = new LinkedList<>();
        this.resolvers = new LinkedList<>();

        this.registerResolvers();
    }

    final AppRegister registerResource(File resource) {
        if (resource.exists()) {
            this.resources.add(resource);
        }

        return this;
    }

    final AppRegister registerResolver(ContainerResolver resolver) {
        resolver.setContainer(this.container);
        this.resolvers.add(resolver);

        return this;
    }

    final AppRegister registerExtension(Extension extension) {
        extension.setContainer(this.container);
        this.extensions.add(extension);

        return this;
    }

    final public LinkedList<Extension> getExtensions() {
        return this.extensions;
    }

    final public LinkedList<ContainerResolver> getResolvers() {
        return this.resolvers;
    }

    final public LinkedList<File> getResources() {
        return this.resources;
    }

    final Container getContainer() {
        return this.container;
    }

    private void registerResolvers(){
        this.registerResolver(new DependencyResolver());
    }

}
