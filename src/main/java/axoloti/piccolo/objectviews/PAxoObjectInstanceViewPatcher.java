package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstancePatcher;
import components.piccolo.control.PButtonComponent;

public class PAxoObjectInstanceViewPatcher extends PAxoObjectInstanceView {

    AxoObjectInstancePatcher model;
    private PButtonComponent BtnUpdate;

    public PAxoObjectInstanceViewPatcher(AxoObjectInstancePatcher model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    public void edit() {
        model.init();
        model.pf.setState(java.awt.Frame.NORMAL);
        model.pf.setVisible(true);
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        //updateObj();
        PButtonComponent BtnEdit = new PButtonComponent("edit", this);
        BtnEdit.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                edit();
            }
        });
        addChild(BtnEdit);
        BtnUpdate = new PButtonComponent("update", this);
        BtnUpdate.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                model.updateObj();
            }
        });
        addChild(BtnUpdate);
        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
        resizeToGrid();
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
