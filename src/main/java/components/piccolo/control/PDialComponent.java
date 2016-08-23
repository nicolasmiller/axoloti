/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.piccolo.control;

import axoloti.MainFrame;
import axoloti.Theme;
import axoloti.datatypes.ValueFrac32;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PUtils;
import axoloti.realunits.NativeToReal;
import axoloti.utils.Constants;
import axoloti.utils.KeyUtils;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PPaintContext;

public class PDialComponent extends PCtrlComponentAbstract {

    private double value;
    private double max;
    private double min;
    private double tick;
    private NativeToReal convs[];
    private String keybBuffer = "";
    private Robot robot;

    public void setNative(NativeToReal convs[]) {
        this.convs = convs;
    }

    public PDialComponent(double value, double min, double max, double tick, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
        this.value = value;
        this.min = min;
        this.max = max;
        this.tick = tick;
        Dimension d = new Dimension(28, 32);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
        setSize(d);

//  TODO ctrl to ctrl copy/paste
//        SetupTransferHandler();
        try {
            robot = new Robot(MouseInfo.getPointerInfo().getDevice());
        } catch (AWTException ex) {
            Logger.getLogger(PDialComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    final int layoutTick = 3;

    @Override
    public void keyboardFocusGained(PInputEvent e) {
        keybBuffer = "";
    }

    @Override
    public void keyboardFocusLost(PInputEvent e) {
        keybBuffer = "";
    }

    @Override
    protected void mouseDragged(PInputEvent e) {
        if (isEnabled()) {
            double v;
            if ((MousePressedBtn == MouseEvent.BUTTON1)) {
                this.robotMoveToCenter();
                Point screenPosition = PUtils.toScreenCoordinates(e.getCanvasPosition(),
                        getCanvas());
                if (MainFrame.prefs.getMouseDialAngular()) {
                    int radius = (int) Math.min(getSize().width, getSize().height) / 2 - layoutTick;
                    double th = Math.atan2(screenPosition.x - radius, radius - screenPosition.y);
                    v = min + (max - min) * (th + 0.75 * Math.PI) / (1.5 * Math.PI);
                    if (!e.isShiftDown()) {
                        v = Math.round(v / tick) * tick;
                    }
                } else {
                    double t = tick;
                    if (e.isShiftDown() || KeyUtils.isControlOrCommandDown(e)) {
                        t = t * 0.1;
                    }
                    v = value + t * ((int) Math.round((MousePressedCoordY - screenPosition.y)));
                }
                setValue(v);
                e.setHandled(true);
            }
        }
    }
    int MousePressedCoordX = 0;
    int MousePressedCoordY = 0;
    int MousePressedBtn = MouseEvent.NOBUTTON;

    @Override
    protected void mousePressed(PInputEvent e) {
        if (!e.isPopupTrigger()) {
            if (isEnabled()) {
                grabFocus();
                Point screenPosition = PUtils.toScreenCoordinates(e.getCanvasPosition(),
                        getCanvas());
                MousePressedCoordX = screenPosition.x;
                MousePressedCoordY = screenPosition.y;

                int lastBtn = MousePressedBtn;
                MousePressedBtn = e.getButton();

                if (lastBtn != MouseEvent.NOBUTTON) {
                    if (lastBtn == MouseEvent.BUTTON1) {
                        // now have both mouse buttons pressed...
                        e.popCursor();
                    }
                }

                if (MousePressedBtn == MouseEvent.BUTTON1) {
                    e.pushCursor(MainFrame.transparentCursor);
                    fireEventAdjustmentBegin();
                } else {
                    e.popCursor();
                }
            }
            e.setHandled(true);
        }
    }

    @Override
    protected void mouseReleased(PInputEvent e) {
        if (isEnabled() && !e.isPopupTrigger()) {
            e.popCursor();
            MousePressedBtn = MouseEvent.NOBUTTON;
            fireEventAdjustmentFinished();
            e.setHandled(true);
        }
    }

    @Override
    public void keyPressed(PInputEvent ke) {
        if (isEnabled()) {
            double steps = tick;
            if (ke.isShiftDown()) {
                steps = steps * 0.1; // mini steps!
                if (KeyUtils.isControlOrCommandDown(ke)) {
                    steps = steps * 0.1; // micro steps!
                }
            } else if (KeyUtils.isControlOrCommandDown(ke)) {
                steps = steps * 10.0; //accelerate!
            }
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_RIGHT:
                    fireEventAdjustmentBegin();
                    setValue(getValue() + steps);
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                    fireEventAdjustmentBegin();
                    setValue(getValue() - steps);
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_PAGE_UP:
                    fireEventAdjustmentBegin();
                    setValue(getValue() + 5 * steps);
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    fireEventAdjustmentBegin();
                    setValue(getValue() - 5 * steps);
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_HOME:
                    fireEventAdjustmentBegin();
                    setValue(getMin());
                    fireEventAdjustmentFinished();
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_END:
                    fireEventAdjustmentBegin();
                    setValue(getMax());
                    fireEventAdjustmentFinished();
                    ke.setHandled(true);
                    break;
                case KeyEvent.VK_ENTER:
                    fireEventAdjustmentBegin();
                    try {
                        setValue(Float.parseFloat(keybBuffer));
                    } catch (java.lang.NumberFormatException ex) {
                    }
                    fireEventAdjustmentFinished();
                    keybBuffer = "";
                    ke.setHandled(true);
                    repaint();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    if (keybBuffer.length() > 0) {
                        keybBuffer = keybBuffer.substring(0, keybBuffer.length() - 1);
                    }
                    ke.setHandled(true);
                    repaint();
                    break;
                case KeyEvent.VK_ESCAPE:
                    keybBuffer = "";
                    ke.setHandled(true);
                    repaint();
                    break;
                default:
            }
            switch (ke.getKeyChar()) {
                case '-':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                case '.':
                    keybBuffer += ke.getKeyChar();
                    ke.setHandled(true);
                    break;
                default:
            }
            repaint();
        }
    }

    @Override
    void keyReleased(PInputEvent ke) {
        if (isEnabled()) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_PAGE_UP:
                case KeyEvent.VK_PAGE_DOWN:
                    fireEventAdjustmentFinished();
                    ke.setHandled(true);
                    break;
                default:
            }
        }
    }

    private static final Stroke strokeThin = new BasicStroke(1);
    private static final Stroke strokeThick = new BasicStroke(2);

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        paintContext.setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

        int radius = Math.min(getSize().width, getSize().height) / 2 - layoutTick;
        g2.setPaint(getForeground());
        g2.drawLine(radius, radius, 0, 2 * radius);
        g2.drawLine(radius, radius, 2 * radius, 2 * radius);
        if (isFocusOwner()) {
            g2.setStroke(strokeThick);
        } else {
            g2.setStroke(strokeThin);
        }
        if (isEnabled()) {
            if (this.customBackgroundColor != null) {
                g2.setColor(this.customBackgroundColor);
            } else {
                g2.setColor(Theme.getCurrentTheme().Component_Secondary);

            }
        } else {
            g2.setColor(Theme.getCurrentTheme().Object_Default_Background);
        }
        g2.fillOval(1, 1, radius * 2 - 2, radius * 2 - 2);
        g2.setPaint(getForeground());
        g2.drawOval(1, 1, radius * 2 - 2, radius * 2 - 2);
        if (isEnabled()) {
            double th = 0.75 * Math.PI + (value - min) * (1.5 * Math.PI) / (max - min);
            int x = (int) (Math.cos(th) * radius),
                    y = (int) (Math.sin(th) * radius);
            g2.drawLine(radius, radius, radius + x, radius + y);
            if (keybBuffer.isEmpty()) {
                String s = String.format("%5.2f", value);
                g2.setFont(Constants.FONT);
                g2.drawString(s, 0, getSize().height);
            } else {
                g2.setColor(Theme.getCurrentTheme().Error_Text);
                g2.setFont(Constants.FONT);
                g2.drawString(keybBuffer, 0, getSize().height);
            }
        }
        paintContext.setRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
    }

    @Override
    public void setValue(double value) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        this.value = value;

        if (convs != null) {
            String s = "<html>";
            for (NativeToReal c : convs) {
                s += c.ToReal(new ValueFrac32(value)) + "<br>";
            }
            this.setToolTipText(s);
        }
        repaint();
        fireEvent();
    }

    @Override
    public double getValue() {
        return value;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getTick() {
        return tick;
    }

    public void setTick(double tick) {
        this.tick = tick;
    }

    public void robotMoveToCenter() {
        robot.mouseMove(MousePressedCoordX, MousePressedCoordY);
    }
}
