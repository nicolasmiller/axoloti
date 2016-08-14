/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.inlets;

import axoloti.MainFrame;
import axoloti.NetView;
import axoloti.Theme;
import axoloti.iolet.IoletAbstract;
import components.JackInputComponent;
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
public class InletInstanceView extends IoletAbstract {
    InletInstancePopupMenu popup = new InletInstancePopupMenu(this);

    InletInstance inletInstance;
    
    public InletInstanceView(InletInstance inletInstance) {
        this.inletInstance = inletInstance;
        this.setBackground(Theme.getCurrentTheme().Object_Default_Background);
    }

    public final void PostConstructor() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        setMaximumSize(new Dimension(32767, 14));
        jack = new JackInputComponent(this);
        jack.setForeground(inletInstance.getInlet().getDatatype().GetColor());
        jack.setBackground(Theme.getCurrentTheme().Object_Default_Background);
        add(jack);
        add(new SignalMetaDataIcon(inletInstance.getInlet().GetSignalMetaData()));
        if (inletInstance.getObjectInstance().getType().GetInlets().size() > 1) {
            add(Box.createHorizontalStrut(3));
            add(new LabelComponent(getName()));
        }
        add(Box.createHorizontalGlue());
        setComponentPopupMenu(popup);
        setToolTipText(inletInstance.getInlet().description);

        addMouseListeners();
    }
    
    @Override
    public JPopupMenu getPopup() {
        return popup;
    }
    
    public String getInletname() {
        int sepIndex = name.lastIndexOf(' ');
            return name.substring(sepIndex + 1);
    }
    
    public InletInstance getInletInstance() {
        return this.inletInstance;
    }
    

}