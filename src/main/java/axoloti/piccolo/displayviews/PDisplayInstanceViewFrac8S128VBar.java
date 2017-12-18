package axoloti.piccolo.displayviews;

import java.beans.PropertyChangeEvent;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.patch.object.display.DisplayInstance;
import axoloti.patch.object.display.DisplayInstanceController;
import axoloti.patch.object.display.DisplayInstanceFrac8S128VBar;
import axoloti.piccolo.components.PVGraphComponent;

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
