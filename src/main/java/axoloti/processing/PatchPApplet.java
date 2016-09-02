package axoloti.processing;

import axoloti.PatchViewportView;
import axoloti.Theme;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class PatchPApplet extends PApplet implements PatchViewportView {

    int p_height = 1000;
    int p_width = 1000;
//    float scale = 9.0f;
    int scale = 9;
    int scaleFactor = 1;
    float scaleInverse = 1.0f;
    public int trans_x = 0;
    public int trans_y = 0;

    int mouse_x;
    int mouse_y;

    private final List<PComponent> components = new ArrayList<>();

    public int getScale() {
        return scale;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public float getScaleInverse() {
        return scaleInverse;
    }

    @Override
    public void mouseMoved() {
        mouse_x = mouseX;
        mouse_y = mouseY;
    }

    private float getWorldX() {
        float newx = mouse_x - trans_x;
        newx /= scale;
        return newx;
    }

    private float getWorldY() {
        float newy = mouse_y - trans_y;
        newy /= scale;
        return newy;
    }

    @Override
    public void mouseReleased() {
        for (PComponent c : components) {
//            c.mouseReleased(getWorldX(), getWorldY());
        }
    }

    @Override
    public void mousePressed() {
        for (PComponent c : components) {
//            c.mousePressed(getWorldX(), getWorldY());
        }
    }

    public void mouseDragged() {
        if (mouseButton == CENTER) {
            trans_x += mouseX - mouse_x;
            trans_y += mouseY - mouse_y;
        }
        mouse_x = mouseX;
        mouse_y = mouseY;

        for (PComponent c : components) {
//            c.mouseDragged(getWorldX(), getWorldY());
        }
    }

//    @Override
//    public void settings() {
//        size(width, height, P3D);
//
//    }
    @Override
    public void setup() {
        // lock to integer coords?
        size(width, height, P3D);
//        noSmooth();
        frameRate(1000);

//        textMode(SCREEN);
//        smooth();
        for (PComponent c : components) {
            c.setup();
        }
    }

    Color backgroundColor = Theme.getCurrentTheme().Patch_Unlocked_Background;

//    private boolean isEntirelyInsideViewport(int x, int y, int width, int height) {
//        int cameraX = (int) -(trans_x / scale);
//        int cameraY = (int) -(trans_y / scale);
//        int minX = cameraX;
//        int maxX = cameraX + (int) (getWidth() / scale);
//        int minY = cameraY;
//        int maxY = cameraY + (int) (getWidth() / scale);
//        int objMaxX = x + width;
//        int objMaxY = y + height;
//
//        if ((x < maxX && x > minX) || (objMaxX < maxX && objMaxX > minX)) {
//            if ((y < maxY && y > minY) || (objMaxY < maxY && objMaxY > minY)) {
//                return true;
//            }
//        }
//        return false;
//    }
    @Override
    public void draw() {
        translate(trans_x, trans_y);
        scale(scale / 9, scale / 9);
        background(backgroundColor.getRGB(), backgroundColor.getAlpha());

        for (PComponent c : components) {
            c.update();
            c.display();
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        //float delta = event.getCount() < 0 ? 1.1f : event.getCount() > 0 ? 1.0f / 1.1f : 1.0f;
        int delta = event.getCount() < 0 ? 9 : event.getCount() > 0 ? -9 : 0;

//        System.out.println("old scale: " + scale);
        int old_scale = (int) scale;
        scale += delta;

        System.out.println("delta: " + delta);
        System.out.println("new scale: " + scale);
        System.out.println("trans: " + trans_x + ", " + trans_y);

        System.out.println(mouse_x + "," + mouse_y);

        trans_x -= mouse_x;
        trans_y -= mouse_y;
        System.out.println("new_scale: " + scale);
        System.out.println("old_scale: " + old_scale);
        System.out.println("ratio: " + scale / old_scale);
        System.out.println();
        trans_x *= ((float) scale / old_scale);
        trans_y *= ((float) scale / old_scale);
        trans_x += mouse_x;
        trans_y += mouse_y;

        scaleFactor = scale / 9;
        scaleInverse = 9.0f / scale;
    }

//    public int getWidth() {
//        return width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
    public void updateSize() {

//        ps.setSize(p_width, p_height);
    }

    @Override
    public void setWidth(int width) {
        this.p_width = width;
    }

    @Override
    public void setHeight(int height) {
        this.p_height = height;
    }

//    private void resizePSurface() {
//        ps = (PSurface) this.initSurface();
//        ps.setResizable(true);
//        ps.setFrameRate(60);
//        ps.setSize(p_width, p_height);
//        ps.initOffscreen(this);
//        c = (PSurfaceAWT.SmoothCanvas) ps.getNative();
//    }
//    private void resizePSurfaceJOGL() {
//        PApplet.runSketch(new String[]{"MyPapplet 3D"}, this);
//        ps = (PSurfaceJOGL) this.getSurface();
//        ps.setResizable(true);
//        ps.initOffscreen(this);
//        // add canvas to JFrame (used as a Component)
//        c = (NewtCanvasAWT) ps.getComponent();
//    }
//    PSurfaceAWT.SmoothCanvas c;
//    NewtCanvasAWT c;
//    PSurfaceJOGL ps;
//    PSurface ps;
    @Override
    public Component getComponent() {
        return this;
//        if (c == null) {
//            resizePSurface();
//        }
//        return c;
    }
//    @Override
//    public void dispose() {
//        ps.stopThread();
//    }
//    @Override
//    public void pause() {
//        ps.pauseThread();
//    }
//    @Override
//    public void resume() {
//        ps.resumeThread();
//    }
//    @Override
//    public void setPreferredSize(Dimension d) {
//        if (c != null) {
//            c.setPreferredSize(d);
//        }
//    }

    public void startThread() {
        this.init();
    }

    public void add(PComponent c) {
        components.add(c);
    }

    public void remove(PComponent c) {
        components.remove(c);
    }
}
