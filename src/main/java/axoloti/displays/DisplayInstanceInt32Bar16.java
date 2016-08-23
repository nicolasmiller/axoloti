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
package axoloti.displays;

import axoloti.MainFrame;
import static axoloti.PatchViewType.PICCOLO;
import axoloti.displayviews.DisplayInstanceViewInt32Bar16;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.piccolo.displayviews.PDisplayInstanceViewInt32Bar16;

/**
 *
 * @author Johannes Taelman
 */
public class DisplayInstanceInt32Bar16 extends DisplayInstanceInt32<DisplayInt32Bar16> {

    public DisplayInstanceInt32Bar16() {
        super();
    }

    @Override
    public IDisplayInstanceView getViewInstance() {
        if (MainFrame.prefs.getPatchViewType() == PICCOLO) {
            return new PDisplayInstanceViewInt32Bar16(this);
        } else {
            return new DisplayInstanceViewInt32Bar16(this);
        }
    }
}
