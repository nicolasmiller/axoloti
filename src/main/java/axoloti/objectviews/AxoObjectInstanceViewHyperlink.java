/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.objectviews;

import axoloti.object.AxoObjectInstanceHyperlink;
import axoloti.objectviews.AxoObjectInstanceView;
import components.LabelComponent;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.control.PulseButtonComponent;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstanceViewHyperlink extends AxoObjectInstanceView {

    AxoObjectInstanceHyperlink model;
    private PulseButtonComponent button;

    public AxoObjectInstanceViewHyperlink(AxoObjectInstanceHyperlink model) {
        super(model);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        button = new PulseButtonComponent();
        button.addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                if (e.getValue() == 1.0) {
                    model.Launch();
                }
            }
        });
        add(button);
        add(Box.createHorizontalStrut(5));
        InstanceLabel = new LabelComponent(model.getInstanceName());
        InstanceLabel.setAlignmentX(LEFT_ALIGNMENT);
        InstanceLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                }
                if (getPatchView() != null) {
                    if (e.getClickCount() == 1) {
                        if (e.isShiftDown()) {
                            SetSelected(!isSelected());
                        } else if (selected == false) {
                            getPatchView().SelectNone();
                            SetSelected(true);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ml.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ml.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        InstanceLabel.addMouseMotionListener(mml);
        add(InstanceLabel);

        resizeToGrid();
    }
    
    @Override
    public void setInstanceName(String s) {
        super.setInstanceName(s);
        resizeToGrid();
    }
}
