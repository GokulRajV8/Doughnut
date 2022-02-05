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
        this.ui.linker = this;
    }

    // Links for app modules
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