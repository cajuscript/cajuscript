/*
 * Element.java
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

package org.cajuscript.parser;

import java.util.List;
import org.cajuscript.CajuScript;
import org.cajuscript.Context;
import org.cajuscript.Value;
import org.cajuscript.Syntax;
import org.cajuscript.CajuScriptException;

/**
 * Interface for all elements of script.
 * @author eduveks
 */
public interface Element {
    /**
     * Append child element.
     * @param element Element
     */
    public void addElement(Element element);
    
    /**
     * Remove child element.
     * @param element Element
     */
    public void removeElement(Element element);
    
    /**
     * Elements
     * @return All child elements
     */
    public List<Element> getElements();
    
    /**
     * Get line detail.
     * @return Line detail
     */
    public LineDetail getLineDetail();
    
    /**
     * Cleaning forced in memory allocation.
     */
    public void clear();
    
    /**
     * Executed this element and all childs elements.
     * @param caju CajuScript instance
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException;  
}