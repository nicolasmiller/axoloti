/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.object;

import axoloti.objecteditor.AxoObjectEditor;
import components.ButtonComponent;
import java.awt.Component;
import javax.swing.SwingUtilities;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstancePatcherObjectView extends AxoObjectInstanceView {

    AxoObjectEditor aoe;

    AxoObjectInstancePatcherObject model;
    ButtonComponent BtnEdit;

    public AxoObjectInstancePatcherObjectView(AxoObjectInstancePatcherObject model) {
        super(model);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        //updateObj();
        BtnEdit = new ButtonComponent("edit");
        BtnEdit.setAlignmentX(LEFT_ALIGNMENT);
        BtnEdit.setAlignmentY(TOP_ALIGNMENT);
        BtnEdit.addActListener(new ButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                edit();
            }
        });
        add(BtnEdit);
        for (Component cmp : getComponents()) {
            cmp.doLayout();
        }
        resizeToGrid();
    }

    @Override
    public void updateObj() {
        if (model.ao != null) {
            model.ao.id = "patch/object";
            model.setType(model.ao);
            PostConstructor();
        }
        for (Component cmp : getComponents()) {
            cmp.doLayout();
        }
        doLayout();
        invalidate();
        validate();
    }

    @Override
    public void OpenEditor() {
        edit();
    }

    public void edit() {
        if (model.ao == null) {
            model.ao = new AxoObject();
//            ao.id = "id";
            model.ao.sDescription = "";
        }
        if (aoe == null) {
            aoe = new AxoObjectEditor(model.ao);
        } else {
            aoe.updateReferenceXML();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                aoe.setState(java.awt.Frame.NORMAL);
                aoe.setVisible(true);
            }
        });
    }

    public boolean isEditorOpen() {
        return aoe != null && aoe.isVisible();
    }

    @Override
    public void Close() {
        super.Close();
        if (aoe != null) {
            aoe.Close();
        }
    }
}
