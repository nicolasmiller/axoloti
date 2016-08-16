/**
 * Copyright (C) 2013, 2014, 2015 Johannes Taelman
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
package axoloti.outlets;

import axoloti.atom.AtomInstance;
import axoloti.datatypes.DataType;
import axoloti.object.AxoObjectInstance;
import org.simpleframework.xml.*;

/**
 *
 * @author Johannes Taelman
 */
@Root(name = "source")
public class OutletInstance<T extends Outlet> implements Comparable<OutletInstance>, AtomInstance<T> {

    @Attribute(name = "outlet", required = false)
    public String outletname;    
    @Deprecated
    @Attribute(required = false)
    public String name;
    @Attribute(name = "obj", required = false)
    public String objname;

    private final T outlet;

    private AxoObjectInstance axoObj;

    @Override
    public AxoObjectInstance getObjectInstance() {
        return this.axoObj;
    }

    public String getOutletname() {
        return outletname;

    }

    @Override
    public T getDefinition() {
        return outlet;
    }

    public OutletInstance() {
        this.outlet = null;
        this.axoObj = null;
    }

    public OutletInstance(T outlet, AxoObjectInstance axoObj) {
        this.outlet = outlet;
        this.axoObj = axoObj;
    }

    public DataType GetDataType() {
        return outlet.getDatatype();
    }

    public String GetLabel() {
        return outlet.name;
    }

    public String GetCName() {
        return outlet.GetCName();
    }

    @Override
    public int compareTo(OutletInstance t) {
        return axoObj.compareTo(t.axoObj);
    }

    public Outlet getOutlet() {
        return outlet;
    }
    
    public void RefreshName() {
        name = axoObj.getInstanceName() + " " + outlet.name;
        objname = axoObj.getInstanceName();
        outletname = outlet.name;
        name = null;
    }
    
    public String getObjname() {
        return this.objname;
    }
}
