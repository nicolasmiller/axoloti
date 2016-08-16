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
import axoloti.object.AxoObjectInstance;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.objectviews.AxoObjectInstanceView;
import axoloti.object.AxoObjects;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstanceView;
import axoloti.utils.Constants;
import axoloti.utils.KeyUtils;
import java.awt.Component;
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import org.simpleframework.xml.Root;
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

/**
 *
 * @author Johannes Taelman
 */
@Root(name = "patch-1.0")
public class PatchView {

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

    public JLayeredPane Layers = new JLayeredPane();

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
    public AxoObjectFromPatch ObjEditor;

    private PatchController patchController;

    boolean locked = false;


    public PatchView(PatchController patchController) {
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
//        Layers.doLayout();

        TransferHandler TH = new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return COPY_OR_MOVE;
            }

            @Override
            public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
                PatchView p = getSelectedObjects();
                if (p.objectInstanceViews.isEmpty()) {
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
                    cleanUpObjectLayer();
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
                    Logger.getLogger(PatchView.class.getName()).log(Level.SEVERE, "paste", ex);
                } catch (IOException ex) {
                    Logger.getLogger(PatchView.class.getName()).log(Level.SEVERE, "paste", ex);
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
                    AxoObjectInstanceViewAbstract ao = AddObjectInstance(MainFrame.axoObjects.GetAxoObjectFromName(patchComment, null).get(0), Layers.getMousePosition());
                    ao.addInstanceNameEditor();
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
                    cleanUpObjectLayer();
                    Layers.revalidate();

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
                        o.SetSelected(false);
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
                    selectionrectangle.setVisible(false);
                    Layers.requestFocusInWindow();
                    me.consume();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (selectionrectangle.isVisible() || me.getButton() == MouseEvent.BUTTON1) {
                    Rectangle r = selectionrectangle.getBounds();
                    for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                        o.SetSelected(o.getBounds().intersects(r));
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
                                if (PatchView.this.patchController.GetCurrentWorkingDirectory() != null
                                        && fn.startsWith(PatchView.this.patchController.GetCurrentWorkingDirectory())) {
                                    o.createdFromRelativePath = true;
                                }
                                AddObjectInstance(o, dtde.getLocation());
                            }
                        }
                        dtde.dropComplete(true);
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(PatchView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(PatchView.class.getName()).log(Level.SEVERE, null, ex);
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
                if (selectionrectangle.isVisible() || ev.getButton() == MouseEvent.BUTTON1) {
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

    void paste(String v, Point pos, boolean restoreConnectionsToExternalOutlets) {
        SelectNone();
        if (v.isEmpty()) {
            return;
        }
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        try {
            // is this supposed to be a view?
            PatchModel p = serializer.read(PatchModel.class, v);
            HashMap<String, String> dict = new HashMap<String, String>();
            for (AxoObjectInstanceAbstract o : p.getObjectInstances()) {
                o.patchModel = this.patchController.patchModel;
                AxoObjectAbstract obj = o.resolveType();
                Modulator[] m = obj.getModulators();
                if (m != null) {
                    for (Modulator mm : m) {
                        mm.objinst = o;
                        PatchView.this.patchController.patchModel.addModulator(mm);
                    }
                }

            }
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            for (AxoObjectInstanceAbstract o : p.getObjectInstances()) {
                String original_name = o.getInstanceName();
                if (original_name != null) {
                    String new_name = original_name;
                    String ss[] = new_name.split("_");
                    boolean hasNumeralSuffix = false;
                    try {
                        if ((ss.length > 1) && (Integer.toString(Integer.parseInt(ss[ss.length - 1]))).equals(ss[ss.length - 1])) {
                            hasNumeralSuffix = true;
                        }
                    } catch (NumberFormatException e) {
                    }
                    if (hasNumeralSuffix) {
                        int n = Integer.parseInt(ss[ss.length - 1]) + 1;
                        String bs = original_name.substring(0, original_name.length() - ss[ss.length - 1].length());
                        while (PatchView.this.patchController.patchModel.GetObjectInstance(new_name) != null) {
                            new_name = bs + n++;
                        }
                        while (dict.containsKey(new_name)) {
                            new_name = bs + n++;
                        }
                    } else {
                        while (PatchView.this.patchController.patchModel.GetObjectInstance(new_name) != null) {
                            new_name = new_name + "_";
                        }
                        while (dict.containsKey(new_name)) {
                            new_name = new_name + "_";
                        }
                    }
                    if (!new_name.equals(original_name)) {
                        o.setInstanceName(new_name);
                    }
                    dict.put(original_name, new_name);
                }
                if (o.getX() < minX) {
                    minX = o.getX();
                }
                if (o.getY() < minY) {
                    minY = o.getY();
                }
                PatchView.this.patchController.patchModel.addObjectInstance(o);
                AxoObjectInstanceView objectView = new AxoObjectInstanceView(o);
                objectLayerPanel.add(objectView, 0);
                objectView.PostConstructor();

                int newposx = o.getX();
                int newposy = o.getY();

                if (pos != null) {
                    // paste at cursor position, with delta snapped to grid
                    newposx += Constants.X_GRID * ((pos.x - minX + Constants.X_GRID / 2) / Constants.X_GRID);
                    newposy += Constants.Y_GRID * ((pos.y - minY + Constants.Y_GRID / 2) / Constants.Y_GRID);
                }
                while (getObjectAtLocation(newposx, newposy) != null) {
                    newposx += Constants.X_GRID;
                    newposy += Constants.Y_GRID;
                }
                objectView.setLocation(newposx, newposy);
                objectView.SetSelected(true);
            }
            for (Net n : p.nets) {
                InletInstance connectedInlet = null;
                OutletInstance connectedOutlet = null;
                if (n.source != null) {
                    ArrayList<OutletInstance> source2 = new ArrayList<OutletInstance>();
                    for (OutletInstance o : n.source) {
                        String objname = o.getObjname();
                        String outletname = o.getOutletname();
                        if ((objname != null) && (outletname != null)) {
                            String on2 = dict.get(objname);
                            if (on2 != null) {
//                                o.name = on2 + " " + r[1];
                                OutletInstance i = new OutletInstance();
                                i.outletname = outletname;
                                i.objname = on2;
                                source2.add(i);
                            } else if (restoreConnectionsToExternalOutlets) {
                                AxoObjectInstanceAbstract obj = p.GetObjectInstance(objname);
                                if ((obj != null) && (connectedOutlet == null)) {
                                    OutletInstance oi = obj.GetOutletInstance(outletname);
                                    if (oi != null) {
                                        connectedOutlet = oi;
                                    }
                                }
                            }
                        }
                    }
                    n.source = source2;
                }
                if (n.dest != null) {
                    ArrayList<InletInstance> dest2 = new ArrayList<InletInstance>();
                    for (InletInstance o : n.dest) {
                        String objname = o.getObjectInstance().getInstanceName();
                        String inletname = o.getInletname();
                        if ((objname != null) && (inletname != null)) {
                            String on2 = dict.get(objname);
                            if (on2 != null) {
                                InletInstance i = new InletInstance();
                                i.inletname = inletname;
                                i.getObjectInstance().setInstanceName(on2);
                                dest2.add(i);
                            }
                        }
                    }
                    n.dest = dest2;
                }
                if (n.source.size() + n.dest.size() > 1) {
                    if ((connectedInlet == null) && (connectedOutlet == null)) {
                        n.patchView = this;
                        n.patchModel = PatchView.this.patchController.patchModel;
                        n.PostConstructor();
                        PatchView.this.patchController.patchModel.addNet(n);
                        netLayerPanel.add(n);
                    } else if (connectedInlet != null) {
                        for (InletInstance o : n.dest) {
                            InletInstance o2 = PatchView.this.patchController.patchModel.getInletByReference(o.getObjectInstance().getInstanceName(), o.getInletname());
                            if ((o2 != null) && (o2 != connectedInlet)) {
                                patchControllerAddConnection(connectedInlet, o2);
                            }
                        }
                        for (OutletInstance o : n.source) {
                            OutletInstance o2 = PatchView.this.patchController.patchModel.getOutletByReference(o.getObjname(), o.getOutletname());
                            if (o2 != null) {
                                AddConnection(connectedInlet, o2);
                            }
                        }
                    } else if (connectedOutlet != null) {
                        for (InletInstance o : n.dest) {
                            InletInstance o2 = PatchView.this.patchController.patchModel.getInletByReference(o.getObjectInstance().getInstanceName(), o.getInletname());
                            if (o2 != null) {
                                AddConnection(o2, connectedOutlet);
                            }
                        }
                    }
                }
            }
            AdjustSize();
            PatchView.this.patchController.patchModel.SetDirty();
        } catch (javax.xml.stream.XMLStreamException ex) {
            // silence
        } catch (Exception ex) {
            Logger.getLogger(PatchView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    AxoObjectInstanceAbstract getObjectAtLocation(int x, int y) {
        for (AxoObjectInstanceAbstract o : PatchView.this.patchController.patchModel.getObjectInstances()) {
            if ((o.getX() == x) && (o.getY() == y)) {
                return o;
            }
        }
        return null;
    }
    public ObjectSearchFrame osf;

    public void ShowClassSelector(Point p, AxoObjectInstanceViewAbstract o, String searchString) {
        if (isLocked()) {
            return;
        }
        if (osf == null) {
            osf = new ObjectSearchFrame(patchController);
        }
        osf.Launch(p, o, searchString);
    }

    void SelectAll() {
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.SetSelected(true);
        }
    }

    public void SelectNone() {
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.SetSelected(false);
        }
    }
    TextEditor NotesFrame;

    void ShowNotesFrame() {
        if (NotesFrame == null) {
            NotesFrame = new TextEditor(new StringRef(), patchController.getPatchFrame());
            NotesFrame.setTitle("notes");
            NotesFrame.SetText(patchController.patchModel.notes);
            NotesFrame.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                }

                @Override
                public void focusLost(FocusEvent e) {
                    patchController.patchModel.notes = NotesFrame.GetText();
                }
            });
        }
        NotesFrame.setVisible(true);
        NotesFrame.toFront();
    }

    enum Direction {

        UP, LEFT, DOWN, RIGHT
    }

    PatchView getSelectedObjects() {
        PatchView p = new PatchView(patchController);
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            if (o.isSelected()) {
                p.objectInstanceViews.add(o);
            }
        }
        p.netViews = new ArrayList<NetView>();

        // need to have net views here
        for (NetView n : netViews) {
            int sel = 0;
            for (InletInstanceView i : n.dest) {
                if (i.getObjectInstanceView().isSelected()) {
                    sel++;
                }
            }
            for (OutletInstanceView i : n.source) {
                if (i.getObjectInstanceView().isSelected()) {
                    sel++;
                }
            }
            if (sel > 0) {
                p.netViews.add(n);
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
            Logger.getLogger(PatchView.class.getName()).log(Level.INFO, "can't move: locked");
        }
    }

    public void PostContructor() {
        // get rid of this
        patchController.patchModel.PostContructor();

        objectLayerPanel.removeAll();
        netLayerPanel.removeAll();
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            objectLayerPanel.add(o);
        }
        for (NetView n : netViews) {
            netLayerPanel.add(n);
        }
        objectLayerPanel.validate();
        netLayerPanel.validate();

        Layers.setPreferredSize(new Dimension(5000, 5000));
        AdjustSize();
        Layers.revalidate();

        for (NetView n : netViews) {
            n.updateBounds();
        }
    }

    public void setFileNamePath(String FileNamePath) {
        patchController.setFileNamePath(FileNamePath);
        patchController.getPatchFrame().setTitle(FileNamePath);
    }

    public NetView AddConnection(InletInstanceView il, OutletInstanceView ol) {
        NetView n = patchController.AddConnection(il, ol);
        if (n != null) {
            netLayerPanel.add(n);
            netViews.add(n);
        }
        return n;
    }

    public NetView AddConnection(InletInstanceView il, InletInstanceView ol) {
        NetView n = patchController.AddConnection(il, ol);
        if (n != null) {
            netLayerPanel.add(n);
            netViews.add(n);
        }
        return n;
    }

    public NetView disconnect(InletInstanceView io) {
        NetView n = patchController.disconnect(io);
        if (n != null) {
            n.updateBounds();
            n.repaint();
        }
        return n;
    }

    public NetView disconnect(OutletInstanceView io) {
        NetView n = patchController.disconnect(io);
        if (n != null) {
            n.updateBounds();
            n.repaint();
        }
        return n;
    }

    public NetView delete(NetView n) {
        if (n != null) {
            netViews.remove(n);
            netLayerPanel.remove(n);
            netLayer.repaint(n.getBounds());
        }
        Net nn = PatchView.this.patchController.delete(n);
        return n;
    }

    public void delete(AxoObjectInstanceViewAbstract o) {
        PatchView.this.patchController.delete(o);
        objectLayerPanel.remove(o);
        objectLayerPanel.validate();
        AdjustSize();
    }

    public AxoObjectInstanceViewAbstract AddObjectInstance(AxoObjectAbstract obj, Point loc) {
        AxoObjectInstanceAbstract objinst = patchController.AddObjectInstance(obj, loc);
        AxoObjectInstanceViewAbstract objView = new AxoObjectInstanceView((AxoObjectInstance) objinst);
        if (objinst != null) {
            SelectNone();
            objectLayerPanel.add(objView);
            objView.SetSelected(true);
            objView.moveToFront();
            objView.revalidate();
            AdjustSize();
        }
        return objView;
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

    void GoLive() {
        PatchView patchView = patchController.patchView;
        if (patchView != null) {
            patchView.Unlock();
        }

        QCmdProcessor qCmdProcessor = patchController.GetQCmdProcessor();

        qCmdProcessor.AppendToQueue(new QCmdStop());
        String f = "/" + patchController.getSDCardPath();
        System.out.println("pathf" + f);
        qCmdProcessor.AppendToQueue(new QCmdCreateDirectory(f));
        qCmdProcessor.AppendToQueue(new QCmdChangeWorkingDirectory(f));
        patchController.UploadDependentFiles();
        patchController.ShowPreset(0);
        patchController.WriteCode();
        patchController.setPresetUpdatePending(false);
        qCmdProcessor.setPatchController(null);
        qCmdProcessor.AppendToQueue(new QCmdCompilePatch(patchController.patchModel));
        qCmdProcessor.AppendToQueue(new QCmdUploadPatch());
        qCmdProcessor.AppendToQueue(new QCmdStart(patchController));
        qCmdProcessor.AppendToQueue(new QCmdLock(patchController));
    }



    void invalidate() {
        Layers.invalidate();
    }

    public void repaint() {
        Layers.repaint();
    }

    void SetDSPLoad(int pct) {
        patchController.getPatchFrame().ShowDSPLoad(pct);
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
        if (s.width < Layers.getParent().getWidth()) {
            s.width = Layers.getParent().getWidth();
        }
        if (s.height < Layers.getParent().getHeight()) {
            s.height = Layers.getParent().getHeight();
        }
    }

    public void AdjustSize() {
        Dimension s = patchController.GetSize();
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
            PatchView.this.patchController.patchModel.notes = NotesFrame.GetText();
        }
        PatchView.this.patchController.patchModel.windowPos = patchController.getPatchFrame().getBounds();
    }

    boolean save(File f) {
        boolean b = PatchView.this.patchController.patchModel.save(f);
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
            PatchView patchView = new PatchView(patchController);
            patchController.setPatchView(patchView);
            patchController.setPatchModel(patchModel);
            PatchFrame pf = new PatchFrame(patchController, QCmdProcessor.getQCmdProcessor());
            patchView.setFileNamePath(name);
            patchView.PostContructor();
            pf.setVisible(true);
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
            PatchView patchView = new PatchView(patchController);
            patchController.setPatchView(patchView);
            patchController.setPatchModel(patchModel);
            PatchFrame pf = new PatchFrame(patchController, QCmdProcessor.getQCmdProcessor());
            patchView.setFileNamePath(f.getAbsolutePath());
            patchView.PostContructor();
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
        pf.setState(java.awt.Frame.NORMAL);
        pf.toFront();
        return pf;
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

    void cleanUpObjectLayer() {
        if (!isLocked()) {
            for (Component c : objectLayerPanel.getComponents()) {
                if (!objectInstanceViews.contains((AxoObjectInstanceViewAbstract) c)) {
                    this.objectLayerPanel.remove(c);
                }
            }
        }
        repaint();
    }

    Dimension GetSize() {
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
            boolean cont = true;
            while (cont) {
                cont = false;
                for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
                    if (o.isSelected()) {
                        patchController.delete(o);
                        cont = true;
                        break;
                    }
                }
            }
            patchController.SetDirty();
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
        }
        return null;
    }

    public List<NetView> getNetViews() {
        return this.netViews;
    }

    public void Lock() {
        patchController.getPatchFrame().SetLive(true);
        Layers.setBackground(Theme.getCurrentTheme().Patch_Locked_Background);
        locked = true;
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.Lock();
        }
    }

    public void Unlock() {
        patchController.getPatchFrame().SetLive(false);
        Layers.setBackground(Theme.getCurrentTheme().Patch_Unlocked_Background);
        locked = false;
        ArrayList<AxoObjectInstanceViewAbstract> objInstsClone = (ArrayList<AxoObjectInstanceViewAbstract>) objectInstanceViews.clone();
        for (AxoObjectInstanceViewAbstract o : objectInstanceViews) {
            o.Unlock();
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void ShowCompileFail() {
        Unlock();
    }

    public PatchController getPatchController() {
        return this.patchController;
    }
}
