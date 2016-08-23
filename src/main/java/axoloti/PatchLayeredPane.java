package axoloti;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

public class PatchLayeredPane extends JLayeredPane implements PatchViewportView {

    @Override
    public JComponent getComponent() {
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

    @Override
    public double getViewScale() {
        return 1.0f;
    }

    @Override
    public JScrollPane createScrollPane() {
        return new JScrollPane();
    }
}
