/*
 * TryCatch.java
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
 * Script element of type try/catch.
 * 
 * @author eduveks
 */
public class TryCatch extends Base {
	private Variable _error = null;
	private Element _try = null;
	private Element _catch = null;
	private Element _finally = null;
	private String errorValueKey;

	/**
	 * Create new TryCatch.
	 * 
	 * @param line
	 *            Line detail
	 */
	public TryCatch(LineDetail line) {
		super(line);
		errorValueKey = org.cajuscript.CajuScript.CAJU_VARS.concat("_value_")
				.concat(Integer.toString(this.hashCode()));
	}

	/**
	 * Get variable of error.
	 * 
	 * @return Variable
	 */
	public Variable getError() {
		return _error;
	}

	/**
	 * Set variable of error.
	 * 
	 * @param e
	 *            Variable
	 */
	public void setError(Variable e) {
		this._error = e;
	}

	/**
	 * Get element for try.
	 * 
	 * @return Element of the try
	 */
	public Element getTry() {
		return _try;
	}

	/**
	 * Set element for try.
	 * 
	 * @param t
	 *            Element of the try
	 */
	public void setTry(Element t) {
		this._try = t;
	}

	/**
	 * Get element for catch.
	 * 
	 * @return Element of the catch
	 */
	public Element getCatch() {
		return _catch;
	}

	/**
	 * Set element for catch.
	 * 
	 * @param c
	 *            Element of the catch
	 */
	public void setCatch(Element c) {
		this._catch = c;
	}

	/**
	 * Get element for finally.
	 * 
	 * @return Element of the finally
	 */
	public Element getFinally() {
		return _finally;
	}

	/**
	 * Set element for finally.
	 * 
	 * @param f
	 *            Element of the finally
	 */
	public void setFinally(Element f) {
		this._finally = f;
	}

	/**
	 * Executed this element and all childs elements.
	 * 
	 * @param caju
	 *            CajuScript
	 * @param context
	 *            Context
	 * @param syntax
	 *            Syntax
	 * @return Value returned by execution
	 * @throws org.cajuscript.CajuScriptException
	 *             Errors ocurred on execution
	 */
	@SuppressWarnings("finally")
	@Override
	public Value execute(CajuScript caju, Context context, Syntax syntax)
			throws CajuScriptException {
		caju.setRunningLine(getLineDetail());
		Value errorValue = context.getVar(errorValueKey);
		if (errorValue != null) {
			errorValue.setValue(null);
		}
		try {
			return _try.execute(caju, context, syntax);
		} catch (Exception e) {
			if (errorValue == null) {
				errorValue = caju.toValue(e, context, syntax);
				context.setVar(errorValueKey, errorValue);
			}
			errorValue.setValue(e);
			context.setVar(_error.getKey(), errorValue);
			return _catch.execute(caju, context, syntax);
		} finally {
			return _finally.execute(caju, context, syntax);
		}
	}
}