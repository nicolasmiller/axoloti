/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.objectviews;

import axoloti.object.AxoObject;
import axoloti.object.AxoObjectInstancePatcherObject;
import axoloti.objectviews.AxoObjectInstanceView;
import axoloti.objecteditor.AxoObjectEditor;
import components.ButtonComponent;
import java.awt.Component;
import javax.swing.SwingUtilities;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstanceViewPatcherObject extends AxoObjectInstanceView {

    AxoObjectEditor aoe;

    AxoObjectInstancePatcherObject model;
    ButtonComponent BtnEdit;

    public AxoObjectInstanceViewPatcherObject(AxoObjectInstancePatcherObject model) {
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
        resizeToGrid();
    }

    @Override
    public void updateObj() {
        if (model.getAxoObject() != null) {
            model.getAxoObject().id = "patch/object";
            model.setType(model.getAxoObject());
            PostConstructor();
        }
        validate();
    }

    @Override
    public void OpenEditor() {
        edit();
    }

    public void edit() {
        if (model.getAxoObject() == null) {
            model.setAxoObject(new AxoObject());
//            ao.id = "id";
            model.getAxoObject().sDescription = "";
        }
        if (aoe == null) {
            aoe = new AxoObjectEditor(model.getAxoObject());
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
