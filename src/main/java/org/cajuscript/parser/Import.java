/*
 * Import.java
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
 * Script element of type import.
 * @author eduveks
 */
public class Import extends Base {
    private String path = "";
    
    /**
     * Create new Import.
     * @param line Line detail
     */
    public Import(LineDetail line) {
        super(line);
    }
    
    /**
     * Get path to be imported.
     * @return Path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Set path to be imported.
     * @param p Path
     */
    public void setPath(String p) {
        path = p;
    }
    
    /**
     * Executed this element and all childs elements.
     * @param caju CajuScript
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        for (Element element : elements) {
            element.execute(caju, context, syntax);
        }
        if (path.startsWith(CajuScript.CAJU_VARS)) {
            caju.evalFile(context.getVar(path).toString());
        } else {
            caju.addImport(path);
        }
        return null;
    }
}
