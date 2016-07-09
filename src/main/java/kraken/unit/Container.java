package kraken.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kraken.AppRegister;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Container
{
    private Map<String, Object> definitions = new LinkedHashMap<>();

    private Map<String, List<String>> extensionMap = new LinkedHashMap<>();

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private AppRegister register;

    private boolean __compiled__ = false;


    final public Container set(String resource, Object definition) {
        if (this.__compiled__) {
            System.out.println("Container is already compiled, ca't set any definition");
        }

        if (resource != null && !resource.isEmpty()) {
            this.definitions.put(resource, definition);
        }

        return this;
    }

    final public Object get(String resource) {
        return this.definitions.get(resource);
    }

    final public LinkedHashMap<String, Object> getByExtensionRoot(String root) {
        LinkedHashMap<String, Object> collection = new LinkedHashMap<>();

        this.register.getExtensions().stream()
        .filter(ext -> ext.getRootName().equals(root))
        .forEach(ext -> this.definitions.entrySet().stream()
            .filter(definition -> this.extend(definition.getKey(), ext))
            .forEach(definition ->
                collection.put(definition.getKey(), definition.getValue())
            )
        );

        return collection;
    }

    final public boolean has(String resource) {
        return !(this.definitions.get(resource) == null);
    }

    final public void compile() {
        if (this.__compiled__) {
            return;
        }

        this.loadResources();

        this.register.getResolvers().forEach(resolver -> {
            resolver.setExtensions(this.register.getExtensions());
            resolver.resolve();
        });
        this.register.getExtensions().forEach(Extension::mapping);

        this.__compiled__ = true;
    }

    final public Container setRegister(AppRegister register) {
        this.register = register;

        return this;
    }

    final public boolean hasExtension(String definition) {
        boolean valid[] = {false};

        for (Map.Entry<String, List<String>> ext : this.extensionMap.entrySet()) {
            ext.getValue().stream().filter(definition::equals).forEach(def -> valid[0] = true);
            if (valid[0]) return valid[0];
        }

        return valid[0];
    }

    private boolean extend(String definition, Extension ext) {
        boolean result = false;
        try {
            result = this.extensionMap.get(ext.getRootName()).contains(definition);
        } catch (Exception ignored) {}

        return result;
    }

    private void loadResources() {
        Iterator<String> fields;
        JsonNode definitions;
        JsonNode tree;
        String definition;

        for (File resources : this.register.getResources()) {
            try {
                tree = this.mapper.readTree(resources);
            } catch (IOException ignored) { continue; }

            for (Extension ext : this.register.getExtensions()) {
                if ((definitions = tree.get(ext.getRootName())) == null) continue;

                fields = definitions.fieldNames();

                while (fields.hasNext()) {
                    definition = fields.next();

                    ext.getConfigurator().setCommonConfig(
                        ext.wrap(definition),
                        definitions.get(definition)
                    );

                    this.extensionMap.putIfAbsent(ext.getRootName(), new LinkedList<>());
                    this.extensionMap.get(ext.getRootName()).add(ext.wrap(definition));
                    this.definitions.put(ext.wrap(definition), definitions.get(definition));
                }
            }
        }
    }
}
