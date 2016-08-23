package axoloti.glview;

import java.util.ArrayList;
import javax.swing.UIManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.geom.Rectangle;

public class AxoComponent extends AbstractComponent {

    protected boolean selected = false;
    private LWJGLTest canvas;

    private ArrayList<AxoComponent> children = new ArrayList<AxoComponent>();

    /**
     * The default state
     */
    private static final int NORMAL = 1;

    /**
     * The mouse down state
     */
    private static final int MOUSE_DOWN = 2;

    /**
     * The mouse over state
     */
    private static final int MOUSE_OVER = 3;

    /**
     * The colour used in normal state
     */
    private Color normalColor;

    /**
     * The colour used in mouseOver state
     */
    private Color mouseOverColor = Color.red;

    /**
     * The colour used in mouseDown state
     */
    private Color mouseDownColor = Color.blue;

    /**
     * The shape defining the area
     */
    protected Shape area;

    /**
     * The current normalImage being displayed
     */
    private Image currentImage;

    /**
     * The current color being used
     */
    private Color currentColor;
    private Color selectedColor;

    /**
     * True if the mouse is over the area
     */
    protected boolean over;

    /**
     * True if the mouse button is pressed
     */
    protected boolean mouseDown;

    /**
     * The state of the area
     */
    private int state = NORMAL;

    /**
     * True if the mouse has been up since last press
     */
    private boolean mouseUp;

    public AxoComponent(GUIContext container, int x, int y,
            int width, int height, LWJGLTest canvas) {
        this(container, new Rectangle(x, y, width, height));
        this.canvas = canvas;
    }

    public AxoComponent(GUIContext container, Shape shape) {
        super(container);

        area = shape;
        java.awt.Color foo = UIManager.getColor("Panel.background");
        normalColor = new Color(foo.getRed(), foo.getGreen(), foo.getBlue());
        this.mouseDownColor = Color.darkGray;
        this.mouseOverColor = new Color(normalColor);
        this.mouseOverColor.a = 0.5f;
        this.selectedColor = mouseOverColor;

        currentColor = normalColor;

        state = NORMAL;
        Input input = container.getInput();
        over = area.contains(input.getMouseX(), input.getMouseY());
        mouseDown = input.isMouseButtonDown(0);
        updateImage();
    }

    public void setLocation(float x, float y) {
        if (area != null) {
            area.setX(x);
            area.setY(y);
        }
    }

    public void setX(float x) {
        area.setX(x);
    }

    public void setY(float y) {
        area.setY(y);
    }

    public int getX() {
        return (int) area.getX();
    }

    public int getY() {
        return (int) area.getY();
    }

    public void setNormalColor(Color color) {
        normalColor = color;
    }

    public void setMouseOverColor(Color color) {
        mouseOverColor = color;
    }

    public void setMouseDownColor(Color color) {
        mouseDownColor = color;
    }

    public void render(GUIContext container, Graphics g) {
        if(selected) {
            g.setColor(selectedColor);
        }
        else {
            g.setColor(currentColor);
        }
        g.fill(area);
        updateImage();
        for (AxoComponent child : children) {
            g.pushTransform();
            g.translate(getX(), getY());
            child.render(container, g);
            g.popTransform();
        }
    }

    private void updateImage() {
        if (!over) {
            currentColor = normalColor;
            state = NORMAL;
            mouseUp = false;
        } else if (mouseDown) {
            if ((state != MOUSE_DOWN) && (mouseUp)) {
                currentColor = mouseDownColor;
                state = MOUSE_DOWN;

                notifyListeners();
                mouseUp = false;
            }

            return;
        } else {
            mouseUp = true;
            if (state != MOUSE_OVER) {
                currentColor = mouseOverColor;
                state = MOUSE_OVER;
            }
        }

        mouseDown = false;
        state = NORMAL;
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        newx -= canvas.trans_x;
        newy -= canvas.trans_y;
        newx /= canvas.scale;
        newy /= canvas.scale;
        AxoComponent p = parent;
        while (p != null) {
            newx -= p.getX();
            newy -= p.getY();
            p = p.parent;
        }
        updateOver(newx, newy);
    }

    protected void updateOver(int newx, int newy) {
        over = area.contains(newx, newy);
        if (over) {
            AxoComponent p = parent;
            while (p != null) {
                p.over = false;
                p.selected = false;
                p.mouseDown = false;
                p.mouseUp = false;
                p.state = NORMAL;
                p = p.parent;
            }
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {        
        newx -= canvas.trans_x;
        newy -= canvas.trans_y;
        newx /= canvas.scale;
        newy /= canvas.scale;
        AxoComponent p = parent;
        while (p != null) {
            newx -= p.getX();
            newy -= p.getY();
            p = p.parent;
        }
        updateOver(newx, newy);
        if (selected && !canvas.middleMouseButtonDown) {
            oldx -= canvas.trans_x;
            oldy -= canvas.trans_y;
            oldx /= canvas.scale;
            oldy /= canvas.scale;
            this.changeLocation(newx - oldx, newy - oldy);
        }
    }

    @Override
    public void mousePressed(int button, int mx, int my) {
        mx -= canvas.trans_x;
        my -= canvas.trans_y;
        mx /= canvas.scale;
        my /= canvas.scale;
        AxoComponent p = parent;
        while (p != null) {
            mx -= p.getX();
            my -= p.getY();
            p = p.parent;
        }
        updateOver(mx, my);
        if (button == 0) {
            mouseDown = true;
        }
    }

    @Override
    public void mouseReleased(int button, int mx, int my) {
        mx -= canvas.trans_x;
        my -= canvas.trans_y;
        mx /= canvas.scale;
        my /= canvas.scale;
        AxoComponent p = parent;
        while (p != null) {
            mx -= p.getX();
            my -= p.getY();
            p = p.parent;
        }
        updateOver(mx, my);
        if (button == 0) {
            mouseDown = false;
        }
    }
    
    public Point getWorldLocation() {
        int x = getX();
        int y = getY();
        AxoComponent p = parent;
        while (p != null) {
            x += p.getX();
            y += p.getY();
            p = p.parent;
        }
        return new Point(x, y);
    }

    @Override
    public int getHeight() {
        return (int) (area.getMaxY() - area.getY());
    }

    @Override
    public int getWidth() {
        return (int) (area.getMaxX() - area.getX());
    }

    public boolean isMouseOver() {
        return over;
    }

    public void changeLocation(int dx, int dy) {
        setLocation(getX() + dx, getY() + dy);
    }

    @Override
    public void setLocation(int x, int y) {
        setLocation((float) x, (float) y);
    }

    public void add(AxoComponent c) {
        c.setParent(this);
        this.children.add(c);
    }

    protected AxoComponent parent;

    public void setParent(AxoComponent parent) {
        this.parent = parent;
    }
}