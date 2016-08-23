package components.piccolo.control;

import axoloti.Theme;
import axoloti.objectviews.IAxoObjectInstanceView;
import java.awt.Graphics2D;
import org.piccolo2d.util.PPaintContext;

public class PVRadioComponent extends PHRadioComponent {

    public PVRadioComponent(int value, int n, IAxoObjectInstanceView axoObjectInstanceView) {
        super(value, n, axoObjectInstanceView);
        bsize = 14;
        setBounds(0, 0, bsize + 2, bsize * n + 2);
    }

    @Override
    int mousePosToVal(int x, int y) {
        int i = y / bsize;
        if (i < 0) {
            return 0;
        }
        if (i > n - 1) {
            return n - 1;
        }
        return i;
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        if (isEnabled()) {
            g2.setColor(Theme.getCurrentTheme().Component_Secondary);
        } else {
            g2.setColor(Theme.getCurrentTheme().Object_Default_Background);
        }
        for (int i = 0; i < n; i++) {
            g2.fillOval(0, i * bsize, bsize, bsize);
        }

        g2.setPaint(getForeground());
        if (isFocusOwner()) {
            g2.setStroke(strokeThick);
        }

        for (int i = 0; i < n; i++) {
            g2.drawOval(0, i * bsize, bsize, bsize);
        }

        if (isEnabled()) {
            g2.fillOval(2, (int) value * bsize + 2, bsize - 3, bsize - 3);
        }
    }
}
