package environment.component.extension;

import java.util.HashMap;
import java.util.Map;

public class Configurator {
    private Map<String, Object> config = new HashMap<>();

    final public Configurator setCommonConfig(String index, Object heap) {
        config.put(index, heap);

        return this;
    }

    final public Object getCommonConfig(String index) {
        return config.get(index);
    }
}
