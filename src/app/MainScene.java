package app;

import java.awt.image.BufferedImage;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class MainScene extends Thread {
    @SuppressWarnings("unused")
    private UI hostUI;
    // buffered image for canvas
    private BufferedImage canvasImage;
    // Scene and elements
    public Scene scene;
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
    // flags for status
    private boolean isColorsDataOkay;
    private boolean isDrawingDataOkay;
    private boolean isCanvasUpdated;
    private boolean isBusy;
    // flag for scene state
    public boolean isActive;

    public MainScene(UI hostUI) throws java.io.IOException{
        this.hostUI = hostUI;

        // activating scene and setting flags
        this.isActive = true;
        this.isColorsDataOkay = true;
        this.isDrawingDataOkay = true;
        this.isBusy = false;

        // creating loader using FXML file and creating scene
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(new java.io.File("src/app/MainScene.fxml").toURI().toURL());
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

        // creation of events
        // click event for set colors button
        setColors.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // starting in a new thread to avoid UI update suspension
                new Thread() {
                    @Override
                    public void run() {
                        if(!isBusy) {
                            isBusy = true;
                            isColorsDataOkay = hostUI.linker.CoreSetColors(penColor.getText(), bgColor.getText());
                            if(isColorsDataOkay) {
                                // updating canvas image
                                canvasImage.getGraphics().drawImage(hostUI.linker.CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
                                isCanvasUpdated = true;
                            }
                            isBusy = false;
                        }
                    }
                }.start();
            }
        });
        // click event for setting drawing data
        setDrawingData.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // starting in a new thread to avoid UI update suspension
                new Thread() {
                    @Override
                    public void run() {
                        if(!isBusy) {
                            isBusy = true;

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
                                isDrawingDataOkay = false;
                                isBusy = false;
                                return;
                            }

                            // setting drawing data
                            DrawingData drawingData = new DrawingData();
                            isDrawingDataOkay = drawingData.setRadii(Integer.parseInt(movingWheelRadius.getText()),
                                                                     Integer.parseInt(staticWheelRadius.getText()),
                                                                     Integer.parseInt(movingWheelHoleRadius.getText()),
                                                                     isIn.isSelected());
                            drawingData.setAngles(Integer.parseInt(movingWheelHoleAngle.getText()),
                                                  Integer.parseInt(startAngle.getText()),
                                                  Integer.parseInt(endAngle.getText()));
                            drawingData.setPenConditions(isOpaque.isSelected(), isThick.isSelected());

                            // sending drawing data to core if data is okay
                            if(isDrawingDataOkay) {
                                hostUI.linker.CoreSetDrawingData(drawingData);

                                // updating canvas image
                                canvasImage.getGraphics().drawImage(hostUI.linker.CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
                                isCanvasUpdated = true;
                            }

                            isBusy = false;
                        }
                    }
                }.start();
            }
        });
        // click event for undoing the last drawing
        undo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // starting in a new thread to avoid UI update suspension
                new Thread() {
                    @Override
                    public void run() {
                        if(!isBusy) {
                            isBusy = true;
                            hostUI.linker.CoreUndo();

                            // updating canvas image
                            canvasImage.getGraphics().drawImage(hostUI.linker.CoreGetCanvas().getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
                            isCanvasUpdated = true;
                            isBusy = false;
                        }
                    }
                }.start();
            }
        });
        // click event for saving the image
        save.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // starting in a new thread to avoid UI update suspension
                new Thread() {
                    @Override
                    public void run() {
                        if(!isBusy) {
                            isBusy = true;
                            hostUI.linker.CoreSave();
                            isBusy = false;
                        }
                    }
                }.start();
            }
        });
    }

    // scene updater
    @Override
    public void run() {
        System.out.println("Main scene update started");

        // polling UI thread until the scene is active
        while(this.isActive) {
            // to prevent polling from happening more than 50 times a second
            try {
                Thread.sleep((long)(1000.0/50.0));
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            // UI update as a runnable polled through Platform.runLater
            javafx.application.Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // update the canvas when update is available
                    if(isCanvasUpdated) {
                        canvas.setImage(javafx.embed.swing.SwingFXUtils.toFXImage(canvasImage, null));
                        isCanvasUpdated = false;
                    }

                    // update status label based on status
                    if(isBusy) {
                        status.setText("Status : BUSY");
                        status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.valueOf("#8888ff"), null, null)));
                    }
                    else if(!isColorsDataOkay || !isDrawingDataOkay) {
                        status.setText("Status : ERROR");
                        status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.valueOf("#ff8888"), null, null)));
                    }
                    else {
                        status.setText("Status : OK");
                        status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.valueOf("#88ff88"), null, null)));
                    }
                }
            });
        }

        System.out.println("Main scene update stopped");
    }
}
