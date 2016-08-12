package axoloti.iolet;

import axoloti.MainFrame;
import axoloti.Net;
import axoloti.NetDragging;
import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.inlets.InletInstance;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.outlets.OutletInstance;
import java.awt.Component;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.simpleframework.xml.Attribute;

public abstract class IoletAbstract extends JPanel {

    @Deprecated
    @Attribute(required = false)
    public String name;
    @Attribute(name = "obj", required = false)
    public String objname;

    public AxoObjectInstanceAbstract axoObj;
    public JLabel lbl;
    public JComponent jack;

    protected DropTarget dt;

    @Deprecated
    public String getName() {
        return name;
    }

    public String getObjname() {
        if (objname != null) {
            return objname;
        } else {
            int sepIndex = name.lastIndexOf(' ');
            return name.substring(0, sepIndex);
        }
    }

    public AxoObjectInstanceAbstract GetObjectInstance() {
        return axoObj;
    }

    private Point getJackLocInCanvasHidden() {
        Point p1 = new Point(5, 5);
        Component p = (Component) jack;
        while (p != null) {
            p1.x = p1.x + p.getX();
            p1.y = p1.y + p.getY();
            if (p == axoObj) {
                break;
            }
            p = (Component) p.getParent();
        }
        return p1;
    }

    public Point getJackLocInCanvas() {
        try {
            Point jackLocation = jack.getLocationOnScreen();
            jackLocation.x += 5;
            jackLocation.y += 5;
            SwingUtilities.convertPointFromScreen(jackLocation, getPatchView().Layers);
            return jackLocation;
        } catch (IllegalComponentStateException e) {
            return getJackLocInCanvasHidden();
        } catch (NullPointerException e) {
            return getJackLocInCanvasHidden();
        }
    }

    abstract public JPopupMenu getPopup();

    public PatchView getPatchView() {
        return axoObj.getPatchView();
    }
    
    public PatchModel getPatchModel() {
        return axoObj.getPatchModel();
    }

    NetDragging dragnet = null;
    IoletAbstract dragtarget = null;

    public void addMouseListeners() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    getPopup().show(IoletAbstract.this, 0, getHeight() - 1);
                    e.consume();
                } else {
                    setHighlighted(true);
                    if (!axoObj.IsLocked()) {
                        if (dragnet == null) {
                            dragnet = new NetDragging(getPatchModel());
                            dragnet.setPatchView(getPatchView());
                            dragtarget = null;
                            if (IoletAbstract.this instanceof InletInstance) {
                                dragnet.connectInlet((InletInstance) IoletAbstract.this);
                            } else {
                                dragnet.connectOutlet((OutletInstance) IoletAbstract.this);
                            }
                        }
                        dragnet.setVisible(true);
                        getPatchView().selectionRectLayerPanel.add(dragnet);
                        e.consume();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragnet != null) {
                    dragnet.repaint();
                    getPatchView().selectionRectLayerPanel.remove(dragnet);
                    dragnet = null;
                    if (dragtarget == null) {
                        Point p = SwingUtilities.convertPoint(IoletAbstract.this, e.getPoint(), getPatchView().selectionRectLayerPanel);
                        Component c = getPatchView().objectLayerPanel.findComponentAt(p);
                        while ((c != null) && !(c instanceof IoletAbstract)) {
                            c = c.getParent();
                        }
                        if (IoletAbstract.this != c) {
                            getPatchView().disconnect(IoletAbstract.this);
                        }
                    } else {
                        if (IoletAbstract.this instanceof InletInstance) {
                            if (dragtarget instanceof InletInstance) {
                                getPatchView().AddConnection((InletInstance) IoletAbstract.this, (InletInstance) dragtarget);
                            } else if (dragtarget instanceof OutletInstance) {
                                getPatchView().AddConnection((InletInstance) IoletAbstract.this, (OutletInstance) dragtarget);
                            }
                        } else if (IoletAbstract.this instanceof OutletInstance) {
                            if (dragtarget instanceof InletInstance) {
                                getPatchView().AddConnection((InletInstance) dragtarget, (OutletInstance) IoletAbstract.this);
                            }
                        }
                        axoObj.getPatchModel().PromoteOverloading(false);
                    }
                    getPatchView().selectionRectLayerPanel.repaint();
                    e.consume();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHighlighted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHighlighted(false);
            }
        }
        );
        addMouseMotionListener(new MouseMotionListener() {

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (!axoObj.IsLocked()) {
                            Point p = SwingUtilities.convertPoint(IoletAbstract.this, e.getPoint(), getPatchView().objectLayerPanel);
                            Component c = getPatchView().objectLayerPanel.findComponentAt(p);
                            while ((c != null) && !(c instanceof IoletAbstract)) {
                                c = c.getParent();
                            }
                            if ((c != null)
                            && (c != IoletAbstract.this)
                            && (!((IoletAbstract.this instanceof OutletInstance) && (c instanceof OutletInstance)))) {
                                // different target and not myself?
                                if (c != dragtarget) {
                                    // new target
                                    dragtarget = (IoletAbstract) c;
                                    Point jackLocation = dragtarget.getJackLocInCanvas();
                                    dragnet.SetDragPoint(jackLocation);
                                }
                            } else {
                                // floating
                                if(dragnet != null) {
                                    dragnet.SetDragPoint(p);
                                    dragtarget = null;
                                }
                            }
                        }
                        e.consume();
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                    }
                });
    }

    public boolean isConnected() {
        if (axoObj == null) {
            return false;
        }
        if (axoObj.getPatchModel() == null) {
            return false;
        }
        return (axoObj.getPatchModel().GetNet(this) != null);
    }

    public void setHighlighted(boolean highlighted) {
        if ((getRootPane() == null
                || getRootPane().getCursor() != MainFrame.transparentCursor)
                && axoObj != null
                && axoObj.getPatchModel() != null) {
            Net n = axoObj.getPatchModel().GetNet(this);
            if (n != null
                    && n.getSelected() != highlighted) {
                n.setSelected(highlighted);
            }
        }
    }

    public void disconnect() {
        axoObj.getPatchModel().disconnect(this);
        axoObj.getPatchModel().SetDirty();
    }

    public void deleteNet() {
        Net n = axoObj.getPatchModel().GetNet(this);
        axoObj.getPatchModel().delete(n);
        axoObj.getPatchModel().SetDirty();
    }
}
