package axoloti;

public class PatchController {
    public PatchModel patchModel;
    public PatchView patchView;
    
    public PatchController() {
    }
    
    public void setPatchModel(PatchModel patchModel) {
        this.patchModel = patchModel;
    }
    
    public void setPatchView(PatchView patchView) {
        this.patchView = patchView;
    }
}