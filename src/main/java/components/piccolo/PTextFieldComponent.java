package components.piccolo;

import axoloti.piccolo.PNodeLayout;
import axoloti.piccolo.PNodeLayoutable;
import axoloti.utils.Constants;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.piccolo2d.extras.pswing.PSwing;

public class PTextFieldComponent extends PSwing implements PNodeLayoutable {

    public PTextFieldComponent() {
        this("");
    }

    public PTextFieldComponent(String text) {
        super(new JTextField(text));
        getComponent().setFont(Constants.FONT);
        getComponent().requestFocus();
    }

    @Override
    public JTextField getComponent() {
        return (JTextField) super.getComponent();
    }

    public String getText() {
        return getComponent().getText();
    }

    public Document getDocument() {
        return getComponent().getDocument();
    }

    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    public void setText(String text) {
        getComponent().setText(text);
    }

    @Override
    public void setLayout(PNodeLayout layout) {

    }

    @Override
    public PNodeLayout getLayout() {
        return PNodeLayout.DEFAULT;
    }

    @Override
    public double getChildrenHeight() {
        return getBounds().height;
    }

    @Override
    public double getChildrenWidth() {
        return getBounds().width;
    }

    @Override
    public boolean setBounds(double x, double y, double width, double height) {
        boolean boundsChanged = super.setBounds(x, y, width, width);

        Dimension d = new Dimension((int) width, (int) height);
        getComponent().setMaximumSize(d);
        getComponent().setMinimumSize(d);
        getComponent().setPreferredSize(d);
        getComponent().setSize(d);

        return boundsChanged;
    }

    public void selectAll() {
        getComponent().selectAll();
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
}
