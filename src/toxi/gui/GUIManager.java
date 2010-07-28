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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import toxi.geom.Vec2D;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Tab;

public class GUIManager {

    private ControlP5 gui;
    private Vec2D currPos;

    private HashMap<String, Controller> controllers =
            new HashMap<String, Controller>();

    private HashMap<Class<?>, GUIElementBuilder> builders =
            new HashMap<Class<?>, GUIElementBuilder>();

    public GUIManager(ControlP5 gui) {
        this.gui = gui;
    }

    public void addDefaultMappings() {
        addMapping(Boolean.class, new CheckboxBuilder());
        addMapping(Integer.class, new IntegerRangeBuilder());
        addMapping(Float.class, new FloatRangeBuilder());
        addMapping(Collection.class, new RadioBuilder());
        addMapping(String.class, new ButtonBuilder());
    }

    public void addListenerFor(String id, final String name,
            final Object context) {
        Controller ctrl = getForID(id);
        if (ctrl != null) {
            ctrl.addListener(new ControlListener() {

                public void controlEvent(ControlEvent e) {
                    try {
                        Method method =
                                context.getClass().getMethod(name,
                                        ControlEvent.class);
                        method.invoke(context, e);
                    } catch (SecurityException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    public void addMapping(Class<?> c, GUIElementBuilder builder) {
        builders.put(c, builder);
    }

    public void createControllers(Object context, int x, int y, Tab tab) {
        this.currPos = new Vec2D(x, y);
        try {
            for (Field f : context.getClass().getFields()) {
                if (f.isAnnotationPresent(GUIElement.class)) {
                    Class<?> type = f.get(context).getClass();
                    GUIElement a = f.getAnnotation(GUIElement.class);
                    GUIElementBuilder builder = null;
                    if (a.builder() != GUIElementBuilder.class) {
                        builder = a.builder().newInstance();
                    } else {
                        builder = getMappingForType(type);
                    }
                    System.out.println(type+": "+builder);
                    if (builder != null) {
                        String label =
                                !a.label().equals(GUIElement.NO_LABEL) ? a
                                        .label() : f.getName();
                        Vec2D pos = getPositionFor(a);
                        List<Controller> items =
                                builder.createElementsFor(context, f, a, pos,
                                        f.getName(), label, this);
                        for (Controller c : items) {
                            if (tab != null) {
                                c.setTab(tab.name());
                            }
                            registerController(c.name(), c);
                        }
                        if (a.y() == -1) {
                            currPos.y += builder.getMinSpacing();
                        }
                    } else {
                        System.out.println("no mapping found for: " + type);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void createControllers(Object context, Tab tab) {
        int x, y;
        GUIConfiguration config =
                context.getClass().getAnnotation(GUIConfiguration.class);
        if (config != null) {
            x = config.x();
            y = config.y();
        } else {
            x = 20;
            y = 20;
        }
        createControllers(context, x, y, tab);
    }

    public Controller getForID(String id) {
        return controllers.get(id);
    }

    public ControlP5 getGUI() {
        return gui;
    }

    private GUIElementBuilder getMappingForType(Class<?> type) {
        GUIElementBuilder builder = builders.get(type);
        if (builder == null) {
            for (Class<?> c : type.getInterfaces()) {
                builder = builders.get(c);
                if (builder != null) {
                    break;
                } else {
                    Class<?> stype = c.getSuperclass();
                    if (stype != Object.class && stype != null) {
                        builder = getMappingForType(stype);
                    }
                }
            }
            if (builder == null) {
                Class<?> stype = type.getSuperclass();
                if (stype != Object.class && stype != null) {
                    builder = getMappingForType(stype);
                }
            }
        }
        return builder;
    }

    protected Vec2D getPositionFor(GUIElement a) {
        float x = a.x() != -1 ? a.x() : currPos.x;
        float y = a.y() != -1 ? a.y() : currPos.y;
        return new Vec2D(x, y);
    }

    protected void registerController(String id, Controller ctrl) {
        controllers.put(id, ctrl);
    }
}