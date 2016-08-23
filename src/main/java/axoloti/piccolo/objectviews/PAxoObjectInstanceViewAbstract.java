/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.piccolo.objectviews;

import axoloti.INetView;
import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.PatchViewPiccolo;
import axoloti.Theme;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.object.AxoObjectAbstract;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.object.ObjectModifiedListener;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.parameterviews.IParameterInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.PatchPNode;
import axoloti.utils.Constants;
import components.piccolo.PLabelComponent;
import components.piccolo.PTextFieldComponent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAxoObjectInstanceViewAbstract extends PatchPNode implements ObjectModifiedListener, IAxoObjectInstanceView {

    protected AxoObjectInstanceAbstract model;
    protected MouseListener ml;
    protected MouseMotionListener mml;
    protected boolean dragging = false;
    protected String InstanceName;
    protected boolean selected = false;
    final PatchPNode titleBar;
    PTextFieldComponent InstanceNameTF;
    PLabelComponent InstanceLabel;
    private boolean Locked = false;

    PAxoObjectInstanceViewAbstract(AxoObjectInstanceAbstract model, PatchViewPiccolo patchView) {
        super(patchView);
        this.model = model;
        titleBar = new PatchPNode(patchView);
    }

    @Override
    public AxoObjectInstanceAbstract getModel() {
        return model;
    }

    @Override
    public void Lock() {
        Locked = true;
    }

    @Override
    public void Unlock() {
        Locked = false;
    }

    @Override
    public boolean isLocked() {
        return Locked;
    }

    JPopupMenu popup;

    @Override
    public void PostConstructor() {
        removeAllChildren();

        titleBar.removeAllChildren();
        titleBar.setPickable(false);
        titleBar.setLayout(HORIZONTAL_CENTERED);
        titleBar.setPaint(Theme.getCurrentTheme().Object_TitleBar_Background);

        model.resolveType();

        setPaint(Theme.getCurrentTheme().Object_Default_Background);

// TODO look below there is actually special replace behavior
//        Titlebar.addMouseListener(this);
//        addMouseListener(this);
//
//        Titlebar.addMouseMotionListener(this);
//        addMouseMotionListener(this);
    }

    JPopupMenu CreatePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        return popup;
    }

    public Dimension getSize() {
        return new Dimension((int) this.getBounds().width, (int) this.getBounds().height);
    }

    public Dimension getPreferredSize() {
        return getSize();
    }

    public Point getLocation() {
        return new Point(this.model.getX(), this.model.getY());
    }

//    @Override
//    public void mouseClicked(MouseEvent me) {
//        if (getPatchView() != null) {
//            if (me.getClickCount() == 1) {
//                if (me.isShiftDown()) {
//                    setSelected(!isSelected());
//                    me.consume();
//                } else if (selected == false) {
//                    getPatchView().SelectNone();
//                    setSelected(true);
//                    me.consume();
//                }
//            }
//            if (me.getClickCount() == 2) {
//                getPatchView().ShowClassSelector(AxoObjectInstanceViewAbstract.this.getLocation(), AxoObjectInstanceViewAbstract.this, null);
//                me.consume();
//            }
//        }
//    }
//    @Override
//    public void mousePressed(MouseEvent me) {
//        handleMousePressed(me);
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent me) {
//        handleMouseReleased(me);
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent me) {
//    }
//
//    @Override
//    public void mouseExited(MouseEvent me) {
//    }
//    @Override
//    public void mouseDragged(MouseEvent me) {
//        if ((getPatchModel() != null) && (draggingObjects != null)) {
//            Point locOnScreen = me.getLocationOnScreen();
//            int dx = locOnScreen.x - dragAnchor.x;
//            int dy = locOnScreen.y - dragAnchor.y;
//            for (AxoObjectInstanceViewAbstract o : draggingObjects) {
//                int nx = o.dragLocation.x + dx;
//                int ny = o.dragLocation.y + dy;
//                if (!me.isShiftDown()) {
//                    nx = ((nx + (Constants.X_GRID / 2)) / Constants.X_GRID) * Constants.X_GRID;
//                    ny = ((ny + (Constants.Y_GRID / 2)) / Constants.Y_GRID) * Constants.Y_GRID;
//                }
//                if (o.model.getX() != nx || o.model.getY() != ny) {
//                    o.setLocation(nx, ny);
//                }
//            }
//        }
//    }
//    @Override
//    public void mouseMoved(MouseEvent me) {
//    }
//
//    private void moveToDraggedLayer(AxoObjectInstanceViewAbstract o) {
//        if (getPatchView().objectLayerPanel.isAncestorOf(o)) {
//            getPatchView().objectLayerPanel.remove(o);
//            getPatchView().draggedObjectLayerPanel.add(o);
//        }
//    }
//
//    ArrayList<AxoObjectInstanceViewAbstract> draggingObjects = null;
//    protected void handleMousePressed(MouseEvent me) {
//        if (getPatchView() != null) {
//            if (me.isPopupTrigger()) {
//                JPopupMenu p = CreatePopupMenu();
//                p.show(Titlebar, 0, Titlebar.getHeight());
//                me.consume();
//            } else if (!patchView.isLocked()) {
//                draggingObjects = new ArrayList<AxoObjectInstanceViewAbstract>();
//                dragAnchor = me.getLocationOnScreen();
//                moveToDraggedLayer(this);
//                draggingObjects.add(this);
//                dragLocation = getLocation();
//                if (isSelected()) {
//                    for (IAxoObjectInstanceView o : getPatchView().getObjectInstanceViews()) {
//                        if (o.isSelected()) {
//                            AxoObjectInstanceViewAbstract oa = (AxoObjectInstanceViewAbstract) o;
//                            moveToDraggedLayer(oa);
//                            draggingObjects.add(oa);
//                            oa.dragLocation = oa.getLocation();
//                        }
//                    }
//                }
//                me.consume();
//            }
//        }
//    }
//    private void moveToObjectLayer(AxoObjectInstanceViewAbstract o, int z) {
//        if (getPatchView().draggedObjectLayerPanel.isAncestorOf(o)) {
//            getPatchView().draggedObjectLayerPanel.remove(o);
//            getPatchView().objectLayerPanel.add(o);
//            getPatchView().objectLayerPanel.setComponentZOrder(o, z);
//        }
//    }
//    protected void handleMouseReleased(MouseEvent me) {
//        if (me.isPopupTrigger()) {
//            JPopupMenu p = CreatePopupMenu();
//            p.show(Titlebar, 0, Titlebar.getHeight());
//            me.consume();
//            return;
//        }
//        int maxZIndex = 0;
//        if (draggingObjects != null) {
//            if (getPatchModel() != null) {
//                boolean dirtyOnRelease = false;
//                for (AxoObjectInstanceViewAbstract o : draggingObjects) {
//                    moveToObjectLayer(o, 0);
//                    if (getPatchView().objectLayerPanel.getComponentZOrder(o) > maxZIndex) {
//                        maxZIndex = getPatchView().objectLayerPanel.getComponentZOrder(o);
//                    }
//                    if (o.model.getX() != dragLocation.x || o.model.getY() != dragLocation.y) {
//                        dirtyOnRelease = true;
//                    }
//                    o.repaint();
//                }
//                draggingObjects = null;
//                if (dirtyOnRelease) {
//                    getPatchModel().SetDirty();
//                }
//                getPatchView().AdjustSize();
//            }
//            me.consume();
//        }
//    }
    @Override
    public PatchView getPatchView() {
        return patchView;
    }

    @Override
    public PatchModel getPatchModel() {
        return patchView.getPatchController().patchModel;
    }

    @Override
    public ArrayList<IInletInstanceView> getInletInstanceViews() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<IOutletInstanceView> getOutletInstanceViews() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<IParameterInstanceView> getParameterInstanceViews() {
        return new ArrayList<>();
    }

    @Override
    public void setLocation(int x, int y) {
        model.setX(x);
        model.setY(y);
        if (getPatchView() != null) {
            repaint();
            for (IInletInstanceView i : getInletInstanceViews()) {
                INetView n = getPatchView().GetNetView(i);
                if (n != null) {
                    n.updateBounds();
                    n.repaint();
                }
            }
            for (IOutletInstanceView i : getOutletInstanceViews()) {
                INetView n = getPatchView().GetNetView(i);
                if (n != null) {
                    n.updateBounds();
                    n.repaint();
                }
            }
        }
    }

    protected void handleInstanceNameEditorAction() {
        String s = InstanceNameTF.getText();
        setInstanceName(s);
        removeChild(InstanceNameTF);
        InstanceLabel.setVisible(true);
        repaint();
    }

    public void addInstanceNameEditor() {
        InstanceNameTF = new PTextFieldComponent(InstanceLabel.getText());
        InstanceNameTF.selectAll();
        PBasicInputEventHandler inputEventHandler = new PBasicInputEventHandler() {
            @Override
            public void keyboardFocusLost(PInputEvent e) {
                handleInstanceNameEditorAction();
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
                InstanceNameTF.selectAll();
                InstanceNameTF.requestFocus();
            }

            @Override
            public void keyPressed(PInputEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleInstanceNameEditorAction();
                }
            }
        };

        InstanceNameTF.addInputEventListener(inputEventHandler);

        InstanceNameTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aef) {
                handleInstanceNameEditorAction();
            }
        });

        InstanceLabel.setVisible(false);

        addChild(1, InstanceNameTF);
        InstanceNameTF.setBounds(0, 0, getWidth() - 1, 16);
        InstanceNameTF.setTransform(InstanceLabel.getTransform());
        getRoot().getDefaultInputManager().setKeyboardFocus(inputEventHandler);
    }

    @Override
    public void setInstanceName(String InstanceName) {
        if (this.InstanceName != null && this.InstanceName.equals(InstanceName)) {
            return;
        }

        if (getPatchModel() != null) {
            AxoObjectInstanceAbstract o1 = getPatchModel().GetObjectInstance(InstanceName);
            if ((o1 != null) && (o1 != this.getObjectInstance())) {
                Logger.getLogger(AxoObjectInstanceAbstract.class.getName()).log(Level.SEVERE, "Object name {0} already exists!", InstanceName);
                repaint();
                return;
            }
        }
        this.InstanceName = InstanceName;
        model.setInstanceName(InstanceName);
        if (InstanceLabel != null) {
            InstanceLabel.setText(InstanceName);
        }
    }

    public static final Border BORDER_SELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Selected);
    public static final Border BORDER_UNSELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Unselected);

    public void SetLocation(int x1, int y1) {
        model.setLocation(x1, y1);
        if (getPatchView() != null) {
            for (INetView n : getPatchView().getNetViews()) {
                n.updateBounds();
            }
        }
    }

    @Override
    public void moveToFront() {
        //TODO
//        getPatchView().objectLayerPanel.setComponentZOrder(this, 0);
    }

    @Override
    public void resizeToGrid() {
        Dimension d = getPreferredSize();
        setWidth(((d.width + Constants.X_GRID - 1) / Constants.X_GRID) * Constants.X_GRID);
        setHeight(((d.height + Constants.Y_GRID - 1) / Constants.Y_GRID) * Constants.Y_GRID);
    }

    public void Close() {
        AxoObjectAbstract t = model.getType();
        if (t != null) {
            t.removeObjectModifiedListener(this);
        }
    }

    public void updateObj() {
        model.getType().addObjectModifiedListener(this);
        getPatchModel().ChangeObjectInstanceType(this.getObjectInstance(), this.getObjectInstance().getType());
        getPatchModel().cleanUpIntermediateChangeStates(3);
    }

    @Override
    public void ObjectModified(Object src) {
    }

    @Override
    public AxoObjectInstanceAbstract getObjectInstance() {
        return model;
    }

    @Override
    public void addParameterInstanceView(IParameterInstanceView view) {
    }

    @Override
    public void addAttributeInstanceView(IAttributeInstanceView view) {

    }

    @Override
    public void addDisplayInstanceView(IDisplayInstanceView view) {

    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {

    }

    @Override
    public void addInletInstanceView(IInletInstanceView view) {

    }

    @Override
    public Component getCanvas() {
        return patchView.getViewportView().getComponent();
    }
}
