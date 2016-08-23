package axoloti.piccolo;

import axoloti.PatchViewportView;
import axoloti.Theme;
import java.awt.Component;
import java.awt.event.InputEvent;
import org.piccolo2d.PCanvas;
import org.piccolo2d.event.PInputEventFilter;

public class PatchPCanvas extends PCanvas implements PatchViewportView {

    public PatchPCanvas() {
        super();
        setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        getPanEventHandler().setEventFilter(new PInputEventFilter(InputEvent.BUTTON2_MASK));

        PatchMouseWheelZoomEventHandler zoomEventHandler = new PatchMouseWheelZoomEventHandler();
        zoomEventHandler.zoomAboutMouse();
        addInputEventListener(zoomEventHandler);

        PatchSelectionEventHandler selectionEventHandler = new PatchSelectionEventHandler(getLayer(),
                getLayer());
        addInputEventListener(selectionEventHandler);
        getRoot().getDefaultInputManager().setKeyboardFocus(selectionEventHandler);
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
}
