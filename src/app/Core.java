package app;

import java.io.IOException;
import java.awt.image.BufferedImage;

public class Core {
    private BufferedImage canvas;

    public boolean getDrawingData(DrawingData drawingData) {
        // gets drawing data and returns whether data is okay
        return true;
    }

    public boolean getColors(String penColor, String bgColor) {
        // get pen color and background color of canvas and returns whether data is okay
        return true;
    }

    public void save() throws IOException{
        // save canvas as a file
    }

    public void undo() {
        // undo last change in the drawing
    }

    public BufferedImage getCanvas() {
        // returns canvas
        return this.canvas;
    }
}