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
import java.util.List;

import toxi.geom.Vec2D;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.Controller;
import controlP5.Toggle;

public class CheckboxBuilder implements GUIElementBuilder {

    public List<Controller> createElementsFor(final Object context,
            final Field field, GUIElement anno, Vec2D pos, String id,
            String label, GUIManager gui) throws IllegalArgumentException,
            IllegalAccessException {
        boolean state = field.getBoolean(context);
        Toggle ctrl =
                gui.getGUI().addToggle(id, state, (int) pos.x, (int) pos.y, 14,
                        14);
        ctrl.setLabel(label);
        ctrl.addListener(new ControlListener() {

            public void controlEvent(ControlEvent e) {
                try {
                    field.setBoolean(context, e.value() > 0);
                } catch (IllegalArgumentException e1) {
                } catch (IllegalAccessException e1) {
                }
            }
        });
        List<Controller> controllers = new ArrayList<Controller>(1);
        controllers.add(ctrl);
        return controllers;
    }

    public int getMinSpacing() {
        return 40;
    }

}
