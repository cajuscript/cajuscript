/*
 * Array.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cajuscript;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.cajuscript.cmd.ScriptCommand;

/**
 * To create a new Array, get the size, get a value or set a value.
 * <p>
 * CajuScript:
 * </p>
 * <p>
 * <blockquote>
 * 
 * <pre>
 *     strArray = array.create(2)
 *     array.set(strArray, 0, &quot;String 0&quot;)
 *     array.set(strArray, 1, &quot;String 1&quot;)
 *     x = 0
 *     x &lt; array.size(&quot;s&quot;, 2) @
 *         System.out.println(array.get(strArray, x))
 *         x = x + 1
 *     &#064;
 * </pre>
 * 
 * </blockquote>
 * </p>
 * <p>
 * Java:
 * </p>
 * <p>
 * <blockquote>
 * 
 * <pre>
 * org.cajuscript.Array cajuArray = new org.cajuscript.Array();
 * Object strArray = cajuArray.create(2);
 * cajuArray.set(strArray, 0, &quot;String 0&quot;);
 * cajuArray.set(strArray, 1, &quot;String 1&quot;);
 * for (int x = 0; x &lt; cajuArray.size(strArray); x++) {
 * 	System.out.println(cajuArray.get(strArray, x));
 * }
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author eduveks
 */
public class Array {
    private ArrayList arrayList = new ArrayList();
    private HashMap mapRelation = new HashMap();
    private HashMap map = new HashMap();

    public Array() {
        
    }

    public Object get(int i) {
        return arrayList.get((Integer)i);
    }

    public Object get(Object i) {
        return map.get(i);
    }

    public void set(int i, Object o) {
        if (i >= arrayList.size()) {
            for (int j = arrayList.size() - 1; j < i; j++) {
                arrayList.add(null);
            }
            arrayList.add(o);
            mapRelation.put(i, null);
            map.put(i, o);
        } else {
            arrayList.set(i, o);
            mapRelation.put(i, null);
            map.put(i, o);
        }
    }

    public void set(Object i, Object o) {
        if (i instanceof Integer) {
            int index = ((Integer)i).intValue();
            if (index >= arrayList.size()) {
                for (int j = arrayList.size() - 1; j < index; j++) {
                    arrayList.add(null);
                }
                arrayList.add(o);
            } else {
                arrayList.set(index, o);
            }
        }
        map.put(i, o);
    }

    public void add(Object o) {
        arrayList.add(o);
    }

	/**
	 * Create a new array.
	 * <p>
	 * CajuScript:
	 * </p>
	 * <p>
	 * Java:
	 * </p>
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * Object array = cajuArray.create(&quot;java.lang.String&quot;, 2);
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param size
	 *            Size of the new array.
	 * @return Newly array created is returned.
	 */
	public static Object create(String type, int size) {
		if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("i")) {
            return new int[size];
        }
        if (type.equalsIgnoreCase("long") || type.equalsIgnoreCase("l")) {
            return new long[size];
        }
        if (type.equalsIgnoreCase("double") || type.equalsIgnoreCase("d")) {
            return new double[size];
        }
        if (type.equalsIgnoreCase("float") || type.equalsIgnoreCase("f")) {
            return new float[size];
        }
        if (type.equalsIgnoreCase("char") || type.equalsIgnoreCase("c")) {
            return new char[size];
        }
        if (type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("b") || type.equalsIgnoreCase("bool")) {
            return new boolean[size];
        }
        if (type.equalsIgnoreCase("byte") || type.equalsIgnoreCase("bt")) {
            return new byte[size];
        }
        if (type.equalsIgnoreCase("string") || type.equalsIgnoreCase("s")) {
            return new String[size];
        }
		return java.lang.reflect.Array.newInstance(Object.class, size);
	}

	/**
	 * Get the size of array.
	 * 
	 * @param array
	 *            The array.
	 * @return The size.
	 */
	public static int size(Object array) {
		return java.lang.reflect.Array.getLength(array);
	}

	/**
	 * Get the value in determined position.
	 * 
	 * @param array
	 *            The array.
	 * @param i
	 *            The position on the array.
	 * @return Object of this position.
	 */
	public static Object get(Object array, int i) {
		return java.lang.reflect.Array.get(array, i);
	}

	/**
	 * Defining the value in specific position.
	 * 
	 * @param array
	 *            The array for be affected.
	 * @param i
	 *            The position on the array for be affected.
	 * @param v
	 *            The new object for the specific position.
	 */
	public static void set(Object array, int i, Object v) {
		java.lang.reflect.Array.set(array, i, v);
	}
}