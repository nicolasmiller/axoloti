package axoloti.objectviews;

import axoloti.object.AxoObjectInstanceComment;
import axoloti.objectviews.AxoObjectInstanceView;
import components.LabelComponent;
import components.TextFieldComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

public class AxoObjectInstanceViewComment extends AxoObjectInstanceView {

    AxoObjectInstanceComment model;

    public AxoObjectInstanceViewComment(AxoObjectInstanceComment model) {
        super(model);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        if (InstanceName != null) {
            model.setCommentText(InstanceName);
            InstanceName = null;
        }
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        InstanceLabel = new LabelComponent(model.getCommentText());
        InstanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        InstanceLabel.setAlignmentX(CENTER_ALIGNMENT);
        InstanceLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    addInstanceNameEditor();
                }
                if (getPatchView() != null) {
                    if (me.getClickCount() == 1) {
                        if (me.isShiftDown()) {
                            SetSelected(!isSelected());
                        } else if (selected == false) {
                            getPatchView().SelectNone();
                            SetSelected(true);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                ml.mousePressed(me);
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
        setLocation(x, y);

        resizeToGrid();
    }

    @Override
    public void addInstanceNameEditor() {
        InstanceNameTF = new TextFieldComponent(model.getCommentText());
        InstanceNameTF.selectAll();
//        InstanceNameTF.setInputVerifier(new AxoObjectInstanceNameVerifier());
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
                getParent().repaint();
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
                    getParent().repaint();
                }
            }
        });
        getParent().add(InstanceNameTF, 0);
        InstanceNameTF.setLocation(getLocation().x, getLocation().y + InstanceLabel.getLocation().y);
        InstanceNameTF.setSize(getWidth(), 15);
        InstanceNameTF.setVisible(true);
        InstanceNameTF.requestFocus();
    }

    @Override
    public void setInstanceName(String s) {
        model.setCommentText(s);
        if (InstanceLabel != null) {
            InstanceLabel.setText(model.getCommentText());
        }
        revalidate();
        if (getParent() != null) {
            getParent().repaint();
        }
        resizeToGrid();
    }
}
