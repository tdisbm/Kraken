package environment.component.tree_builder.chain;

import java.util.ArrayList;
import java.util.List;

public class SupportChain
{
    private List<Class> supports = new ArrayList<>();

    public SupportChain add(Class clazz) {
        if (!this.supports.contains(clazz)) {
            this.supports.add(clazz);
        }

        return this;
    }

    public boolean supports (Object obj) {
        for (Class<?> support : this.supports) {
            if (obj == null) {
                if (support == null) {
                    return true;
                }

                continue;
            }

            if (support.getName().equals(obj.getClass().getName())) {
                return true;
            }
        }

        return false;
    }
}
