package app;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UI {
    // host container
    private Container container;
    // scenes
    private MainScene mainScene;

    public UI(Container container) {
        // setting host container
        this.container = container;
    }

    public Container getContainer() {
        return this.container;
    }

    public void start(Stage mainStage) {
        // creating main scene
        try {
            this.mainScene = new MainScene(this);
        } catch (java.io.IOException e){
            System.out.println("Cannot create Main scene since .fxml file is not present");
            System.exit(1);
        }

        // setting up stage and displaying
        mainStage.setTitle("Doughnut");
        mainStage.setResizable(false);
        mainStage.setScene(this.mainScene.scene);
        mainStage.show();

        // shutdown event for window close
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                javafx.application.Platform.exit();
                System.exit(0);
            }
        });
    }
}