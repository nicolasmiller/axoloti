/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attributeviews;

import axoloti.attribute.AttributeInstanceSDFile;
import axoloti.objectviews.AxoObjectInstanceView;
import axoloti.utils.Constants;
import components.ButtonComponent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceViewSDFile extends AttributeInstanceViewString {

    AttributeInstanceSDFile attributeInstance;

    JTextField TFFileName;
    JLabel vlabel;
    ButtonComponent ButtonChooseFile;

    public AttributeInstanceViewSDFile(AttributeInstanceSDFile attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFFileName = new JTextField(attributeInstance.getString());
        Dimension d = TFFileName.getSize();
        d.width = 128;
        d.height = 22;
        TFFileName.setFont(Constants.FONT);
        TFFileName.setMaximumSize(d);
        TFFileName.setMinimumSize(d);
        TFFileName.setPreferredSize(d);
        TFFileName.setSize(d);
        add(TFFileName);
        TFFileName.addKeyListener(new KeyListener() {
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
        TFFileName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                attributeInstance.setString(TFFileName.getText());
            }
        });
        TFFileName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                attributeInstance.setString(TFFileName.getText());
            }
        });
        ButtonChooseFile = new ButtonComponent("choose");
        ButtonChooseFile.addActListener(new ButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                JFileChooser fc = new JFileChooser(attributeInstance.getObjectInstance().getPatchModel().GetCurrentWorkingDirectory());
                int returnVal = fc.showOpenDialog(getPatchView().getPatchController().getPatchFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String f = attributeInstance.toRelative(fc.getSelectedFile());
                    TFFileName.setText(f);
                    attributeInstance.fileName = f;
                }
            }
        });
        add(ButtonChooseFile);
    }

    @Override
    public void Lock() {
        if (TFFileName != null) {
            TFFileName.setEnabled(false);
        }
        if (ButtonChooseFile != null) {
            ButtonChooseFile.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFFileName != null) {
            TFFileName.setEnabled(true);
        }
        if (ButtonChooseFile != null) {
            ButtonChooseFile.setEnabled(true);
        }
    }

    @Override
    public String getString() {
        return attributeInstance.getString();
    }

    @Override
    public void setString(String tableName) {
        attributeInstance.setString(tableName);
        if (TFFileName != null) {
            TFFileName.setText(tableName);
        }
    }
}
