/*
 * DefaultExecutable.java
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

package org.cajuscript.compiler;

import org.cajuscript.CajuScript;
import org.cajuscript.CajuScriptException;
import org.cajuscript.Context;
import org.cajuscript.Syntax;
import org.cajuscript.Value;

/**
 * Default executable to be used with override.
 * @author eduveks
 */
public class DefaultExecutable implements Executable {
    /**
     * To be overrided.
     * @param caju CajuScript instance
     * @param context Context
     * @param syntax Syntax
     * @return Value return by executing
     * @throws org.cajuscript.CajuScriptException Exceptions that ocurr on executing
     */
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        throw CajuScriptException.create(caju, context, "Not implemented.");
    }
}
