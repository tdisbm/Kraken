package kraken.extension.system;

import com.fasterxml.jackson.databind.JsonNode;
import kraken.component.tree_builder.TreeBuilder;
import kraken.unit.Extension;

import java.util.LinkedHashMap;

public class SystemExtension extends Extension {
    @Override
    public void map(Object definition, JsonNode prototype) throws Exception {

    }

    @Override
    public TreeBuilder buildPrototype(TreeBuilder builder) {
        return builder.setRoot("system");
    }

    public LinkedHashMap<String, Object> registerSystemDefaults(LinkedHashMap<String, Object> defaults) {
        defaults.put("container", this.getContainer());

        return defaults;
    }
}
