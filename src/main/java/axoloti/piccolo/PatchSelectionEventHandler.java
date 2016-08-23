package axoloti.piccolo;

import axoloti.PatchViewPiccolo;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewAbstract;
import axoloti.utils.Constants;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.extras.event.PSelectionEventHandler;

public class PatchSelectionEventHandler extends PSelectionEventHandler {

    PNode canvas;
    PatchViewPiccolo parent;

    public PatchSelectionEventHandler(final PNode marqueeParent,
            final PNode selectableParent,
            PatchViewPiccolo parent) {
        super(marqueeParent, selectableParent);
        this.canvas = marqueeParent;
        this.parent = parent;
    }

    @Override
    public void decorateSelectedNode(final PNode node) {
        // avoid adding bounds handles
    }

    @Override
    public void undecorateSelectedNode(final PNode node) {
        // avoid removing bounds handles we never added
    }

    final private int gridSpacing = Constants.X_GRID;
    final private Map<PAxoObjectInstanceViewAbstract, Point2D> nodeStartPositions = new HashMap<>();

    @Override
    protected void startDrag(final PInputEvent event) {
        super.startDrag(event);

        for (Object o : getSelection()) {
            PAxoObjectInstanceViewAbstract pn = (PAxoObjectInstanceViewAbstract) o;
            nodeStartPositions.put(pn, pn.getOffset());
        }
    }

    boolean dragging = false;

    @Override
    protected void dragStandardSelection(final PInputEvent e) {
        if (!e.isMiddleMouseButton()) {
            dragging = true;

            final Point2D start = e.getCamera().localToView(
                    (Point2D) getMousePressedCanvasPoint().clone());
            final Point2D current = e.getPositionRelativeTo(canvas);
            final Point2D dest = new Point2D.Double();

            final Iterator selectionEn = getSelection().iterator();
            while (selectionEn.hasNext()) {
                final PAxoObjectInstanceViewAbstract node = (PAxoObjectInstanceViewAbstract) selectionEn.next();
                dest.setLocation(nodeStartPositions.get(node).getX() + current.getX() - start.getX(),
                        nodeStartPositions.get(node).getY() + current.getY() - start.getY());

                if (!e.isShiftDown()) {
                    // quantize to grid
                    dest.setLocation(dest.getX() - dest.getX() % gridSpacing, dest.getY() - dest.getY() % gridSpacing);
                }
                node.setOffset(dest.getX(), dest.getY());
                node.setLocation((int) dest.getX(), (int) dest.getY());
            }
        }
    }

    @Override
    public void mouseReleased(PInputEvent e) {
        super.mouseReleased(e);
        if (dragging) {
            dragging = false;
            parent.AdjustSize();
            parent.getPatchController().SetDirty();
        }
    }
}
