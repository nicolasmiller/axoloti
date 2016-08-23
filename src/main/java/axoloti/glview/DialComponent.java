package axoloti.glview;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;

public class DialComponent extends AxoComponent {

    private double value = 0;
    private double max = 127;
    private double min = 0;
    private double tick = 1;
    private Robot robot;

    private LWJGLTest canvas;

    public DialComponent(GUIContext container, int x, int y,
            int width, int height, LWJGLTest canvas) {
        super(container, x, y,
                width, height, canvas);
        this.canvas = canvas;
        try {
            robot = new Robot(MouseInfo.getPointerInfo().getDevice());
        } catch (AWTException ex) {
            Logger.getLogger(DialComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        super.mouseDragged(oldx, oldy, newx, newy);
        if (over) {
            double y_screen = MouseInfo.getPointerInfo().getLocation().getY();
            this.robotMoveToCenter();
            double t = tick;
            double v = value + t * (
                    (int) Math.round((MousePressedCoordY - y_screen) / canvas.scale));
            setValue(v);
        }
    }
    int MousePressedCoordX = 0;
    int MousePressedCoordY = 0;
    int MousePressedBtn = MouseEvent.NOBUTTON;

    @Override
    public void mousePressed(int button, int mx, int my) {
        super.mousePressed(button, mx, my);
        if (over) {
            MousePressedCoordX = (int) MouseInfo.getPointerInfo().getLocation().getX();
            MousePressedCoordY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        }
    }

    public void setValue(double value) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        this.value = value;

    }

    final int layoutTick = 3;

    int radius = Math.min(getWidth(), getHeight()) / 2 - layoutTick;
    int diameter = 2 * radius;
    int diameter_minus_2 = diameter - 2;
    Double lastValue;
    double th = 0.75 * Math.PI + (value - min) * (1.5 * Math.PI) / (max - min);
    int x = (int) (Math.cos(th) * radius),
            y = (int) (Math.sin(th) * radius);

    @Override
    public void render(GUIContext container, org.newdawn.slick.Graphics g) {
        g.setAntiAlias(true);
        g.setColor(Color.black);
        g.pushTransform();
        g.translate(this.getX(), this.getY());

        g.setLineWidth(3 * canvas.scale);

        // left diag
//        g.drawLine(radius, radius, 0, diameter);
// right diag
//        g.drawLine(radius, radius, diameter, diameter);
//        if (isFocusOwner()) {
//            g2.setStroke(strokeThick);
//        } else {
//            g2.setStroke(strokeThin);
//        }
//        if (isEnabled()) {
//            if (this.customBackgroundColor != null) {
//                g2.setColor(this.customBackgroundColor);
//            } else {
//                g2.setColor(Theme.getCurrentTheme().Component_Secondary);
//
//            }
//        } else {
//            g2.setColor(Theme.getCurrentTheme().Object_Default_Background);
//        }
        g.setColor(Color.white);
        //background
        g.fillOval(1, 1, diameter_minus_2, diameter_minus_2);
        g.setColor(Color.black);
        g.drawOval(1, 1, diameter_minus_2, diameter_minus_2);
        if (lastValue == null || value != lastValue) {
            th = 0.75 * Math.PI + (value - min) * (1.5 * Math.PI) / (max - min);
            x = (int) (Math.cos(th) * radius);
            y = (int) (Math.sin(th) * radius);
        }

        g.drawLine(radius, radius, radius + x, radius + y);
//            if (keybBuffer.isEmpty()) {
//                String s = String.format("%5.2f", value);
//                g2.setFont(Constants.FONT);
//                g2.drawString(s, 0, getSize().height);
//            } else {
//                g2.setColor(Theme.getCurrentTheme().Error_Text);
//                g2.setFont(Constants.FONT);
//                g2.drawString(keybBuffer, 0, getSize().height);
//            }
        g.setAntiAlias(false);
        g.popTransform();
        g.setLineWidth(1);
        lastValue = value;
    }

    public void robotMoveToCenter() {
        //getRootPane().setCursor(MainFrame.transparentCursor);
        robot.mouseMove(MousePressedCoordX, MousePressedCoordY);
    }
}
