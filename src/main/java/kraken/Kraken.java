package kraken;

import kraken.container.ContainerResolver;
import kraken.extension.parameter.ParameterExtension;
import kraken.extension.sensor.SensorExtension;
import kraken.extension.service.ServiceExtension;
import kraken.extension.task.TaskExtension;
import kraken.unit.Extension;

import java.io.File;
import java.io.InputStream;

public class Kraken {
    private Register register = new Register();

    final public Kraken dive() {
        this.registerDefaultExtensions();
        this.loadContainer();

        System.gc();

        return this;
    }

    final public Kraken sink(Object o) {
        if (o instanceof ContainerResolver) {
            this.register.registerResolver((ContainerResolver) o);
        }

        if (o instanceof Extension) {
            this.register.registerExtension((Extension) o);
        }

        if (o instanceof File || o instanceof InputStream || o instanceof String) {
            this.register.registerResource(o);
        }

        return this;
    }

    private Kraken registerDefaultExtensions() {
        return this
            .sink(new SensorExtension())
            .sink(new ServiceExtension())
            .sink(new TaskExtension())
            .sink(new ParameterExtension())
        ;
    }

    protected Kraken loadContainer() {
        this.register
            .getContainer()
            .setRegister(this.register)
            .compile();

        return this;
    }
}
