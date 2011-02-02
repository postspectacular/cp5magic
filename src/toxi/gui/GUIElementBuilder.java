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
import java.util.List;
import java.util.logging.Logger;

import toxi.geom.Vec2D;
import controlP5.Controller;

public interface GUIElementBuilder {

    public static final Logger logger = Logger
            .getLogger(GUIElementBuilder.class.getName());

    /**
     * @param context
     *            the instance the variable to be controlled is belonging to
     * @param field
     *            reference to the actual variable field within the context
     *            object
     * @param pos
     *            screen position for the controller
     * @param id
     *            controller ID
     * @param label
     *            controller label
     * @param gui
     *            ControlP5 parent instance
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public List<Controller> createElementsFor(final Object context,
            final Field field, Vec2D pos, String id, String label,
            GUIManager gui) throws IllegalArgumentException,
            IllegalAccessException;

    public int getMinSpacing();
}
