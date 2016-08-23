package axoloti.processing;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

public class PComponent {
    
    private List<PComponent> children = new ArrayList<PComponent>();

    private PApplet p;
    
    int x;
    int y;
    int width;
    int height;
    
    Rectangle bounds;
    
    protected boolean selected = false;

    protected boolean mouseOver;

    protected boolean mouseDown;

    private boolean mouseUp;
    
//    boolean locked = false;
//    boolean otherslocked = false;
//    ProcessingComponent[] others;
    
    public PComponent(PApplet p) {
        this.p = p;
    }

    public void setSize(Dimension size) {
        this.width = size.width;
        this.height = size.height;
        if(this.bounds == null) {
            this.bounds = new Rectangle(x, y, width, height);
        }
        else {
            this.bounds.width = width;
            this.bounds.height = height;
        }
    }
    
    boolean overRect(int x, int y, int width, int height) {
        if (p.mouseX >= x && p.mouseX <= x + width && 
                p.mouseY >= y && p.mouseY <= y + height) {
            return true;
        } else {
            return false;
        }
    }
    
    protected void updateOver(float mouseX, float mouseY) {
        mouseOver = bounds.contains(mouseX, mouseY);
        if (mouseOver) {
            PComponent p = parent;
            while (p != null) {
                p.mouseOver = false;
                p.selected = false;
                p.mouseDown = false;
                p.mouseUp = false;
                p = p.parent;
            }
        }
    }

    void update() {
    }

    float xOffset = 0; 
    float yOffset = 0; 
    
    public void mouseDragged(float mouseX, float mouseY) {
        updateOver(mouseX, mouseY);

        for(PComponent c : children) {
            float childX = mouseX - this.getX();
            float childY = mouseY - this.getY();
            c.mouseDragged(childX, childY);
        }
        if(mouseOver && p.mouseButton != p.CENTER) {
            bounds.x = (int) (mouseX - xOffset);
            bounds.y = (int) (mouseY - yOffset);
        }
    }
    
    public void mousePressed(float mouseX, float mouseY) {
        updateOver(mouseX, mouseY);
        xOffset = mouseX - bounds.x;
        yOffset = mouseY - bounds.y;
        mouseX -= this.getX();
        mouseY -= this.getY();
        for(PComponent c : children) {
            c.mousePressed(mouseX, mouseY);
        }
    }
    
    public void mouseReleased(float mouseX, float mouseY) {
        updateOver(mouseX, mouseY);
        mouseX -= this.getX();
        mouseY -= this.getY();
        for(PComponent c : children) {
            c.mouseReleased(mouseX, mouseY);
        }
    }

    void display() {
        p.fill(255);
        p.stroke(0);
        p.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        if (mouseOver || mouseDown) {
            p.line(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
            p.line(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y);
        }
        
        for (PComponent child : children) {
            p.pushMatrix();
            p.translate(getX(), getY());
            child.display();
            p.popMatrix();
        }
    }
    
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        if (bounds != null) {
            bounds.setLocation(x, y);
        }
        else {
            this.bounds = new Rectangle(x, y, width, height);
        }
    }

    public int getX() {
        return (int) bounds.getX();
    }

    public int getY() {
        return (int) bounds.getY();
    }
    
    public void add(PComponent c) {
        c.setParent(this);
        this.children.add(c);
    }

    protected PComponent parent;

    public void setParent(PComponent parent) {
        this.parent = parent;
    }
}