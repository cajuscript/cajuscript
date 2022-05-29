/*
 * Return.java
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

import org.cajuscript.CajuScript;
import org.cajuscript.Context;
import org.cajuscript.Value;
import org.cajuscript.Syntax;
import org.cajuscript.CajuScriptException;

/**
 * Script element of type return.
 * @author eduveks
 */
public class Return extends Base {
    private Element value = null;
    
    /**
     * Create new Return.
     * @param line Line detail
     */
    public Return(LineDetail line) {
        super(line);
    }
    
    /**
     * Get value to be returned.
     * @return Element of the value
     */
    public Element getValue() {
        return value;
    }
    
    /**
     * Set value to be returned.
     * @param v Element of the value
     */
    public void setValue(Element v) {
        value = v;
    }
    
    /**
     * Executed this element.
     * @param caju CajuScript
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        if (value != null) {
            return value.execute(caju, context, syntax);
        }
        return new Value(caju, context, syntax);
    }
}
