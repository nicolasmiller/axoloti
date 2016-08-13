package axoloti.object;

import javax.swing.JPanel;

public class TitleBarPanel extends JPanel {
    private AxoObjectInstanceAbstractView axoObjView;
    
    TitleBarPanel(AxoObjectInstanceAbstractView axoObjView) {
        this.axoObjView = axoObjView;
    }
}
