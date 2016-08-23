package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.inlets.PInletInstanceView;
import java.awt.BasicStroke;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.piccolo2d.util.PPaintContext;

public class PJackInputComponent extends PatchPNode {

    private static final int sz = 10;
    private static final int margin = 2;
    private static final Dimension dim = new Dimension(sz, sz);

    private static final int SZ = 10;
    private static final int MARGIN = 2;
    private static final int MARGIN_SHADOW = MARGIN + 1;
    private static final int DIM = SZ - MARGIN - MARGIN;
    final PInletInstanceView inletInstanceView;

    public PJackInputComponent(PInletInstanceView inletInstanceView) {
        super(inletInstanceView.getPatchView());
        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
        setAlignmentY(CENTER_ALIGNMENT);
        setAlignmentX(RIGHT_ALIGNMENT);
        this.inletInstanceView = inletInstanceView;
    }
    private final Stroke stroke = new BasicStroke(1.5f);

    @Override
    protected void paint(PPaintContext paintContext) {
        paintContext.setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        Graphics2D g2 = paintContext.getGraphics();
        g2.setStroke(stroke);
//        if (inletInstanceView.getInletInstance().isConnected()) {
//            g2.setPaint(Theme.getCurrentTheme().Component_Primary);
//            g2.drawOval(MARGIN_SHADOW, MARGIN_SHADOW, DIM, DIM);
//            g2.setPaint(getForeground());
//            g2.fillOval(MARGIN, MARGIN, DIM, DIM);
//            g2.drawOval(MARGIN, MARGIN, DIM, DIM);
//        } else {
//            g2.setPaint(Theme.getCurrentTheme().Component_Primary);
//            g2.drawOval(MARGIN_SHADOW, MARGIN_SHADOW, DIM, DIM);
//            g2.setPaint(getForeground());
//            g2.drawOval(MARGIN, MARGIN, DIM, DIM);
//        }
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
        paintContext.setRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
    }
}
