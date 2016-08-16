/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.objectviews;

import axoloti.object.AxoObject;
import axoloti.object.AxoObjectInstancePatcher;
import axoloti.objectviews.AxoObjectInstanceView;
import components.ButtonComponent;
import java.awt.Component;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstanceViewPatcher extends AxoObjectInstanceView {

    AxoObjectInstancePatcher model;
    private ButtonComponent BtnUpdate;


    public AxoObjectInstanceViewPatcher(AxoObjectInstancePatcher model) {
        super(model);
        this.model = model;
    }

    @Override
    public void updateObj() {
        if (model.getPatchController() != null) {
            AxoObject ao = model.getPatchController().patchModel.GenerateAxoObj();
            model.setType(ao);
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
    public void PostConstructor() {
        super.PostConstructor();
        //updateObj();
        ButtonComponent BtnEdit = new ButtonComponent("edit");
        BtnEdit.setAlignmentX(LEFT_ALIGNMENT);
        BtnEdit.setAlignmentY(TOP_ALIGNMENT);
        BtnEdit.addActListener(new ButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                model.edit();
            }
        });
        add(BtnEdit);
        BtnUpdate = new ButtonComponent("update");
        BtnUpdate.setAlignmentX(LEFT_ALIGNMENT);
        BtnUpdate.setAlignmentY(TOP_ALIGNMENT);
        BtnUpdate.addActListener(new ButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                updateObj();
            }
        });
        add(BtnUpdate);
        resizeToGrid();
    }

    @Override
    public void Close() {
        super.Close();
        if (model.getPatchFrame() != null) {
            model.getPatchFrame().Close();
        }
    }
    
    @Override
    public void Unlock() {
        super.Unlock();
        if (BtnUpdate != null) {
            BtnUpdate.setEnabled(true);
        }
    }

    @Override
    public void Lock() {
        super.Lock();
        if (BtnUpdate != null) {
            BtnUpdate.setEnabled(false);
        }
    }
}