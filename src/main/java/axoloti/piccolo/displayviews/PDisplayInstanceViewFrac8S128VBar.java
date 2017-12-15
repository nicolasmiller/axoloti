package axoloti.piccolo.displayviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.PVGraphComponent;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.displays.DisplayInstanceFrac8S128VBar;

public class PDisplayInstanceViewFrac8S128VBar extends PDisplayInstanceView {

    private PVGraphComponent vgraph;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac8S128VBar(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vgraph = new PVGraphComponent(getModel().getN(), 128, -64, 64, axoObjectInstanceView);
        addChild(vgraph);
    }

    @Override
    DisplayInstanceFrac8S128VBar getModel() {
	return (DisplayInstanceFrac8S128VBar) super.getModel();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            vgraph.setValue(getModel().getIDst());
        }
    }
}
