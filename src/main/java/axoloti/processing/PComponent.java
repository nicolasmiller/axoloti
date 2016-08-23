package axoloti.processing;

import axoloti.Theme;
import axoloti.pobjectviews.PAxoObjectInstanceView;
import static axoloti.pobjectviews.PAxoObjectInstanceView.MIN_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

public class PComponent {
    private final PApplet p;
    private final List<PComponent> children = new ArrayList<PComponent>();
    
    private Color backgroundColor = Theme.getCurrentTheme().Object_Default_Background;
    private Color foregroundColor = Theme.getCurrentTheme().Object_TitleBar_Foreground;
    
    Rectangle bounds = new Rectangle(0, 0, 0, 0);
    
    protected boolean mouseOver;
    protected boolean mouseDown;
    
    public PComponent(PApplet p) {
        this.p = p;
    }
    
    public PApplet getPApplet() {
        return p;
    }

    public void setSize(Dimension size) {
        bounds.width = size.width;
        bounds.height = size.height;
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
        mouseOver = getBounds().contains(mouseX, mouseY);
        if (mouseOver) {
            PComponent p = parent;
            while (p != null) {
                p.mouseOver = false;
                p.mouseDown = false;
                p = p.parent;
            }
        }
    }

    public void update() {
        
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
            getBounds().x = (int) (mouseX - xOffset);
            getBounds().y = (int) (mouseY - yOffset);
        }
    }
    
    public void mousePressed(float mouseX, float mouseY) {
        updateOver(mouseX, mouseY);
        xOffset = mouseX - getBounds().x;
        yOffset = mouseY - getBounds().y;
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

    protected boolean sizedToChildren = true;
    protected static final int PADDING = PAxoObjectInstanceView.PADDING;
    
    public void setup() {
        if(sizedToChildren) {
            int newWidth = MIN_WIDTH;
            int newHeight = 0;
            for(PComponent c : getChildren()) {
                c.setup();
                int max_x = c.getX() + c.getWidth();
                int max_y = c.getY() + c.getHeight();
                newWidth = max_x > newWidth ? max_x : newWidth;
                newHeight = max_y > newHeight ? max_y : newHeight;
            }
            this.setBounds(PADDING + getX(), PADDING + getY(), newWidth + PADDING, newHeight + PADDING);
        }
    }

    public void display() {
        p.fill(backgroundColor.getRGB(), backgroundColor.getAlpha());
        p.noStroke();
        p.rect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        if (mouseOver || mouseDown) {
            p.line(getBounds().x, getBounds().y, 
                    getBounds().x + getBounds().width, getBounds().y + getBounds().height);
            p.line(getBounds().x, getBounds().y + getBounds().height, 
                    getBounds().x + getBounds().width, getBounds().y);
        }
        
        for (PComponent child : children) {
            p.pushMatrix();
            p.translate(getX(), getY());
            child.display();
            p.popMatrix();
        }
    }
    
    public void setLocation(int x, int y) {
        bounds.setLocation(x, y);
    }
    
    public Point getLocation() {
        return getBounds().getLocation();
    }
    
    public void setBounds(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public Dimension getSize() {
        return bounds.getSize();
    }
    
    public Dimension getPreferredSize() {
        return getSize();
    }

    public int getX() {
        return (int) getBounds().getX();
    }

    public int getY() {
        return (int) getBounds().getY();
    }
    
    public void add(PComponent c) {
        c.setParent(this);
        this.children.add(c);
    }

    protected PComponent parent;

    public void setParent(PComponent parent) {
        this.parent = parent;
    }
    
    public void removeAll() {
        this.children.clear();
    }
    
    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public void setForeground(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    
    public Color getForeground() {
        return this.foregroundColor;
    }
    
    public Color getBackground() {
        return this.backgroundColor;
    }
    
    public int getWidth() {
        return getBounds().width;
    }
    
    public int getHeight() {
        return getBounds().height;
    }
    
    public List<PComponent> getChildren() {
        return this.children;
    }
    
    public boolean isEnabled() {
        return true;
    }
}