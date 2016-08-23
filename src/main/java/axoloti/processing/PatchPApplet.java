package axoloti.processing;

import axoloti.PatchViewportView;
import com.jogamp.newt.awt.NewtCanvasAWT;
import java.awt.Component;
import java.awt.Dimension;
import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.opengl.PSurfaceJOGL;

public class PatchPApplet extends PApplet implements PatchViewportView {

    int height = 1000;
    int width = 1000;
    float scale = 1.0f;
    float trans_x = 0.0f;
    float trans_y = 0.0f;
    int objDim = 75;

    int mouse_x;
    int mouse_y;

    ProcessingComponent[][] components;

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

    public void mouseReleased() {
        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                components[i][j].mouseReleased(getWorldX(), getWorldY());
            }
        }
    }

    public void mousePressed() {
        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                components[i][j].mousePressed(getWorldX(), getWorldY());
            }
        }
    }

    public void mouseDragged() {
        if (mouseButton == CENTER) {
            trans_x += mouseX - mouse_x;
            trans_y += mouseY - mouse_y;
        }
        mouse_x = mouseX;
        mouse_y = mouseY;

        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                components[i][j].mouseDragged(getWorldX(), getWorldY());
            }
        }
    }

    public void settings() {
        size(width, height, P3D);
    }

    public void setup() {
        frameRate(60);
        components = new ProcessingComponent[objDim][objDim];

        for (int i = 0; i < objDim; i++) {
            for (int j = 0; j < objDim; j++) {
                components[i][j] = new ProcessingComponent(this, 220 * i, 420 * j, 200, 400);
                ProcessingComponent child = new ProcessingComponent(this, 5, 5, 25, 25);
                components[i][j].add(child);
            }
        }
    }

    public void init() {

    }

    public void draw() {
        translate(trans_x, trans_y);
        scale(scale, scale);

        background(153);
        if (components != null) {
            for (int i = 0; i < objDim; i++) {
                for (int j = 0; j < objDim; j++) {
                    components[i][j].update();
                    components[i][j].display();
                }
            }
        }
    }

    public void mouseWheel(MouseEvent event) {
        float delta = event.getCount() < 0 ? 1.1f : event.getCount() > 0 ? 1.0f / 1.1f : 1.0f;

        trans_x -= mouse_x;
        trans_y -= mouse_y;
        scale *= delta;
        trans_x *= delta;
        trans_y *= delta;
        trans_x += mouse_x;
        trans_y += mouse_y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    private void resizePSurface() {
        PApplet.runSketch(new String[]{"MyPapplet 3D"}, this);
        ps = (PSurfaceJOGL) this.getSurface();
        ps.setResizable(true);
        ps.initOffscreen(this);
        // add canvas to JFrame (used as a Component)
        c = (NewtCanvasAWT) ps.getComponent();
    }

    NewtCanvasAWT c;

    PSurfaceJOGL ps;

    @Override
    public Component getComponent() {
        if (c == null) {
            resizePSurface();
        }
        return c;
    }

    public void dispose() {
        ps.stopThread();
    }

    public void pause() {
        ps.pauseThread();
    }

    public void resume() {
        ps.resumeThread();
    }

    public void setPreferredSize(Dimension d) {
        if (c != null) {
            c.setPreferredSize(d);
        }
    }

    public void startThread() {
        ps.startThread();
    }
}
