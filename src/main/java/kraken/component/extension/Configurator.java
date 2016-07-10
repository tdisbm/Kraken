package kraken.component.extension;

import java.util.HashMap;
import java.util.Map;

public class Configurator {
    private Map<String, Object> config = new HashMap<>();

    final public Configurator set(String index, Object heap) {
        config.put(index, heap);

        return this;
    }

    final public Object get(String index) {
        return config.get(index);
    }
}
