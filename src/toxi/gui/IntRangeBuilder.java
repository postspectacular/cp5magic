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
import toxi.util.datatypes.IntegerRange;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.Controller;
import controlP5.Slider;

public class IntRangeBuilder implements GUIElementBuilder {

    protected static Slider createSlider(GUIManager gui, String id, int min,
            int max, int curr, Vec2D pos, String label, ControlListener listener) {
        Slider s =
                gui.getGUI().addSlider(id, min, max, curr, (int) pos.x,
                        (int) pos.y, 100, 14);
        s.setLabel(label);
        s.addListener(listener);
        return s;
    }

    public List<Controller> createElementsFor(final Object context,
            final Field field, GUIElement anno, Vec2D pos, String id,
            String label, GUIManager gui) throws IllegalArgumentException,
            IllegalAccessException {
        List<Controller> controllers = new ArrayList<Controller>(1);
        IntegerRange r = null;
        boolean singleValue = false;
        if (field.get(context).getClass() == Integer.class) {
            Range ra = field.getAnnotation(Range.class);
            if (ra != null) {
                r = new IntegerRange((int) ra.min(), (int) ra.max());
                r.setCurrent(field.getInt(context));
                singleValue = true;
            }
        } else {
            r = (IntegerRange) field.get(context);
        }
        final IntegerRange range = r;
        final boolean isRange = !singleValue;
        if (range != null) {
            range.setCurrent(field.getInt(context));
            Slider s =
                    createSlider(gui, id, range.min, range.max,
                            range.currValue, pos, label, new ControlListener() {

                                public void controlEvent(ControlEvent e) {
                                    try {
                                        int val = (int) e.value();
                                        if (isRange) {
                                            range.setCurrent(val);
                                        } else {
                                            field.setInt(context, val);
                                        }
                                        e.controller().setValueLabel("" + val);
                                    } catch (IllegalArgumentException e1) {
                                    } catch (IllegalAccessException e1) {
                                    }
                                }
                            });
            controllers.add(s);
        }
        return controllers;
    }

    public int getMinSpacing() {
        return 20;
    }
}
