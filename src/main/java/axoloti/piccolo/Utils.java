package axoloti.piccolo;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Utils {

    public static Point asPoint(Point2D p) {
        return new Point((int) p.getX(), (int) p.getY());
    }
}
