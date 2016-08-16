package axoloti.object;

import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import javax.swing.JPanel;

public class TitleBarPanel extends JPanel {
    private AxoObjectInstanceViewAbstract axoObjView;
    
    TitleBarPanel(AxoObjectInstanceViewAbstract axoObjView) {
        this.axoObjView = axoObjView;
    }
}
