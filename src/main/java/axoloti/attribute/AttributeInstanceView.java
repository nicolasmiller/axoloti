/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.PatchView;
import axoloti.Theme;
import axoloti.atom.AtomInstanceView;
import axoloti.object.AxoObjectInstanceView;
import components.LabelComponent;
import javax.swing.BoxLayout;

/**
 *
 * @author nicolas
 */
public abstract class AttributeInstanceView extends AtomInstanceView {
    AxoObjectInstanceView axoObjectInstanceView;
    PatchView patchView;

    AttributeInstance attributeInstance;

    AttributeInstanceView(AttributeInstance attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        this.attributeInstance = attributeInstance;
        this.axoObjectInstanceView = axoObjectInstanceView;

    }

    public abstract void Lock();

    public abstract void UnLock();

    public void PostConstructor() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        add(new LabelComponent(attributeInstance.getDefinition().getName()));
        doLayout();
        setSize(getPreferredSize());
        doLayout();
    }

    @Override
    public String getName() {
        return attributeInstance.attributeName;
    }
    
    public PatchView getPatchView() {
        return axoObjectInstanceView.getPatchView();
    }
}
