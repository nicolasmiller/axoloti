package axoloti.piccolo;

import axoloti.PatchViewPiccolo;
import axoloti.PatchViewportView;
import axoloti.Theme;
import java.awt.Component;
import java.awt.event.InputEvent;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.extras.pswing.PSwingCanvas;
import org.piccolo2d.util.PPaintContext;

public class PatchPCanvas extends PSwingCanvas implements PatchViewportView {

    private PatchSelectionEventHandler selectionEventHandler;
    private PatchViewPiccolo parent;

    public PatchPCanvas() {
        this(null);
    }

    public PatchPCanvas(PatchViewPiccolo parent) {
        super();
//        PDebug.debugPrintFrameRate = true;
//        PDebug.debugPaintCalls = true;
//        PDebug.debugBounds = true;
        this.setLocation(0, 0);
        setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        getPanEventHandler().setEventFilter(new PInputEventFilter(InputEvent.BUTTON2_MASK));

        this.setZoomEventHandler(null);
        PatchMouseWheelZoomEventHandler zoomEventHandler = new PatchMouseWheelZoomEventHandler();
        zoomEventHandler.zoomAboutMouse();
        addInputEventListener(zoomEventHandler);

        selectionEventHandler = new PatchSelectionEventHandler(getLayer(),
                getLayer(), parent);
        addInputEventListener(selectionEventHandler);
        getRoot().getDefaultInputManager().setKeyboardFocus(selectionEventHandler);

        this.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        this.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        this.setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
    }

    public void updateSize() {

    }

    public Component getComponent() {
        return this;
    }

    public void setWidth(int foo) {

    }

    public void setHeight(int bar) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public boolean isPaused() {
        return false;
    }

    public void stop() {

    }

    public void destroy() {

    }

    public void init() {

    }

    public boolean isDestroyed() {
        return false;
    }

    @Override
    public double getViewScale() {
        return this.getCamera().getViewScale();
    }

    public void select(PNode node) {
        this.selectionEventHandler.select(node);
    }

    public void unselect(PNode node) {
        this.selectionEventHandler.unselect(node);
    }

    public boolean isSelected(PNode node) {
        return this.selectionEventHandler.isSelected(node);
    }
}
