package kraken.extension.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.unit.Extension;
import kraken.component.tree_builder.TreeBuilder;
import kraken.component.tree_builder.nodes.*;


public class SensorExtension extends Extension
{
    @Override
    public void map(Object definition, JsonNode prototype) throws Exception {
        if (!(definition instanceof Sensor)) {
            throw new Exception("Sensor class must abstract " + Sensor.class.getName());
        }
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) {
        return treeBuilder.setRoot("sensors")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }
}
