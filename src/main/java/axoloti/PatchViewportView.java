package axoloti;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public interface PatchViewportView {
//    public void setSize(int width, int height);

    public void updateSize();

    public void setWidth(int width);

    public void setHeight(int height);

    public JComponent getComponent();

    public void pause();

    public void resume();

    public boolean isPaused();

    public void stop();

    public void destroy();

    public void init();

    public double getViewScale();

    public JScrollPane createScrollPane();
}
