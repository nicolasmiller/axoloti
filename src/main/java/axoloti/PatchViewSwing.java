/**
 * Copyright (C) 2013, 2014, 2015 Johannes Taelman
 *
 * This file is part of Axoloti.
 *
 * Axoloti is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Axoloti is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Axoloti. If not, see <http://www.gnu.org/licenses/>.
 */
package axoloti;

import axoloti.datatypes.DataType;
import axoloti.inlets.InletInstance;
import axoloti.inlets.InletInstanceView;
import axoloti.iolet.IoletAbstract;
import axoloti.object.AxoObjectAbstract;
import axoloti.object.AxoObjectFromPatch;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.object.AxoObjects;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.objectviews.AxoObjectInstanceViewComment;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstanceView;
import axoloti.parameterviews.ParameterInstanceView;
import axoloti.utils.Constants;
import axoloti.utils.KeyUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import qcmds.QCmdProcessor;

/**
 *
 * @author Johannes Taelman
 */
@Root(name = "patch-1.0")
public class PatchViewSwing extends PatchView {

    ArrayList<AxoObjectInstanceViewAbstract> objectInstanceViews = new ArrayList<AxoObjectInstanceViewAbstract>();

    public ArrayList<AxoObjectInstanceViewAbstract> getObjectInstanceViews() {
        return objectInstanceViews;
    }

    public ArrayList<NetView> netViews = new ArrayList<NetView>();

    // shortcut patch names
    final static String patchComment = "patch/comment";
    final static String patchInlet = "patch/inlet";
    final static String patchOutlet = "patch/outlet";
    final static String patchAudio = "audio/";
    final static String patchAudioOut = "audio/out stereo";
    final static String patchMidi = "midi";
    final static String patchMidiKey = "midi/in/keyb";
    final static String patchDisplay = "disp/";

    public PatchLayeredPane Layers = new PatchLayeredPane();

    public JPanel objectLayerPanel = new JPanel();
    public JPanel draggedObjectLayerPanel = new JPanel();
    public JPanel netLayerPanel = new JPanel();
    public JPanel selectionRectLayerPanel = new JPanel();

    JLayer<JComponent> objectLayer = new JLayer<JComponent>(objectLayerPanel);
    JLayer<JComponent> draggedObjectLayer = new JLayer<JComponent>(draggedObjectLayerPanel);
    JLayer<JComponent> netLayer = new JLayer<JComponent>(netLayerPanel);
    JLayer<JComponent> selectionRectLayer = new JLayer<JComponent>(selectionRectLayerPanel);

    SelectionRectangle selectionrectangle = new SelectionRectangle();
    Point selectionRectStart;
    Point panOrigin;

    private PatchController patchController;

    public PatchViewSwing(PatchController patchController) {
        super();

        this.patchController = patchController;

        Layers.setLayout(null);
        Layers.setSize(Constants.PATCH_SIZE, Constants.PATCH_SIZE);
        Layers.setLocation(0, 0);

        JComponent[] layerComponents = {
            objectLayer, objectLayerPanel, draggedObjectLayerPanel, netLayerPanel,
            selectionRectLayerPanel, draggedObjectLayer, netLayer, selectionRectLayer};
        for (JComponent c : layerComponents) {
            c.setLayout(null);
            c.setSize(Constants.PATCH_SIZE, Constants.PATCH_SIZE);
            c.setLocation(0, 0);
            c.setOpaque(false);
            c.validate();
        }

        Layers.add(objectLayer, new Integer(1));
        Layers.add(netLayer, new Integer(2));
        Layers.add(draggedObjectLayer, new Integer(3));
        Layers.add(selectionRectLayer, new Integer(4));

        objectLayer.setName("objectLayer");
        draggedObjectLayer.setName("draggedObjectLayer");
        netLayer.setName("netLayer");
        netLayerPanel.setName("netLayerPanel");
        selectionRectLayerPanel.setName("selectionRectLayerPanel");
        selectionRectLayer.setName("selectionRectLayer");

        objectLayerPanel.setName(Constants.OBJECT_LAYER_PANEL);
        draggedObjectLayerPanel.setName(Constants.DRAGGED_OBJECT_LAYER_PANEL);

        selectionRectLayerPanel.add(selectionrectangle);
        selectionrectangle.setLocation(100, 100);
        selectionrectangle.setSize(100, 100);
        selectionrectangle.setOpaque(false);
        selectionrectangle.setVisible(false);

        Layers.setSize(Constants.PATCH_SIZE, Constants.PATCH_SIZE);
        Layers.setVisible(true);
        Layers.setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        Layers.setOpaque(true);
        Layers.revalidate();

        TransferHandler TH = new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return COPY_OR_MOVE;
            }

            @Override
            public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
                PatchModel p = getSelectedObjects();
                if (p.getObjectInstances().isEmpty()) {
                    clip.setContents(new StringSelection(""), null);
                    return;
                }
                p.PreSerialize();
                Serializer serializer = new Persister();
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    serializer.write(p, baos);
                    StringSelection s = new StringSelection(baos.toString());
                    clip.setContents(s, (ClipboardOwner) null);
                } catch (Exception ex) {
                    Logger.getLogger(AxoObjects.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (action == MOVE) {
                    deleteSelectedAxoObjectInstanceViews();
                }
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                return super.importData(support);
            }

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    if (!isLocked()) {
                        if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {

                            paste((String) t.getTransferData(DataFlavor.stringFlavor), comp.getMousePosition(), false);
                        }
                    }
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(PatchViewSwing.class.getName()).log(Level.SEVERE, "paste", ex);
                } catch (IOException ex) {
                    Logger.getLogger(PatchViewSwing.class.getName()).log(Level.SEVERE, "paste", ex);
                }
                return true;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringSelection("copy");
            }

            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                boolean r = super.canImport(support);
                return r;
            }

        };

        Layers.setTransferHandler(TH);

        InputMap inputMap = Layers.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                KeyUtils.CONTROL_OR_CMD_MASK), "cut");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                KeyUtils.CONTROL_OR_CMD_MASK), "copy");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                KeyUtils.CONTROL_OR_CMD_MASK), "paste");

        ActionMap map = Layers.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());

        Layers.setEnabled(true);
        Layers.setFocusable(true);
        Layers.setFocusCycleRoot(true);
        Layers.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int xsteps = 1;
                int ysteps = 1;
                if (!ke.isShiftDown()) {
                    xsteps = Constants.X_GRID;
                    ysteps = Constants.Y_GRID;
                }
                if ((ke.getKeyCode() == KeyEvent.VK_SPACE)
                        || ((ke.getKeyCode() == KeyEvent.VK_N) && !KeyUtils.isControlOrCommandDown(ke))
                        || ((ke.getKeyCode() == KeyEvent.VK_1) && KeyUtils.isControlOrCommandDown(ke))) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        ShowClassSelector(p, null, null);
                    }
                } else if (((ke.getKeyCode() == KeyEvent.VK_C) && !KeyUtils.isControlOrCommandDown(ke))
                        || ((ke.getKeyCode() == KeyEvent.VK_5) && KeyUtils.isControlOrCommandDown(ke))) {
                    getPatchController().AddObjectInstance(MainFrame.axoObjects.GetAxoObjectFromName(patchComment, null).get(0), Layers.getMousePosition());
                    ke.consume();
                } else if ((ke.getKeyCode() == KeyEvent.VK_I) && !KeyUtils.isControlOrCommandDown(ke)) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        ShowClassSelector(p, null, patchInlet);
                    }
                } else if ((ke.getKeyCode() == KeyEvent.VK_O) && !KeyUtils.isControlOrCommandDown(ke)) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        ShowClassSelector(p, null, patchOutlet);
                    }
                } else if ((ke.getKeyCode() == KeyEvent.VK_D) && !KeyUtils.isControlOrCommandDown(ke)) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        ShowClassSelector(p, null, patchDisplay);
                    }
                } else if ((ke.getKeyCode() == KeyEvent.VK_M) && !KeyUtils.isControlOrCommandDown(ke)) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        if (ke.isShiftDown()) {
                            ShowClassSelector(p, null, patchMidiKey);
                        } else {
                            ShowClassSelector(p, null, patchMidi);
                        }
                    }
                } else if ((ke.getKeyCode() == KeyEvent.VK_A) && !KeyUtils.isControlOrCommandDown(ke)) {
                    Point p = Layers.getMousePosition();
                    ke.consume();
                    if (p != null) {
                        if (ke.isShiftDown()) {
                            ShowClassSelector(p, null, patchAudioOut);
                        } else {
                            ShowClassSelector(p, null, patchAudio);
                        }
                    }
                } else if ((ke.getKeyCode() == KeyEvent.VK_DELETE) || (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
                    deleteSelectedAxoObjectInstanceViews();

                    ke.consume();
                } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    MoveSelectedAxoObjInstances(Direction.UP, xsteps, ysteps);
                    ke.consume();
                } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    MoveSelectedAxoObjInstances(Direction.DOWN, xsteps, ysteps);
                    ke.consume();
                } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    MoveSelectedAxoObjInstances(Direction.RIGHT, xsteps, ysteps);
                    ke.consume();
                } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    MoveSelectedAxoObjInstances(Direction.LEFT, xsteps, ysteps);
                    ke.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });

        Layers.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                        o.setSelected(false);
                    }
                    if (me.getClickCount() == 2) {
                        ShowClassSelector(me.getPoint(), null, null);
                    } else {
                        if ((osf != null) && osf.isVisible()) {
                            osf.Accept();
                        }
                        Layers.requestFocusInWindow();
                    }
                    me.consume();
                } else {
                    if ((osf != null) && osf.isVisible()) {
                        osf.Cancel();
                    }
                    Layers.requestFocusInWindow();
                    me.consume();
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    selectionRectStart = me.getPoint();
                    selectionrectangle.setBounds(me.getX(), me.getY(), 1, 1);
                    selectionrectangle.setVisible(true);

                    Layers.requestFocusInWindow();
                    me.consume();
                } else {
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (selectionrectangle.isVisible() || me.getButton() == MouseEvent.BUTTON1) {
                    Rectangle r = selectionrectangle.getBounds();
                    for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                        o.setSelected(o.getBounds().intersects(r));
                    }
                    selectionrectangle.setVisible(false);
                    me.consume();
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        Layers.setVisible(true);

        DropTarget dt;
        dt = new DropTarget() {

            @Override
            public synchronized void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<File> flist = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File f : flist) {
                            if (f.exists() && f.canRead()) {
                                AxoObjectAbstract o = new AxoObjectFromPatch(f);
                                String fn = f.getCanonicalPath();
                                if (getPatchController().GetCurrentWorkingDirectory() != null
                                        && fn.startsWith(getPatchController().GetCurrentWorkingDirectory())) {
                                    o.createdFromRelativePath = true;
                                }

                                getPatchController().AddObjectInstance(o, dtde.getLocation());
                            }
                        }
                        dtde.dropComplete(true);
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(PatchViewSwing.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(PatchViewSwing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
                super.drop(dtde);
            }
        ;
        };

        Layers.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent ev) {
                if (selectionrectangle.isVisible()) {
                    int x1 = selectionRectStart.x;
                    int y1 = selectionRectStart.y;
                    int x2 = ev.getX();
                    int y2 = ev.getY();
                    int xmin = x1 < x2 ? x1 : x2;
                    int xmax = x1 > x2 ? x1 : x2;
                    int ymin = y1 < y2 ? y1 : y2;
                    int ymax = y1 > y2 ? y1 : y2;
                    int width = xmax - xmin;
                    int height = ymax - ymin;
                    selectionrectangle.setBounds(xmin, ymin, width, height);
                    selectionrectangle.setVisible(true);
                    ev.consume();
                }
            }
        });

        Layers.setDropTarget(dt);
        Layers.setVisible(true);
    }



    void SelectAll() {
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.setSelected(true);
        }
    }

    @Override
    public void SelectNone() {
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.setSelected(false);
        }
    }

    enum Direction {

        UP, LEFT, DOWN, RIGHT
    }

    PatchModel getSelectedObjects() {
        PatchModel p = new PatchModel();
        for (AxoObjectInstanceViewAbstract o : this.getObjectInstanceViews()) {
            if (o.IsSelected()) {
                p.objectinstances.add(o.getObjectInstance());
            }
        }
        p.nets = new ArrayList<Net>();
        for (NetView n : netViews) {
            int sel = 0;
            for (InletInstanceView i : n.dest) {
                if (i.getObjectInstanceView().IsSelected()) {
                    sel++;
                }
            }
            for (OutletInstanceView i : n.source) {
                if (i.getObjectInstanceView().IsSelected()) {
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
            for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
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

    public void PostConstructor() {
        getPatchController().patchModel.PostContructor();
        Layers.setPreferredSize(new Dimension(5000, 5000));
        modelChanged(false);
        ShowPreset(0);
    }

    void SetCordsInBackground(boolean b) {
        if (b) {
            Layers.removeAll();
            Layers.add(netLayer, new Integer(1));
            Layers.add(objectLayer, new Integer(2));
            Layers.add(draggedObjectLayer, new Integer(3));
            Layers.add(selectionRectLayer, new Integer(4));
        } else {
            Layers.removeAll();
            Layers.add(objectLayer, new Integer(1));
            Layers.add(netLayer, new Integer(2));
            Layers.add(draggedObjectLayer, new Integer(3));
            Layers.add(selectionRectLayer, new Integer(4));
        }
    }

    public void repaint() {
        if (Layers != null) {
            Layers.repaint();
        }
    }

    void SetDSPLoad(int pct) {
        getPatchController().getPatchFrame().ShowDSPLoad(pct);
    }

    Dimension GetInitialSize() {
        int mx = 100; // min size
        int my = 100;
        for (AxoObjectInstanceViewAbstract i : objectInstanceViews) {

            Dimension s = i.getPreferredSize();

            int ox = i.getX() + (int) s.getWidth();
            int oy = i.getY() + (int) s.getHeight();

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

    public void clampLayerSize(Dimension s) {
        if (Layers.getParent() != null) {
            if (s.width < Layers.getParent().getWidth()) {
                s.width = Layers.getParent().getWidth();
            }
            if (s.height < Layers.getParent().getHeight()) {
                s.height = Layers.getParent().getHeight();
            }
        }
    }

    public void AdjustSize() {
        Dimension s = getPatchController().GetSize();
        clampLayerSize(s);
        Dimension s2 = Layers.getSize();
        if (s2.equals(s)) {
            return;
        }
        Layers.setSize(s);
        Layers.setPreferredSize(s);
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
        for (NetView n : netViews) {
            DataType d = n.net.getDataType();
            if (d != null) {
                n.setVisible(isCableTypeEnabled(d));
            }
        }
        Layers.repaint();
    }

    public void Close() {
        Unlock();
        Collection<AxoObjectInstanceViewAbstract> c = (Collection<AxoObjectInstanceViewAbstract>) objectInstanceViews.clone();
        for (AxoObjectInstanceViewAbstract o : c) {
            o.Close();
        }
        if (NotesFrame != null) {
            NotesFrame.dispose();
        }
        if ((patchController.getSettings() != null)
                && (patchController.getSettings().editor != null)) {
            patchController.getSettings().editor.dispose();
        }
    }

    public Dimension GetSize() {
        int nx = 0;
        int ny = 0;
        // negative coordinates?
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            Point p = o.getLocation();
            if (p.x < nx) {
                nx = p.x;
            }
            if (p.y < ny) {
                ny = p.y;
            }
        }
        if ((nx < 0) || (ny < 0)) { // move all to positive coordinates
            for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                Point p = o.getLocation();
                o.SetLocation(p.x - nx, p.y - ny);
            }
        }

        int mx = 0;
        int my = 0;
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
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
            for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                if (o.isSelected()) {
                    getPatchController().delete(o);
                }
            }
            getPatchController().SetDirty();
        } else {
            Logger.getLogger(PatchModel.class.getName()).log(Level.INFO, "Can't delete: locked!");
        }
    }

    public NetView GetNetView(IoletAbstract io) {
        for (NetView netView : netViews) {
            for (IoletAbstract d : netView.dest) {
                if (d == io) {
                    return netView;
                }
            }
            for (IoletAbstract d : netView.source) {
                if (d == io) {
                    return netView;
                }
            }
        }
        return null;
    }

    public List<NetView> getNetViews() {
        return this.netViews;
    }

    @Override
    public void Lock() {
        getPatchController().getPatchFrame().SetLive(true);
        Layers.setBackground(Theme.getCurrentTheme().Patch_Locked_Background);
        locked = true;
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.Lock();
        }
    }

    @Override
    public void Unlock() {
        getPatchController().getPatchFrame().SetLive(false);
        Layers.setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        locked = false;
        ArrayList<AxoObjectInstanceViewAbstract> objectInstanceViewsClone = (ArrayList<AxoObjectInstanceViewAbstract>) objectInstanceViews.clone();
        for (AxoObjectInstanceViewAbstract o : objectInstanceViewsClone) {
            o.Unlock();
        }
    }

    public PatchController getPatchController() {
        return patchController;
    }

    public void ShowPreset(int i) {
        ArrayList<AxoObjectInstanceViewAbstract> objectInstanceViewsClone = (ArrayList<AxoObjectInstanceViewAbstract>) objectInstanceViews.clone();
        for (AxoObjectInstanceViewAbstract o : objectInstanceViewsClone) {
            for (ParameterInstanceView p : o.getParameterInstanceViews()) {
                p.ShowPreset(i);
            }
        }
    }

    @Override
    public void modelChanged() {
        modelChanged(true);
    }

    public void modelChanged(boolean updateSelection) {
        Map<String, AxoObjectInstanceViewAbstract> existingViews = new HashMap<String, AxoObjectInstanceViewAbstract>();
        Set<String> newObjectNames = new HashSet<String>();
        
        if(getPatchController().isLoadingUndoState()) {
            // prevent detached sub-windows
            Close();
            objectLayerPanel.removeAll();
            objectInstanceViews.clear();
        } else {
            for (AxoObjectInstanceViewAbstract view : objectInstanceViews) {
                String instanceName = view.getObjectInstance().getInstanceName();
                existingViews.put(instanceName, view);
            }

            for (AxoObjectInstanceAbstract o : getPatchController().patchModel.getObjectInstances()) {
                String instanceName = o.getInstanceName();
                newObjectNames.add(instanceName);
            }

            for (String existingObjectName : existingViews.keySet()) {
                if (!newObjectNames.contains(existingObjectName)) {
                    AxoObjectInstanceViewAbstract viewToRemove = existingViews.get(existingObjectName);
                    objectLayerPanel.remove(viewToRemove);
                    objectInstanceViews.remove(viewToRemove);
                }
            }
        }

        netViews.clear();
        netLayerPanel.removeAll();

        Map<InletInstance, InletInstanceView> i_map = new HashMap<InletInstance, InletInstanceView>();
        Map<OutletInstance, OutletInstanceView> o_map = new HashMap<OutletInstance, OutletInstanceView>();

        int newObjects = 0;
        AxoObjectInstanceViewAbstract editorView = null;

        for (AxoObjectInstanceAbstract o : getPatchController().patchModel.getObjectInstances()) {
            AxoObjectInstanceViewAbstract view = existingViews.get(o.getInstanceName());
            boolean isNewObject = false;
            if (view == null) {
                view = o.CreateView(this);
                isNewObject = true;
            }

            for (InletInstanceView ii : view.getInletInstanceViews()) {
                i_map.put(ii.getInletInstance(), ii);
            }
            for (OutletInstanceView oi : view.getOutletInstanceViews()) {
                o_map.put(oi.getOutletInstance(), oi);
            }
            if (isNewObject) {
                newObjects += 1;
                objectInstanceViews.add(view);
                objectLayerPanel.add(view);

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
            NetView view = n.CreateView(this);
            for (InletInstance i : n.dest) {
                view.connectInlet(i_map.get(i));
            }
            for (OutletInstance o : n.source) {
                view.connectOutlet(o_map.get(o));
            }
            this.netViews.add(view);
            netLayerPanel.add(view);
        }
        objectLayerPanel.validate();
        netLayerPanel.validate();

        AdjustSize();
        Layers.validate();

        for (NetView n : netViews) {
            n.updateBounds();
        }

        Layers.repaint();
    }
    
    public PatchViewportView getViewportView() {
        return Layers;
    }
    
    public Point getLocationOnScreen() {
        return objectLayerPanel.getLocationOnScreen();
    }
    
    @Override
    public void requestFocus() {
        Layers.requestFocus();
    }
    
    @Override
    public void startRendering() {
        
    }
}