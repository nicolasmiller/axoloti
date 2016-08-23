package axoloti.slick2d;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Slick2dRenderer extends BasicGame {

    public float scale = 1.0f;
    public float trans_x = 0.0f;
    public float trans_y = 0.0f;
    private static int width = 1000;
    private static int height = 1000;
    private int mouse_x;
    private int mouse_y;
    private int objDim = 5;
    private Slick2dComponent[][] objs = new Slick2dComponent[objDim][objDim];
    private ArrayList<Net> nets = new ArrayList<Net>();
    private Net dragNet;

    public Slick2dRenderer(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                objs[i][j] = new Slick2dComponent(gc, 220 * i, 420 * j, 200, 400, this);

                objs[i][j].add(new DialComponent(gc, 100, 5, 100, 100, this));
                Iolet child = new Iolet(gc, 5, 5, 25, 25, this);
                objs[i][j].add(child);
            }
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        if (dragSource != null) {
            Point sourceLocation = dragSource.getWorldLocation();
            dragNet = new Net(gc, (int) sourceLocation.getX() + dragSource.getWidth() / 2, (int) sourceLocation.getY() + dragSource.getHeight() / 2, 100, 100, this);
            dragNet.setSource(dragSource);
            dragSource = null;
        }
    }

    private boolean isInsideViewport(GameContainer gc, int x, int y, int width, int height) {
        int cameraX = (int) -(trans_x / scale);
        int cameraY = (int) -(trans_y / scale);
        int minX = cameraX;
        int maxX = cameraX + (int) (gc.getWidth() / scale);
        int minY = cameraY;
        int maxY = cameraY + (int) (gc.getWidth() / scale);
        int objMaxX = x + width;
        int objMaxY = y + height;

        if ((x < maxX && x > minX) || (objMaxX < maxX && objMaxX > minX)) {
            if ((y < maxY && y > minY) || (objMaxY < maxY && objMaxY > minY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setBackground(Color.lightGray);
        g.translate(trans_x, trans_y);
        g.scale(scale, scale);
        ArrayList<Slick2dComponent> selected = new ArrayList<Slick2dComponent>();
        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                Slick2dComponent c = objs[i][j];

                if (isInsideViewport(gc, c.getX(), c.getY(), c.getWidth(), c.getHeight())) {
                    if (c.selected) {
                        selected.add(c);
                    } else {
                        c.render(gc, g);
                    }
                }
            }
        }
        for (Slick2dComponent c : selected) {
            c.render(gc, g);
        }

        if (selection != null) {
            g.setColor(Color.black);
            g.setLineWidth(2);
            g.drawRect(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
            g.setLineWidth(1);
        }

        if (dragNet != null) {
            dragNet.render(gc, g);
        }

        for (Net n : nets) {
            n.render(gc, g);
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouse_x = newx;
        mouse_y = newy;

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (middleMouseButtonDown) {
            trans_x += newx - oldx;
            trans_y += newy - oldy;
        }
        mouse_x = newx;
        mouse_y = newy;

        if (selection != null) {
            newx -= trans_x;
            newy -= trans_y;
            newx /= scale;
            newy /= scale;
            int min_x = Math.min((int) selectOrigin.getX(), newx);
            int min_y = Math.min((int) selectOrigin.getY(), newy);
            int max_x = Math.max((int) selectOrigin.getX(), newx);
            int max_y = Math.max((int) selectOrigin.getY(), newy);
            selection.setBounds(min_x, min_y, max_x - min_x, max_y - min_y);
        }

        if (dragNet != null) {
            newx -= trans_x;
            newy -= trans_y;
            newx /= scale;
            newy /= scale;
            dragNet.setEnd(new Point(newx, newy));
        }
    }

    private boolean overBackground() {
        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                if (objs[i][j].over) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean middleMouseButtonDown = false;

    public Rectangle selection;
    private Point selectOrigin;

    @Override
    public void mousePressed(int button, int mx, int my) {
        if (button == 2) {
            middleMouseButtonDown = true;
        } else if (overBackground()) {
            mx -= trans_x;
            my -= trans_y;
            mx /= scale;
            my /= scale;
            selectOrigin = new Point(mx, my);
            selection = new Rectangle(mx, my, 0, 0);
            for (int i = 0; i < objDim; i++) {
                for (int j = 0; j < objDim; j++) {
                    objs[i][j].selected = false;
                }
            }
        } else {
            boolean clearRest = false;
            Slick2dComponent foo = null;
            for (int i = 0; i < objDim; i++) {
                for (int j = 0; j < objDim; j++) {
                    if (objs[i][j].over && !objs[i][j].selected) {
                        objs[i][j].selected = true;
                        foo = objs[i][j];
                        clearRest = true;
                        break;
                    }
                }
            }
            if (clearRest) {
                for (int i = 0; i < objDim; i++) {
                    for (int j = 0; j < objDim; j++) {
                        if (objs[i][j] != foo) {
                            objs[i][j].selected = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int button, int mx, int my) {
        middleMouseButtonDown = false;
        if (selection != null) {
            for (int i = 0; i < objDim; i++) {
                for (int j = 0; j < objDim; j++) {
                    if (selection.intersects(objs[i][j].area)) {
                        objs[i][j].selected = true;
                    }
                }
            }
        }
        selection = null;
        dragNet = null;
    }

    @Override
    public void mouseWheelMoved(int change) {
        float delta = change > 0 ? 1.1f : change < 0 ? 1.0f / 1.1f : 1.0f;

        trans_x -= mouse_x;
        trans_y -= mouse_y;
        scale *= delta;
        trans_x *= delta;
        trans_y *= delta;
        trans_x += mouse_x;
        trans_y += mouse_y;
    }
    
    public static CanvasGameContainer getCanvas() {
        try {
            CanvasGameContainer container;
            Slick2dRenderer game = new Slick2dRenderer("Simple Slick Game");

            container = new CanvasGameContainer(game) {
                public int getWidth() {
                    return width;
                }

                public int getHeight() {
                    return height;
                }
            };
            container.getContainer().setTargetFrameRate(60);
            container.getContainer().setSmoothDeltas(true);
            container.getContainer().setVSync(true);
            container.getContainer().setAlwaysRender(true);
            container.setPreferredSize(new Dimension(width, height));
            return container;
        } catch (SlickException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
       System.setProperty("java.library.path", "lib");
       System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
       JFrame frame = new JFrame("Test");
       
       CanvasGameContainer container = getCanvas();
       JScrollPane scrollPane = new JScrollPane();
       
       scrollPane.setViewportView(container);
            
       frame.getContentPane().add(scrollPane);
       frame.setSize(width, height);
       frame.addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
               container.dispose();
               System.exit(0);
           }
       });

       scrollPane.addComponentListener(new ComponentAdapter() {
           public void componentResized(ComponentEvent e) {
               width = scrollPane.getWidth();
               height = scrollPane.getHeight();
               container.setPreferredSize(scrollPane.getSize());
           }
       });

       frame.setVisible(true);
       try {
           container.start();
       }
       catch (SlickException ex) {
            ex.printStackTrace();
       }
    }

    public Net getDragNet() {
        return this.dragNet;
    }

    private Iolet dragSource;

    public void addDragNet(Iolet dragSource) {
        this.dragSource = dragSource;
    }

    public void endDragNet(Iolet dest) {
        if (dragNet != null) {
            dragNet.setDest(dest);
            nets.add(dragNet);
            dragNet = null;
        }
    }
}