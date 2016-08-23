package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.Theme;
import axoloti.inlets.IInletInstanceView;
import axoloti.object.AxoObjectInstanceZombie;
import axoloti.outlets.IOutletInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import axoloti.piccolo.PUtils;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.inlets.PInletInstanceView;
import axoloti.piccolo.outlets.POutletInstanceView;
import components.piccolo.PLabelComponent;
import components.piccolo.PPopupIcon;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAxoObjectInstanceViewZombie extends PAxoObjectInstanceViewAbstract {

    AxoObjectInstanceZombie model;

    public PatchPNode p_ioletViews;
    public PatchPNode p_inletViews;
    public PatchPNode p_outletViews;

    public PAxoObjectInstanceViewZombie(AxoObjectInstanceZombie model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    public void PostConstructor() {
        super.PostConstructor();
        setLayout(VERTICAL_CENTERED);

        p_ioletViews = new PatchPNode(patchView);
        p_ioletViews.setLayout(HORIZONTAL_CENTERED);
        p_ioletViews.setPickable(false);
        p_inletViews = new PatchPNode(patchView);
        p_inletViews.setPickable(false);
        p_inletViews.setLayout(VERTICAL_CENTERED);
        p_outletViews = new PatchPNode(patchView);
        p_outletViews.setLayout(VERTICAL_CENTERED);
        p_outletViews.setPickable(false);

        PLabelComponent idlbl = new PLabelComponent(model.typeName);
        idlbl.setPaint(Theme.getCurrentTheme().Object_TitleBar_Foreground);

        final PPopupIcon popupIcon = new PPopupIcon(this);
        popupIcon.setPopupIconListener(new PPopupIcon.PopupIconListener() {
            @Override
            public void ShowPopup(PInputEvent e) {
                JPopupMenu popup = CreatePopupMenu();
                Point popupLocation = PUtils.getPopupLocation(e);
                popup.show(getCanvas(), popupLocation.x, popupLocation.y);
            }
        });

        titleBar.addChild(popupIcon);
        titleBar.addChild(idlbl);
        titleBar.setBounds(0, 0, titleBar.getChildrenWidth(), titleBar.getChildrenHeight());

        addChild(titleBar);

        setPaint(Theme.getCurrentTheme().Object_Zombie_Background);
        setLayout(VERTICAL_CENTERED);
        InstanceLabel = new PLabelComponent(model.getInstanceName());
        InstanceLabel.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                    e.setHandled(true);
                }
            }
        });

        InstanceLabel.setPickable(true);
        addChild(InstanceLabel);

        p_ioletViews.addChild(p_inletViews);
        p_ioletViews.addChild(p_outletViews);
        addChild(p_ioletViews);

        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());

        resizeToGrid();
    }

    @Override
    JPopupMenu CreatePopupMenu() {
        JPopupMenu popup = super.CreatePopupMenu();
        JMenuItem popm_substitute = new JMenuItem("replace");
        popm_substitute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((PatchViewPiccolo) getPatchView()).ShowClassSelector(PAxoObjectInstanceViewZombie.this.getLocation(), null, PAxoObjectInstanceViewZombie.this, null);
            }
        });
        popup.add(popm_substitute);
        JMenuItem popm_editInstanceName = new JMenuItem("edit instance name");
        popm_editInstanceName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addInstanceNameEditor();
            }
        });
        popup.add(popm_editInstanceName);
        return popup;
    }

    @Override
    public void setInstanceName(String s) {
        super.setInstanceName(s);
        resizeToGrid();
        repaint();
    }

    private ArrayList<IInletInstanceView> inletInstanceViews = new ArrayList<IInletInstanceView>();
    private ArrayList<IOutletInstanceView> outletInstanceViews = new ArrayList<IOutletInstanceView>();

    @Override
    public ArrayList<IInletInstanceView> getInletInstanceViews() {
        return inletInstanceViews;
    }

    @Override
    public ArrayList<IOutletInstanceView> getOutletInstanceViews() {
        return outletInstanceViews;
    }

    @Override
    public void addInletInstanceView(IInletInstanceView view) {
        inletInstanceViews.add(view);
        p_inletViews.addChild((PInletInstanceView) view);
        setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        outletInstanceViews.add(view);
        p_outletViews.addChild((POutletInstanceView) view);
        setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
    }

    @Override
    public boolean isZombie() {
        return true;
    }
}
