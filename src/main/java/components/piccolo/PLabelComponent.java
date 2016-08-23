package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.utils.Constants;
import java.awt.Dimension;
import org.piccolo2d.nodes.PText;

public class PLabelComponent extends PatchPNode {

    private final PText textNode;

    public PLabelComponent(String text) {
        textNode = new PText(text);
        textNode.setFont(Constants.FONT);
        textNode.setTextPaint(Theme.getCurrentTheme().Label_Text);
        textNode.setPickable(false);
        this.setPickable(false);
        addChild(textNode);
        setPreferredSize(new Dimension(roundUp(textNode.getWidth()), roundUp(textNode.getHeight())));
    }

    public String getText() {
        return textNode.getText();
    }

    public void setText(String text) {
        this.textNode.setText(text);
    }

    public void setHorizontalAlignment(float horizontalAlignment) {
        textNode.setHorizontalAlignment(horizontalAlignment);
    }
}
