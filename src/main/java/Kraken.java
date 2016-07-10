import kraken.AppRegister;
import kraken.container.ContainerResolver;
import kraken.extension.parameter.ParameterExtension;
import kraken.extension.sensor.SensorExtension;
import kraken.extension.task.TaskExtension;
import kraken.unit.Extension;

import java.io.File;

public class Kraken {
    private AppRegister register = new AppRegister();

    final public Kraken dive() {
        long startTime = System.currentTimeMillis();

        this.registerExtensions()
            .loadContainer();

        long endTime = System.currentTimeMillis();
        System.out.printf("[+] Kraken is running! \n - Compile time: %d ms", endTime - startTime);
        System.gc();

        return this;
    }

    final public Kraken sink(Object o) {
        if (o instanceof ContainerResolver) {
            this.register.registerResolver((ContainerResolver) o);
            return this;
        }

        if (o instanceof Extension) {
            this.register.registerExtension((Extension) o);
            return this;
        }

        if (o instanceof File) {
            this.register.registerResource((File) o);
        }

        return this;
    }

    private Kraken registerExtensions() {
        return this
            .sink(new SensorExtension())
            .sink(new TaskExtension())
        .sink(new ParameterExtension());
    }

    private Kraken loadContainer() {
        this.register
            .getContainer()
            .setRegister(this.register)
            .compile();

        return this;
    }
}
