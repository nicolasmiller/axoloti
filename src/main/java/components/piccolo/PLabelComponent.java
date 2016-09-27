package components.piccolo;

import axoloti.Theme;
import axoloti.utils.Constants;
import org.piccolo2d.nodes.PText;

public class PLabelComponent extends PText {

    public PLabelComponent(String text) {
        this.setText(text);
        this.setFont(Constants.FONT);
        this.setTextPaint(Theme.getCurrentTheme().Label_Text);
        this.setPickable(false);
    }
}
