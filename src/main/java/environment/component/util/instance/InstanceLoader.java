package environment.component.util.instance;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InstanceLoader {

    public static Object newInstance(Class<?> clazz, ArrayList<?> arguments) {
        Class[] classes = new Class[arguments.size()];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = arguments.get(i).getClass();
        }

        try {
            return clazz.getConstructor(classes).newInstance(arguments.toArray());
        } catch (Exception e) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                e1.printStackTrace();
                System.exit(1);
            }
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static LinkedHashMap<String, Class<?>> getClasses(LinkedHashMap<String, ?> collection) {
        LinkedHashMap<String, Class<?>> classes = new LinkedHashMap<>();

        collection.forEach((key, clazz) -> {
            try {
                classes.put(key, Class.forName((String) clazz));
            } catch (ClassNotFoundException e) {
                System.out.format("Fatal error: Can't find class %s", clazz);
                System.exit(1);
            }
        });

        return classes;
    }

}
