package axoloti.piccolo;

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

    public PatchSelectionEventHandler(final PNode marqueeParent, final PNode selectableParent) {
        super(marqueeParent, selectableParent);
        this.canvas = marqueeParent;
    }

    @Override
    public void decorateSelectedNode(final PNode node) {
        PAxoObjectInstanceViewAbstract view = (PAxoObjectInstanceViewAbstract) node;
        view.setSelected(true);
    }

    @Override
    public void undecorateSelectedNode(final PNode node) {
        PAxoObjectInstanceViewAbstract view = (PAxoObjectInstanceViewAbstract) node;
        view.setSelected(false);
    }

    final private int gridSpacing = Constants.X_GRID;
    final private Map<PNode, Point2D> nodeStartPositions = new HashMap<>();

    @Override
    protected void startDrag(final PInputEvent event) {
        super.startDrag(event);

        for (Object o : getSelection()) {
            PNode pn = (PNode) o;
            nodeStartPositions.put(pn, pn.getOffset());
        }
    }

    @Override
    protected void dragStandardSelection(final PInputEvent e) {
        if (!e.isShiftDown()) {
            final Point2D start = e.getCamera().localToView(
                    (Point2D) getMousePressedCanvasPoint().clone());
            final Point2D current = e.getPositionRelativeTo(canvas);
            final Point2D dest = new Point2D.Double();

            final Iterator selectionEn = getSelection().iterator();
            while (selectionEn.hasNext()) {
                final PNode node = (PNode) selectionEn.next();
                dest.setLocation(nodeStartPositions.get(node).getX() + current.getX() - start.getX(), nodeStartPositions.get(node).getY()
                        + current.getY() - start.getY());

                dest.setLocation(dest.getX() - dest.getX() % gridSpacing, dest.getY() - dest.getY() % gridSpacing);

                node.setOffset(dest.getX(), dest.getY());
            }
        } else {
            super.dragStandardSelection(e);
        }
    }

    @Override
    public void mouseDragged(PInputEvent e) {
        if (!e.isMiddleMouseButton()) {
            super.mouseDragged(e);
            for (Object o : this.getSelection()) {
                PAxoObjectInstanceViewAbstract view = (PAxoObjectInstanceViewAbstract) o;
                view.setLocation((int) e.getPosition().getX(), (int) e.getPosition().getY());
            }
        }
    }
}
