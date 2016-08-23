package axoloti;

import axoloti.datatypes.DataType;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstance;
import axoloti.iolet.IoletAbstract;
import axoloti.object.AxoObjectFromPatch;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.objectviews.AxoObjectInstanceViewComment;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import axoloti.parameterviews.IParameterInstanceView;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import qcmds.QCmdChangeWorkingDirectory;
import qcmds.QCmdCompilePatch;
import qcmds.QCmdCreateDirectory;
import qcmds.QCmdLock;
import qcmds.QCmdProcessor;
import qcmds.QCmdStart;
import qcmds.QCmdStop;
import qcmds.QCmdUploadPatch;

public abstract class PatchView implements ModelChangedListener {

    private final PatchController patchController;

    PatchView(PatchController patchController) {
        this.patchController = patchController;
    }

    ArrayList<IAxoObjectInstanceView> objectInstanceViews = new ArrayList<>();

    public ArrayList<IAxoObjectInstanceView> getObjectInstanceViews() {
        return objectInstanceViews;
    }

    public ArrayList<INetView> netViews = new ArrayList<>();
    boolean locked = false;

    public abstract PatchViewportView getViewportView();

    public void initViewportView() {
    }

    public abstract void repaint();

    public abstract Point getLocationOnScreen();

    public abstract void PostConstructor();

    public abstract void requestFocus();

    public abstract void AdjustSize();

    abstract void SetCordsInBackground(boolean cordsInBackground);

    public abstract void startRendering();

    public void clearViewportView() {
    }

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

    public abstract void add(IAxoObjectInstanceView v);

    public abstract void remove(IAxoObjectInstanceView v);

    public abstract void removeAllObjectViews();

    public abstract void removeAllNetViews();

    public abstract void add(INetView v);

    void SelectAll() {
        for (IAxoObjectInstanceView o : objectInstanceViews) {
            o.setSelected(true);
        }
    }

    public void SelectNone() {
        for (IAxoObjectInstanceView o : objectInstanceViews) {
            o.setSelected(false);
        }
    }

    PatchModel getSelectedObjects() {
        PatchModel p = new PatchModel();
        for (IAxoObjectInstanceView o : this.getObjectInstanceViews()) {
            if (o.isSelected()) {
                p.objectinstances.add(o.getObjectInstance());
            }
        }
        p.nets = new ArrayList<Net>();
        for (INetView n : netViews) {
            int sel = 0;
            for (IInletInstanceView i : n.getDestinationViews()) {
                if (i.getObjectInstanceView().isSelected()) {
                    sel++;
                }
            }
            for (IOutletInstanceView i : n.getSourceViews()) {
                if (i.getObjectInstanceView().isSelected()) {
                    sel++;
                }
            }
            if (sel > 0) {
                p.nets.add(n.getNet());
            }
        }
        p.PreSerialize();
        return p;
    }

    enum Direction {
        UP, LEFT, DOWN, RIGHT
    }

    void MoveSelectedAxoObjInstances(Direction dir, int xsteps, int ysteps) {
        if (!isLocked()) {
            int xgrid = 1;
            int ygrid = 1;
            int xstep = 0;
            int ystep = 0;
            switch (dir) {
                case DOWN:
                    ystep = ysteps;
                    ygrid = ysteps;
                    break;
                case UP:
                    ystep = -ysteps;
                    ygrid = ysteps;
                    break;
                case LEFT:
                    xstep = -xsteps;
                    xgrid = xsteps;
                    break;
                case RIGHT:
                    xstep = xsteps;
                    xgrid = xsteps;
                    break;
            }
            boolean isUpdate = false;
            for (IAxoObjectInstanceView o : objectInstanceViews) {
                if (o.isSelected()) {
                    isUpdate = true;
                    Point p = o.getLocation();
                    p.x = p.x + xstep;
                    p.y = p.y + ystep;
                    p.x = xgrid * (p.x / xgrid);
                    p.y = ygrid * (p.y / ygrid);
                    o.SetLocation(p.x, p.y);
                    o.repaint();
                }
            }
            if (isUpdate) {
                AdjustSize();
                patchController.SetDirty();
            }
        } else {
            Logger.getLogger(PatchViewSwing.class.getName()).log(Level.INFO, "can't move: locked");
        }
    }

    void SetDSPLoad(int pct) {
        getPatchController().getPatchFrame().ShowDSPLoad(pct);
    }

    Dimension GetInitialSize() {
        int mx = 100; // min size
        int my = 100;
        for (IAxoObjectInstanceView i : objectInstanceViews) {

            Dimension s = i.getPreferredSize();

            int ox = i.getLocation().x + (int) s.getWidth();
            int oy = i.getLocation().y + (int) s.getHeight();

            if (ox > mx) {
                mx = ox;
            }
            if (oy > my) {
                my = oy;
            }
        }
        // adding more, as getPreferredSize is not returning true dimension of
        // object
        return new Dimension(mx + 300, my + 300);
    }

    void PreSerialize() {
        if (NotesFrame != null) {
            getPatchController().patchModel.notes = NotesFrame.GetText();
        }
        getPatchController().patchModel.windowPos = getPatchController().getPatchFrame().getBounds();
    }

    boolean save(File f) {
        boolean b = getPatchController().patchModel.save(f);
        if (ObjEditor != null) {
            ObjEditor.UpdateObject();
        }
        return b;
    }

    public static void OpenPatch(String name, InputStream stream) {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        try {
            PatchModel patchModel = serializer.read(PatchModel.class, stream);
            PatchController patchController = new PatchController();
            PatchView patchView = MainFrame.prefs.getPatchView(patchController);
            patchModel.addModelChangedListener(patchView);
            patchController.setPatchView(patchView);
            patchController.setPatchModel(patchModel);
            PatchFrame pf = new PatchFrame(patchController, QCmdProcessor.getQCmdProcessor());
            patchView.setFileNamePath(name);
            patchView.PostConstructor();
            pf.setVisible(true);
            pf.startRendering();

        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static PatchFrame OpenPatchInvisible(File f) {
        for (DocumentWindow dw : DocumentWindowList.GetList()) {
            if (f.equals(dw.getFile())) {
                JFrame frame1 = dw.GetFrame();
                if (frame1 instanceof PatchFrame) {
                    return (PatchFrame) frame1;
                } else {
                    return null;
                }
            }
        }

        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        try {
            PatchModel patchModel = serializer.read(PatchModel.class, f);
            PatchController patchController = new PatchController();
            PatchView patchView = MainFrame.prefs.getPatchView(patchController);
            patchModel.addModelChangedListener(patchView);
            patchController.setPatchView(patchView);
            patchController.setPatchModel(patchModel);
            PatchFrame pf = new PatchFrame(patchController, QCmdProcessor.getQCmdProcessor());
            patchView.setFileNamePath(f.getAbsolutePath());
            patchView.PostConstructor();
            patchView.setFileNamePath(f.getPath());
            return pf;
        } catch (java.lang.reflect.InvocationTargetException ite) {
            if (ite.getTargetException() instanceof PatchModel.PatchVersionException) {
                PatchModel.PatchVersionException pve = (PatchModel.PatchVersionException) ite.getTargetException();
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, "Patch produced with newer version of Axoloti {0} {1}",
                        new Object[]{f.getAbsoluteFile(), pve.getMessage()});
            } else {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ite);
            }
            return null;
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static PatchFrame OpenPatch(File f) {
        PatchFrame pf = OpenPatchInvisible(f);
        pf.setVisible(true);
        pf.startRendering();
        pf.setState(java.awt.Frame.NORMAL);
        pf.toFront();
        return pf;
    }

    public void updateNetVisibility() {
        for (INetView n : netViews) {
            DataType d = n.getNet().getDataType();
            if (d != null) {
                n.setVisible(isCableTypeEnabled(d));
            }
        }
        repaint();
    }

    public PatchController getPatchController() {
        return patchController;
    }

    public void Close() {
        Unlock();
        Collection<IAxoObjectInstanceView> c = (Collection<IAxoObjectInstanceView>) objectInstanceViews.clone();
        for (IAxoObjectInstanceView o : c) {
            o.Close();
        }
        if (NotesFrame != null) {
            NotesFrame.dispose();
        }
        if ((getPatchController().getSettings() != null)
                && (getPatchController().getSettings().editor != null)) {
            getPatchController().getSettings().editor.dispose();
        }
    }

    public Dimension GetSize() {
        int nx = 0;
        int ny = 0;
        // negative coordinates?
        for (IAxoObjectInstanceView o : objectInstanceViews) {
            Point p = o.getLocation();
            if (p.x < nx) {
                nx = p.x;
            }
            if (p.y < ny) {
                ny = p.y;
            }
        }
        if ((nx < 0) || (ny < 0)) { // move all to positive coordinates
            for (IAxoObjectInstanceView o : objectInstanceViews) {
                Point p = o.getLocation();
                o.SetLocation(p.x - nx, p.y - ny);
            }
        }

        int mx = 0;
        int my = 0;
        for (IAxoObjectInstanceView o : objectInstanceViews) {
            Point p = o.getLocation();
            Dimension s = o.getSize();
            int px = p.x + s.width;
            int py = p.y + s.height;
            if (px > mx) {
                mx = px;
            }
            if (py > my) {
                my = py;
            }
        }
        return new Dimension(mx, my);
    }

    void deleteSelectedAxoObjectInstanceViews() {
        Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "deleteSelectedAxoObjInstances()");
        if (!isLocked()) {
            for (IAxoObjectInstanceView o : objectInstanceViews) {
                if (o.isSelected()) {
                    getPatchController().delete(o);
                }
            }
            getPatchController().SetDirty();
        } else {
            Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "Can't delete: locked!");
        }
    }

    public INetView GetNetView(IInletInstanceView i) {
        for (INetView netView : netViews) {
            for (IInletInstanceView d : netView.getDestinationViews()) {
                if (d == i) {
                    return netView;
                }
            }
        }
        return null;
    }

    public INetView GetNetView(IOutletInstanceView o) {
        for (INetView netView : netViews) {
            for (IOutletInstanceView d : netView.getSourceViews()) {
                if (d == o) {
                    return netView;
                }
            }
        }
        return null;
    }

    public INetView GetNetView(IoletAbstract io) {
        for (INetView netView : netViews) {
            for (IInletInstanceView d : netView.getDestinationViews()) {
                if (d == io) {
                    return netView;
                }
            }
            for (IOutletInstanceView d : netView.getSourceViews()) {
                if (d == io) {
                    return netView;
                }
            }
        }
        return null;
    }

    public List<INetView> getNetViews() {
        return netViews;
    }

    public void Lock() {
        getPatchController().getPatchFrame().SetLive(true);
        locked = true;
        for (IAxoObjectInstanceView o : objectInstanceViews) {
            o.Lock();
        }
    }

    public void Unlock() {
        getPatchController().getPatchFrame().SetLive(false);
        locked = false;
        List<IAxoObjectInstanceView> objectInstanceViewsClone = (ArrayList<IAxoObjectInstanceView>) objectInstanceViews.clone();
        for (IAxoObjectInstanceView o : objectInstanceViewsClone) {
            o.Unlock();
        }
    }

    public void ShowPreset(int i) {
        ArrayList<IAxoObjectInstanceView> objectInstanceViewsClone = (ArrayList<IAxoObjectInstanceView>) objectInstanceViews.clone();
        for (IAxoObjectInstanceView o : objectInstanceViewsClone) {
            for (IParameterInstanceView p : o.getParameterInstanceViews()) {
                p.ShowPreset(i);
            }
        }
    }

    public abstract void validate();

    public abstract void validateObjects();

    public abstract void validateNets();

    public void modelChanged() {
        modelChanged(true);
    }

    public void modelChanged(boolean updateSelection) {
        Map<String, IAxoObjectInstanceView> existingViews = new HashMap<>();
        Set<String> newObjectNames = new HashSet<>();

        if (getPatchController().isLoadingUndoState()) {
            // prevent detached sub-windows
            Close();
            removeAllObjectViews();
        } else {
            for (IAxoObjectInstanceView view : objectInstanceViews) {
                String instanceName = view.getModel().getInstanceName();
                existingViews.put(instanceName, view);
            }

            for (AxoObjectInstanceAbstract o : getPatchController().patchModel.getObjectInstances()) {
                String instanceName = o.getInstanceName();
                newObjectNames.add(instanceName);
            }

            for (String existingObjectName : existingViews.keySet()) {
                if (!newObjectNames.contains(existingObjectName)) {
                    remove(existingViews.get(existingObjectName));
                }
            }
        }

        removeAllNetViews();

        Map<InletInstance, IInletInstanceView> i_map = new HashMap<>();
        Map<OutletInstance, IOutletInstanceView> o_map = new HashMap<>();

        int newObjects = 0;
        IAxoObjectInstanceView editorView = null;

        for (AxoObjectInstanceAbstract o : getPatchController().patchModel.getObjectInstances()) {
            IAxoObjectInstanceView view = existingViews.get(o.getInstanceName());
            boolean isNewObject = false;
            if (view == null) {
                view = o.createView(this);
                isNewObject = true;
            }

            for (IInletInstanceView ii : view.getInletInstanceViews()) {
                i_map.put(ii.getInletInstance(), ii);
            }
            for (IOutletInstanceView oi : view.getOutletInstanceViews()) {
                o_map.put(oi.getOutletInstance(), oi);
            }
            if (isNewObject) {
                newObjects += 1;
                add(view);

                if (updateSelection && !getPatchController().isLoadingUndoState()) {
                    view.setSelected(isNewObject);
                    if (isNewObject && view instanceof AxoObjectInstanceViewComment) {
                        editorView = view;
                    }
                }
            }
        }

        getPatchController().clearLoadingUndoState();

        if (newObjects == 1 && editorView != null) {
            // if single new comment added, show instancename editor
            editorView.addInstanceNameEditor();
        }

        for (Net n : (List<Net>) getPatchController().patchModel.getNets().clone()) {
            INetView view = n.createView(this);
            if (view != null) {
                for (InletInstance i : n.dest) {
                    view.connectInlet(i_map.get(i));
                }
                for (OutletInstance o : n.source) {
                    view.connectOutlet(o_map.get(o));
                }
                add(view);
            }
        }

        validateObjects();
        validateNets();

        AdjustSize();
        validate();

        for (INetView n : netViews) {
            n.updateBounds();
        }
        repaint();
    }
}
