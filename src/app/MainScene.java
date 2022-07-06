package app;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class MainScene {
    // scene
    public Scene scene;
    // buffered image for canvas
    private BufferedImage canvasImage;
    // scene elements
    private ImageView canvas;
    private Label status;
    private TextField bgColor;
    private TextField penColor;
    private TextField staticWheelRadius;
    private TextField movingWheelRadius;
    private TextField movingWheelHoleRadius;
    private TextField movingWheelHoleAngle;
    private TextField startAngle;
    private TextField endAngle;
    private CheckBox isIn;
    private CheckBox isThick;
    private CheckBox isOpaque;
    private Button setColors;
    private Button setDrawingData;
    private Button undo;
    private Button save;

    public MainScene(UI hostUI) throws java.io.IOException {
        // creating loader using FXML file and creating scene
        FXMLLoader loader = new FXMLLoader(new java.io.File("../../resources/MainScene.fxml").toURI().toURL());
        this.scene = new Scene(loader.load(), 1003, 802, true);

        // image for canvas
        this.canvasImage = new BufferedImage(800, 800, BufferedImage.TYPE_3BYTE_BGR);

        // mapping scene elements
        // canvas
        this.canvas = (ImageView)loader.getNamespace().get("canvas");
        // label
        this.status = (Label)loader.getNamespace().get("status");
        // fields
        this.bgColor = (TextField)loader.getNamespace().get("bgColor");
        this.penColor = (TextField)loader.getNamespace().get("penColor");
        this.staticWheelRadius = (TextField)loader.getNamespace().get("staticWheelRadius");
        this.movingWheelRadius = (TextField)loader.getNamespace().get("movingWheelRadius");
        this.movingWheelHoleRadius = (TextField)loader.getNamespace().get("movingWheelHoleRadius");
        this.movingWheelHoleAngle = (TextField)loader.getNamespace().get("movingWheelHoleAngle");
        this.startAngle = (TextField)loader.getNamespace().get("startAngle");
        this.endAngle = (TextField)loader.getNamespace().get("endAngle");
        // checkboxes
        this.isIn = (CheckBox)loader.getNamespace().get("isIn");
        this.isThick = (CheckBox)loader.getNamespace().get("isThick");
        this.isOpaque = (CheckBox)loader.getNamespace().get("isOpaque");
        // buttons
        this.setColors = (Button)loader.getNamespace().get("setColors");
        this.setDrawingData = (Button)loader.getNamespace().get("setDrawingData");
        this.undo = (Button)loader.getNamespace().get("undo");
        this.save = (Button)loader.getNamespace().get("save");

        // creating events
        // click event for set colors button
        setColors.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // setting colors
                if(hostUI.getContainer().CoreSetColors(penColor.getText(), bgColor.getText())) {
                    // updating canvas image
                    canvasImage.getGraphics().drawImage(hostUI.getContainer().CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

                    // scene update - updating canvas and setting status as OK
                    updateScene("Canvas : UPDATE");
                    updateScene("Status : SET : OK");
                }
                else {
                    // scene update - setting status as ERROR
                    updateScene("Status : SET : ERROR");
                }
            }
        });

        // click event for setting drawing data
        setDrawingData.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // validation
                try{
                    // to catch exceptions
                    Integer.parseInt(staticWheelRadius.getText());
                    Integer.parseInt(movingWheelRadius.getText());
                    Integer.parseInt(movingWheelHoleRadius.getText());
                    Integer.parseInt(movingWheelHoleAngle.getText());
                    Integer.parseInt(startAngle.getText());
                    Integer.parseInt(endAngle.getText());
                } catch (Exception exception) {
                    // scene update - setting status as ERROR
                    updateScene("Status : SET : ERROR");
                    return;
                }

                // setting drawing data and sending to core if data is okay
                DrawingData drawingData = new DrawingData();
                drawingData.setAngles(Integer.parseInt(movingWheelHoleAngle.getText()), Integer.parseInt(startAngle.getText()), Integer.parseInt(endAngle.getText()));
                drawingData.setPenConditions(isOpaque.isSelected(), isThick.isSelected());
                if(drawingData.setRadii(Integer.parseInt(movingWheelRadius.getText()), Integer.parseInt(staticWheelRadius.getText()),
                                        Integer.parseInt(movingWheelHoleRadius.getText()), isIn.isSelected())) {
                    // setting drawing data
                    hostUI.getContainer().CoreSetDrawingData(drawingData);

                    // updating canvas image
                    canvasImage.getGraphics().drawImage(hostUI.getContainer().CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

                    // scene update - updating canvas and setting status as OK
                    updateScene("Canvas : UPDATE");
                    updateScene("Status : SET : OK");
                }
                else {
                    // scene update - setting status as ERROR
                    updateScene("Status : SET : ERROR");
                }
            }
        });

        // click event for undoing the last drawing
        undo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // undoing last drawing
                hostUI.getContainer().CoreUndo();

                // updating canvas image
                canvasImage.getGraphics().drawImage(hostUI.getContainer().CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

                // scene update - updating canvas
                updateScene("Canvas : UPDATE");
            }
        });

        // click event for saving the image
        save.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // saving the canvas image
                hostUI.getContainer().CoreSave();
            }
        });
    }

    // scene updates polled through Platform.runLater
    private void updateScene(String updateName) {
        // setting status as OK
        if(updateName.compareTo("Status : SET : OK") == 0)
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    status.setText("Status : OK");
                    status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.valueOf("#88ff88"), null, null)));
                }
            });

        // setting status as ERROR
        else if(updateName.compareTo("Status : SET : ERROR") == 0)
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    status.setText("Status : ERROR");
                    status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.valueOf("#ff8888"), null, null)));
                }
            });

        // updating canvas
        else if(updateName.compareTo("Canvas : UPDATE") == 0)
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    canvas.setImage(javafx.embed.swing.SwingFXUtils.toFXImage(canvasImage, null));
                }
            });
    }
}
