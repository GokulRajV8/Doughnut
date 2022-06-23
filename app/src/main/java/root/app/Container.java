package root.app;

public class Container {
    // modules
    private Core core;
    private UI ui;

    public Container() {
        // creating modules
        core = new Core(this);
        ui = new UI(this);
    }

    // links for modules
    // Core
    public boolean CoreSetColors(String penColor, String bgColor) {
        return this.core.setColors(penColor, bgColor);
    }

    public void CoreSetDrawingData(DrawingData drawingData) {
        this.core.setDrawingData(drawingData);
    }

    public java.awt.image.BufferedImage CoreGetCanvas() {
        return this.core.getCanvas();
    }

    public void CoreSave() {
        this.core.save();
    }

    public void CoreUndo() {
        this.core.undo();
    }

    // UI
    public void UIStart(javafx.stage.Stage mainStage) {
        this.ui.start(mainStage);
    }
}