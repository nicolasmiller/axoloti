package components.piccolo.control;

import axoloti.Theme;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.piccolo2d.util.PBounds;
import org.piccolo2d.util.PPaintContext;

public class PVRadioComponent extends PHRadioComponent {

    public PVRadioComponent(int value, int n) {
        super(value, n);
        bsize = 14;
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
        } else {
        }
    }

    @Override
    public PBounds getBounds() {
        return new PBounds(0, 0, bsize + 2, bsize * n + 2);
    }
}
