package test.tasks;

import environment.extension.task.Task;
import test.sensors.DHT22;

public class DHT22Task extends Task
{
    private DHT22 dht22;

    public DHT22Task(DHT22 dht22) {
        this.dht22 = dht22;
    }

    public void run() {
        dht22.read();
    }
}
