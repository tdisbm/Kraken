package external;

import kraken.Kraken;
import kraken.container.ContainerResolver;
import kraken.unit.Container;
import kraken.unit.Extension;

import java.util.*;

public abstract class KrakenApp {
    private Kraken kraken = new Kraken();

    public KrakenApp() {
        LinkedHashMap<String, Object> systemDefaults = this.registerSystemDefaults(new LinkedHashMap<>());
        List<ContainerResolver> resolvers = this.registerResolvers(new LinkedList<>());
        List<Extension> extensions = this.registerExtensions(new LinkedList<>());
        List<Object> resources = this.registerResources(new LinkedList<>());

        for (Extension extension: extensions) {
            this.kraken.sink(extension);
        }

        for (Object resource: resources) {
            this.kraken.sink(resource);
        }

        for (ContainerResolver resolver: resolvers) {
            this.kraken.sink(resolver);
        }

        Container c = this.kraken.getContainer();

        for (Map.Entry def: systemDefaults.entrySet()) {
            c.set((String) def.getKey(), def.getValue());
        }

        this.kraken.dive();
    }

    public Kraken getKraken() {
        return this.kraken;
    }

    protected LinkedHashMap<String, Object> registerSystemDefaults(LinkedHashMap<String, Object> defaults) {
        defaults.put("system.container", this.kraken.getContainer());

        return defaults;
    }

    protected List<Extension> registerExtensions(List<Extension> extensions) {
        return extensions;
    }

    protected List<ContainerResolver> registerResolvers(List<ContainerResolver> resolvers) {
        return resolvers;
    }

    protected abstract List<Object> registerResources(List<Object> resources);
}
