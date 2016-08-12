package axoloti;

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

    public void undo() {
        if (patchModel.canUndo()) {
            patchModel.currentState -= 1;
            patchModel.loadState();
            SetDirty(false);
            patchView.PostContructor();
        }
    }

    public void redo() {
        if (patchModel.canRedo()) {
            patchModel.currentState += 1;
            patchModel.loadState();
            SetDirty(false);
            patchView.PostContructor();
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
        patchModel.ShowPreset(i);
    }

    public void Compile() {
        GetQCmdProcessor().AppendToQueue(new QCmdCompilePatch(patchModel));
    }

    void UploadDependentFiles() {
        String sdpath = patchModel.getSDCardPath();
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
        qcmdprocessor.AppendToQueue(new qcmds.QCmdCompilePatch(patchModel));
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
            if (patchModel.getFileNamePath() != null && !patchModel.getFileNamePath().isEmpty()) {
                File f = new File(patchModel.getFileNamePath());
                if (f.exists()) {
                    cal.setTimeInMillis(f.lastModified());
                }
            }
        }
        qcmdprocessor.AppendToQueue(new qcmds.QCmdUploadFile(patchModel.getBinFile(), sdfilename, cal));
    }

    public void UploadToSDCard() {
        UploadToSDCard("/" + patchModel.getSDCardPath() + "/patch.bin");
    }
}
