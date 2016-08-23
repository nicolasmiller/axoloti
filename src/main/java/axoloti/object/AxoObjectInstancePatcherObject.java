/**
 * Copyright (C) 2013 - 2016 Johannes Taelman
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
package axoloti.object;

import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.PatchViewPiccolo;
import axoloti.PatchViewSwing;
import axoloti.objectviews.AxoObjectInstanceViewPatcherObject;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewPatcherObject;
import java.awt.Point;
import org.simpleframework.xml.Element;

/**
 *
 * @author Johannes Taelman
 */
public class AxoObjectInstancePatcherObject extends AxoObjectInstance {

    @Element(name = "object")
    AxoObjectPatcherObject ao;

    public AxoObjectInstancePatcherObject() {
    }

    public AxoObjectInstancePatcherObject(AxoObject type, PatchModel patch1, String InstanceName1, Point location) {
        super(type, patch1, InstanceName1, location);
    }

    @Override
    public void updateObj1() {
        if (ao == null) {
            ao = new AxoObjectPatcherObject();
            ao.id = "patch/object";
            ao.sDescription = "";
        }
        setType(ao);
        /*
         if (pg != null) {
         AxoObject ao = pg.GenerateAxoObj();
         setType(ao);
         pg.container(patch);
         }
         */
    }

    @Override
    public IAxoObjectInstanceView getViewInstance(PatchView patchView) {
        if (patchView instanceof PatchViewPiccolo) {
            return new PAxoObjectInstanceViewPatcherObject(this, (PatchViewPiccolo) patchView);
        } else {
            return new AxoObjectInstanceViewPatcherObject(this, (PatchViewSwing) patchView);
        }
    }

    public AxoObject getAxoObject() {
        return ao;
    }

    public void setAxoObject(AxoObjectPatcherObject axoObject) {
        this.ao = axoObject;
    }
}
