package axoloti;

import axoloti.inlets.InletInstanceView;
import axoloti.object.AxoObjectAbstract;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.outlets.OutletInstanceView;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import qcmds.QCmdCompilePatch;
import qcmds.QCmdProcessor;
import qcmds.QCmdRecallPreset;
import qcmds.QCmdUploadFile;

public class PatchController {

    public PatchModel patchModel;
    public PatchView patchView;
    public PatchFrame patchFrame;

    public PatchController() {
    }

    public void setPatchModel(PatchModel patchModel) {
        this.patchModel = patchModel;
    }

    public void setPatchView(PatchView patchView) {
        this.patchView = patchView;
    }

    public void setPatchFrame(PatchFrame patchFrame) {
        this.patchFrame = patchFrame;
    }

    QCmdProcessor GetQCmdProcessor() {
        if (patchFrame == null) {
            return null;
        }
        return patchFrame.qcmdprocessor;
    }

    MainFrame GetMainFrame() {
        return MainFrame.mainframe;
    }

    public PatchFrame getPatchFrame() {
        return patchFrame;
    }

    void ShowDisconnect() {
        if (patchFrame != null) {
            patchFrame.ShowDisconnect();
        }
    }

    void ShowConnect() {
        if (patchFrame != null) {
            patchFrame.ShowConnect();
        }
    }
    
    public boolean canUndo() {
        return !patchView.isLocked() && patchModel.canUndo();
    }
    
    public boolean canRedo() {
        return !patchView.isLocked() && patchModel.canRedo();
    }

    public void undo() {
        if (canUndo()) {
            patchModel.currentState -= 1;
            patchModel.loadState();
            SetDirty(false);
            patchView.PostConstructor();
        }
    }

    public void redo() {
        if (canRedo()) {
            patchModel.currentState += 1;
            patchModel.loadState();
            SetDirty(false);
            patchView.PostConstructor();
        }
    }

    public void SetDirty(boolean shouldSaveState) {
        patchModel.SetDirty(shouldSaveState);
        patchFrame.updateUndoRedoEnabled();
    }

    public void RecallPreset(int i) {
        GetQCmdProcessor().AppendToQueue(new QCmdRecallPreset(i));
    }

    public void ShowPreset(int i) {
        patchView.ShowPreset(i);
    }

    public void Compile() {
        GetQCmdProcessor().AppendToQueue(new QCmdCompilePatch(this));
    }

    void UploadDependentFiles() {
        String sdpath = getSDCardPath();
        ArrayList<SDFileReference> files = patchModel.GetDependendSDFiles();
        for (SDFileReference fref : files) {
            File f = fref.localfile;
            if (!f.exists()) {
                Logger.getLogger(PatchModel.class.getName()).log(Level.SEVERE, "File reference unresolved: {0}", f.getName());
                continue;
            }
            if (!f.canRead()) {
                Logger.getLogger(PatchModel.class.getName()).log(Level.SEVERE, "Can't read file {0}", f.getName());
                continue;
            }
            if (!SDCardInfo.getInstance().exists("/" + sdpath + "/" + fref.targetPath, f.lastModified(), f.length())) {
                if (f.length() > 8 * 1024 * 1024) {
                    Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "file {0} is larger than 8MB, skip uploading", f.getName());
                    continue;
                }
                GetQCmdProcessor().AppendToQueue(new QCmdUploadFile(f, "/" + sdpath + "/" + fref.targetPath));
            } else {
                Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "file {0} matches timestamp and size, skip uploading", f.getName());
            }
        }
    }

    public void UploadToSDCard(String sdfilename) {
        patchModel.WriteCode();
        Logger.getLogger(PatchFrame.class.getName()).log(Level.INFO, "sdcard filename:{0}", sdfilename);
        QCmdProcessor qcmdprocessor = QCmdProcessor.getQCmdProcessor();
        qcmdprocessor.AppendToQueue(new qcmds.QCmdStop());
        qcmdprocessor.AppendToQueue(new qcmds.QCmdCompilePatch(this));
        // create subdirs...

        for (int i = 1; i < sdfilename.length(); i++) {
            if (sdfilename.charAt(i) == '/') {
                qcmdprocessor.AppendToQueue(new qcmds.QCmdCreateDirectory(sdfilename.substring(0, i)));
                qcmdprocessor.WaitQueueFinished();
            }
        }
        qcmdprocessor.WaitQueueFinished();
        Calendar cal;
        if (patchModel.isDirty()) {
            cal = Calendar.getInstance();
        } else {
            cal = Calendar.getInstance();
            if (getFileNamePath() != null && !getFileNamePath().isEmpty()) {
                File f = new File(getFileNamePath());
                if (f.exists()) {
                    cal.setTimeInMillis(f.lastModified());
                }
            }
        }
        qcmdprocessor.AppendToQueue(new qcmds.QCmdUploadFile(patchModel.getBinFile(), sdfilename, cal));
    }

    public void UploadToSDCard() {
        UploadToSDCard("/" + getSDCardPath() + "/patch.bin");
    }

    public NetView AddConnection(InletInstanceView il, OutletInstanceView ol) {
        if (!patchView.isLocked()) {
            return new NetView(patchModel.AddConnection(il.getInletInstance(), ol.getOutletInstance()));
        } else {
            Logger.getLogger(PatchController.class.getName()).log(Level.INFO, "can't add connection: locked");
        }
        return null;
    }

    public NetView AddConnection(InletInstanceView il, InletInstanceView ol) {
        if (!patchView.isLocked()) {
            return new NetView(patchModel.AddConnection(il.getInletInstance(), ol.getInletInstance()));
        } else {
            Logger.getLogger(PatchController.class.getName()).log(Level.INFO, "Can't add connection: locked!");
            return null;
        }
    }

    public void setFileNamePath(String FileNamePath) {
        patchModel.setFileNamePath(FileNamePath);
    }

    public NetView disconnect(InletInstanceView io) {
        if (!patchView.isLocked()) {
            return new NetView(patchModel.disconnect(io.getInletInstance()));
        } else {
            Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "Can't disconnect: locked!");
            return null;
        }
    }

    public NetView disconnect(OutletInstanceView io) {
        if (!patchView.isLocked()) {
            return new NetView(patchModel.disconnect(io.getOutletInstance()));
        } else {
            Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "Can't disconnect: locked!");
            return null;
        }
    }

    public Net delete(NetView n) {
        if (!patchView.isLocked()) {
            return patchModel.delete(n.net);
        }
        else {
            Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "Can't delete: locked!");
            return null;
        }
    }

    public void delete(AxoObjectInstanceViewAbstract o) {
        patchModel.delete(o.getModel());
    }

    public AxoObjectInstanceAbstract AddObjectInstance(AxoObjectAbstract obj, Point loc) {
        if (!patchView.isLocked()) {
            return patchModel.AddObjectInstance(obj, loc);
        } else {
            Logger.getLogger(PatchController.class.getName()).log(Level.INFO, "can't add connection: locked!");
            return null;
        }
    }

    public void deleteSelectedAxoObjectInstanceViews() {
        patchView.deleteSelectedAxoObjectInstanceViews();
    }

    public ArrayList<AxoObjectInstanceAbstract> getObjectInstances() {
        return patchModel.getObjectInstances();
    }

    public String GetCurrentWorkingDirectory() {
        return patchModel.GetCurrentWorkingDirectory();
    }

    public ArrayList<Net> getNets() {
        return patchModel.getNets();
    }

    public void SetDirty() {
        patchModel.SetDirty();
    }
    
    public String getFileNamePath() {
        return patchModel.getFileNamePath();
    }

    public String getSDCardPath() {
        String FileNameNoPath = getFileNamePath();
        String separator = System.getProperty("file.separator");
        int lastSeparatorIndex = FileNameNoPath.lastIndexOf(separator);
        if (lastSeparatorIndex > 0) {
            FileNameNoPath = FileNameNoPath.substring(lastSeparatorIndex + 1);
        }
        String FileNameNoExt = FileNameNoPath;
        if (FileNameNoExt.endsWith(".axp") || FileNameNoExt.endsWith(".axs") || FileNameNoExt.endsWith(".axh")) {
            FileNameNoExt = FileNameNoExt.substring(0, FileNameNoExt.length() - 4);
        }
        return FileNameNoExt;
    }

    public void WriteCode() {
        patchModel.WriteCode();
    }

    public void setPresetUpdatePending(boolean updatePending) {
        patchModel.presetUpdatePending = updatePending;
    }

    public boolean isPresetUpdatePending() {
        return patchModel.presetUpdatePending;
    }

    Dimension GetSize() {
        return patchView.GetSize();
    }

    public PatchSettings getSettings() {
        return patchModel.settings;
    }
    
    public void ShowCompileFail() {
        patchView.ShowCompileFail();
    }
}
