package axoloti.objectviews;

import axoloti.PatchView;
import axoloti.object.AxoObjectInstancePatcherObject;
import axoloti.object.AxoObjectPatcherObject;
import axoloti.objecteditor.AxoObjectEditor;
import components.ButtonComponent;
import javax.swing.SwingUtilities;

public class AxoObjectInstanceViewPatcherObject extends AxoObjectInstanceView {

    AxoObjectEditor aoe;

    AxoObjectInstancePatcherObject model;
    ButtonComponent BtnEdit;

    public AxoObjectInstanceViewPatcherObject(AxoObjectInstancePatcherObject model, PatchView patchView) {
        super(model, patchView);
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
            model.setAxoObject(new AxoObjectPatcherObject());
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
