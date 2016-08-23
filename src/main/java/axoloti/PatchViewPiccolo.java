package axoloti;

import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PObjectSearchFrame;
import static axoloti.piccolo.PUtils.asPoint;
import axoloti.piccolo.PatchPCanvas;
import axoloti.piccolo.PatchPNode;
import java.awt.Dimension;
import java.awt.Point;
import org.piccolo2d.PCamera;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PatchViewPiccolo extends PatchView {

    private PatchPCanvas container;

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
            //TODO only do this if canvas has focus
//            int xsteps = 1;
//            int ysteps = 1;
//            if (!e.isShiftDown()) {
//                xsteps = Constants.X_GRID;
//                ysteps = Constants.Y_GRID;
//            }
//            if ((e.getKeyCode() == KeyEvent.VK_SPACE)
//                    || ((e.getKeyCode() == KeyEvent.VK_N) && !KeyUtils.isControlOrCommandDown(e))
//                    || ((e.getKeyCode() == KeyEvent.VK_1) && KeyUtils.isControlOrCommandDown(e))) {
//                e.setHandled(true);
//                ShowClassSelector(e, null, null);
//            } else if (((e.getKeyCode() == KeyEvent.VK_C) && !KeyUtils.isControlOrCommandDown(e))
//                    || ((e.getKeyCode() == KeyEvent.VK_5) && KeyUtils.isControlOrCommandDown(e))) {
//                getPatchController().AddObjectInstance(MainFrame.axoObjects.GetAxoObjectFromName(patchComment, null).get(0), asPoint(e.getPosition()));
//                e.setHandled(true);
//            } else if ((e.getKeyCode() == KeyEvent.VK_I) && !KeyUtils.isControlOrCommandDown(e)) {
//                e.setHandled(true);
//                ShowClassSelector(e, null, patchInlet);
//            } else if ((e.getKeyCode() == KeyEvent.VK_O) && !KeyUtils.isControlOrCommandDown(e)) {
//                e.setHandled(true);
//                ShowClassSelector(e, null, patchOutlet);
//            } else if ((e.getKeyCode() == KeyEvent.VK_D) && !KeyUtils.isControlOrCommandDown(e)) {
//                e.setHandled(true);
//                ShowClassSelector(e, null, patchDisplay);
//            } else if ((e.getKeyCode() == KeyEvent.VK_M) && !KeyUtils.isControlOrCommandDown(e)) {
//                e.setHandled(true);
//                if (e.isShiftDown()) {
//                    ShowClassSelector(e, null, patchMidiKey);
//                } else {
//                    ShowClassSelector(e, null, patchMidi);
//                }
//            } else if ((e.getKeyCode() == KeyEvent.VK_A) && !KeyUtils.isControlOrCommandDown(e)) {
//                e.setHandled(true);
//                if (e.isShiftDown()) {
//                    ShowClassSelector(e, null, patchAudioOut);
//                } else {
//                    ShowClassSelector(e, null, patchAudio);
//                }
//            } else if ((e.getKeyCode() == KeyEvent.VK_DELETE) || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
//                deleteSelectedAxoObjectInstanceViews();
//
//                e.setHandled(true);
//            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
//                MoveSelectedAxoObjInstances(Direction.UP, xsteps, ysteps);
//                e.setHandled(true);
//            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                MoveSelectedAxoObjInstances(Direction.DOWN, xsteps, ysteps);
//                e.setHandled(true);
//            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                MoveSelectedAxoObjInstances(Direction.RIGHT, xsteps, ysteps);
//                e.setHandled(true);
//            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                MoveSelectedAxoObjInstances(Direction.LEFT, xsteps, ysteps);
//                e.setHandled(true);
//            }
        }
    };

    @Override
    public PatchViewportView getViewportView() {
        if (container == null) {
            container = new PatchPCanvas(this);
            container.setEnabled(true);
            container.addInputEventListener(inputEventHandler);
        }
        return container;
    }

    @Override
    public void repaint() {

    }

    @Override
    public Point getLocationOnScreen() {
        return container.getLocationOnScreen();
    }

    @Override
    public void PostConstructor() {
        getPatchController().patchModel.PostContructor();
        modelChanged(false);
        ShowPreset(0);
    }

    @Override
    public void requestFocus() {

    }

    public void clampLayerSize(Dimension s) {
        if (container.getParent() != null) {
            if (s.width < container.getParent().getWidth()) {
                s.width = container.getParent().getWidth();
            }
            if (s.height < container.getParent().getHeight()) {
                s.height = container.getParent().getHeight();
            }
        }
    }

    @Override
    public void AdjustSize() {
        Dimension s = getPatchController().GetSize();
        clampLayerSize(s);

        if (!container.getSize().equals(s)) {
            container.setSize(s);
        }
        if (!container.getPreferredSize().equals(s)) {
            container.setPreferredSize(s);
        }
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

    public void add(IAxoObjectInstanceView v) {
        container.getLayer().addChild((PatchPNode) v);
        objectInstanceViews.add(v);
    }

    public void remove(IAxoObjectInstanceView v) {
        container.getLayer().removeChild((PatchPNode) v);
        objectInstanceViews.remove(v);
    }

    public void removeAllObjectViews() {
        for (IAxoObjectInstanceView objectView : objectInstanceViews) {
            container.getLayer().removeChild((PatchPNode) objectView);
        }
        objectInstanceViews.clear();
    }

    public void removeAllNetViews() {
        for (INetView netView : netViews) {
            container.getLayer().removeChild((PatchPNode) netView);
        }
        netViews.clear();
    }

    public void add(INetView v) {
        netViews.add(v);
        container.getLayer().addChild((PatchPNode) v);
    }

    public void validate() {
    }

    public void validateObjects() {
    }

    public void validateNets() {
    }

    public PatchPCanvas getCanvas() {
        return container;
    }

    public void ShowClassSelector(PInputEvent e, AxoObjectInstanceViewAbstract o, String searchString) {
        if (isLocked()) {
            return;
        }
        if (osf == null) {
            osf = new PObjectSearchFrame(getPatchController());
        }
        osf.Launch(asPoint(e.getPosition()), o, searchString);
        Point ps = getPatchController().getViewLocationOnScreen();
        Point patchLocClipped = osf.clipToStayWithinScreen(asPoint(e.getCanvasPosition()));
        osf.setLocation(patchLocClipped.x + ps.x, patchLocClipped.y + ps.y);
        osf.setVisible(true);
    }
}
