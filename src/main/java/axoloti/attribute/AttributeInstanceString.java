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
package axoloti.attribute;

import axoloti.attributedefinition.AxoAttribute;
import axoloti.attributeviews.AttributeInstanceViewString;
import axoloti.object.AxoObjectInstance;
import axoloti.objectviews.AxoObjectInstanceView;

/**
 *
 * @author Johannes Taelman
 */
public abstract class AttributeInstanceString<T extends AxoAttribute> extends AttributeInstance<T> {

    public AttributeInstanceString() {
    }

    public AttributeInstanceString(T param, AxoObjectInstance axoObj1) {
        super(param, axoObj1);
    }

    public abstract String getString();

    public abstract void setString(String s);

    @Override
    public void CopyValueFrom(AttributeInstance a) {
        if (a instanceof AttributeInstanceString) {
            AttributeInstanceString a1 = (AttributeInstanceString) a;
            setString(a1.getString());
        }
    }
    
    @Override
    public AttributeInstanceViewString ViewFactory(AxoObjectInstanceView o) {
        throw new RuntimeException("Cannot instantiate AttributeInstanceStringView");
    }
    
    private String valueBeforeAdjustment = "";

    
    public void setValueBeforeAdjustment(String valueBeforeAdjustment) {
        this.valueBeforeAdjustment = valueBeforeAdjustment;
    }
    
    public String getValueBeforeAdjustment() {
        return valueBeforeAdjustment;
    }
}
