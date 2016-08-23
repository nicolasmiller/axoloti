package components.piccolo;

import javax.swing.text.BadLocationException;
import org.piccolo2d.extras.nodes.PStyledText;

public class PTextFieldComponent extends PStyledText {

    public PTextFieldComponent(String initialText) {
        try {
            this.getDocument().insertString(0, initialText, null);
        } catch (BadLocationException e) {
            throw new RuntimeException("Unabled to initialize PTextFieldComponent");
        }
    }

    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getText() {
        try {
            return getDocument().getText(0, getDocument().getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setText(String text) {
        // TODO
    }
}
