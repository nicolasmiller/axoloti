package axoloti.piccolo;

import java.awt.Point;
import java.awt.geom.Point2D;
import org.piccolo2d.event.PInputEvent;

public class PUtils {

    public static Point asPoint(Point2D p) {
        return new Point((int) Math.round(p.getX()), (int) Math.round(p.getY()));
    }

    public static Point getPopupLocation(PInputEvent e) {

        Point2D location = e.getPickedNode().getBounds().getOrigin();
        location.setLocation(location.getX(),
                location.getY() + e.getPickedNode().getBounds().getHeight());

        e.getPath().getPathTransformTo(e.getPickedNode()).transform(location, location);
        return asPoint(location);
    }
}
