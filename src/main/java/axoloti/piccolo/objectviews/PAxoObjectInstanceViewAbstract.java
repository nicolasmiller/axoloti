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
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.parameterviews.IParameterInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.PatchPCanvas;
import axoloti.piccolo.PatchPNode;
import axoloti.utils.Constants;
import components.piccolo.PLabelComponent;
import components.piccolo.PTextFieldComponent;
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
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAxoObjectInstanceViewAbstract extends PatchPNode implements IAxoObjectInstanceView {

    protected AxoObjectInstanceAbstract model;
    protected MouseListener ml;
    protected MouseMotionListener mml;
    protected boolean dragging = false;
    protected String InstanceName;
    final PatchPNode titleBar;
    PTextFieldComponent InstanceNameTF;
    PLabelComponent instanceLabel;
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
        instanceLabel.setVisible(true);
        repaint();
    }

    public void addInstanceNameEditor() {
        InstanceNameTF = new PTextFieldComponent(instanceLabel.getText());
        InstanceNameTF.selectAll();
        PBasicInputEventHandler inputEventHandler = new PBasicInputEventHandler() {
            @Override
            public void keyboardFocusLost(PInputEvent e) {
                handleInstanceNameEditorAction();
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
                InstanceNameTF.selectAll();
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

        instanceLabel.setVisible(false);

        addChild(1, InstanceNameTF);
        InstanceNameTF.setBounds(0, 0, getWidth() - 1, 16);
        InstanceNameTF.setTransform(instanceLabel.getTransform());
        InstanceNameTF.grabFocus();
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
        if (instanceLabel != null) {
            instanceLabel.setText(InstanceName);
        }
    }

    public static final Border BORDER_SELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Selected);
    public static final Border BORDER_UNSELECTED = BorderFactory.createLineBorder(Theme.getCurrentTheme().Object_Border_Unselected);

    public void SetLocation(int x1, int y1) {
        model.setLocation(x1, y1);
        model.setDirty(true);
        if (getPatchView() != null) {
            for (INetView n : getPatchView().getNetViews()) {
                n.updateBounds();
            }
        }
    }

    @Override
    public void moveToFront() {
        // new objects added to front by default
    }

    @Override
    public void resizeToGrid() {
        Dimension d = getPreferredSize();
        setWidth(((d.width + Constants.X_GRID - 1) / Constants.X_GRID) * Constants.X_GRID);
        setHeight(((d.height + Constants.Y_GRID - 1) / Constants.Y_GRID) * Constants.Y_GRID);
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
    public JComponent getCanvas() {
        return patchView.getViewportView().getComponent();
    }

    protected PatchPCanvas getPatchPCanvas() {
        return (PatchPCanvas) getCanvas();
    }

    @Override
    public boolean isZombie() {
        return false;
    }
}
