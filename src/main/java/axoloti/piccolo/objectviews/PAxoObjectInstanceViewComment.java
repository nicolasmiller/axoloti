package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstanceComment;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import components.piccolo.PLabelComponent;

public class PAxoObjectInstanceViewComment extends PAxoObjectInstanceViewAbstract {

    AxoObjectInstanceComment model;

    public PAxoObjectInstanceViewComment(AxoObjectInstanceComment model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setDrawBorder(true);

        if (InstanceName != null) {
            model.setCommentText(InstanceName);
            InstanceName = null;
        }
        setLayout(HORIZONTAL_CENTERED);
        InstanceLabel = new PLabelComponent(model.getCommentText());
        // TODO
//        InstanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
//        InstanceLabel.setAlignmentX(CENTER_ALIGNMENT);
//        InstanceLabel.addInputEventListener(new PBasicInputEventHandler() {
//            @Override
//            public void mouseClicked(PInputEvent me) {
//                if (me.getClickCount() == 2) {
//                    addInstanceNameEditor();
//                }
//                if (getPatchView() != null) {
//                    if (me.getClickCount() == 1) {
//                        if (me.isShiftDown()) {
//                            setSelected(!isSelected());
//                        } else if (isSelected() == false) {
//                            getPatchView().SelectNone();
//                            setSelected(true);
//                        }
//                    }
//                }
//            }

//            @Override
//            public void mousePressed(PInputEvent me) {
//                PAxoObjectInstanceViewComment.this.mousePressed(me);
//            }
//
//            @Override
//            public void mouseReleased(PInputEvent e) {
//                PAxoObjectInstanceViewComment.this.mouseReleased(e);
//            }
//    }
        addChild(InstanceLabel);

        setBounds(
                0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());

        resizeToGrid();
    }

    @Override

    public void addInstanceNameEditor() {
//        InstanceNameTF = new TextFieldComponent(model.getCommentText());
//        InstanceNameTF.selectAll();
////        InstanceNameTF.setInputVerifier(new AxoObjectInstanceNameVerifier());
//        InstanceNameTF.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                String s = InstanceNameTF.getText();
//                setInstanceName(s);
//                getParent().remove(InstanceNameTF);
//            }
//        });
//        InstanceNameTF.addFocusListener(new FocusListener() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String s = InstanceNameTF.getText();
//                setInstanceName(s);
//                getParent().remove(InstanceNameTF);
//                getParent().repaint();
//            }
//
//            @Override
//            public void focusGained(FocusEvent e) {
//            }
//        });
//        InstanceNameTF.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent ke) {
//            }
//
//            @Override
//            public void keyReleased(KeyEvent ke) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent ke) {
//                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
//                    String s = InstanceNameTF.getText();
//                    setInstanceName(s);
//                    getParent().remove(InstanceNameTF);
//                    getParent().repaint();
//                }
//            }
//        });
//        getParent().add(InstanceNameTF, 0);
//        InstanceNameTF.setLocation(getLocation().x, getLocation().y + InstanceLabel.getLocation().y);
//        InstanceNameTF.setSize(getWidth(), 15);
//        InstanceNameTF.setVisible(true);
//        InstanceNameTF.requestFocus();
    }

    @Override
    public void setInstanceName(String s) {
        model.setCommentText(s);
        if (InstanceLabel != null) {
            InstanceLabel.setText(model.getCommentText());
        }
        //    doLayout();
        if (getParent() != null) {
            getParent().repaint();
        }
        resizeToGrid();
    }
}
