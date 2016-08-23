package axoloti;

import axoloti.inlets.IInletInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.piccolo.PObjectSearchFrame;
import axoloti.piccolo.PPatchBorder;
import static axoloti.piccolo.PUtils.asPoint;
import axoloti.piccolo.PatchPCanvas;
import axoloti.piccolo.PatchPNode;
import axoloti.utils.Constants;
import axoloti.utils.KeyUtils;
import components.piccolo.PFocusable;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import org.piccolo2d.PCamera;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PatchViewPiccolo extends PatchView {

    private PatchPCanvas canvas;

    public PatchViewPiccolo(PatchController patchController) {
        super(patchController);
    }

    PBasicInputEventHandler inputEventHandler = new PBasicInputEventHandler() {
        @Override
        public void mousePressed(PInputEvent e) {
            e.getInputManager().setKeyboardFocus(this);
        }

        @Override
        public void mouseClicked(PInputEvent e) {
            if (e.isLeftMouseButton()) {
                if (e.getPickedNode() instanceof PCamera) {
                    if (e.getClickCount() == 2) {
                        ShowClassSelector(e, null, null);
                    } else if ((osf != null) && osf.isVisible()) {
                        osf.Accept();
                    } //                Layers.requestFocusInWindow();
                }
            } else if ((osf != null) && osf.isVisible()) {
                osf.Cancel();
            } //          Layers.requestFocusInWindow();
            e.setHandled(true);
        }

        @Override
        public void keyPressed(PInputEvent e) {
            int xsteps = 1;
            int ysteps = 1;
            if (!e.isShiftDown()) {
                xsteps = Constants.X_GRID;
                ysteps = Constants.Y_GRID;
            }
            if ((e.getKeyCode() == KeyEvent.VK_SPACE)
                    || ((e.getKeyCode() == KeyEvent.VK_N) && !KeyUtils.isControlOrCommandDown(e))
                    || ((e.getKeyCode() == KeyEvent.VK_1) && KeyUtils.isControlOrCommandDown(e))) {
                e.setHandled(true);
                ShowClassSelector(e, null, null);
            } else if (((e.getKeyCode() == KeyEvent.VK_C) && !KeyUtils.isControlOrCommandDown(e))
                    || ((e.getKeyCode() == KeyEvent.VK_5) && KeyUtils.isControlOrCommandDown(e))) {
                Point patchPosition = asPoint(e.getInputManager().getCurrentCanvasPosition());
                getCanvas().getCamera().getViewTransform().inverseTransform(patchPosition, patchPosition);
                getPatchController().AddObjectInstance(
                        MainFrame.axoObjects.GetAxoObjectFromName(patchComment, null).get(0), patchPosition);
                e.setHandled(true);
            } else if ((e.getKeyCode() == KeyEvent.VK_I) && !KeyUtils.isControlOrCommandDown(e)) {
                e.setHandled(true);
                ShowClassSelector(e, null, patchInlet);
            } else if ((e.getKeyCode() == KeyEvent.VK_O) && !KeyUtils.isControlOrCommandDown(e)) {
                e.setHandled(true);
                ShowClassSelector(e, null, patchOutlet);
            } else if ((e.getKeyCode() == KeyEvent.VK_D) && !KeyUtils.isControlOrCommandDown(e)) {
                e.setHandled(true);
                ShowClassSelector(e, null, patchDisplay);
            } else if ((e.getKeyCode() == KeyEvent.VK_M) && !KeyUtils.isControlOrCommandDown(e)) {
                e.setHandled(true);
                if (e.isShiftDown()) {
                    ShowClassSelector(e, null, patchMidiKey);
                } else {
                    ShowClassSelector(e, null, patchMidi);
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_A) && !KeyUtils.isControlOrCommandDown(e)) {
                e.setHandled(true);
                if (e.isShiftDown()) {
                    ShowClassSelector(e, null, patchAudioOut);
                } else {
                    ShowClassSelector(e, null, patchAudio);
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_DELETE) || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
                deleteSelectedAxoObjectInstanceViews();

                e.setHandled(true);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                MoveSelectedAxoObjInstances(Direction.UP, xsteps, ysteps);
                e.setHandled(true);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                MoveSelectedAxoObjInstances(Direction.DOWN, xsteps, ysteps);
                e.setHandled(true);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                MoveSelectedAxoObjInstances(Direction.RIGHT, xsteps, ysteps);
                e.setHandled(true);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                MoveSelectedAxoObjInstances(Direction.LEFT, xsteps, ysteps);
                e.setHandled(true);
            }
        }
    };

    @Override
    public PatchViewportView getViewportView() {
        if (canvas == null) {
            canvas = new PatchPCanvas(this);

            canvas.addInputEventListener(inputEventHandler);
            canvas.setTransferHandler(TH);

            InputMap inputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    KeyUtils.CONTROL_OR_CMD_MASK), "cut");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                    KeyUtils.CONTROL_OR_CMD_MASK), "copy");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                    KeyUtils.CONTROL_OR_CMD_MASK), "paste");

            ActionMap map = canvas.getActionMap();
            map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                    TransferHandler.getCutAction());
            map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                    TransferHandler.getCopyAction());
            map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                    TransferHandler.getPasteAction());
            canvas.setEnabled(true);
            canvas.setFocusable(true);
            canvas.setFocusCycleRoot(true);
            canvas.setDropTarget(dt);
            canvas.getRoot().getDefaultInputManager().setKeyboardFocus(inputEventHandler);
        }
        return canvas;
    }

    @Override
    public void repaint() {
        // no need to explicitly repaint on model change
    }

    @Override
    public Point getLocationOnScreen() {
        return getCanvas().getLocationOnScreen();
    }

    @Override
    public void PostConstructor() {
        getPatchController().patchModel.PostContructor();
        modelChanged(false);
        getPatchController().patchModel.PromoteOverloading(true);
        ShowPreset(0);
        SelectNone();
    }

    @Override
    public void SelectNone() {
        canvas.getSelectionEventHandler().unselectAll();
    }

    @Override
    public void requestFocus() {

    }

    private PPatchBorder patchBorder;

    @Override
    public void AdjustSize() {
//        Dimension s = getPatchController().GetSize();
//        if (patchBorder == null) {
//            patchBorder = new PPatchBorder();
//            getCanvas().getLayer().addChild(patchBorder);
//        }
//        patchBorder.setBounds(0, 0, s.width, s.height);
    }

    @Override
    public void Close() {
        super.Close();
    }

    @Override
    void SetCordsInBackground(boolean cordsInBackground) {

    }

    @Override
    public void startRendering() {
    }

    public void addFocusables(ListIterator<PNode> childrenIterator) {
        while (childrenIterator.hasNext()) {
            PNode child = childrenIterator.next();
            addFocusables(child.getChildrenIterator());
            if (child instanceof PFocusable) {
                addFocusable((PFocusable) child);
            }
        }
    }

    public void removeFocusables(ListIterator<PNode> childrenIterator) {
        while (childrenIterator.hasNext()) {
            PNode child = childrenIterator.next();
            removeFocusables(child.getChildrenIterator());
            if (child instanceof PFocusable) {
                removeFocusable((PFocusable) child);
            }
        }
    }

    public void add(IAxoObjectInstanceView v) {
        PatchPNode node = (PatchPNode) v;
        getCanvas().getLayer().addChild(node);
        objectInstanceViews.add(v);
        addFocusables(node.getChildrenIterator());
    }

    public void remove(IAxoObjectInstanceView v) {
        PatchPNode node = (PatchPNode) v;
        getCanvas().getLayer().removeChild(node);
        objectInstanceViews.remove(v);
        removeFocusables(node.getChildrenIterator());
    }

    public void removeAllObjectViews() {
        for (IAxoObjectInstanceView objectView : objectInstanceViews) {
            getCanvas().getLayer().removeChild((PatchPNode) objectView);
        }
        objectInstanceViews.clear();
    }

    public void removeAllNetViews() {
        for (INetView netView : netViews) {
            getCanvas().getLayer().removeChild((PatchPNode) netView);
            for (IInletInstanceView iiv : netView.getDestinationViews()) {
                iiv.repaint();
            }
            for (IOutletInstanceView oiv : netView.getSourceViews()) {
                oiv.repaint();
            }
        }
        netViews.clear();
    }

    public void add(INetView v) {
        netViews.add(v);
        getCanvas().getLayer().addChild((PatchPNode) v);
    }

    public void validate() {
        getCanvas().validate();
    }

    public void validateObjects() {
        for (IAxoObjectInstanceView objectInstanceView : objectInstanceViews) {
            objectInstanceView.validate();
        }
    }

    public void validateNets() {
        for (INetView netView : netViews) {
            netView.validate();
        }
    }

    public PatchPCanvas getCanvas() {
        return (PatchPCanvas) this.getViewportView();
    }

    public void ShowClassSelector(PInputEvent e, IAxoObjectInstanceView o, String searchString) {
        try {
            Point2D p = e.getPosition();
            Point2D q = e.getCanvasPosition();
            ShowClassSelector(asPoint(e.getPosition()), asPoint(e.getCanvasPosition()), o, searchString);
        } catch (RuntimeException ex) {
            // if this is from a keyboard event
            Point canvasPosition = asPoint(e.getInputManager().getCurrentCanvasPosition());
            Point patchPosition = (Point) canvasPosition.clone();
            getCanvas().getCamera().getViewTransform().inverseTransform(patchPosition, patchPosition);
            ShowClassSelector(patchPosition, canvasPosition, o, searchString);
        }
    }

    public void ShowClassSelector(Point patchPosition, Point canvasPosition, IAxoObjectInstanceView o, String searchString) {
        if (isLocked()) {
            return;
        }
        if (canvasPosition == null) {
            canvasPosition = asPoint(getCanvas().getRoot().getDefaultInputManager().getCurrentCanvasPosition());
        }
        if (osf == null) {
            osf = new PObjectSearchFrame(getPatchController());
        }
        osf.Launch(patchPosition, o, searchString);
        Point ps = getPatchController().getViewLocationOnScreen();
        Point patchLocClipped = osf.clipToStayWithinScreen(canvasPosition);
        osf.setLocation(patchLocClipped.x + ps.x, patchLocClipped.y + ps.y);
        osf.setVisible(true);
    }

    @Override
    void paste(String v, Point pos, boolean restoreConnectionsToExternalOutlets) {
        SelectNone();
        getCanvas().getCamera().getViewTransform().inverseTransform(pos, pos);
        getPatchController().paste(v,
                pos,
                restoreConnectionsToExternalOutlets);
    }

    private final List<PFocusable> focusables = new ArrayList<PFocusable>();
    private int focusableIndex = 0;

    public void addFocusable(PFocusable focusable) {
        focusables.add(focusable);
        focusable.setFocusableIndex(focusableIndex);
        focusableIndex += 1;
    }

    public void removeFocusable(PFocusable focusable) {
        focusables.remove(focusable.getFocusableIndex());
        int focusableCount = focusables.size();
        for (int i = focusable.getFocusableIndex(); i < focusableCount; i++) {
            focusables.get(i).setFocusableIndex(i);
        }
        focusableIndex = focusableCount;
    }

    public void transferFocus(PFocusable focusable) {
        focusables.get((focusable.getFocusableIndex() + 1) % focusables.size()).grabFocus();
    }
}
