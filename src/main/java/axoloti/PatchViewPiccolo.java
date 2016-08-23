package axoloti;

import static axoloti.PatchViewSwing.patchAudio;
import static axoloti.PatchViewSwing.patchAudioOut;
import static axoloti.PatchViewSwing.patchComment;
import static axoloti.PatchViewSwing.patchDisplay;
import static axoloti.PatchViewSwing.patchInlet;
import static axoloti.PatchViewSwing.patchMidi;
import static axoloti.PatchViewSwing.patchMidiKey;
import static axoloti.PatchViewSwing.patchOutlet;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PObjectSearchFrame;
import axoloti.piccolo.PatchPCanvas;
import axoloti.piccolo.PatchPNode;
import static axoloti.piccolo.Utils.asPoint;
import axoloti.utils.Constants;
import axoloti.utils.KeyUtils;
import java.awt.Point;
import java.awt.event.KeyEvent;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PatchViewPiccolo extends PatchView {

    private PatchPCanvas container;

    public PatchViewPiccolo(PatchController patchController) {
        super(patchController);
    }

    PBasicInputEventHandler inputEventHandler = new PBasicInputEventHandler() {
        @Override
        public void mouseClicked(PInputEvent e) {
            if (e.isLeftMouseButton()) {
                if (e.getClickCount() == 2) {
                    ShowClassSelector(new Point((int) e.getPosition().getX(), (int) e.getPosition().getY()), null, null);
                } else if ((osf != null) && osf.isVisible()) {
                    osf.Accept();
                } //                Layers.requestFocusInWindow();
                e.setHandled(true);
            } else {
                if ((osf != null) && osf.isVisible()) {
                    osf.Cancel();
                }
                //          Layers.requestFocusInWindow();
                e.setHandled(true);
            }
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
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    ShowClassSelector(p, null, null);
                }
            } else if (((e.getKeyCode() == KeyEvent.VK_C) && !KeyUtils.isControlOrCommandDown(e))
                    || ((e.getKeyCode() == KeyEvent.VK_5) && KeyUtils.isControlOrCommandDown(e))) {
                getPatchController().AddObjectInstance(MainFrame.axoObjects.GetAxoObjectFromName(patchComment, null).get(0), asPoint(e.getCanvasPosition()));
                e.setHandled(true);
            } else if ((e.getKeyCode() == KeyEvent.VK_I) && !KeyUtils.isControlOrCommandDown(e)) {
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    ShowClassSelector(p, null, patchInlet);
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_O) && !KeyUtils.isControlOrCommandDown(e)) {
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    ShowClassSelector(p, null, patchOutlet);
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_D) && !KeyUtils.isControlOrCommandDown(e)) {
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    ShowClassSelector(p, null, patchDisplay);
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_M) && !KeyUtils.isControlOrCommandDown(e)) {
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    if (e.isShiftDown()) {
                        ShowClassSelector(p, null, patchMidiKey);
                    } else {
                        ShowClassSelector(p, null, patchMidi);
                    }
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_A) && !KeyUtils.isControlOrCommandDown(e)) {
                Point p = asPoint(e.getCanvasPosition());
                e.setHandled(true);
                if (p != null) {
                    if (e.isShiftDown()) {
                        ShowClassSelector(p, null, patchAudioOut);
                    } else {
                        ShowClassSelector(p, null, patchAudio);
                    }
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
        if (container == null) {
            container = new PatchPCanvas();
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

    @Override
    public void AdjustSize() {

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
        //start your sketch
        //container.startThread();
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

    @Override
    public void ShowClassSelector(Point p, AxoObjectInstanceViewAbstract o, String searchString) {
        if (isLocked()) {
            return;
        }
        if (osf == null) {
            osf = new PObjectSearchFrame(getPatchController());
        }
        osf.Launch(p, o, searchString);
    }
}
