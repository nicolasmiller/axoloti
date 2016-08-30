package axoloti.piolet;

import axoloti.PatchModel;
import axoloti.PatchViewProcessing;
import axoloti.iolet.IIoletAbstract;
import axoloti.pobjectviews.PAxoObjectInstanceView;
import axoloti.processing.PComponent;
import axoloti.processing.PNetDragging;
import components.processing.PLabelComponent;
import java.awt.Point;
import processing.core.PApplet;

public abstract class PIoletAbstract extends PComponent implements IIoletAbstract {

    public PIoletAbstract(PApplet p) {
        super(p);
    }

    public PAxoObjectInstanceView axoObj;
    public PLabelComponent lbl;
    public PComponent jack;

    public PAxoObjectInstanceView getObjectInstanceView() {
        return axoObj;
    }

    public Point getJackLocInCanvas() {
        Point p1 = new Point(5, 5);
        PComponent p = jack;
        while (p != null) {
            p1.x = p1.x + p.getX();
            p1.y = p1.y + p.getY();
            if (p == axoObj) {
                break;
            }
            p = (PComponent) p.getParent();
        }
        return p1;
    }

//    abstract public PPopupMenu getPopup();
    public PatchViewProcessing getPatchView() {
        return axoObj.getPatchView();
    }

    public PatchModel getPatchModel() {
        return axoObj.getPatchModel();
    }

    PNetDragging dragnet = null;
    PIoletAbstract dragtarget = null;

//    @Override
//    public void mouseClicked(MouseEvent e) {
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        if (e.isPopupTrigger()) {
//            getPopup().show(IoletAbstract.this, 0, getHeight() - 1);
//            e.consume();
//        } else {
//            setHighlighted(true);
//            if (!axoObj.isLocked()) {
//                if (dragnet == null) {
//                    dragnet = new NetDragging(getPatchView());
//                    dragtarget = null;
//                    if (this instanceof InletInstanceView) {
//                        dragnet.connectInlet((IInletInstanceView) this);
//                    } else {
//                        dragnet.connectOutlet((IOutletInstanceView) this);
//                    }
//                }
//                dragnet.setVisible(true);
//                if (getPatchView() != null) {
//                    getPatchView().selectionRectLayerPanel.add(dragnet);
//                }
//                e.consume();
//            }
//        }
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        if (e.isPopupTrigger()) {
//            getPopup().show(this, 0, getHeight() - 1);
//            e.consume();
//        } else if ((dragnet != null) && (getPatchView() != null)) {
//            dragnet.repaint();
//            getPatchView().selectionRectLayerPanel.remove(dragnet);
//            dragnet = null;
//            Net n = null;
//            if (dragtarget == null) {
//                Point p = SwingUtilities.convertPoint(IoletAbstract.this, e.getPoint(), getPatchView().selectionRectLayerPanel);
//                Component c = getPatchView().objectLayerPanel.findComponentAt(p);
//                while ((c != null) && !(c instanceof IoletAbstract)) {
//                    c = c.getParent();
//                }
//
//                if (this != c) {
//                    if (IoletAbstract.this instanceof InletInstanceView) {
//                        n = getPatchView().getPatchController().disconnect((InletInstanceView) IoletAbstract.this);
//                    } else {
//                        n = getPatchView().getPatchController().disconnect((OutletInstanceView) IoletAbstract.this);
//                    }
//                }
//            } else {
//                if (this instanceof InletInstanceView) {
//                    if (dragtarget instanceof InletInstanceView) {
//                        n = getPatchView().getPatchController().AddConnection(((InletInstanceView) IoletAbstract.this), ((InletInstanceView) dragtarget));
//                    } else if (dragtarget instanceof OutletInstanceView) {
//                        n = getPatchView().getPatchController().AddConnection(((InletInstanceView) IoletAbstract.this), ((OutletInstanceView) dragtarget));
//                    }
//                } else if (this instanceof OutletInstanceView) {
//                    if (dragtarget instanceof InletInstanceView) {
//                        n = getPatchView().getPatchController().AddConnection(((InletInstanceView) dragtarget), ((OutletInstanceView) IoletAbstract.this));
//                    }
//                }
//                axoObj.getPatchModel().PromoteOverloading(false);
//            }
//            if (n != null) {
//                getPatchModel().SetDirty();
//            }
//            getPatchView().selectionRectLayerPanel.repaint();
//            e.consume();
//        }
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//        setHighlighted(true);
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//        setHighlighted(false);
//    }
//
//    @Override
//    public void mouseDragged(MouseEvent e) {
//        if (!axoObj.isLocked()) {
//            Point p = SwingUtilities.convertPoint(IoletAbstract.this, e.getPoint(), getPatchView().objectLayerPanel);
//            Component c = getPatchView().objectLayerPanel.findComponentAt(p);
//            while ((c != null) && !(c instanceof IoletAbstract)) {
//                c = c.getParent();
//            }
//            if ((c != null)
//                    && (c != this)
//                    && (!((this instanceof OutletInstanceView) && (c instanceof OutletInstanceView)))) {
//                // different target and not myself?
//                if (c != dragtarget) {
//                    // new target
//                    dragtarget = (IoletAbstract) c;
//                    Point jackLocation = dragtarget.getJackLocInCanvas();
//                    dragnet.SetDragPoint(jackLocation);
//                }
//            } else // floating
//             if (dragnet != null) {
//                    dragnet.SetDragPoint(p);
//                    dragtarget = null;
//                }
//        }
//        e.consume();
//    }
//
//    @Override
//    public void mouseMoved(MouseEvent e) {
//    }
//    public void setHighlighted(boolean highlighted) {
//        if ((getRootPane() == null
//                || getRootPane().getCursor() != MainFrame.transparentCursor)
//                && axoObj != null
//                && axoObj.getPatchView() != null) {
//            INetView netView = axoObj.getPatchView().GetNetView(this);
//            if (netView != null
//                    && netView.getSelected() != highlighted) {
//                netView.setSelected(highlighted);
//            }
//        }
//    }
}
