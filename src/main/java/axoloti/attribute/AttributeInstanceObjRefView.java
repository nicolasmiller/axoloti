/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.SubPatchMode;
import axoloti.utils.CharEscape;
import axoloti.utils.Constants;
import components.DropDownComponent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceObjRefView extends AttributeInstanceStringView {
    AttributeInstanceObjRef attributeInstance;
    
    JTextField TFObjName;
    JLabel vlabel;

    AttributeInstanceObjRefView(AttributeInstanceObjRef attributeInstance) {
        super(attributeInstance);
        this.attributeInstance = attributeInstance;
    }
    
        @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFObjName = new JTextField(attributeInstance.objName);
        Dimension d = TFObjName.getSize();
        d.width = 92;
        d.height = 22;
        TFObjName.setFont(Constants.FONT);
        TFObjName.setMaximumSize(d);
        TFObjName.setMinimumSize(d);
        TFObjName.setPreferredSize(d);
        TFObjName.setSize(d);
        add(TFObjName);
        TFObjName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                repaint();
            }
        });
        TFObjName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                attributeInstance.objName = TFObjName.getText();
                System.out.println("objref change " + attributeInstance.objName);
            }
        });
        TFObjName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                attributeInstance.objName = TFObjName.getText();
                System.out.println("objref change " + attributeInstance.objName);
            }
        });
    }



    @Override
    public void Lock() {
        if (TFObjName != null) {
            TFObjName.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFObjName != null) {
            TFObjName.setEnabled(true);
        }
    }

    @Override
    public String getString() {
        return attributeInstance.getString();
    }

    @Override
    public void setString(String objName) {
        attributeInstance.setString(objName);
        if (TFObjName != null) {
            TFObjName.setText(objName);
        }
    }
}
