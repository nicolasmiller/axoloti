package axoloti;

import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.slick2d.PatchCanvasGameContainer;
import axoloti.slick2d.Slick2dRenderer;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

public class PatchViewSlick2D extends PatchView {

    private PatchController patchController;
    private PatchCanvasGameContainer container;

    public PatchViewSlick2D(PatchController patchController) {
        this.patchController = patchController;
        container = Slick2dRenderer.getCanvas();
    }

    @Override
    public PatchViewportView getViewportView() {
        return container;
    }

    @Override
    public void modelChanged() {
    }

    @Override
    public void ShowPreset(int presetNumber) {
        
    }
    
    @Override
    public Dimension GetSize() {
        return new Dimension(1000, 1000);
    }
    
    @Override
    public void repaint() {
        
    }
    
    @Override
    public Point getLocationOnScreen() {
        return container.getLocationOnScreen();
    }
    
    @Override
    public void Lock() {
        getPatchController().getPatchFrame().SetLive(true);
//        Layers.setBackground(Theme.getCurrentTheme().Patch_Locked_Background);
        locked = true;
//        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
//            o.Lock();
//        }
    }

    @Override
    public void Unlock() {
        getPatchController().getPatchFrame().SetLive(false);
//        Layers.setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        locked = false;
//        ArrayList<AxoObjectInstanceViewAbstract> objectInstanceViewsClone = (ArrayList<AxoObjectInstanceViewAbstract>) objectInstanceViews.clone();
//        for (AxoObjectInstanceViewAbstract o : objectInstanceViewsClone) {
//            o.Unlock();
//        }
    }
    
    @Override
    public void PostConstructor() {
        
    }

    @Override    
    PatchModel getSelectedObjects() {
        return new PatchModel();
    }
    
    @Override
    void deleteSelectedAxoObjectInstanceViews() {
        
    }
    
    @Override
    public PatchController getPatchController() {
        return patchController;
    }
    
    @Override
    public void SelectNone() {
        
    }
    
    @Override
    Dimension GetInitialSize() {
        return new Dimension(1000, 1000);
    }
    
    @Override
    public void requestFocus() {
        
    }
    
    @Override
    public void Close() {
        
    }
    
    @Override
    boolean save(File f) {
        // fix this
        // this should really be on the model
        return false;
    }
    
    @Override
    public void AdjustSize() {
        
    }
    
    @Override
    void SetCordsInBackground(boolean cordsInBackground) {
        
    }
    
    @Override
    void SelectAll() {
        
    }
    
    @Override
    public ArrayList<AxoObjectInstanceViewAbstract> getObjectInstanceViews() {
        return null;
    }
    
    @Override
    public void updateNetVisibility() {
        
    }
    
    @Override
    public void modelChanged(boolean updateSelection) {
        
    }
    
    @Override
    public void startRendering() {
        try {
            container.start();
        }
        catch(SlickException e) {
            throw new RuntimeException(e);
        }
    }
}