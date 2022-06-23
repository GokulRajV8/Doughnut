package root.app;

public class DrawingData {
    // wheels data
    private int movingWheelRadius;
    private int staticWheelRadius;
    private boolean isIn;
    // hole data
    private int movingWheelHoleRadius;
    private double movingWheelHoleAngle;
    // moving wheel position
    private int startAngle;
    private int endAngle;
    // pen conditions
    private boolean isOpaque;
    private boolean isThick;

    public DrawingData() {
        // defaulting values to prevent no data error while get methods are called
        this.movingWheelRadius = 10;
        this.staticWheelRadius = 100;
        this.isIn = true;
        this.movingWheelHoleRadius = 5;
        this.movingWheelHoleAngle = 0;
        this.startAngle = 0;
        this.endAngle = 360;
        this.isOpaque = true;
        this.isThick = false;
    }

    // get methods
    public int getMovingWheelRadius() {
        return this.movingWheelRadius;
    }

    public int getStaticWheelRadius() {
        return this.staticWheelRadius;
    }

    public boolean getIsIn() {
        return this.isIn;
    }

    public int getMovingWheelHoleRadius() {
        return this.movingWheelHoleRadius;
    }

    public double getMovingWheelHoleAngle() {
        return this.movingWheelHoleAngle;
    }

    public int getStartAngle() {
        return this.startAngle;
    }

    public int getEndAngle() {
        return this.endAngle;
    }

    public boolean isOpaque() {
        return this.isOpaque;
    }

    public boolean isThick() {
        return this.isThick;
    }

    // set methods
    public boolean setRadii(int movingWheelRadius, int staticWheelRadius, int movingWheelHoleRadius, boolean isIn) {
        // validation
        // radii cannot be less than zero and the drawing cannot cross the canvas edges
        if(movingWheelRadius < 0)
            return false;
        if(staticWheelRadius < 0)
            return false;
        if(movingWheelHoleRadius < 0)
            return false;
        if(isIn && (staticWheelRadius - movingWheelRadius + movingWheelHoleRadius > 2000))
            return false;
        if(!isIn && (staticWheelRadius + movingWheelRadius + movingWheelHoleRadius > 2000))
            return false;

        this.isIn = isIn;
        this.movingWheelRadius = movingWheelRadius;
        this.staticWheelRadius = staticWheelRadius;
        this.movingWheelHoleRadius = movingWheelHoleRadius;
        return true;
    }

    public void setPenConditions(boolean isOpaque, boolean isThick) {
        this.isOpaque = isOpaque;
        this.isThick = isThick;
    }

    public void setAngles(int movingWheelHoleStartAngle, int startAngle, int endAngle) {
        this.movingWheelHoleAngle = movingWheelHoleStartAngle;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public void setMovingWheelHoleAngle(double movingWheelHoleAngle) {
        this.movingWheelHoleAngle = movingWheelHoleAngle;
    }
}