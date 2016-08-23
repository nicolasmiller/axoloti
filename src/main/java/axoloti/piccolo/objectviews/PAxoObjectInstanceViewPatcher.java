package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObject;
import axoloti.object.AxoObjectInstancePatcher;
import axoloti.object.AxoObjectPatcher;
import components.piccolo.control.PButtonComponent;

public class PAxoObjectInstanceViewPatcher extends PAxoObjectInstanceView {

    AxoObjectInstancePatcher model;
    private PButtonComponent BtnUpdate;

    public PAxoObjectInstanceViewPatcher(AxoObjectInstancePatcher model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    @Override
    public void updateObj() {
        if (model.getPatchController() != null) {
            AxoObject ao = model.getPatchController().patchModel.GenerateAxoObj(new AxoObjectPatcher());
            model.setType(ao);
            PostConstructor();
        }
        //TODO what does this do? probably a swing thing
//        validate();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        //updateObj();
        PButtonComponent BtnEdit = new PButtonComponent("edit");
//        BtnEdit.setAlignmentX(LEFT_ALIGNMENT);
//        BtnEdit.setAlignmentY(TOP_ALIGNMENT);
        BtnEdit.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                model.edit();
            }
        });
        addChild(BtnEdit);
        BtnUpdate = new PButtonComponent("update");
//        BtnUpdate.setAlignmentX(LEFT_ALIGNMENT);
//        BtnUpdate.setAlignmentY(TOP_ALIGNMENT);
        BtnUpdate.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                updateObj();
            }
        });
        addChild(BtnUpdate);
        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());
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
