package axoloti;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.processing.PComponent;
import axoloti.processing.PatchPApplet;
import java.awt.Dimension;
import java.awt.Point;

public class PatchViewProcessing extends PatchView {
    private PatchPApplet container;

    public PatchViewProcessing(PatchController patchController) {
        super(patchController);
    }

    @Override
    public PatchViewportView getViewportView() {
        if(container == null) {
            container = new PatchPApplet();
        }
        return container;
    }
    
    @Override
    public void repaint() {

    }

    @Override
    public Point getLocationOnScreen() {
        return container.getComponent().getLocationOnScreen();
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
    void SetCordsInBackground(boolean cordsInBackground) {

    }

    @Override
    public void startRendering() {
        //start your sketch
        container.startThread();
    }
    
    public void add(IAxoObjectInstanceView v) {
        container.add((PComponent) v);
    }
    
    public void remove(IAxoObjectInstanceView v) {
        container.remove((PComponent) v);
    }
    public void removeAllObjectViews() {}
    public void removeAllNetViews() {}
    public void add(INetView v) {}
    
    public void validate() {}
    public void validateObjects() {}
    public void validateNets() {}
    
    public PatchPApplet getPatchPApplet() {
        return container;
    }
}
