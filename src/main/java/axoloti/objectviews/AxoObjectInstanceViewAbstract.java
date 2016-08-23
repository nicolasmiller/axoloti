package axoloti.objectviews;

import axoloti.INetView;
import axoloti.PatchModel;
import axoloti.PatchViewSwing;
import axoloti.Theme;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.outlets.IOutletInstanceView;
import axoloti.parameterviews.IParameterInstanceView;
import axoloti.utils.Constants;
import components.LabelComponent;
import components.TextFieldComponent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

public class AxoObjectInstanceViewAbstract extends JPanel implements MouseListener, MouseMotionListener, IAxoObjectInstanceView {

    protected AxoObjectInstanceAbstract model;
    protected MouseListener ml;
    protected MouseMotionListener mml;
    protected boolean dragging = false;
    protected String InstanceName;
    private Point dragLocation = null;
    private Point dragAnchor = null;
    protected boolean selected = false;
    final JPanel Titlebar = new JPanel();
    TextFieldComponent InstanceNameTF;
    LabelComponent InstanceLabel;
    private boolean Locked = false;

    AxoObjectInstanceViewAbstract(AxoObjectInstanceAbstract model, PatchViewSwing patchView) {
        this.model = model;
        this.patchView = patchView;
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

    private static final Dimension TITLEBAR_MINIMUM_SIZE = new Dimension(40, 12);
    private static final Dimension TITLEBAR_MAXIMUM_SIZE = new Dimension(32768, 12);

    @Override
    public void PostConstructor() {
        removeAll();
        setMinimumSize(new Dimension(60, 40));
        //setMaximumSize(new Dimension(Short.MAX_VALUE,
        //        Short.MAX_VALUE));

//        setFocusable(true);
        Titlebar.removeAll();
        Titlebar.setLayout(new BoxLayout(Titlebar, BoxLayout.LINE_AXIS));
        Titlebar.setBackground(Theme.getCurrentTheme().Object_TitleBar_Background);
        Titlebar.setMinimumSize(TITLEBAR_MINIMUM_SIZE);
        Titlebar.setMaximumSize(TITLEBAR_MAXIMUM_SIZE);

        setBorder(BORDER_UNSELECTED);
        model.resolveType();

        setBackground(Theme.getCurrentTheme().Object_Default_Background);

        setVisible(true);

        Titlebar.addMouseListener(this);
        addMouseListener(this);

        Titlebar.addMouseMotionListener(this);
        addMouseMotionListener(this);
    }

    JPopupMenu CreatePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        return popup;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (getPatchView() != null) {
            if (me.getClickCount() == 1) {
                if (me.isShiftDown()) {
                    setSelected(!isSelected());
                    me.consume();
                } else if (selected == false) {
                    getPatchView().SelectNone();
                    setSelected(true);
                    me.consume();
                }
            }
            if (me.getClickCount() == 2) {
                getPatchView().ShowClassSelector(AxoObjectInstanceViewAbstract.this.getLocation(), AxoObjectInstanceViewAbstract.this, null);
                me.consume();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        handleMousePressed(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        handleMouseReleased(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if ((getPatchModel() != null) && (draggingObjects != null)) {
            Point locOnScreen = me.getLocationOnScreen();
            int dx = locOnScreen.x - dragAnchor.x;
            int dy = locOnScreen.y - dragAnchor.y;
            for (AxoObjectInstanceViewAbstract o : draggingObjects) {
                int nx = o.dragLocation.x + dx;
                int ny = o.dragLocation.y + dy;
                if (!me.isShiftDown()) {
                    nx = ((nx + (Constants.X_GRID / 2)) / Constants.X_GRID) * Constants.X_GRID;
                    ny = ((ny + (Constants.Y_GRID / 2)) / Constants.Y_GRID) * Constants.Y_GRID;
                }
                if (o.model.getX() != nx || o.model.getY() != ny) {
                    o.setLocation(nx, ny);
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    private void moveToDraggedLayer(AxoObjectInstanceViewAbstract o) {
        if (getPatchView().objectLayerPanel.isAncestorOf(o)) {
            getPatchView().objectLayerPanel.remove(o);
            getPatchView().draggedObjectLayerPanel.add(o);
        }
    }

    ArrayList<AxoObjectInstanceViewAbstract> draggingObjects = null;

    protected void handleMousePressed(MouseEvent me) {
        if (getPatchView() != null) {
            if (me.isPopupTrigger()) {
                JPopupMenu p = CreatePopupMenu();
                p.show(Titlebar, 0, Titlebar.getHeight());
                me.consume();
            } else if (!patchView.isLocked()) {
                draggingObjects = new ArrayList<AxoObjectInstanceViewAbstract>();
                dragAnchor = me.getLocationOnScreen();
                moveToDraggedLayer(this);
                draggingObjects.add(this);
                dragLocation = getLocation();
                if (isSelected()) {
                    for (IAxoObjectInstanceView o : getPatchView().getObjectInstanceViews()) {
                        if (o.isSelected()) {
                            AxoObjectInstanceViewAbstract oa = (AxoObjectInstanceViewAbstract) o;
                            moveToDraggedLayer(oa);
                            draggingObjects.add(oa);
                            oa.dragLocation = oa.getLocation();
                        }
                    }
                }
                me.consume();
            }
        }
    }

    private void moveToObjectLayer(AxoObjectInstanceViewAbstract o, int z) {
        if (getPatchView().draggedObjectLayerPanel.isAncestorOf(o)) {
            getPatchView().draggedObjectLayerPanel.remove(o);
            getPatchView().objectLayerPanel.add(o);
            getPatchView().objectLayerPanel.setComponentZOrder(o, z);
        }
    }

    protected void handleMouseReleased(MouseEvent me) {
        if (me.isPopupTrigger()) {
            JPopupMenu p = CreatePopupMenu();
            p.show(Titlebar, 0, Titlebar.getHeight());
            me.consume();
            return;
        }
        int maxZIndex = 0;
        if (draggingObjects != null) {
            if (getPatchModel() != null) {
                boolean dirtyOnRelease = false;
                for (AxoObjectInstanceViewAbstract o : draggingObjects) {
                    moveToObjectLayer(o, 0);
                    if (getPatchView().objectLayerPanel.getComponentZOrder(o) > maxZIndex) {
                        maxZIndex = getPatchView().objectLayerPanel.getComponentZOrder(o);
                    }
                    if (o.model.getX() != dragLocation.x || o.model.getY() != dragLocation.y) {
                        dirtyOnRelease = true;
                    }
                    o.repaint();
                }
                draggingObjects = null;
                if (dirtyOnRelease) {
                    getPatchModel().SetDirty();
                }
                getPatchView().AdjustSize();
            }
            me.consume();
        }
    }

    private final PatchViewSwing patchView;

    @Override
    public PatchViewSwing getPatchView() {
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

        super.setLocation(x, y);

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

    public void addInstanceNameEditor() {
        InstanceNameTF = new TextFieldComponent(InstanceName);
        InstanceNameTF.selectAll();
        InstanceNameTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String s = InstanceNameTF.getText();
                setInstanceName(s);
                getParent().remove(InstanceNameTF);
            }
        });
        InstanceNameTF.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                String s = InstanceNameTF.getText();
                setInstanceName(s);
                getParent().remove(InstanceNameTF);
                repaint();
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        InstanceNameTF.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    String s = InstanceNameTF.getText();
                    setInstanceName(s);
                    getParent().remove(InstanceNameTF);
                    repaint();
                }
            }
        });

        getParent().add(InstanceNameTF, 0);
        InstanceNameTF.setLocation(getLocation().x, getLocation().y + InstanceLabel.getLocation().y);
        InstanceNameTF.setSize(getWidth(), 15);
        InstanceNameTF.setVisible(true);
        InstanceNameTF.requestFocus();
    }

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
        doLayout();
    }

    public static final Border BORDER_SELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Selected);
    public static final Border BORDER_UNSELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Unselected);

    public void setSelected(boolean Selected) {
        if (this.selected != Selected) {
            if (Selected) {
                setBorder(BORDER_SELECTED);
            } else {
                setBorder(BORDER_UNSELECTED);
            }
            repaint();
        }
        this.selected = Selected;
    }

    public Boolean isSelected() {
        return this.selected;
    }

    public void SetLocation(int x1, int y1) {
        super.setLocation(x1, y1);
        model.setLocation(x1, y1);
        if (getPatchView() != null) {
            for (INetView n : getPatchView().getNetViews()) {
                n.updateBounds();
            }
        }
    }

    @Override
    public void moveToFront() {
        getPatchView().objectLayerPanel.setComponentZOrder(this, 0);
    }

    public void resizeToGrid() {
        validate();
        Dimension d = getPreferredSize();
        d.width = ((d.width + Constants.X_GRID - 1) / Constants.X_GRID) * Constants.X_GRID;
        d.height = ((d.height + Constants.Y_GRID - 1) / Constants.Y_GRID) * Constants.Y_GRID;
        setSize(d);
    }

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
