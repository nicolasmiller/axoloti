package axoloti.piccolo;

import axoloti.PatchViewPiccolo;
import axoloti.PatchViewportView;
import axoloti.Theme;
import components.piccolo.PPopupIcon;
import java.awt.event.InputEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.extras.pswing.PSwingCanvas;
import org.piccolo2d.extras.swing.PScrollPane;
import org.piccolo2d.util.PPaintContext;

public class PatchPCanvas extends PSwingCanvas implements PatchViewportView {

    protected PatchSelectionEventHandler selectionEventHandler;
    protected PatchMouseWheelZoomEventHandler zoomEventHandler;
    private PatchViewPiccolo parent;

    public PatchPCanvas() {
        // for embededd canvas in ObjectSearchFrame
        this(null);
    }

    public PatchPCanvas(PatchViewPiccolo parent) {
        super();
//        PDebug.debugPrintFrameRate = true;
//        PDebug.debugPaintCalls = true;
//        PDebug.debugBounds = true;
        setLocation(0, 0);
        setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        getPanEventHandler().setEventFilter(new PInputEventFilter(InputEvent.BUTTON2_MASK));

        setZoomEventHandler(null);
        zoomEventHandler = new PatchMouseWheelZoomEventHandler();
        zoomEventHandler.zoomAboutMouse();
        addInputEventListener(zoomEventHandler);

        selectionEventHandler = new PatchSelectionEventHandler(getLayer(),
                getLayer(), parent);
        addInputEventListener(selectionEventHandler);
        getRoot().getDefaultInputManager().setKeyboardFocus(selectionEventHandler);

        setAnimatingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        setInteractingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
    }

    public void updateSize() {

    }

    @Override
    public JComponent getComponent() {
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

    public PatchSelectionEventHandler getSelectionEventHandler() {
        return selectionEventHandler;
    }

    private PPopupIcon popupParent;

    public void setPopupParent(PPopupIcon icon) {
        this.popupParent = icon;
    }

    public PPopupIcon getPopupParent() {
        return popupParent;
    }

    public void clearPopupParent() {
        popupParent = null;
    }

    public boolean isPopupVisible() {
        return popupParent != null;
    }

    @Override
    public JScrollPane createScrollPane() {
        return new PScrollPane();
    }
}
