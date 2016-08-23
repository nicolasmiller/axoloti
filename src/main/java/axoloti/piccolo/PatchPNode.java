package axoloti.piccolo;

import axoloti.PatchView;
import axoloti.Theme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.piccolo2d.util.PBounds;
import org.piccolo2d.util.PPaintContext;

public class PatchPNode extends SwingLayoutNode {

    public PatchPNode() {
        this(null);
    }

    public PatchPNode(PatchView patchView) {
        this.patchView = patchView;
    }

    protected final PatchView patchView;

    private Color foregroundColor = Theme.getCurrentTheme().Label_Text;

    public void setForeground(Color c) {
        this.foregroundColor = c;
    }

    public Color getForeground() {
        return foregroundColor;
    }

    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Dimension getSize() {
        return this.getProxyComponent().getSize();
    }

    private String toolTipText;

    public void setToolTipText(String text) {
        this.toolTipText = text;
    }

    public String getToolTipText() {
        return this.toolTipText;
    }

    private final Stroke stroke = new BasicStroke(1f);
    private boolean drawBorder = false;

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        if (getPaint() != null) {
            final Graphics2D g2 = paintContext.getGraphics();

            if (drawBorder) {
                if (isSelected()) {
                    g2.setPaint(Theme.getCurrentTheme().Object_Border_Selected);
                } else {
                    g2.setPaint(Theme.getCurrentTheme().Object_Border_Unselected);
                }
                PBounds bounds = getBounds();
                g2.fill(bounds);
                bounds.inset(1, 1);
                g2.setPaint(getPaint());
                g2.fill(bounds);
            } else {
                g2.setPaint(getPaint());
                g2.fill(getBoundsReference());
            }
        }
    }

    public Boolean isSelected() {
        return ((PatchPCanvas) patchView.getViewportView().getComponent()).isSelected(this);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            ((PatchPCanvas) patchView.getViewportView().getComponent()).select(this);
        } else {
            ((PatchPCanvas) patchView.getViewportView().getComponent()).unselect(this);
        }
    }
}
