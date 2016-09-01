package axoloti.poutlets;

import axoloti.Theme;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import axoloti.piolet.PIoletAbstract;
import axoloti.pobjectviews.PAxoObjectInstanceView;
import static axoloti.processing.PLayoutType.HORIZONTAL_CENTERED;
import components.processing.PJackOutputComponent;
import components.processing.PLabelComponent;
import components.processing.PSignalMetaDataIcon;
import processing.core.PApplet;

public class POutletInstanceView extends PIoletAbstract implements IOutletInstanceView {

    POutletInstancePopupMenu popup;
    OutletInstance outletInstance;

    public POutletInstanceView(OutletInstance outletInstance, PAxoObjectInstanceView axoObj) {
        super(axoObj.getPApplet());
        this.outletInstance = outletInstance;
        this.axoObj = axoObj;
        popup = new POutletInstancePopupMenu(axoObj.getPApplet(), this);
    }

    @Override
    public void PostConstructor() {
        PApplet p = getPApplet();
        setLayout(HORIZONTAL_CENTERED);
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        if (axoObj.getObjectInstance().getType().GetOutlets().size() > 1) {
            add(new PLabelComponent(p, outletInstance.getOutlet().getName()));
        }
        add(new PSignalMetaDataIcon(p, outletInstance.getOutlet().GetSignalMetaData()));
        jack = new PJackOutputComponent(p, this);
        jack.setForeground(outletInstance.getOutlet().getDatatype().GetColor());
        add(jack);

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
//        addMouseListener(this);
//        addMouseMotionListener(this);
    }

    @Override
    public OutletInstance getOutletInstance() {
        return outletInstance;
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
