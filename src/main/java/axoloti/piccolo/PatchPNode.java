package axoloti.piccolo;

import axoloti.PatchView;
import axoloti.Theme;
import static axoloti.piccolo.PNodeLayout.DEFAULT;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import static axoloti.piccolo.objectviews.PAxoObjectInstanceView.PADDING;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Iterator;
import org.piccolo2d.PNode;
import org.piccolo2d.util.PPaintContext;

public class PatchPNode extends PNode implements PNodeLayoutable {

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

    // TODO refactor
    @Override
    public void layoutChildren() {
        if (!completedLayout) {
            if (layout != DEFAULT) {
                double xOffset = PADDING;
                double yOffset = PADDING;
                Iterator i = getChildrenIterator();
                while (i.hasNext()) {
                    PNodeLayoutable each = (PNodeLayoutable) i.next();
                    if (layout == HORIZONTAL_CENTERED) {
                        each.setOffset(xOffset - each.getX(), yOffset);
                        if (each.getLayout() != DEFAULT) {
                            xOffset += each.getChildrenWidth() + PADDING;
                        } else {
                            xOffset += each.getFullBoundsReference().getWidth() + PADDING;
                        }
                    } else if (layout == VERTICAL_CENTERED) {
                        each.setOffset(xOffset, yOffset - each.getY());
                        if (each.getLayout() != DEFAULT) {
                            yOffset += each.getChildrenHeight() + PADDING;
                        } else {
                            yOffset += each.getFullBoundsReference().getHeight() + PADDING;
                        }
                    }
                }
            } else {
                super.layoutChildren();
            }
            completedLayout = true;
        }
    }

    // TODO refactor
    @Override
    public double getChildrenWidth() {
        if (layout == HORIZONTAL_CENTERED) {
            double offset = PADDING;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenWidth() + PADDING;
                } else {
                    offset += each.getFullBoundsReference().getWidth() + PADDING;
                }
            }
            return offset;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w = 0;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenWidth() + 2 * PADDING;
                } else {
                    w = each.getFullBoundsReference().getWidth() + 2 * PADDING;
                }
                max = w > max ? w : max;
            }
            return max;
        }
    }

    @Override
    public double getChildrenHeight() {
        if (layout == VERTICAL_CENTERED) {
            double offset = PADDING;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenHeight() + PADDING;
                } else {
                    offset += each.getFullBoundsReference().getHeight() + PADDING;
                }
            }
            return offset;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w = 0;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenHeight() + 2 * PADDING;
                } else {
                    w = each.getFullBoundsReference().getHeight() + 2 * PADDING;
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

    private String tooltipText;

    public void setToolTipText(String text) {
        this.tooltipText = text;
    }

    private final Stroke stroke = new BasicStroke(1f);
    private boolean drawBorder = true;

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        super.paint(paintContext);
        if (drawBorder) {
            Graphics2D g2 = paintContext.getGraphics();
            g2.setStroke(stroke);
            if (isSelected()) {
                g2.setColor(Theme.getCurrentTheme().Object_Border_Selected);
            } else {
                g2.setColor(Theme.getCurrentTheme().Object_Border_Unselected);
            }
//            g2.draw(getBounds());
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
