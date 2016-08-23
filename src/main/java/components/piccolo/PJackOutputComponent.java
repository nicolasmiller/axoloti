package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.outlets.POutletInstanceView;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.piccolo2d.util.PPaintContext;

public class PJackOutputComponent extends PatchPNode {

    private static final int sz = 10;
    private static final int margin = 2;
    private static final int inset = 1;
    private static final Dimension dim = new Dimension(sz, sz);
    final POutletInstanceView outletInstanceView;

    public PJackOutputComponent(POutletInstanceView outletInstanceView) {
        setBounds(0, 0, sz, sz);
        this.outletInstanceView = outletInstanceView;
    }
    private final static Stroke stroke = new BasicStroke(1.5f);

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(stroke);
        g2.setPaint(Theme.getCurrentTheme().Component_Primary);
        g2.drawRect(margin, margin + 1, sz - margin - margin, sz - margin - margin);

        if (outletInstanceView.getOutletInstance().isConnected()) {
            g2.fillOval(margin + inset, margin + inset + 1, sz - (margin + inset) * 2 + 1, sz - (margin + inset) * 2 + 1);
            g2.setPaint(getForeground());
            g2.fillOval(margin + inset - 1, margin + inset, sz - (margin + inset) * 2 + 1, sz - (margin + inset) * 2 + 1);
        } else {
            g2.drawOval(margin + inset, margin + inset + 1, sz - (margin + inset) * 2, sz - (margin + inset) * 2);
            g2.setPaint(getForeground());
            g2.drawOval(margin + inset - 1, margin + inset, sz - (margin + inset) * 2, sz - (margin + inset) * 2);
        }

        g2.drawRect(margin - 1, margin, sz - margin - margin, sz - margin - margin);
    }
}
