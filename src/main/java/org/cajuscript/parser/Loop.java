/*
 * Loop.java
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
 * Script element of type loop.
 * @author eduveks
 */
public class Loop extends Base {
    private Element condition = null;
    private String label = "";
    
    /**
     * Create new Loop.
     * @param line Line detail.
     */
    public Loop(LineDetail line) {
        super(line);
    }
    
    /**
     * Get condition.
     * @return Element of condition.
     */
    public Element getCondition() {
        return condition;
    }
    
    /**
     * Set condition.
     * @param condition Element of condition.
     */
    public void setCondition(Element condition) {
        this.condition = condition;
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
        loop: while (true) {
            if (condition.execute(caju, context, syntax).getBooleanValue()) {
                for (Element element : elements) {
                    Value v = element.execute(caju, context, syntax);
                    if (v != null && canElementReturn(element)) {
                        if (element instanceof Break && v.getFlag().length() == 0) {
                            break loop;
                        } else if (element instanceof Continue && v.getFlag().length() == 0) {
                            continue loop;
                        } else if (v.getFlag().length() != 0) {
                            String act, lbl = "";
                            int f = v.getFlag().indexOf(':');
                            if (f > -1) {
                                act = v.getFlag().substring(0, f);
                                lbl = v.getFlag().substring(f+1);
                            } else {
                                act = v.getFlag();
                            }
                            if (!lbl.equals(getLabel())) {
                                return v;
                            } else {
                                v.setFlag("");
                            }
                            if (act.equals("break")) {
                                break loop;
                            } else if (act.equals("continue")) {
                                continue loop;
                            }
                        } else {
                            return v;
                        }
                    }
                }
            } else {
                break;
            }
        }
        return null;
    }
}
