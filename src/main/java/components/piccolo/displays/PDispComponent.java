package components.piccolo.displays;

import axoloti.Theme;
import axoloti.utils.Constants;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import org.piccolo2d.util.PPaintContext;

public class PDispComponent extends PDispComponentAbstract {

    private double value;
    private final double max;
    private final double min;
    boolean overflow = false;
    private static final Dimension dim = new Dimension(28, 32);

    public PDispComponent(double value, double min, double max) {
        this.value = value;
        this.max = max;
        this.min = min;
        setBounds(0, 0, dim.width, dim.height);
    }
    private static final Stroke strokeThin = new BasicStroke(0.5f);
    private static final Stroke strokeThick = new BasicStroke(1);

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int tick = 1;
        int radius = (int) Math.min(getBounds().width, getBounds().height) / 2 - tick;
        g2.setStroke(strokeThin);
        g2.setColor(Theme.getCurrentTheme().Component_Secondary);
        g2.setPaint(getForeground());
        int b = radius / 2;
        g2.drawArc(b, b, 2 * (radius - b), 2 * (radius - b), -45, 270);
        double th = 0.75 * Math.PI + (value - min) * (1.5 * Math.PI) / (max - min);
        int x = (int) (Math.cos(th) * radius);
        int y = (int) (Math.sin(th) * radius);
        if (overflow) {
            g2.setColor(Theme.getCurrentTheme().Error_Text);
        }
        g2.setStroke(strokeThick);
        g2.drawLine(radius, radius, radius + x, radius + y);
        String s = String.format("%5.2f", value);
        g2.setFont(Constants.FONT);
        g2.drawString(s, 0, (int) getBounds().height);
    }

    @Override
    public void setValue(double value) {
        overflow = false;
        if (value < min) {
            value = min;
            overflow = true;
        }
        if (value > max) {
            value = max;
            overflow = true;
        }
        if (this.value != value) {
            this.value = value;
            repaint();
        }
    }

    public double getMinimum() {
        return min;
    }

    public double getMaximum() {
        return max;
    }
}
