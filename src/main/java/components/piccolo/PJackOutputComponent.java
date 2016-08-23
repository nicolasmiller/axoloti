package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.outlets.POutletInstanceView;
import java.awt.BasicStroke;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.piccolo2d.util.PPaintContext;

public class PJackOutputComponent extends PatchPNode {

    private static final int sz = 10;
    private static final int margin = 2;
    private static final int inset = 1;
    private static final Dimension dim = new Dimension(sz, sz);

//    private static final int SZ = 10;
//    private static final int MARGIN = 2;
//    private static final int MARGIN_SHADOW = MARGIN + 1;
//    private static final int MARGIN_SHADOW_PLUS_1 = MARGIN_SHADOW + 1;
//    private static final int MARGIN_SHADOW_MINUS_1 = MARGIN_SHADOW - 1;
//    private static final int INSET = 1;
//    private static final int DIM = SZ - MARGIN - MARGIN;
//    private static final int DIM2 = SZ - (MARGIN_SHADOW) * 2;
//    private static final int DIM2_PLUS_1 = DIM2 + 1;
    final POutletInstanceView outletInstanceView;

    public PJackOutputComponent(POutletInstanceView outletInstanceView) {
        super(outletInstanceView.getPatchView());
        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
        setAlignmentY(CENTER_ALIGNMENT);
        setAlignmentX(RIGHT_ALIGNMENT);
        this.outletInstanceView = outletInstanceView;
    }
    private final static Stroke stroke = new BasicStroke(1.5f);

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();

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

//    @Override
//    protected void paint(PPaintContext paintContext) {
//        Graphics2D g2 = paintContext.getGraphics();
//        g2.setStroke(stroke);
//        g2.setPaint(Theme.getCurrentTheme().Component_Primary);
//        g2.drawRect(MARGIN, MARGIN_SHADOW, DIM, DIM);
//
//        if (outletInstanceView.getOutletInstance().isConnected()) {
//            g2.fillOval(MARGIN_SHADOW, MARGIN_SHADOW_PLUS_1, DIM2_PLUS_1, DIM2_PLUS_1);
//            g2.setPaint(getForeground());
//            g2.fillOval(MARGIN_SHADOW_MINUS_1, MARGIN_SHADOW, DIM2_PLUS_1, DIM2_PLUS_1);
//        } else {
//            g2.drawOval(MARGIN_SHADOW, MARGIN_SHADOW_PLUS_1, DIM2, DIM2);
//            g2.setPaint(getForeground());
//            g2.drawOval(MARGIN_SHADOW_MINUS_1, MARGIN_SHADOW, DIM2, DIM2);
//        }
//
//        g2.drawRect(INSET, MARGIN, DIM, DIM);
//    }
}
