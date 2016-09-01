package axoloti.pinlets;

import axoloti.Theme;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstance;
import axoloti.piolet.PIoletAbstract;
import axoloti.pobjectviews.PAxoObjectInstanceView;
import static axoloti.processing.PLayoutType.HORIZONTAL_CENTERED;
import components.processing.PJackInputComponent;
import components.processing.PLabelComponent;
import components.processing.PSignalMetaDataIcon;
import processing.core.PApplet;

public class PInletInstanceView extends PIoletAbstract implements IInletInstanceView {

    PInletInstancePopupMenu popup;
    InletInstance inletInstance;

    public PInletInstanceView(InletInstance inletInstance, PAxoObjectInstanceView axoObj) {
        super(axoObj.getPApplet());
        this.inletInstance = inletInstance;
        this.axoObj = axoObj;
        popup = new PInletInstancePopupMenu(axoObj.getPApplet(), this);
    }

    @Override
    public void PostConstructor() {
        PApplet p = getPApplet();
        setLayout(HORIZONTAL_CENTERED);

        setBackground(Theme.getCurrentTheme().Object_Default_Background);

        add(new PSignalMetaDataIcon(p, inletInstance.getInlet().GetSignalMetaData()));
        jack = new PJackInputComponent(p, this);
        jack.setForeground(inletInstance.getInlet().getDatatype().GetColor());
        add(jack);
        if (axoObj.getObjectInstance().getType().GetInlets().size() > 1) {
            add(new PLabelComponent(p, inletInstance.getInlet().getName()));
        }

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
//        addMouseListener(this);
//        addMouseMotionListener(this);
    }

    @Override
    public InletInstance getInletInstance() {
        return inletInstance;
    }

    @Override
    public void setHighlighted(boolean highlighted) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void deleteNet() {

    }
}
