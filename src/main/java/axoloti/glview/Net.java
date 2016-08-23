package axoloti.glview;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

public class Net extends AxoComponent {
    private Iolet source;
    private Iolet dest;
    private Point end;
    private LWJGLTest canvas;
    
    public Net(GUIContext container, int x, int y,
            int width, int height, LWJGLTest canvas) {
        super(container, x, y,
                width, height, canvas);
        this.canvas = canvas;
    }
    
    float CtrlPointY(float x1, float y1, float x2, float y2) {
        return Math.max(y1, y2) + Math.abs(y2 - y1) * 0.1f + Math.abs(x2 - x1) * 0.1f;
    }

    public void render(GUIContext container, Graphics g) {
        g.setColor(Color.black);
        g.setLineWidth(3 * canvas.scale);
        Point start = source.getWorldLocation();
        if(dest != null) {
            end = dest.getWorldLocation();
            end.setX(end.getX() + dest.getWidth() / 2);
            end.setY(end.getY() + dest.getHeight() / 2);
        }
        if(end == null) {
            end = start;
        }
        int x1 = (int) start.getX() + source.getWidth() / 2;
        int x2 = (int) end.getX();
        int y1 = (int) start.getY() + source.getHeight() / 2;
        int y2 = (int) end.getY();
        Vector2f v_start = new Vector2f(x1, y1);
        Vector2f v_end = new Vector2f(x2, y2);
        Vector2f ctrl = new Vector2f((x1 + x2) / 2, CtrlPointY(x1, y1, x2, y2));
        Vector2f ctrl1 = ctrl.add(v_start).scale(0.5f);
        Vector2f ctrl2 = ctrl.add(v_start).scale(0.5f);
        Curve c = new Curve(v_start,
                ctrl1,
                ctrl2,
                v_end);
        g.setAntiAlias(true);
        g.draw(c);
        
        g.setAntiAlias(false);
        g.setLineWidth(1);
    }
    
    public void setEnd(Point p) {
        this.end = p;
    }
    
    public void setSource(Iolet i) {
        this.source = i;
    }
    
    public void setDest(Iolet i) {
        this.dest = i;
    }
    
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
    
    @Override
    public void mousePressed(int button, int mx, int my) {

    }
    
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {

    }
}