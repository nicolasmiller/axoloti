package components.piccolo;

import axoloti.piccolo.PNodeLayout;
import axoloti.piccolo.PNodeLayoutable;
import axoloti.utils.Constants;
import org.piccolo2d.nodes.PText;

public class PLabelComponent extends PText implements PNodeLayoutable {

    public PLabelComponent(String text) {
        this.setText(text);
        this.setFont(Constants.FONT);
        this.setPickable(false);
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
}
