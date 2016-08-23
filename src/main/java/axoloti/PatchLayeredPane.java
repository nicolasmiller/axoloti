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

    public void destroy() {
    }

    public void reinit() {

    }

    public void resume() {

    }

    public void pause() {

    }

    public void updateSize() {

    }

    public boolean isPaused() {
        return false;
    }

    public void stop() {

    }

    public void init() {

    }

    public boolean isDestroyed() {
        return false;
    }

    @Override
    public double getViewScale() {
        return 1.0f;
    }
}
