/*
 * Copyright (c) 2010 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package toxi.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import toxi.geom.Vec2D;
import controlP5.Controller;
import controlP5.Radio;

public class RadioBuilder implements GUIElementBuilder {

    private int itemCount;

    public List<Controller> createElementsFor(Object context, Field field,
            Vec2D pos, String id, String label, GUIManager gui)
            throws IllegalArgumentException, IllegalAccessException {
        Radio r = gui.getGUI().addRadio(id, (int) pos.x, (int) pos.y);
        r.setBroadcast(false);
        itemCount = 0;
        for (Iterator<?> i = ((Collection<?>) field.get(context)).iterator(); i
                .hasNext();) {
            r.addItem(i.next().toString(), itemCount++);
        }
        r.setBroadcast(true);
        r.setLabel(label);
        List<Controller> controllers = new ArrayList<Controller>(1);
        controllers.add(r);
        return controllers;
    }

    public Vec2D getMinSpacing() {
        return new Vec2D(100, (itemCount + 1) * 16);
    }

}
