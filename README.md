# Kraken

# Description:
Raspberry Pi GPIO controll environment, task runner with JavaFX embedded application runner that make simple add/remove a sensor, run sensor read task and many other things

# Usage:
```java
public static void main(String[] args) {
    Kraken kraken = new Kraken();
    kraken
        .sink(new File("path/to/resource.yml"))
        .sink(new File("another/path/to/resource.yml"))
    .dive();
    /* Other staff */
}
```
Sink arguments are resources to yml files that can look like this:

```yml
sensors:
    DHT22:
        class: device.sensors.DHT22
        arguments:
          - argument1
          - argument2
```

or customized, user defined extension. (**about this later**)

By default are available 4 extensions:
 * sensors - Raspberry Pi GPIO attached sensor
 * parameters - any parammeters
 * tasks - schelude threads runner
 * scene - javafx application runner

# Maven Dependency
```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ...
</repositories>

<dependencies>
    ...
    <dependency>
        <groupId>com.github.tdisbm</groupId>
        <artifactId>Kraken</artifactId>
        <version>${kraken.version}</version>
    </dependency>
    ...
</dependencies>

<properties>
    ...
    <kraken.version>1.0.0</kraken.version>
    ...
</properties>
```

Example project: https://github.com/tdisbm/EgleeyDevice

