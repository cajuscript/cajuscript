/*
 * CajuScriptException.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Exceptions of the CajuScript.
 * @author eduveks
 */
public class CajuScriptException extends Exception {
    /**
     * Newly exception.
     */
    public CajuScriptException() {
	super();
    }
    
    /**
     * Newly exception.
     * @param message Message.
     */
    public CajuScriptException(String message) {
	super(message);
    }
    
    /**
     * Newly exception.
     * @param cause More exceptions.
     */
    public CajuScriptException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Newly exception.
     * @param message Message.
     * @param cause More exceptions.
     */
    public CajuScriptException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create an newly exception.
     * @param caju CajuScript instance.
     * @param message Message of the exception.
     * @return Newly exception.
     * @throws org.cajuscript.CajuScriptException Creating exception.
     */
    public static CajuScriptException create(CajuScript caju, Context context, String message) throws CajuScriptException {
        return create(caju, context, message, null);
    }
    
    /**
     * Create an newly exception.
     * @param caju CajuScript instance.
     * @param message Message of the exception.
     * @param cause More exceptions.
     * @return Newly exception.
     * @throws org.cajuscript.CajuScriptException Creating exception.
     */
    public static CajuScriptException create(CajuScript caju, Context context, String message, Throwable cause) throws CajuScriptException {
        if (message == null) {
            if (caju != null && context != null) {
                message = Integer.toString(caju.getRunningLine().getNumber()).concat(": ").concat(formatScript(context, caju.getRunningLine().getContent()));
            } else if (caju != null) {
                message = Integer.toString(caju.getRunningLine().getNumber()).concat(": ").concat(formatScript(caju.getContext(), caju.getRunningLine().getContent()));
            }
            return new CajuScriptException(message, cause);
        }
        if (cause instanceof ClassNotFoundException) {
            message = "Class "+ message +" not found.";
        }
        message = formatScript(context, message);
        return new CajuScriptException(
                message.concat("\n")
                        .concat("\n")
                        .concat("\t> ")
                        .concat(Integer.toString(caju.getRunningLine().getNumber()))
                        .concat(": ")
                        .concat(formatScript(context, caju.getRunningLine().getContent()))
                        .concat("\n")
                        .concat("\n")
                        .concat("\t# "+ cause.toString()),
                cause);
    }
    
    private static String formatScript(Context context, String script) throws CajuScriptException {
        Pattern pattern = Pattern.compile("("+ CajuScript.CAJU_VARS +"[_a-z0-9]*)");
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            String key = matcher.group(1);
            script = script.replace(key, "\"".concat(context.getVar(key).toString()).concat("\""));
        }
        return script;
    }
}