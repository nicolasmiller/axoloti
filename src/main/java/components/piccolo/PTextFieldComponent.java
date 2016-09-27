package components.piccolo;

import axoloti.PatchView;
import axoloti.PatchViewPiccolo;
import axoloti.utils.Constants;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.extras.pswing.PSwing;

public class PTextFieldComponent extends PSwing implements PFocusable {

    public PTextFieldComponent() {
        this("");
    }

    public PTextFieldComponent(String text) {
        super(new JTextField(text));
        getComponent().setFont(Constants.FONT);
        getComponent().setFocusTraversalKeysEnabled(false);
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
    public boolean setBounds(double x, double y, double width, double height) {
        boolean boundsChanged = super.setBounds(x, y, width, height);

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

    @Override
    public void grabFocus() {
        getComponent().requestFocus();
        this.getRoot().getDefaultInputManager().setKeyboardFocus(this.getInputEventListeners()[0]);
    }

    private int focusableIndex;

    @Override
    public void setFocusableIndex(int index) {
        focusableIndex = index;
    }

    @Override
    public int getFocusableIndex() {
        return focusableIndex;
    }

    public void transferFocus(PInputEvent ke, PatchView view) {
        PatchViewPiccolo patchViewPiccolo = (PatchViewPiccolo) view;
        if (ke.getKeyChar() == KeyEvent.VK_ENTER
                || ke.getKeyChar() == KeyEvent.VK_TAB) {
            patchViewPiccolo.transferFocus(this);
        }
    }
}
