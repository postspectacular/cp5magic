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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import processing.core.PApplet;
import toxi.util.datatypes.FloatRange;
import controlP5.ControlEvent;
import controlP5.ControlP5;

public class AnnoTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.gui.AnnoTest" });
    }

    @GUIElement(label = "foo")
    public FloatRange range = new FloatRange(0, 100);

    @GUIElement(label = "enabled", x = 20, y = 300)
    public boolean isActive = true;

    @GUIElement(label = "file format", x = 20, y = 200)
    public List<String> options = new ArrayList<String>();

    @GUIElement(label = "hash", x = 220, y = 200)
    public HashSet<Integer> hash = new HashSet<Integer>();

    @GUIElement
    public String btRestart = "restart";

    @GUIElement(label = "columns")
    @Range(min = 1, max = 30)
    public int cols = 10;

    @GUIElement(label = "ratio")
    @Range(min = 0.01f, max = 1f)
    public float ratio = 0.5f;

    @GUIElement(builder = FloatRangeBuilder.class)
    public FloatRange r2 = new FloatRange(-1, 1);
    
    private GUIManager gui;

    public void doRestart(ControlEvent e) {
        System.out.println("--> " + e.controller().name());
    }

    public void draw() {
        background(0);
    }

    private void initGUI() {
        ControlP5 cp5 = new ControlP5(this);
        gui = new GUIManager(cp5);
        gui.addMapping(toxi.util.datatypes.FloatRange.class,
                new FloatRangeMinMaxBuilder());
        gui.createControllers(this);        
        gui.addListenerFor("isActive", "toggleActive", this);
        gui.addListenerFor("btRestart", "doRestart", this);
    }

    public void toggleActive(ControlEvent e) {
        e.controller().setLabel(e.value()>0 ? "enabled" : "disabled");
    }
    
    public void setup() {
        size(400, 400);
        options.add("png");
        options.add("tga");
        options.add("jpg");
        hash.add(23);
        hash.add(42);
        initGUI();
    }
}
