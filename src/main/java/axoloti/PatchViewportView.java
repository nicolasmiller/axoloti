package axoloti;

import java.awt.Component;

public interface PatchViewportView {
//    public void setSize(int width, int height);

    public void updateSize();

    public void setWidth(int width);

    public void setHeight(int height);

    public Component getComponent();

    public void dispose();

    public void pause();

    public void resume();
}
