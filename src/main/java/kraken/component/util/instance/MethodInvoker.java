package kraken.component.util.instance;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodInvoker {
    public static <T> T invoke(Object object, String methodName, ArrayList<?> arguments) throws NoSuchMethodException {
        boolean methodNotFound = true;
        boolean withoutArgs = arguments == null || arguments.size() <= 0;

        Class clazz = object.getClass();
        Object result = null;

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                try {
                    result = withoutArgs
                        ? method.invoke(object)
                        : method.invoke(object, arguments.toArray());
                    methodNotFound = false;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (methodNotFound) {
            throw new NoSuchMethodException();
        }

        return (T) result;
    }
}
