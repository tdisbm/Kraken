package environment;

import environment.extension.ParameterExtension;
import environment.extension.scene.FXExtension;
import environment.extension.sensor.SensorExtension;
import environment.extension.task.TaskExtension;
import environment.unit.Extension;
import environment.resolver.container.ContainerResolver;

import java.io.File;

public class Kraken {
    private AppRegister register = new AppRegister();

    final public void dive() {
        long startTime = System.currentTimeMillis();

        this.registerExtensions()
            .loadContainer();

        long endTime = System.currentTimeMillis();
        System.out.printf("[+] Kraken is running! \n - Compile time: %d ms", endTime - startTime);
        System.gc();
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
            .sink(new ParameterExtension())
        .sink(new FXExtension());
    }

    private Kraken loadContainer() {
        this.register
            .getContainer()
            .setRegister(this.register)
            .compile();

        return this;
    }
}
