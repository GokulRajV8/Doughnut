package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class Container extends Application{
    // App modules
    public static Core core;
    public static UI ui;
    // Linker to connect all app modules
    public static Linker linker;

    // start method to create JavaFX UI
    public void start(Stage mainStage) throws Exception {
        // creating UI
        ui = new UI();

        // linking UI module to Linker
        linker.setUI(ui);

        // starting UI module
        linker.UIStart(mainStage);
    }

    public static void main(String args[]) {
        // creating linker
        linker = new Linker();

        // creating Core
        core = new Core();

        // linking Core module to Linker
        linker.setCore(core);

        // welcome prompt
        System.out.println("Welcome to Spiro!!");

        // launching the app
        launch();
    }
}