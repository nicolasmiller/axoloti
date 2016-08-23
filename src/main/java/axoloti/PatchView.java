package axoloti;

import axoloti.datatypes.DataType;
import axoloti.object.AxoObjectFromPatch;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import qcmds.QCmdChangeWorkingDirectory;
import qcmds.QCmdCompilePatch;
import qcmds.QCmdCreateDirectory;
import qcmds.QCmdLock;
import qcmds.QCmdProcessor;
import qcmds.QCmdStart;
import qcmds.QCmdStop;
import qcmds.QCmdUploadPatch;

public abstract class PatchView implements ModelChangedListener {
    boolean locked = false;
    
    public abstract PatchViewportView getViewportView();
    public abstract void modelChanged(boolean updateSelection);
    public abstract void ShowPreset(int presetNumber);
    public abstract Dimension GetSize();
    public abstract void repaint();
    public abstract Point getLocationOnScreen();
    public abstract void Lock();
    public abstract void Unlock();
    public abstract void PostConstructor();
    abstract PatchModel getSelectedObjects();
    abstract void deleteSelectedAxoObjectInstanceViews();
    public abstract PatchController getPatchController();
    public abstract void SelectNone();
    abstract Dimension GetInitialSize();
    public abstract void requestFocus();
    public abstract void Close();    
    abstract boolean save(File f);
    public abstract void AdjustSize();
    abstract void SetCordsInBackground(boolean cordsInBackground);
    abstract void SelectAll();
    public abstract ArrayList<AxoObjectInstanceViewAbstract> getObjectInstanceViews();
    public abstract void updateNetVisibility();
    public abstract void startRendering();
    
    void paste(String v, Point pos, boolean restoreConnectionsToExternalOutlets) {
        SelectNone();
        getPatchController().paste(v, pos, restoreConnectionsToExternalOutlets);
    }
    
    public void setFileNamePath(String FileNamePath) {
        getPatchController().setFileNamePath(FileNamePath);
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    TextEditor NotesFrame;
    
    void ShowNotesFrame() {
        if (NotesFrame == null) {
            NotesFrame = new TextEditor(new StringRef(), getPatchController().getPatchFrame());
            NotesFrame.setTitle("notes");
            NotesFrame.SetText(getPatchController().patchModel.notes);
            NotesFrame.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                }

                @Override
                public void focusLost(FocusEvent e) {
                    getPatchController().patchModel.notes = NotesFrame.GetText();
                }
            });
        }
        NotesFrame.setVisible(true);
        NotesFrame.toFront();
    }
    
    public ObjectSearchFrame osf;

    public void ShowClassSelector(Point p, AxoObjectInstanceViewAbstract o, String searchString) {
        if (isLocked()) {
            return;
        }
        if (osf == null) {
            osf = new ObjectSearchFrame(getPatchController());
        }
        osf.Launch(p, o, searchString);
    }
    
    void GoLive() {
        PatchView patchView = getPatchController().getPatchView();
        if (patchView != null) {
            patchView.Unlock();
        }

        QCmdProcessor qCmdProcessor = getPatchController().GetQCmdProcessor();

        qCmdProcessor.AppendToQueue(new QCmdStop());
        String f = "/" + getPatchController().getSDCardPath();
        System.out.println("pathf" + f);
        qCmdProcessor.AppendToQueue(new QCmdCreateDirectory(f));
        qCmdProcessor.AppendToQueue(new QCmdChangeWorkingDirectory(f));
        getPatchController().UploadDependentFiles();
        getPatchController().ShowPreset(0);
        getPatchController().WriteCode();
        getPatchController().setPresetUpdatePending(false);
        qCmdProcessor.setPatchController(null);
        qCmdProcessor.AppendToQueue(new QCmdCompilePatch(getPatchController()));
        qCmdProcessor.AppendToQueue(new QCmdUploadPatch());
        qCmdProcessor.AppendToQueue(new QCmdStart(getPatchController()));
        qCmdProcessor.AppendToQueue(new QCmdLock(getPatchController()));
    }
    
    private Map<DataType, Boolean> cableTypeEnabled = new HashMap<DataType, Boolean>();

    public void setCableTypeEnabled(DataType type, boolean enabled) {
        cableTypeEnabled.put(type, enabled);
    }

    public Boolean isCableTypeEnabled(DataType type) {
        if (cableTypeEnabled.containsKey(type)) {
            return cableTypeEnabled.get(type);
        } else {
            return true;
        }
    }
    
    public AxoObjectFromPatch ObjEditor;
    
    public void setObjEditor(AxoObjectFromPatch ObjEditor) {
        this.ObjEditor = ObjEditor;
    }
    
    public void ShowCompileFail() {
        Unlock();
    }
}