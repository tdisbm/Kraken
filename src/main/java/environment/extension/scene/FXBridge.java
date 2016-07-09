package environment.extension.scene;

import environment.unit.Container;
import environment.extension.scene.controller.Controller;
import environment.extension.scene.controller.ControllerSwitcher;


class FXBridge {
    private ControllerSwitcher switcher;

    private Container container;

    final void up(Controller main, String ...args) {
        if (main == null || this.container == null) {
            System.out.println("Invalid controller or null container provided to FXBridge");
            return;
        }

        Thread master = new Thread(() -> {
            FXLauncher.launchApp((app, stage) -> {
                switcher = new ControllerSwitcher(stage);
                switcher.setContainer(this.container);
                switcher.load(main).show();
            }, args);
        });

        master.start();
    }

    public void setContainer(Container container) {
        this.container = container;
    }
}

