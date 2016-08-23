package axoloti.processing;

import axoloti.PatchViewportView;
import com.jogamp.newt.awt.NewtCanvasAWT;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.opengl.PSurfaceJOGL;

public class PatchPApplet extends PApplet implements PatchViewportView {
    int p_height = 1000;
    int p_width = 1000;
    float scale = 1.0f;
    float trans_x = 0.0f;
    float trans_y = 0.0f;

    int mouse_x;
    int mouse_y;
    
    private final List<PComponent> components = new ArrayList<>();

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
        for(PComponent c : components) {
            c.mouseReleased(getWorldX(), getWorldY());
            
        }
    }

    @Override
    public void mousePressed() {
        for(PComponent c : components) {
            c.mousePressed(getWorldX(), getWorldY());
        }
    }

    public void mouseDragged() {
        if (mouseButton == CENTER) {
            trans_x += mouseX - mouse_x;
            trans_y += mouseY - mouse_y;
        }
        mouse_x = mouseX;
        mouse_y = mouseY;
        
        for(PComponent c : components) {
            c.mouseDragged(getWorldX(), getWorldY());
        }
    }

    @Override
    public void settings() {
        size(width, height, P3D);
    }

    public void setup() {
        frameRate(60);
    }

    public void init() {

    }

    @Override
    public void draw() {
        translate(trans_x, trans_y);
        scale(scale, scale);

        background(153);
        for(PComponent c : components) {
            c.update();
            c.display();
        }
    }

    @Override
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

    @Override
    public void dispose() {
        ps.stopThread();
    }

    @Override
    public void pause() {
        ps.pauseThread();
    }

    @Override
    public void resume() {
        ps.resumeThread();
    }

    @Override
    public void setPreferredSize(Dimension d) {
        if (c != null) {
            c.setPreferredSize(d);
        }
    }

    public void startThread() {
        ps.startThread();
    }
    
    public void add(PComponent c) {
        components.add(c);
    }
    
    public void remove(PComponent c) {
        components.remove(c);
    }
}