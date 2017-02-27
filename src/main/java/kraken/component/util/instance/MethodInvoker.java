package kraken.component.util.instance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodInvoker {
    public static <T> T invoke(Object object, String methodName, ArrayList<?> arguments) {
        Class[] classes = new Class[arguments.size()];
        Object[] args = new Object[arguments.size()];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = arguments.get(i).getClass();
            args[i] = arguments.get(i);
        }

        boolean foundOrNotExist = false;
        Class<?> tempClass = object.getClass();
        Method method = null;

        while(!foundOrNotExist) {
            try {
                if (tempClass == null) {
                    foundOrNotExist = true;
                    break;
                }

                if (arguments.size() == 0) {
                    method = tempClass.getDeclaredMethod(methodName);
                } else {
                    method = tempClass.getDeclaredMethod(methodName, classes);
                }

                foundOrNotExist = true;
            } catch (NoSuchMethodException e) {
                tempClass = tempClass.getSuperclass();
            }
        }

        if (method != null) {
            try {
                return (T) (arguments.size() == 0
                    ? method.invoke(object)
                    : method.invoke(object, args))
                ;
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
