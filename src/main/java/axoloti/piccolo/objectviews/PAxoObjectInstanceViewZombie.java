package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.Theme;
import axoloti.object.AxoObjectInstanceZombie;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import components.piccolo.PLabelComponent;
import components.piccolo.PPopupIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PAxoObjectInstanceViewZombie extends PAxoObjectInstanceViewAbstract {

    AxoObjectInstanceZombie model;

    public PAxoObjectInstanceViewZombie(AxoObjectInstanceZombie model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    public void PostConstructor() {
        super.PostConstructor();
        PLabelComponent idlbl = new PLabelComponent(model.typeName);
//        idlbl.setAlignmentX(LEFT_ALIGNMENT);
        idlbl.setPaint(Theme.getCurrentTheme().Object_TitleBar_Foreground);

        final PPopupIcon popupIcon = new PPopupIcon(this);
        //TODO
//        popupIcon.setPopupIconListener(
//                new PPopupIcon.PopupIconListener() {
//            @Override
//            public void ShowPopup() {
//                JPopupMenu popup = CreatePopupMenu();
//                popupIcon.add(popup);
//                popup.show(popupIcon,
//                        0, popupIcon.getHeight());
//            }
//        });
        titleBar.addChild(popupIcon);
        titleBar.addChild(idlbl);
        titleBar.setBounds(0, 0, titleBar.getChildrenWidth(), titleBar.getChildrenHeight());

//        titlerBar.setToolTipText("<html>" + "Unresolved object!");
//        Titlebar.setAlignmentX(LEFT_ALIGNMENT);
        addChild(titleBar);

//        setOpaque(true);
//        setBackground(Theme.getCurrentTheme().Object_Zombie_Background);
        setLayout(VERTICAL_CENTERED);
        InstanceLabel = new PLabelComponent(model.getInstanceName());
//        InstanceLabel.setAlignmentX(LEFT_ALIGNMENT);
//TODO
//        InstanceLabel.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    addInstanceNameEditor();
//                }
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
        addChild(InstanceLabel);
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
                //TODO
//                getPatchView().ShowClassSelector(PAxoObjectInstanceViewZombie.this.getLocation(), PAxoObjectInstanceViewZombie.this, null);
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
}
