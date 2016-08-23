package components.piccolo.control;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PatchPNode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventListener;

public abstract class PCtrlComponentAbstract extends PatchPNode {

    static final Stroke strokeThin = new BasicStroke(1);
    static final Stroke strokeThick = new BasicStroke(2);

    protected IAxoObjectInstanceView axoObjectView;
    protected Color customBackgroundColor;
    protected PInputEventListener inputEventListener = new PBasicInputEventHandler() {
        @Override
        public void keyboardFocusGained(PInputEvent event) {
            PCtrlComponentAbstract.this.keyboardFocusGained(event);
        }

        @Override
        public void keyboardFocusLost(PInputEvent event) {
            PCtrlComponentAbstract.this.keyboardFocusLost(event);
        }

        @Override
        public void mousePressed(PInputEvent event) {
            PCtrlComponentAbstract.this.mousePressed(event);
        }

        @Override
        public void mouseReleased(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseReleased(event);
        }

        @Override
        public void mouseDragged(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseDragged(event);
        }

        @Override
        public void keyTyped(PInputEvent event) {
            PCtrlComponentAbstract.this.keyTyped(event);
        }

        @Override
        public void keyPressed(PInputEvent event) {
            PCtrlComponentAbstract.this.keyPressed(event);
        }

        @Override
        public void keyReleased(PInputEvent event) {
            PCtrlComponentAbstract.this.keyReleased(event);

        }

        @Override
        public void mouseEntered(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseEntered(event);
        }

        @Override
        public void mouseExited(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseExited(event);
        }

        @Override
        public void mouseMoved(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseMoved(event);
        }

        @Override
        public void mouseClicked(PInputEvent event) {
            PCtrlComponentAbstract.this.mouseClicked(event);
        }
    };

    public PCtrlComponentAbstract(IAxoObjectInstanceView view) {
        super(view.getPatchView());
        this.axoObjectView = view;
        addInputEventListener(inputEventListener);
    }

    public void grabFocus() {
        this.getRoot().getDefaultInputManager().setKeyboardFocus(inputEventListener);
    }

    public boolean isFocusOwner() {
        return this.getRoot().getDefaultInputManager().getKeyboardFocus() == inputEventListener;
    }

    abstract public double getValue();

    abstract public void setValue(double value);

    void mouseDragged(PInputEvent e) {

    }

    void mousePressed(PInputEvent e) {

    }

    void mouseReleased(PInputEvent e) {

    }

    void mouseEntered(PInputEvent e) {

    }

    void mouseExited(PInputEvent e) {

    }

    void mouseMoved(PInputEvent e) {

    }

    void mouseClicked(PInputEvent e) {

    }

    void keyPressed(PInputEvent ke) {

    }

    void keyReleased(PInputEvent ke) {

    }

    void keyTyped(PInputEvent ke) {

    }

    void keyboardFocusGained(PInputEvent event) {
        repaint();
    }

    void keyboardFocusLost(PInputEvent event) {
        repaint();
    }

    List<PCtrlListener> listenerList = new ArrayList<>();

// TODO fix avoid undo adjustment stuff
    public void addPCtrlListener(PCtrlListener listener) {
        listenerList.add(listener);
    }

    public void removeACtrlListener(PCtrlListener listener) {
        listenerList.remove(listener);
    }

    void fireEvent() {
        for (PCtrlListener listener : listenerList) {
            listener.PCtrlAdjusted(new PCtrlEvent(this, getValue()));
        }
    }

    void fireEventAdjustmentBegin() {
        for (PCtrlListener listener : listenerList) {
            listener.PCtrlAdjustmentBegin(new PCtrlEvent(this, getValue()));
        }
    }

    void fireEventAdjustmentFinished() {
        for (PCtrlListener listener : listenerList) {
            listener.PCtrlAdjustmentFinished(new PCtrlEvent(this, getValue()));
        }
    }
// TODO What is this used for?
//    void SetupTransferHandler() {
//        TransferHandler TH = new TransferHandler() {
//            @Override
//            public int getSourceActions(JComponent c) {
//                return TransferHandler.COPY_OR_MOVE;
//            }
//
//            @Override
//            public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
//                System.out.println("export to clip " + Double.toString(getValue()));
//                clip.setContents(new StringSelection(Double.toString(getValue())), (ClipboardOwner) null);
//            }
//
//            @Override
//            public boolean importData(TransferHandler.TransferSupport support) {
//                return super.importData(support);
//            }
//
//            @Override
//            public boolean importData(JComponent comp, Transferable t) {
//                if (isEnabled()) {
//                    try {
//                        String s = (String) t.getTransferData(DataFlavor.stringFlavor);
//                        System.out.println("paste on control: " + s);
//                        setValue(Double.parseDouble(s));
//                    } catch (UnsupportedFlavorException ex) {
//                        Logger.getLogger(NumberBoxComponent.class.getName()).log(Level.SEVERE, "paste", ex);
//                    } catch (IOException ex) {
//                        Logger.getLogger(NumberBoxComponent.class.getName()).log(Level.SEVERE, "paste", ex);
//                    }
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            protected Transferable createTransferable(JComponent c) {
//                System.out.println("createTransferable");
//                return new StringSelection("copy");
//            }
//        };
//        setTransferHandler(TH);
//        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,
//                KeyUtils.CONTROL_OR_CMD_MASK), "cut");
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,
//                KeyUtils.CONTROL_OR_CMD_MASK), "copy");
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,
//                KeyUtils.CONTROL_OR_CMD_MASK), "paste");
//
//        ActionMap map = getActionMap();
//        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
//                TransferHandler.getCutAction());
//        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
//                TransferHandler.getCopyAction());
//        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
//                TransferHandler.getPasteAction());
//
//    }

    public void robotMoveToCenter(PInputEvent e) {

    }

    public void setCustomBackgroundColor(Color c) {
        this.customBackgroundColor = c;
    }
}
