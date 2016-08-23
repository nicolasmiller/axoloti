package components.piccolo.displays;

import axoloti.piccolo.PatchPNode;
import axoloti.utils.Constants;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.piccolo2d.util.PPaintContext;

public class PVValueLabelsComponent extends PatchPNode {

    private final double max;
    private final double min;
    private final double tick;

    int height = 128;
    int width = 25;

    public PVValueLabelsComponent(double min, double max, double tick) {
        this.max = max;
        this.min = min;
        this.tick = tick;
        Dimension d = new Dimension(width, height);
        setBounds(0, 0, d.width, d.height);
    }

    final int margin = 0;

    int ValToPos(double v) {
        return (int) (margin + ((max - v) * (height - 2 * margin)) / (max - min));
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.setPaint(getForeground());
        int inset = 3;
        for (double v = min + tick; v < max; v += tick) {
            int y = ValToPos(v);
            g2.drawLine(width - inset, y, width, y);
            String s;
            if (Math.rint(v) == v) {
                s = String.format("%4.0f", v);
            } else {
                s = String.format("%4.1f", v);
            }
            g2.setFont(Constants.FONT);
            g2.drawString(s, 0, y + 4);
        }
    }
}
