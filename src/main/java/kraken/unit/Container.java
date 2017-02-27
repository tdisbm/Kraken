package kraken.unit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kraken.Register;
import kraken.container.ContainerResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class Container
{
    private Map<String, List<String>> extensionMap;
    private Map<String, Object> rawDefinitions;
    private Map<String, Object> definitions;

    private ObjectMapper mapper;
    private Register register;

    private boolean __compiled__ = false;
    private static Container instance;

    private Container() {
        rawDefinitions = new LinkedHashMap<>();
        extensionMap = new LinkedHashMap<>();
        definitions = new LinkedHashMap<>();
        mapper = new ObjectMapper(new YAMLFactory());

        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    public static Container getInstance() {
        if (instance == null) {
            instance = new Container();
        }

        return instance;
    }

    final public Container set(String id, Object definition) {
        if (this.__compiled__) {
            System.out.println("Container is already compiled, ca't set any definition");
        }

        if (id != null && !id.isEmpty()) {
            this.definitions.put(id, definition);
        }

        return this;
    }

    final public <T> T get(String id) {
        return (T) this.definitions.get(id);
    }

    final public <T> T getRaw(String id) {
        return __compiled__ ? null : (T) rawDefinitions.get(id);
    }

    final public Map<String, Object> getRaw() {
        return __compiled__ ? null : rawDefinitions;
    }

    final public LinkedHashMap<String, Object> getRawByExtensionRoot(String root) {
        LinkedHashMap<String, Object> collection = new LinkedHashMap<>();

        for (Extension ext : this.register.getExtensions()) {
            if (!ext.getRootName().equals(root)) {
                continue;
            }

            for (Map.Entry<String, Object> definition : this.rawDefinitions.entrySet()) {
                if (!this.extend(definition.getKey(), ext)) {
                    continue;
                }

                collection.put(definition.getKey(), definition.getValue());
            }
        }

        return collection;
    }

    final public boolean has(String id) {
        return !(this.definitions.get(id) == null);
    }

    final public boolean hasRaw(String id) {
        return !(this.rawDefinitions.get(id) == null);
    }

    final public void compile() {
        if (this.__compiled__) {
            return;
        }

        this.loadResources();

        for (ContainerResolver resolver : this.register.getResolvers()) {
            resolver.setExtensions(Container.this.register.getExtensions());
            resolver.resolve();
        }

        for (Extension e : this.register.getExtensions()) {
            e.mapping();
        }

        this.__compiled__ = true;
    }

    final public Container setRegister(Register register) {
        this.register = register;

        return this;
    }

    final public boolean hasExtension(String definition) {
        for (Map.Entry<String, List<String>> ext : this.extensionMap.entrySet()) {
            for (String s : ext.getValue()) {
                if (definition.equals(s)) {
                    return true;
                }
            }
        }

        return false;
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
        JsonNode raw;
        JsonNode tree;
        String definition;

        for (InputStream resource : this.register.getResources()) {
            if (null == (tree = this.loadResource(resource))) {
                continue;
            }

            for (Extension ext : this.register.getExtensions()) {
                if (null == (raw = tree.get(ext.getRootName()))) continue;

                fields = raw.fieldNames();

                while (fields.hasNext()) {
                    definition = fields.next();
                    ext.getConfigurator().set(ext.wrap(definition), raw.get(definition));

                    if (this.extensionMap.get(ext.getRootName()) == null) {
                        this.extensionMap.put(ext.getRootName(), new LinkedList<>());
                    }

                    this.extensionMap.get(ext.getRootName()).add(ext.wrap(definition));
                    this.rawDefinitions.put(ext.wrap(definition), raw.get(definition));
                }
            }
        }
    }

    private JsonNode loadResource(InputStream resource) {
        try {
            return this.mapper.readTree(resource);
        } catch (IOException e) {
            return null;
        }
    }
}
