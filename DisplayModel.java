import java.awt.Color;

public class DisplayModel {
    private final Color color1;
    private final Color color2;
    private float angle;

    public DisplayModel() {
        color1 = new Color(0, 0, 139); //warna biru tua
        color2 = new Color(135, 206, 250); //biru langit
        angle = 0;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }

    public float getAngle() {
        return angle;
    }

    public void updateAngle(float increment) {
        angle += increment;
        if (angle > Math.PI * 2) {
            angle = 0;
        }
    }
}
