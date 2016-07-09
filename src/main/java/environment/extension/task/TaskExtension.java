package environment.extension.task;

import com.fasterxml.jackson.databind.JsonNode;
import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.component.tree_builder.nodes.StringNode;
import environment.unit.Extension;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskExtension extends Extension
{
    private static final long OUT_OF_MEMORY_DELAY = 20;

    private static final int  OUT_OF_MEMORY_ADDITIONAL_DELAY = 2000000;

    @Override
    public void map(Object definition, JsonNode prototype) throws Exception {
        if (!(definition instanceof Task)) {
            throw new Exception("tasks must be instance of " + Task.class.getName());
        }

        Task task = (Task) definition;
        task.setRate(prototype.get("rate").intValue());

        if (!runSafeThreads(task)) {
            System.out.println("Warning: Task '" + definition.getClass().getName() + "' is not running");
        }
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) {
        return treeBuilder.setRoot("tasks")
            .addChild(new InstanceNode("class"))
            .addChild(new StringNode("rate"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }

    private boolean runSafeThreads(Task task) {
        boolean success = false;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try {
            executor.scheduleAtFixedRate(task, 0, task.getRate(), TimeUnit.MILLISECONDS);
            success =  true;
        } catch (OutOfMemoryError e) {
            try {
                executor.wait(OUT_OF_MEMORY_DELAY, OUT_OF_MEMORY_ADDITIONAL_DELAY);
                runSafeThreads(task);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                success = false;
            }
        }

        return success;
    }
}
