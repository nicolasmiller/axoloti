package axoloti;

import java.awt.Component;
import java.awt.Dimension;

public interface PatchViewportView {
    public void setWidth(int width);
    public void setHeight(int height);
    public void setPreferredSize(Dimension size);
    public Component getComponent();
}