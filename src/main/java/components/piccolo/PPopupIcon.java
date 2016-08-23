/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.piccolo;

import axoloti.Theme;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PatchPNode;
import java.awt.Dimension;
import java.awt.Graphics2D;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PPaintContext;

/**
 *
 * @author nicolas
 */
public class PPopupIcon extends PatchPNode {

    public interface PopupIconListener {

        public void ShowPopup(PInputEvent e);
    }

    private PopupIconListener pl;

    private final Dimension minsize = new Dimension(10, 12);
    private final Dimension maxsize = new Dimension(10, 12);

    public PPopupIcon(IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView.getPatchView());
        setMinimumSize(minsize);
        setPreferredSize(maxsize);
        setMaximumSize(maxsize);
        setSize(minsize);
        this.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mousePressed(PInputEvent e) {
                pl.ShowPopup(e);
            }
        });
    }

    public void setPopupIconListener(PopupIconListener pl) {
        this.pl = pl;
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        g2.setColor(Theme.getCurrentTheme().Component_Primary);
        final int rmargin = 3;
        final int htick = 3;
        int[] xp = new int[]{(int) (getBoundsReference().width - rmargin - htick * 2),
            (int) (getBoundsReference().width - rmargin),
            (int) (getBoundsReference().width - rmargin - htick)};
        final int vmargin = 3;
        int[] yp = new int[]{vmargin, vmargin, vmargin + htick * 2};
        if (isEnabled()) {
            g2.fillPolygon(xp, yp, 3);
        } else {
            g2.drawPolygon(xp, yp, 3);
        }
    }
}
