package axoloti.processing;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

class ProcessingComponent {
    
    private List<ProcessingComponent> children = new ArrayList<ProcessingComponent>();

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

    ProcessingComponent(PApplet p, int x, int y, int width, int height) {
        this.p = p;
        this.bounds = new Rectangle(x, y, width, height);
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
            ProcessingComponent p = parent;
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

        for(ProcessingComponent c : children) {
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
        for(ProcessingComponent c : children) {
            c.mousePressed(mouseX, mouseY);
        }
    }
    
    public void mouseReleased(float mouseX, float mouseY) {
        updateOver(mouseX, mouseY);
        mouseX -= this.getX();
        mouseY -= this.getY();
        for(ProcessingComponent c : children) {
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
        
        for (ProcessingComponent child : children) {
            p.pushMatrix();
            p.translate(getX(), getY());
            child.display();
            p.popMatrix();
        }
    }
    
    public void setLocation(int x, int y) {
        if (bounds != null) {
            bounds.setLocation(x, y);
        }
    }

    public int getX() {
        return (int) bounds.getX();
    }

    public int getY() {
        return (int) bounds.getY();
    }
    
    public void add(ProcessingComponent c) {
        c.setParent(this);
        this.children.add(c);
    }

    protected ProcessingComponent parent;

    public void setParent(ProcessingComponent parent) {
        this.parent = parent;
    }
}