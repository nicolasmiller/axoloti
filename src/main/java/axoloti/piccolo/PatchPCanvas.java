package axoloti.piccolo;

import axoloti.PatchViewPiccolo;
import axoloti.PatchViewportView;
import axoloti.Theme;
import java.awt.event.InputEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.extras.pswing.PSwingCanvas;
import org.piccolo2d.extras.swing.PScrollPane;
import org.piccolo2d.util.PAffineTransform;
import org.piccolo2d.util.PPaintContext;

public class PatchPCanvas extends PSwingCanvas implements PatchViewportView {

    protected PatchSelectionEventHandler selectionEventHandler;
    protected PatchMouseWheelZoomEventHandler zoomEventHandler;
    private PatchViewPiccolo parent;
    private PPatchBorder patchBorder;

    public PatchPCanvas() {
        // for embededd canvas in ObjectSearchFrame
        this(null);
    }

    public PatchPCanvas(PatchViewPiccolo parent) {
        super();
//        PDebug.debugPrintFrameRate = true;
//        PDebug.debugThreads = true;
//        PDebug.debugPaintCalls = true;
//        PDebug.debugRegionManagement = true;
//        PDebug.debugBounds = true;
        setLocation(0, 0);
        setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        getPanEventHandler().setEventFilter(new PInputEventFilter(InputEvent.BUTTON2_MASK));
        getPanEventHandler().setAutopan(false);

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

    public PPatchBorder getPatchBorder() {
        return patchBorder;
    }

    @Override
    public JComponent getComponent() {
        return this;
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

    private PatchPNode popupParent;

    public void setPopupParent(PatchPNode icon) {
        this.popupParent = icon;
    }

    public PatchPNode getPopupParent() {
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

    @Override
    public void zoomIn() {
        getCamera().scaleView(1.0d + zoomEventHandler.getScaleFactor());
    }

    @Override
    public void zoomOut() {
        getCamera().scaleView(1.0d + -zoomEventHandler.getScaleFactor());
    }

    @Override
    public void zoomDefault() {
        // set transform to identity
        getCamera().setViewTransform(new PAffineTransform());
    }
}
