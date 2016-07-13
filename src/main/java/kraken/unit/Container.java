package kraken.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kraken.AppRegister;
import kraken.container.ContainerResolver;

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

        for (Extension ext : this.register.getExtensions()) {
            if (!ext.getRootName().equals(root)) {
                continue;
            }

            for (Map.Entry<String, Object> definition : this.definitions.entrySet()) {
                if (!this.extend(definition.getKey(), ext)) {
                    continue;
                }

                collection.put(definition.getKey(), definition.getValue());
            }
        }

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

        for (ContainerResolver resolver : this.register.getResolvers()) {
            resolver.setExtensions(Container.this.register.getExtensions());
            resolver.resolve();
        }

        for (Extension e : this.register.getExtensions()) {
            e.mapping();
        }

        this.__compiled__ = true;
    }

    final public Container setRegister(AppRegister register) {
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

                    ext.getConfigurator().set(
                        ext.wrap(definition),
                        definitions.get(definition)
                    );

                    if (this.extensionMap.get(ext.getRootName()) == null) {
                        this.extensionMap.put(ext.getRootName(), new LinkedList<String>());
                    }

                    this.extensionMap.get(ext.getRootName()).add(ext.wrap(definition));
                    this.definitions.put(ext.wrap(definition), definitions.get(definition));
                }
            }
        }
    }
}
