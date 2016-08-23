package axoloti.objectviews;

import axoloti.PatchViewSwing;
import axoloti.Theme;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstanceView;
import axoloti.object.AxoObjectInstanceZombie;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstanceView;
import components.LabelComponent;
import components.PopupIcon;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class AxoObjectInstanceViewZombie extends AxoObjectInstanceViewAbstract {

    AxoObjectInstanceZombie model;

    public AxoObjectInstanceViewZombie(AxoObjectInstanceZombie model, PatchViewSwing patchView) {
        super(model, patchView);
        this.model = model;
    }

    public void PostConstructor() {
        super.PostConstructor();
        LabelComponent idlbl = new LabelComponent(model.typeName);
        idlbl.setAlignmentX(LEFT_ALIGNMENT);
        idlbl.setForeground(Theme.getCurrentTheme().Object_TitleBar_Foreground);

        final PopupIcon popupIcon = new PopupIcon();
        popupIcon.setPopupIconListener(
                new PopupIcon.PopupIconListener() {
            @Override
            public void ShowPopup() {
                JPopupMenu popup = CreatePopupMenu();
                popupIcon.add(popup);
                popup.show(popupIcon,
                        0, popupIcon.getHeight());
            }
        });
        Titlebar.add(popupIcon);
        Titlebar.add(idlbl);

        Titlebar.setToolTipText("<html>" + "Unresolved object!");

        Titlebar.setAlignmentX(LEFT_ALIGNMENT);
        add(Titlebar);

        setOpaque(true);
        setBackground(Theme.getCurrentTheme().Object_Zombie_Background);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        InstanceLabel = new LabelComponent(model.getInstanceName());
        InstanceLabel.setAlignmentX(LEFT_ALIGNMENT);
        InstanceLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        add(InstanceLabel);
        setLocation(model.getX(), model.getY());

        resizeToGrid();
    }

    @Override
    JPopupMenu CreatePopupMenu() {
        JPopupMenu popup = super.CreatePopupMenu();
        JMenuItem popm_substitute = new JMenuItem("replace");
        popm_substitute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getPatchView().ShowClassSelector(AxoObjectInstanceViewZombie.this.getLocation(), AxoObjectInstanceViewZombie.this, null);
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
        add((InletInstanceView) view);
    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        outletInstanceViews.add(view);
        add((OutletInstanceView) view);
    }

    @Override
    public boolean isZombie() {
        return true;
    }
}
