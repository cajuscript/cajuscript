/*
 * Break.java
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
 * Script element of type break.
 * @author eduveks
 */
public class Break extends Base {
    private String label = "";
    
    /**
     * Create new Break.
     * @param line Line detail
     */
    public Break(LineDetail line) {
        super(line);
    }
    
    /**
     * Get Label.
     * @return Label.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Set Label.
     * @param label Label.
     */
    public void setLabel(String label) {
        this.label = label;
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
        Value v = new Value(caju, context, syntax);
        if (getLabel().length() != 0) {
            v.setFlag("break:".concat(getLabel()));
        } else {
            v.setFlag("break");
        }
        return v;
    }
}
