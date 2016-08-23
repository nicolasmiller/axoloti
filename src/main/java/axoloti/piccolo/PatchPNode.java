package axoloti.piccolo;

import axoloti.PatchView;
import axoloti.Theme;
import static axoloti.piccolo.PNodeLayout.DEFAULT;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Iterator;
import org.piccolo2d.PNode;
import org.piccolo2d.util.PBounds;
import org.piccolo2d.util.PPaintContext;

public class PatchPNode extends PNode implements PNodeLayoutable {

    public PatchPNode() {
        this(null);
    }

    public PatchPNode(PatchView patchView) {
        this.patchView = patchView;
    }

    protected final PatchView patchView;

    private Color foregroundColor = Theme.getCurrentTheme().Component_Primary;

    private PNodeLayout layout = DEFAULT;

    public void setForeground(Color c) {
        this.foregroundColor = c;
    }

    public Color getForeground() {
        return foregroundColor;
    }

    protected boolean completedLayout = false;

    @Override
    public void layoutChildren() {
        if (!completedLayout) {
            if (layout != DEFAULT) {
                double xOffset = 1;
                double yOffset = 1;
                Iterator i = getChildrenIterator();
                while (i.hasNext()) {
                    PNodeLayoutable each = (PNodeLayoutable) i.next();
                    if (layout == HORIZONTAL_CENTERED) {
                        each.setOffset(xOffset - each.getX(), yOffset);
                        if (each.getLayout() != DEFAULT) {
                            xOffset += each.getChildrenWidth();
                        } else {
                            xOffset += each.getFullBoundsReference().getWidth();
                        }
                    } else if (layout == VERTICAL_CENTERED) {
                        each.setOffset(xOffset, yOffset - each.getY());
                        if (each.getLayout() != DEFAULT) {
                            yOffset += each.getChildrenHeight() + 1;
                        } else {
                            yOffset += each.getFullBoundsReference().getHeight();
                        }
                    }
                }
            } else {
                super.layoutChildren();
            }
            completedLayout = true;
        }
    }

    @Override
    public double getChildrenWidth() {
        if (layout == HORIZONTAL_CENTERED) {
            double offset = 1;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenWidth();
                } else {
                    offset += each.getFullBoundsReference().getWidth();
                }
            }
            return offset;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenWidth();
                } else {
                    w = each.getFullBoundsReference().getWidth();
                }
                max = w > max ? w : max;
            }
            return max;
        }
    }

    @Override
    public double getChildrenHeight() {
        if (layout == VERTICAL_CENTERED) {
            double offset = 1;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenHeight();
                } else {
                    offset += each.getFullBoundsReference().getHeight();
                }
            }
            return offset;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenHeight();
                } else {
                    w = each.getFullBoundsReference().getHeight();
                }
                max = w > max ? w : max;
            }
            return max;
        }
    }

    @Override
    public void setLayout(final PNodeLayout layout
    ) {
        this.layout = layout;
    }

    @Override
    public PNodeLayout getLayout() {
        return this.layout;
    }

    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Dimension getSize() {
        return new Dimension((int) getBounds().width, (int) getBounds().height);
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

            if (drawBorder && isSelected()) {
                g2.setPaint(Theme.getCurrentTheme().Object_Border_Selected);
                PBounds bounds = getBounds();
                g2.fill(bounds);
                bounds.inset(1, 1);
                g2.setPaint(getPaint());
                g2.fill(bounds);
            } else {
                g2.setPaint(getPaint());
                g2.fill(getBounds());
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
