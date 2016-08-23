package axoloti;

import java.awt.Component;
import javax.swing.JLayeredPane;

public class PatchLayeredPane extends JLayeredPane implements PatchViewportView {
    @Override
    public Component getComponent() {
        return this;
    }
    
    @Override
    public void setHeight(int height) {
    }
    
    @Override
    public void setWidth(int width) {
    }
    
    public void dispose() {
        
    }
    
    public void reinit() {
        
    }
    
    public void resume() {
        
    }
    
    public void pause() {
        
    }
}