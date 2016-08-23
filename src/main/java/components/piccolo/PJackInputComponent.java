package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.inlets.PInletInstanceView;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.piccolo2d.util.PPaintContext;

public class PJackInputComponent extends PatchPNode {

    private static final int sz = 10;
    private static final int margin = 2;
    private static final Dimension dim = new Dimension(sz, sz);
    final PInletInstanceView inletInstanceView;

    public PJackInputComponent(PInletInstanceView inletInstanceView) {
        setBounds(0, 0, sz, sz);
        this.inletInstanceView = inletInstanceView;
    }
    private final Stroke stroke = new BasicStroke(1.5f);

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(stroke);
        if (inletInstanceView.getInletInstance().isConnected()) {
            g2.setPaint(Theme.getCurrentTheme().Component_Primary);
            g2.drawOval(margin + 1, margin + 1, sz - margin - margin, sz - margin - margin);
            g2.setPaint(getForeground());
            g2.fillOval(margin, margin, sz - margin - margin, sz - margin - margin);
            g2.drawOval(margin, margin, sz - margin - margin, sz - margin - margin);
        } else {
            g2.setPaint(Theme.getCurrentTheme().Component_Primary);
            g2.drawOval(margin + 1, margin + 1, sz - margin - margin, sz - margin - margin);
            g2.setPaint(getForeground());
            g2.drawOval(margin, margin, sz - margin - margin, sz - margin - margin);
        }
    }
}
