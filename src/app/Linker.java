package app;

public class Linker {
    // App modules
    private Core core;
    private UI ui;

    public void setCore(Core core) {
        this.core = core;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    // Links for app modules
    // Core
    public boolean CoreGetDrawingData(DrawingData drawingData) {
        return this.core.getDrawingData(drawingData);
    }

    public boolean CoreGetColors(String penColor, String bgColor) {
        return this.core.getColors(penColor, bgColor);
    }

    public void CoreSave() throws java.io.IOException{
        this.core.save();
    }

    public void undo() {
        this.core.undo();
    }

    public java.awt.image.BufferedImage CoreGetCanvas() {
        return this.core.getCanvas();
    }

    // UI
    public void UIStart(javafx.stage.Stage mainStage) {
        this.ui.start(mainStage);
    }
}