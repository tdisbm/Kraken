package kraken.component.util.instance;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class InstanceLoader {

    public static Object newInstance(Class<?> clazz, ArrayList<?> arguments) {
        boolean withoutArgs = arguments == null || arguments.size() == 0;
        Object instance = null;

        if (withoutArgs) {
            try {
                instance = clazz.newInstance();
            } catch (Exception ignored) {
            }
        } else {
            Constructor[] constructors = clazz.getDeclaredConstructors();
            Object[] args = arguments.toArray();

            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == args.length) {
                    try {
                        instance = constructor.newInstance(args);
                        break;
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return instance;
    }

    public static LinkedHashMap<String, Class<?>> getClasses(LinkedHashMap<String, ?> collection) {
        LinkedHashMap<String, Class<?>> classes = new LinkedHashMap<>();
        String namespace;

        for (Map.Entry<String, ?> item : collection.entrySet()) {
            namespace = (String) item.getValue();
            try {
                classes.put(item.getKey(), Class.forName(namespace));
            } catch (ClassNotFoundException e) {
                System.out.format("Fatal error: Can't find class %s", namespace);
                System.exit(1);
            }
        }

        return classes;
    }

}
