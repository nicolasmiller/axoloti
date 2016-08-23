/**
 * Copyright (C) 2013, 2014 Johannes Taelman
 *
 * This file is part of Axoloti.
 *
 * Axoloti is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Axoloti is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Axoloti. If not, see <http://www.gnu.org/licenses/>.
 */
package components.piccolo;

import axoloti.Theme;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.parameterviews.PParameterInstanceViewFrac32UMap;
import axoloti.utils.Constants;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PPaintContext;

/**
 *
 * @author Johannes Taelman
 */
public class PAssignMidiCCComponent extends PatchPNode {

    private static final Dimension dim = new Dimension(16, 12);

    PParameterInstanceViewFrac32UMap parameterInstanceView;

    public PAssignMidiCCComponent(PParameterInstanceViewFrac32UMap param) {
        super(param.getPatchView());
        setBounds(0, 0, dim.width, dim.height);
        this.parameterInstanceView = param;
        this.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                doPopup();
                e.setHandled(true);
            }
        });
    }

    void doPopup() {
        JPopupMenu sub1 = new JPopupMenu();
        PAssignMidiCCMenuItems assignMidiCCMenuItems = new PAssignMidiCCMenuItems(parameterInstanceView, sub1);
        sub1.show(parameterInstanceView.getCanvas(), (int) getBounds().x, (int) (getBounds().y + getHeight() - 1));
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        if (parameterInstanceView.getParameterInstance().getMidiCC() >= 0) {
            Graphics2D g2 = paintContext.getGraphics();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(Constants.FONT);
            g2.setColor(Theme.getCurrentTheme().Object_Default_Background);
            g2.fillRect(1, 1, (int) getWidth(), (int) getHeight());
            if (parameterInstanceView.getParameterInstance().getMidiCC() >= 0) {
                g2.setColor(Theme.getCurrentTheme().Component_Primary);
                g2.fillRect(1, 1, 8, (int) getHeight());
                g2.setColor(Theme.getCurrentTheme().Component_Secondary);
            } else {
                g2.setColor(Theme.getCurrentTheme().Component_Primary);
            }
            g2.drawString("C", 1, (int) getHeight() - 2);
            g2.setColor(Theme.getCurrentTheme().Component_Primary);
            final int rmargin = 2;
            final int htick = 2;
            int[] xp = new int[]{(int) getWidth() - rmargin - htick * 2, (int) getWidth() - rmargin, (int) getWidth() - rmargin - htick};
            final int vmargin = 4;
            int[] yp = new int[]{vmargin, vmargin, vmargin + htick * 2};
            g2.fillPolygon(xp, yp, 3);
        }
    }

    public void setCC(int i) {
        repaint();
    }
}
