package external;

import kraken.Kraken;
import kraken.container.ContainerResolver;
import kraken.unit.Extension;

import java.util.LinkedList;
import java.util.List;

public abstract class KrakenApp {
    private Kraken kraken = new Kraken();

    public KrakenApp() {
        List<Extension> extensions = this.registerExtensions(new LinkedList<>());
        List<Object> resources = this.registerResources(new LinkedList<>());
        List<ContainerResolver> resolvers = this.registerResolvers(new LinkedList<>());

        for (Extension extension: extensions) {
            this.kraken.sink(extension);
        }

        for (Object resource: resources) {
            this.kraken.sink(resource);
        }

        for (ContainerResolver resolver: resolvers) {
            this.kraken.sink(resolver);
        }

        this.kraken.dive();
    }

    public Kraken getKraken() {
        return this.kraken;
    }

    protected abstract List<Extension> registerExtensions(List<Extension> extensions);

    protected abstract List<Object> registerResources(List<Object> resources);

    protected abstract List<ContainerResolver> registerResolvers(List<ContainerResolver> resolvers);
}
