package axoloti.slick2d;

import axoloti.PatchViewportView;
import java.awt.Component;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

public class PatchCanvasGameContainer extends CanvasGameContainer implements PatchViewportView {
    int width;
    int height;
    
    PatchCanvasGameContainer(Game game) throws SlickException {
        super(game);
    }
    
    @Override
    public int getWidth() {
        return width;
    }
                
    @Override
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
    
    public Component getComponent() {
        return this;
    }
    
    public void dispose() {
        super.dispose();
    }
    
    public void pause() {
        this.getContainer().pause();
    }
    
    public void resume() {
        this.getContainer().resume();
    }
}