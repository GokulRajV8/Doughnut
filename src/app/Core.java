package app;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class Core {
    // host container
    private Container container;
    // drawing data
    private DrawingData drawingData;
    // every point in the drawing is represented by an integer as below :
    // x,rrr,ggg,bbb is a 10 digit integer where,
    // x is 0 if background and 1 if marked
    // rrr - red 000 - 255; ggg - green 000 - 255; bbb - blue 000 - 255
    private int[][] drawing;
    private int[][] lastDrawing;
    // colors
    private Color penColor;
    private Color bgColor;

    public Core(Container container) {
        // setting host container
        this.container = container;

        // initialising drawing with 4000x4000 points
        this.drawing = new int[4000][4000];
        this.lastDrawing = new int[4000][4000];

        // making plain drawings
        for(int i = 0; i < 4000; ++i)
            for(int j = 0; j < 4000; ++j) {
                this.drawing[i][j] = 0;
                this.lastDrawing[i][j] = 0;
            }

        // setting default colors
        this.penColor = Color.BLACK;
        this.bgColor = Color.WHITE;
    }

    public Container getContainer() {
        return this.container;
    }

    public boolean setColors(String penColor, String bgColor) {
        // checking string validity
        if(penColor.substring(0, 1).compareTo("#") != 0 || penColor.length() != 7 || bgColor.substring(0, 1).compareTo("#") != 0 || bgColor.length() != 7)
            return false;

        // checking colors
        try {
            // to catch exceptions
            new Color(Integer.valueOf(penColor.substring(1, 3), 16), Integer.valueOf(penColor.substring(3, 5), 16), Integer.valueOf(penColor.substring(5, 7), 16));
            new Color(Integer.valueOf(bgColor.substring(1, 3), 16), Integer.valueOf(bgColor.substring(3, 5), 16), Integer.valueOf(bgColor.substring(5, 7), 16));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        // setting colors
        this.penColor = new Color(Integer.valueOf(penColor.substring(1, 3), 16), Integer.valueOf(penColor.substring(3, 5), 16), Integer.valueOf(penColor.substring(5, 7), 16));
        this.bgColor = new Color(Integer.valueOf(bgColor.substring(1, 3), 16), Integer.valueOf(bgColor.substring(3, 5), 16), Integer.valueOf(bgColor.substring(5, 7), 16));
        return true;
    }

    // sets drawing data and draws with it
    public void setDrawingData(DrawingData drawingData) {
        // setting drawing data
        this.drawingData = drawingData;

        // moving drawing to last drawing
        for(int i = 0; i < 4000; ++i)
            for(int j = 0; j < 4000; ++j)
                lastDrawing[i][j] = drawing[i][j];

        // defaulting pen co-ordinates to top-left corner
        int penX = 0;
        int penY = 0;

        // setting radius of moving wheel orbit, moving wheel angle change and moving wheel hole angle change
        int movingWheelOrbitRadius = this.drawingData.getStaticWheelRadius();
        // moving wheel moves 0.02 degrees per iteration
        double movingWheelAngleChange = (this.drawingData.getStartAngle() < this.drawingData.getEndAngle()) ? 0.02 : -0.02;
        double movingWheelHoleAngleChange = movingWheelAngleChange;
        if(this.drawingData.getIsIn()) {
            movingWheelOrbitRadius -= this.drawingData.getMovingWheelRadius();
            movingWheelHoleAngleChange -= movingWheelAngleChange * (double)this.drawingData.getStaticWheelRadius() / (double)this.drawingData.getMovingWheelRadius();
        }
        else {
            movingWheelOrbitRadius += this.drawingData.getMovingWheelRadius();
            movingWheelHoleAngleChange += movingWheelAngleChange * (double)this.drawingData.getStaticWheelRadius() / (double)this.drawingData.getMovingWheelRadius();
        }

        // drawing logic
        double movingWheelPosition = this.drawingData.getStartAngle();
        while(true) {
            // removing full circle rotations for simplicity and converting angles from degrees to radians
            double smallWheelAngle = (movingWheelPosition - ((int)movingWheelPosition / 360 * 360.0)) * Math.PI / 180.0;
            double smallWheelHoleAngle = (this.drawingData.getMovingWheelHoleAngle() - ((int)this.drawingData.getMovingWheelHoleAngle() / 360 * 360.0)) * Math.PI / 180.0;

            // getting pen co-ordinates
            penX = 2000 + (int)((double)movingWheelOrbitRadius * Math.cos(smallWheelAngle) + (double)this.drawingData.getMovingWheelHoleRadius() * Math.cos(smallWheelHoleAngle));
            penY = 2000 - (int)((double)movingWheelOrbitRadius * Math.sin(smallWheelAngle) + (double)this.drawingData.getMovingWheelHoleRadius() * Math.sin(smallWheelHoleAngle));

            // ignoring right and bottom edges
            if(penX == 4000 || penY == 4000) {
                // updating moving wheel angle and moving wheel hole angle
                movingWheelPosition += movingWheelAngleChange;
                this.drawingData.setMovingWheelHoleAngle(this.drawingData.getMovingWheelHoleAngle() + movingWheelHoleAngleChange);

                // exit when moving wheel has crossed end angle
                // multiplied by moving wheel angle change to work in both the directions
                if(movingWheelPosition * movingWheelAngleChange > this.drawingData.getEndAngle() * movingWheelAngleChange)
                    break;
                continue;
            }

            // marking on the drawing
            // default mark at the point
            this.mark(penX, penY);

            // conditional mark for thickness
            if(this.drawingData.isThick()) {
                if(penX != 0)
                    this.mark(penX - 1, penY);
                if(penX != 3999)
                    this.mark(penX + 1, penY);
                if(penY != 0)
                    this.mark(penX, penY - 1);
                if(penY != 3999)
                    this.mark(penX, penY + 1);
            }

            // updating moving wheel angle and moving wheel hole angle
            movingWheelPosition += movingWheelAngleChange;
            this.drawingData.setMovingWheelHoleAngle(this.drawingData.getMovingWheelHoleAngle() + movingWheelHoleAngleChange);

            // exit when moving wheel has crossed end angle
            // multiplied by moving wheel angle change to work in both the directions
            if(movingWheelPosition * movingWheelAngleChange > this.drawingData.getEndAngle() * movingWheelAngleChange)
                break;
        }
    }

    // returns a canvas with the complete drawing
    public BufferedImage getCanvas() {
        // giving background color to the drawing
        int[][] tempdrawing = new int[4000][4000];
        for(int i = 0; i < 4000; ++i)
            for(int j = 0; j < 4000; ++j) {
                if(this.drawing[i][j] > 999999999) {
                    tempdrawing[i][j] = this.drawing[i][j] - 1000000000;
                }
                else {
                    tempdrawing[i][j] = this.bgColor.getRed() * 1000000 + this.bgColor.getGreen() * 1000 + this.bgColor.getBlue();
                }
            }

        // canvas to hold the final image
        BufferedImage canvas = new BufferedImage(4000, 4000, BufferedImage.TYPE_3BYTE_BGR);

        // marking on canvas
        for(int i = 0; i < 4000; ++i)
            for(int j = 0; j < 4000; ++j) {
                int point, red, green, blue;
                point = tempdrawing[i][j];
                red = point / 1000000;
                point %= 1000000;
                green = point / 1000;
                point %= 1000;
                blue = point;
                canvas.setRGB(i, j, new Color(red, green, blue).getRGB());
            }

        return canvas;
    }

    // saves the complete drawing as a .png file
    public void save() {
        try {
            javax.imageio.ImageIO.write(this.getCanvas(), "png", new java.io.File("output.png"));
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // interchanging drawing with last drawing
    public void undo() {
        // temp point to swap points in drawings
        int tempPoint = 0;
        for(int i = 0; i < 4000; ++i)
            for(int j = 0; j < 4000; ++j) {
                tempPoint = this.drawing[i][j];
                this.drawing[i][j] = this.lastDrawing[i][j];
                this.lastDrawing[i][j] = tempPoint;
            }
    }

    // private methods
    // marks on the drawing
    private void mark(int penX, int penY) {
        // initialising marking colors
        int pointRed = this.penColor.getRed();
        int pointGreen = this.penColor.getGreen();
        int pointBlue = this.penColor.getBlue();

        // computing marking colors if not opaque
        if(!this.drawingData.isOpaque()) {
            int existingColor = this.drawing[penX][penY] % 1000000000;
            pointRed += existingColor / 1000000;
            if(pointRed > 255)
                pointRed = 255;
            existingColor %= 1000000;
            pointGreen += existingColor / 1000;
            if(pointGreen > 255)
                pointGreen = 255;
            existingColor %= 1000;
            pointBlue += existingColor;
            if(pointBlue > 255)
                pointBlue = 255;
        }

        // marking the point
        this.drawing[penX][penY] = 1000000000 + 1000000 * pointRed + 1000 * pointGreen + pointBlue;
    }
}