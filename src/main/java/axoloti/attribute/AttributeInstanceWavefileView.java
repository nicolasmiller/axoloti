/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.object.AxoObjectInstanceView;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceWavefileView extends AttributeInstanceView {

    AttributeInstanceWavefile attributeInstance;
    JTextField TFwaveFilename;
    JLabel vlabel;

    public AttributeInstanceWavefileView(AttributeInstanceWavefile attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFwaveFilename = new JTextField(attributeInstance.waveFilename);
        Dimension d = TFwaveFilename.getSize();
        d.width = 128;
        d.height = 22;
        TFwaveFilename.setMaximumSize(d);
        TFwaveFilename.setMinimumSize(d);
        TFwaveFilename.setPreferredSize(d);
        TFwaveFilename.setSize(d);
        add(TFwaveFilename);
        TFwaveFilename.addKeyListener(new KeyListener() {
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
        TFwaveFilename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                attributeInstance.waveFilename = TFwaveFilename.getText();
            }
        });
    }

    @Override
    public void Lock() {
        if (TFwaveFilename != null) {
            TFwaveFilename.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFwaveFilename != null) {
            TFwaveFilename.setEnabled(true);
        }
    }
}
