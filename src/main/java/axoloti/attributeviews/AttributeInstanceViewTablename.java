/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attributeviews;

import axoloti.attribute.AttributeInstanceTablename;
import axoloti.attributeviews.AttributeInstanceViewString;
import axoloti.objectviews.AxoObjectInstanceView;
import axoloti.utils.Constants;
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
public class AttributeInstanceViewTablename extends AttributeInstanceViewString {

    AttributeInstanceTablename attributeInstance;
    JTextField TFtableName;
    JLabel vlabel;

    public AttributeInstanceViewTablename(AttributeInstanceTablename attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFtableName = new JTextField(attributeInstance.tableName);
        Dimension d = TFtableName.getSize();
        d.width = 128;
        d.height = 22;
        TFtableName.setFont(Constants.FONT);
        TFtableName.setMaximumSize(d);
        TFtableName.setMinimumSize(d);
        TFtableName.setPreferredSize(d);
        TFtableName.setSize(d);
        add(TFtableName);
        TFtableName.addKeyListener(new KeyListener() {
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
        TFtableName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                attributeInstance.tableName = TFtableName.getText();
                System.out.println("tablename change " + attributeInstance.tableName);
            }
        });
        TFtableName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                attributeInstance.tableName = TFtableName.getText();
                System.out.println("tablename change " + attributeInstance.tableName);
            }
        });
    }

    @Override
    public void Lock() {
        if (TFtableName != null) {
            TFtableName.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFtableName != null) {
            TFtableName.setEnabled(true);
        }
    }

    @Override
    public String getString() {
        return attributeInstance.tableName;
    }

    @Override
    public void setString(String tableName) {
        attributeInstance.setString(tableName);
        if (TFtableName != null) {
            TFtableName.setText(tableName);
        }
    }
}
