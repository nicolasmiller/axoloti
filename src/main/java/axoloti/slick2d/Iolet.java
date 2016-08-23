package axoloti.slick2d;

import org.newdawn.slick.gui.GUIContext;

public class Iolet extends Slick2dComponent {
    private Slick2dRenderer canvas;
    public Iolet(GUIContext container, int x, int y,
            int width, int height, Slick2dRenderer canvas) {
        super(container, x, y,
                width, height, canvas);
        this.canvas = canvas;
    }
    
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        int ox = newx;
        int oy = newy;
        oldx -= canvas.trans_x;
        oldy -= canvas.trans_y;
        oldx /= canvas.scale;
        oldy /= canvas.scale;
        newx -= canvas.trans_x;
        newy -= canvas.trans_y;
        newx /= canvas.scale;
        newy /= canvas.scale;
        Slick2dComponent p = parent;
        while (p != null) {
            oldx -= p.getX();
            oldy -= p.getY();
            newx -= p.getX();
            newy -= p.getY();
            p = p.parent;
        }
        updateOver(newx, newy);
        if (over) {
            if(canvas.getDragNet() == null && canvas.selection == null) {
                canvas.addDragNet(this);
            }
        }
    }
    
    @Override
    public void mouseReleased(int button, int mx, int my) {
        super.mouseReleased(button, mx, my);
        if(over) {
            canvas.endDragNet(this);
        }
    }
}