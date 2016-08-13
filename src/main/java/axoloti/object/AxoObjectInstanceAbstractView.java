/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.object;

import axoloti.Net;
import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.Theme;
import axoloti.inlets.InletInstance;
import axoloti.outlets.OutletInstance;
import axoloti.utils.Constants;
import components.LabelComponent;
import components.TextFieldComponent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstanceAbstractView extends JPanel {

    protected AxoObjectInstanceAbstract model;
    protected JPanel Titlebar;
    protected TextFieldComponent InstanceNameTF;
    protected LabelComponent InstanceLabel;
    protected MouseListener ml;
    protected MouseMotionListener mml;
    protected boolean dragging = false;
    protected String InstanceName;
    protected boolean Selected = false;
    protected int dX, dY;

    AxoObjectInstanceAbstractView(AxoObjectInstanceAbstract model) {
        this.model = model;
    }

    public AxoObjectInstanceAbstract getModel() {
        return model;
    }

    public boolean isLocked() {
        return model.IsLocked();
    }

    public void PostConstructor() {
        removeAll();
        setMinimumSize(new Dimension(60, 40));
        //setMaximumSize(new Dimension(Short.MAX_VALUE,
        //        Short.MAX_VALUE));

        this.setLocation(model.x, model.y);
//        setFocusable(true);
        Titlebar = new TitleBarPanel(this);
        Titlebar.setLayout(new BoxLayout(Titlebar, BoxLayout.LINE_AXIS));
        Titlebar.setBackground(Theme.getCurrentTheme().Object_TitleBar_Background);
        Titlebar.setMinimumSize(TitleBarMinimumSize);
        Titlebar.setMaximumSize(TitleBarMaximumSize);
        setBorder(BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Unselected));
        setOpaque(true);
        model.resolveType();

        setBackground(Theme.getCurrentTheme().Object_Default_Background);

        setVisible(true);

        popup = new JPopupMenu();

        ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (getPatchView() != null) {
                    if (me.getClickCount() == 1) {
                        if (me.isShiftDown()) {
                            SetSelected(!GetSelected());
                            me.consume();
                        } else if (Selected == false) {
                            getPatchView().SelectNone();
                            SetSelected(true);
                            me.consume();
                        }
                    }
                    if (me.getClickCount() == 2) {
                        getPatchView().ShowClassSelector(AxoObjectInstanceAbstractView.this.getLocation(), AxoObjectInstanceAbstractView.this, null);
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
        };

        Titlebar.addMouseListener(ml);
        addMouseListener(ml);

        mml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent me) {
                if (getPatchModel() != null) {
                    if (dragging) {
                        for (AxoObjectInstanceAbstractView o : getPatchView().getObjectInstanceViews()) {
                            if (o.dragging) {
                                model.x = me.getLocationOnScreen().x - o.dX;
                                model.y = me.getLocationOnScreen().y - o.dY;
                                o.dX = me.getLocationOnScreen().x - o.getX();
                                o.dY = me.getLocationOnScreen().y - o.getY();
                                o.setLocation(model.x, model.y);
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent me) {
            }
        };

        Titlebar.addMouseMotionListener(mml);
        addMouseMotionListener(mml);

    }

    JPopupMenu popup;

    private final Dimension TitleBarMinimumSize = new Dimension(40, 12);
    private final Dimension TitleBarMaximumSize = new Dimension(32768, 12);

    private void moveToDraggedLayer(AxoObjectInstanceAbstractView o) {
        if (getPatchView().objectLayerPanel.isAncestorOf(o)) {
            getPatchView().draggedObjectLayerPanel.add(o);
            getPatchView().objectLayerPanel.remove(o);
        }
    }

    protected void handleMousePressed(MouseEvent me) {
        if (getPatchModel() != null) {
            if (me.isPopupTrigger()) {

            } else if (!model.IsLocked()) {
                dX = me.getXOnScreen() - getX();
                dY = me.getYOnScreen() - getY();
                dragging = true;
                moveToDraggedLayer(this);
                if (IsSelected()) {
                    for (AxoObjectInstanceAbstractView o : getPatchView().getObjectInstanceViews()) {
                        if (o.IsSelected()) {
                            moveToDraggedLayer(o);

                            o.dX = me.getXOnScreen() - o.getX();
                            o.dY = me.getYOnScreen() - o.getY();
                            o.dragging = true;
                        }
                    }
                }
            }
        }
    }

    private void moveToObjectLayer(AxoObjectInstanceAbstractView o, int z) {
        if (getPatchView().draggedObjectLayerPanel.isAncestorOf(o)) {
            getPatchView().objectLayerPanel.add(o);
            getPatchView().draggedObjectLayerPanel.remove(o);
            getPatchView().objectLayerPanel.setComponentZOrder(o, z);
        }
    }

    protected void handleMouseReleased(MouseEvent me) {
        int maxZIndex = 0;
        if (dragging) {
            dragging = false;
            if (getPatchModel() != null) {
                boolean setDirty = false;
                for (AxoObjectInstanceAbstractView o : getPatchView().getObjectInstanceViews()) {
                    moveToObjectLayer(o, 0);
                    if (getPatchView().objectLayerPanel.getComponentZOrder(o) > maxZIndex) {
                        maxZIndex = getPatchView().objectLayerPanel.getComponentZOrder(o);
                    }
                    o.dragging = false;
                    int original_x = model.x;
                    int original_y = model.y;
                    model.x = ((model.x + (Constants.X_GRID / 2)) / Constants.X_GRID) * Constants.X_GRID;
                    model.y = ((model.y + (Constants.Y_GRID / 2)) / Constants.Y_GRID) * Constants.Y_GRID;
                    o.setLocation(model.x, model.y);
                    if (model.x != original_x || model.y != original_y) {
                        setDirty = true;
                    }
                }
                if (setDirty) {
                    getPatchModel().SetDirty();
                }
                getPatchView().AdjustSize();
            }
        }
        moveToObjectLayer(this, maxZIndex);
    }

    private PatchView patchView;
    private PatchModel patchModel;

    public PatchView getPatchView() {
        return this.patchView;
    }

    public PatchModel getPatchModel() {
        return this.patchModel;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);

        // set on model
        model.x = x;
        model.y = y;
        if (getPatchModel() != null) {
            repaint();
            for (InletInstance i : model.GetInletInstances()) {
                Net n = getPatchModel().GetNet(i);
                if (n != null) {
                    n.updateBounds();
                    n.repaint();
                }
            }
            for (OutletInstance i : model.GetOutletInstances()) {
                Net n = getPatchModel().GetNet(i);
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
        if (this.InstanceName.equals(InstanceName)) {
            return;
        }

        // set on model
        if (getPatchModel() != null) {
// fix instance name checking
//            AxoObjectInstanceAbstract o1 = getPatchModel().GetObjectInstance(InstanceName);
//            if ((o1 != null) && (o1 != this)) {
//                Logger.getLogger(AxoObjectInstanceAbstract.class.getName()).log(Level.SEVERE, "Object name {0} already exists!", InstanceName);
//                doLayout();
//                repaint();
//                return;
//            }
        }
        this.InstanceName = InstanceName;
        if (InstanceLabel != null) {
            InstanceLabel.setText(InstanceName);
        }
        doLayout();
    }

    public void SetSelected(boolean Selected) {
        if (this.Selected != Selected) {
            if (Selected) {
                setBorder(BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Selected));
            } else {
                setBorder(BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Unselected));
            }
            repaint();
        }
        this.Selected = Selected;
    }

    public void SetLocation(int x1, int y1) {
        super.setLocation(x1, y1);

// set on model?        
//        x = x1;
//        y = y1;
        if (getPatchModel() != null) {
            for (Net n : getPatchModel().getNets()) {
                n.updateBounds();
            }
        }
    }

    public void moveToFront() {
        getPatchView().objectLayerPanel.setComponentZOrder(this, 0);
    }

    public void resizeToGrid() {
        doLayout();
        Dimension d = getPreferredSize();
        d.width = ((d.width + Constants.X_GRID - 1) / Constants.X_GRID) * Constants.X_GRID;
        d.height = ((d.height + Constants.Y_GRID - 1) / Constants.Y_GRID) * Constants.Y_GRID;
        setSize(d);
    }

    public boolean IsSelected() {
        return Selected;
    }

    public boolean GetSelected() {
        return Selected;
    }
}
