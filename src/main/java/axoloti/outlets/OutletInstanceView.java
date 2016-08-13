/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.outlets;

import axoloti.Theme;
import axoloti.iolet.IoletAbstract;
import components.LabelComponent;
import components.SignalMetaDataIcon;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public class OutletInstanceView extends IoletAbstract {

    OutletInstancePopupMenu popup = new OutletInstancePopupMenu(this);

    OutletInstance outletInstance;

    public OutletInstanceView(OutletInstance outletInstance) {
        this.outletInstance = outletInstance;
        this.setBackground(Theme.getCurrentTheme().Object_Default_Background);
        PostConstructor();
    }

    public final void PostConstructor() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(32767, 14));
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        add(Box.createHorizontalGlue());
        if (outletInstance.getObjectInstance().getType().GetOutlets().size() > 1) {
            add(new LabelComponent(outletInstance.getOutlet().name));
            add(Box.createHorizontalStrut(2));
        }
        add(new SignalMetaDataIcon(outletInstance.getOutlet().GetSignalMetaData()));
        jack = new components.JackOutputComponent(this);
        jack.setForeground(outletInstance.getOutlet().getDatatype().GetColor());
        add(jack);
        setComponentPopupMenu(popup);
        setToolTipText(outletInstance.getOutlet().description);

        addMouseListeners();
    }

    @Override
    public JPopupMenu getPopup() {
        return popup;
    }

    public OutletInstance getOutletInstance() {
        return this.outletInstance;
    }
}
