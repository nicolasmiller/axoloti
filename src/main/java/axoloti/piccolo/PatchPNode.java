package axoloti.piccolo;

import axoloti.PatchView;
import axoloti.Theme;
import static axoloti.piccolo.PNodeLayout.DEFAULT;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_TOP;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_LEFT;
import static axoloti.piccolo.PNodeLayout.VERTICAL_RIGHT;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private static final int PADDING = 1;

    @Override
    public void layoutChildren() {
//        System.out.println("layoutChildren");
        if (!completedLayout) {
            if (layout != DEFAULT) {
                double xOffset = PADDING;
                double yOffset = PADDING;
                Iterator i = getChildrenIterator();
                int max_width = Integer.MIN_VALUE;
                List<PNodeLayoutable> justifiedNodes = new ArrayList<PNodeLayoutable>();
                while (i.hasNext()) {
                    PNodeLayoutable each = (PNodeLayoutable) i.next();
                    if (layout == HORIZONTAL_TOP) {
                        each.setOffset(xOffset - each.getX(), yOffset);
                        if (each.getLayout() != DEFAULT) {
                            xOffset += each.getChildrenWidth() + PADDING;
                        } else {
                            xOffset += each.getFullBoundsReference().getWidth() + PADDING;
                        }
                    } else if (layout == VERTICAL_LEFT) {
                        each.setOffset(xOffset, yOffset - each.getY());
                        if (each.getLayout() != DEFAULT) {
                            yOffset += each.getChildrenHeight() + PADDING;
                        } else {
                            yOffset += each.getFullBoundsReference().getHeight() + PADDING;
                        }
                    } else if (layout == VERTICAL_RIGHT) {
                        each.setOffset(xOffset, yOffset - each.getY());
                        double w;
                        if (each.getLayout() != DEFAULT) {
                            yOffset += each.getChildrenHeight() + PADDING;
                            w = each.getChildrenWidth();
                        } else {
                            yOffset += each.getFullBoundsReference().getHeight() + PADDING;
                            w = each.getFullBoundsReference().getWidth();
                        }
                        max_width = (int) (w > max_width ? w : max_width);
                        justifiedNodes.add(each);
                    } else if (layout == VERTICAL_CENTERED) {
                        each.setOffset(xOffset, yOffset - each.getY());
                        double w;
                        if (each.getLayout() != DEFAULT) {
                            yOffset += each.getChildrenHeight() + PADDING;
                            w = each.getChildrenWidth();
                        } else {
                            yOffset += each.getFullBoundsReference().getHeight() + PADDING;
                            w = each.getFullBoundsReference().getWidth();
                        }
                        max_width = (int) (w > max_width ? w : max_width);
                        justifiedNodes.add(each);
                    }
                }
                if (layout == VERTICAL_RIGHT) {
                    for (PNodeLayoutable node : justifiedNodes) {
                        Point2D offset = node.getOffset();
                        if (node.getLayout() != DEFAULT) {
                            node.setOffset(max_width - node.getChildrenWidth() + PADDING, offset.getY());
                        } else {
                            node.setOffset(max_width - node.getFullBoundsReference().getWidth() + PADDING, offset.getY());
                        }
                    }
                }
                if (layout == VERTICAL_CENTERED) {
                    for (PNodeLayoutable node : justifiedNodes) {
                        Point2D offset = node.getOffset();
                        if (node.getLayout() != DEFAULT) {
                            node.setOffset(max_width / 2 - node.getChildrenWidth() / 2 + PADDING, offset.getY());
                        } else {
                            node.setOffset(max_width / 2 - node.getFullBoundsReference().getWidth() / 2 + PADDING, offset.getY());
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
        if (layout == HORIZONTAL_TOP) {
            double offset = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenWidth() + PADDING;
                } else {
                    offset += each.getFullBoundsReference().getWidth() + PADDING;
                }
            }
            return offset + PADDING;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenWidth() + PADDING;
                } else {
                    w = each.getFullBoundsReference().getWidth() + PADDING;
                }
                max = w > max ? w : max;
            }
            return max + PADDING;
        }
    }

    @Override
    public double getChildrenHeight() {
        if (layout == VERTICAL_LEFT || layout == VERTICAL_CENTERED || layout == VERTICAL_RIGHT) {
            double offset = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                if (each.getLayout() != DEFAULT) {
                    offset += each.getChildrenHeight() + PADDING;
                } else {
                    offset += each.getFullBoundsReference().getHeight() + PADDING;
                }
            }
            return offset + PADDING;
        } else {
            double max = 0;

            Iterator i = getChildrenIterator();
            while (i.hasNext()) {
                PNodeLayoutable each = (PNodeLayoutable) i.next();
                double w;
                if (each.getLayout() != DEFAULT) {
                    w = each.getChildrenHeight() + PADDING;
                } else {
                    w = each.getFullBoundsReference().getHeight() + PADDING;
                }
                max = w > max ? w : max;
            }
            return max + PADDING;
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
