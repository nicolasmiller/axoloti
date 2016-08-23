package axoloti;

import axoloti.slick2d.Slick2dRenderer;
import org.newdawn.slick.CanvasGameContainer;

public class PatchViewSlick2D implements ModelChangedListener {
    private PatchController patchController;
    private CanvasGameContainer container;

    public PatchViewSlick2D(PatchController patchController) {
        this.patchController = patchController;
        container = Slick2dRenderer.getCanvas();
    }
    
    public CanvasGameContainer getCanvas() {
        return container;
    }

    @Override
    public void modelChanged() {
    }
}