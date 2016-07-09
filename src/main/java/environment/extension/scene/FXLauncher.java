package environment.extension.scene;

import javafx.application.Application;
import javafx.stage.Stage;

final class FXLauncher {

    private FXLauncher() {
        throw new AssertionError("Class cannot be instantiated.");
    }

    static void launchApp(AppLaunch appLaunch, String... args) {
        App.appLaunch = appLaunch;
        App.launch(App.class, args);
    }

    public static class App extends Application {

        private static AppLaunch appLaunch;

        @Override
        public void start(Stage primaryStage) throws Exception {
            if (appLaunch != null) {
                appLaunch.start(this, primaryStage);
            }
        }

        @Override
        public void init() throws Exception {
            if (appLaunch != null) {
                appLaunch.init(this);
            }
        }

        @Override
        public void stop() throws Exception {
            if (appLaunch != null) {
                appLaunch.stop(this);
            }
        }
    }

    @FunctionalInterface
    public interface AppLaunch {
        void start(Application app, Stage stage) throws Exception;

        default void init(Application app) throws Exception {
        }

        default void stop(Application app) throws Exception {
        }
    }
}
