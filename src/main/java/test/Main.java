package test;

import environment.Kraken;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Kraken kraken = new Kraken();

        kraken
            .sink(new File("src/main/java/test/config/parameters.yml"))
            .sink(new File("src/main/java/test/config/controllers.yml"))
            .sink(new File("src/main/java/test/config/sensors.yml"))
            .sink(new File("src/main/java/test/config/tasks.yml"))
        .dive();
    }
}
