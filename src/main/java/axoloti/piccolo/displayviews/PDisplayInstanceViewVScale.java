package axoloti.piccolo.displayviews;

import axoloti.patch.object.display.DisplayInstanceVScale;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.displays.PVValueLabelsComponent;
import java.beans.PropertyChangeEvent;
import axoloti.displays.DisplayInstanceController;

public class PDisplayInstanceViewVScale extends PDisplayInstanceView {
    private PVValueLabelsComponent vlabels;
    private IAxoObjectInstanceView axoObjectInstanceView;


    public PDisplayInstanceViewVScale(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
