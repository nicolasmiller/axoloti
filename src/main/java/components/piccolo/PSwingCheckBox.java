package components.piccolo;

import axoloti.piccolo.PNodeLayout;
import axoloti.piccolo.PNodeLayoutable;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import javax.swing.JCheckBox;
import org.piccolo2d.extras.pswing.PSwing;

public class PSwingCheckBox extends PSwing implements PNodeLayoutable {

    public PSwingCheckBox(String text) {
        super(new JCheckBox(text));
    }

    @Override
    public JCheckBox getComponent() {
        return (JCheckBox) super.getComponent();
    }

    public String getText() {
        return getComponent().getText();
    }

    @Override
    public double getChildrenHeight() {
        return getComponent().getHeight();
    }

    @Override
    public double getChildrenWidth() {
        return getComponent().getWidth();
    }

    @Override
    public void setLayout(PNodeLayout layout) {

    }

    @Override
    public PNodeLayout getLayout() {
        return PNodeLayout.DEFAULT;
    }

    @Override
    public boolean setBounds(double x, double y, double width, double height) {
        boolean boundsChanged = super.setBounds(x, y, width, height);

        Dimension d = new Dimension((int) width, (int) height);
        getComponent().setMaximumSize(d);
        getComponent().setMinimumSize(d);
        getComponent().setPreferredSize(d);
        getComponent().setSize(d);

        return boundsChanged;
    }

    public void addActionListener(ActionListener al) {
        getComponent().addActionListener(al);
    }

    public void addFocusListener(FocusListener fl) {
        getComponent().addFocusListener(fl);
    }

    public void addKeyListener(KeyListener kl) {
        getComponent().addKeyListener(kl);
    }

    public void requestFocus() {
        getComponent().requestFocus();
    }

    public void setSelected(boolean selected) {
        getComponent().setSelected(selected);
    }
}
