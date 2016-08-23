package axoloti.piccolo;

import axoloti.INetView;
import axoloti.Net;
import axoloti.PatchViewPiccolo;
import axoloti.Theme;
import axoloti.inlets.IInletInstanceView;
import axoloti.outlets.IOutletInstanceView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import org.piccolo2d.util.PPaintContext;

public class PNetView extends PatchPNode implements INetView {

    private final ArrayList<IOutletInstanceView> source = new ArrayList<>();
    private final ArrayList<IInletInstanceView> dest = new ArrayList<>();

    Net net;
    boolean selected = false;

    PatchViewPiccolo patchView;

    public PNetView(Net net, PatchViewPiccolo patchView) {
        this.net = net;
        this.patchView = patchView;
        this.setPickable(false);
    }

    @Override
    public void PostConstructor() {
        this.net.PostConstructor();
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected == selected) {
            return;
        }
        this.selected = selected;
        for (IOutletInstanceView i : source) {
            i.setHighlighted(selected);
        }
        for (IInletInstanceView i : dest) {
            i.setHighlighted(selected);
        }
        repaint();
    }

    @Override
    public boolean getSelected() {
        return this.selected;
    }

    final static float[] dash = {2.f, 4.f};
    final static Stroke strokeValidSelected = new BasicStroke(1.75f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    final static Stroke strokeValidDeselected = new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    final static Stroke strokeBrokenSelected = new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0.f);
    final static Stroke strokeBrokenDeselected = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0.f);
    final QuadCurve2D.Float curve = new QuadCurve2D.Float();

    float CtrlPointY(float x1, float y1, float x2, float y2) {
        return Math.max(y1, y2) + Math.abs(y2 - y1) * 0.1f + Math.abs(x2 - x1) * 0.1f;
    }

    void DrawWire(Graphics2D g2, float x1, float y1, float x2, float y2) {
        curve.setCurve(x1, y1, (x1 + x2) / 2, CtrlPointY(x1, y1, x2, y2), x2, y2);
        g2.draw(curve);
    }

    @Override
    public void connectInlet(IInletInstanceView inlet) {
        if (inlet == null) {
            throw new RuntimeException("Cannot connect a null InletInstanceView to a NetView.");
        }
        dest.add(inlet);
    }

    @Override
    public void connectOutlet(IOutletInstanceView outlet) {
        if (outlet == null) {
            throw new RuntimeException("Cannot connect a null OutInstanceView to a NetView.");
        }
        source.add(outlet);
    }

    @Override
    public void updateBounds() {
        int min_y = Integer.MAX_VALUE;
        int min_x = Integer.MAX_VALUE;
        int max_y = Integer.MIN_VALUE;
        int max_x = Integer.MIN_VALUE;

        for (IInletInstanceView i : dest) {
            Point p1 = i.getJackLocInCanvas();
            min_x = Math.min(min_x, p1.x);
            min_y = Math.min(min_y, p1.y);
            max_x = Math.max(max_x, p1.x);
            max_y = Math.max(max_y, p1.y);
        }
        for (IOutletInstanceView i : source) {
            Point p1 = i.getJackLocInCanvas();
            min_x = Math.min(min_x, p1.x);
            min_y = Math.min(min_y, p1.y);
            max_x = Math.max(max_x, p1.x);
            max_y = Math.max(max_y, p1.y);
        }
        setBounds(min_x, min_y, Math.max(1, max_x - min_x),
                (int) CtrlPointY(min_x, min_y, max_x, max_y) - min_y);
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        float shadowOffset = 0.5f;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Point p0;
        Color c;
        if (net.isValidNet()) {
            if (selected) {
                g2.setStroke(strokeValidSelected);
            } else {
                g2.setStroke(strokeValidDeselected);
            }

            c = net.getDataType().GetColor();
            p0 = source.get(0).getJackLocInCanvas();
        } else {
            if (selected) {
                g2.setStroke(strokeBrokenSelected);
            } else {
                g2.setStroke(strokeBrokenDeselected);
            }

            if (net.getDataType() != null) {
                c = net.getDataType().GetColor();
            } else {
                c = Theme.getCurrentTheme().Cable_Shadow;
            }

            if (!source.isEmpty()) {
                p0 = source.get(0).getJackLocInCanvas();
            } else if (!dest.isEmpty()) {
                p0 = dest.get(0).getJackLocInCanvas();
            } else {
                throw new Error("empty nets should not exist");
            }
        }

        Point2D from = globalToLocal(p0);
        for (IInletInstanceView i : dest) {
            Point p1 = i.getJackLocInCanvas();

            Point2D to = this.globalToLocal(p1);

// TODO fix shadows
//            g2.setColor(Theme.getCurrentTheme().Cable_Shadow);
//            DrawWire(g2, (int) from.getX() + shadowOffset, (int) from.getY() + shadowOffset, (int) to.getX() + shadowOffset, (int) to.getY() + shadowOffset);
            g2.setColor(c);
            DrawWire(g2, (int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
        }
        for (IOutletInstanceView i : source) {
            Point p1 = i.getJackLocInCanvas();

            Point2D to = globalToLocal(p1);
//            g2.setColor(Theme.getCurrentTheme().Cable_Shadow);
//            DrawWire(g2, (int) from.getX() + shadowOffset, (int) from.getY() + shadowOffset, (int) to.getX() + shadowOffset, (int) to.getY() + shadowOffset);
            g2.setColor(c);
            DrawWire(g2, (int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
        }
    }

    @Override
    public Net getNet() {
        return this.net;
    }

    @Override
    public ArrayList<IOutletInstanceView> getSourceViews() {
        return source;
    }

    @Override
    public ArrayList<IInletInstanceView> getDestinationViews() {
        return dest;
    }
}
