package root;

import root.app.Container;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    // app container
    private static Container container;

    // start method to create JavaFX UI
    public void start(Stage mainStage) throws Exception {
        // starting UI module
        container.UIStart(mainStage);
    }

    public static void main(String args[]) {
        // welcoming prompt
        System.out.println("Welcome to Doughnut 4000 x 4000");

        // creating app container
        container = new Container();

        // launching the application
        launch();
    }
}