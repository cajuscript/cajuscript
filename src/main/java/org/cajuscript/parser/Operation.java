/*
 * Operation.java
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
import org.cajuscript.math.Operable;
import org.cajuscript.CajuScriptException;

/**
 * Script element of type operation.
 * 
 * @author eduveks
 */
@SuppressWarnings("unchecked")
public class Operation extends Base {
	private Element firstCommand = null;
	private Element secondCommand = null;
	private Operator operator = null;
	private String contextsKey;
	private String valueKey;

	/**
	 * Create new Operation.
	 * 
	 * @param line
	 *            Line detail
	 */
	public Operation(LineDetail line) {
		super(line);
		contextsKey = org.cajuscript.CajuScript.CAJU_VARS.concat("_contexts_")
				.concat(Integer.toString(this.hashCode()));
		valueKey = org.cajuscript.CajuScript.CAJU_VARS.concat("_value_")
				.concat(Integer.toString(this.hashCode()));
	}

	/**
	 * Get first command
	 * 
	 * @return First command
	 */
	public Element getFirstCommand() {
		return firstCommand;
	}

	/**
	 * Get Operator
	 * 
	 * @return Operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Get second command
	 * 
	 * @return Second command
	 */
	public Element getSecondCommand() {
		return secondCommand;
	}

	/**
	 * Set commands of this operation
	 * 
	 * @param firstCommand
	 *            First command
	 * @param operator
	 *            Operator
	 * @param secondCommand
	 *            Second command
	 * @throws org.cajuscript.CajuScriptException
	 *             Operator is invalid!
	 */
	public void setCommands(Element firstCommand, Operator operator,
			Element secondCommand) throws CajuScriptException {
		this.firstCommand = firstCommand;
		this.operator = operator;
		this.secondCommand = secondCommand;
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
	@Override
	public Value execute(CajuScript caju, Context context, Syntax syntax)
			throws CajuScriptException {
		caju.setRunningLine(getLineDetail());
		for (Element element : elements) {
			element.execute(caju, context, syntax);
		}
		Value contextsValue = context.getVar(contextsKey);
		if (contextsValue == null) {
			contextsValue = caju.toValue(new java.util.ArrayList<Context>());
			context.setVar(contextsKey, contextsValue);
		}
		java.util.ArrayList<Context> contexts = (java.util.ArrayList<Context>) contextsValue
				.getValue();
		Value v = context.getVar(valueKey);
		if (v == null || !contexts.contains(context)) {
			contexts.add(context);
			v = new Value(caju, context, syntax);
			context.setVar(valueKey, v);
		}
		Value v1 = firstCommand.execute(caju, context, syntax);
		Value v2 = secondCommand.execute(caju, context, syntax);
		operator.compare(v, v1, v2);
		return v;
	}

	/**
	 * Operators.
	 */
	public static enum Operator {
		ADDITION {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					if (v1.getTypeNumber() == Value.TypeNumber.INTEGER
							&& v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
						v.setValue(new Integer(v1.getNumberIntegerValue()
								+ v2.getNumberIntegerValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.FLOAT)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.FLOAT)) {
						v.setValue(new Float(v1.getNumberFloatValue()
								+ v2.getNumberFloatValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.LONG)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.LONG)) {
						v.setValue(new Long(v1.getNumberLongValue()
								+ v2.getNumberLongValue()));
					} else {
						v.setValue(new Double(v1.getNumberDoubleValue()
								+ v2.getNumberDoubleValue()));
					}
				} else if (v1.getValue() instanceof Operable
						&& v2.getValue() instanceof Operable) {
					v.setValue(Operable.class.cast(v1.getValue()).plus(
							v2.getValue()));
				} else if ((v1.getType() == Value.Type.STRING
						|| v1.getType() == Value.Type.NUMBER || v1.getType() == Value.Type.OBJECT)
						&& (v2.getType() == Value.Type.STRING
								|| v2.getType() == Value.Type.NUMBER || v2
								.getType() == Value.Type.OBJECT)) {
					v.setValue(v1.getStringValue() + v2.getStringValue());
				}
			}
		},
		SUBTRACTION {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					if (v1.getTypeNumber() == Value.TypeNumber.INTEGER
							&& v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
						v.setValue(new Integer(v1.getNumberIntegerValue()
								- v2.getNumberIntegerValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.FLOAT)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.FLOAT)) {
						v.setValue(new Float(v1.getNumberFloatValue()
								- v2.getNumberFloatValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.LONG)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.LONG)) {
						v.setValue(new Long(v1.getNumberLongValue()
								- v2.getNumberLongValue()));
					} else {
						v.setValue(new Double(v1.getNumberDoubleValue()
								- v2.getNumberDoubleValue()));
					}
				} else if (v1.getType() == Value.Type.OBJECT
						&& v2.getType() == Value.Type.OBJECT
						&& v1.getValue() instanceof Operable
						&& v2.getValue() instanceof Operable) {
					v.setValue(Operable.class.cast(v1.getValue()).subtract(
							v2.getValue()));
				}
			}
		},
		MULTIPLICATION {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					if (v1.getTypeNumber() == Value.TypeNumber.INTEGER
							&& v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
						v.setValue(new Integer(v1.getNumberIntegerValue()
								* v2.getNumberIntegerValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.FLOAT)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.FLOAT)) {
						v.setValue(new Float(v1.getNumberFloatValue()
								* v2.getNumberFloatValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.LONG)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.LONG)) {
						v.setValue(new Long(v1.getNumberLongValue()
								* v2.getNumberLongValue()));
					} else {
						v.setValue(new Double(v1.getNumberDoubleValue()
								* v2.getNumberDoubleValue()));
					}
				} else if (v1.getType() == Value.Type.OBJECT
						&& v2.getType() == Value.Type.OBJECT
						&& v1.getValue() instanceof Operable
						&& v2.getValue() instanceof Operable) {
					v.setValue(Operable.class.cast(v1.getValue()).multiply(
							v2.getValue()));
				}
			}
		},
		DIVISION {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					if (v1.getTypeNumber() == Value.TypeNumber.INTEGER
							&& v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
						v.setValue(new Integer(v1.getNumberIntegerValue()
								/ v2.getNumberIntegerValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.FLOAT)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.FLOAT)) {
						v.setValue(new Float(v1.getNumberFloatValue()
								/ v2.getNumberFloatValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.LONG)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.LONG)) {
						v.setValue(new Long(v1.getNumberLongValue()
								/ v2.getNumberLongValue()));
					} else {
						v.setValue(new Double(v1.getNumberDoubleValue()
								/ v2.getNumberDoubleValue()));
					}
				} else if (v1.getType() == Value.Type.OBJECT
						&& v2.getType() == Value.Type.OBJECT
						&& v1.getValue() instanceof Operable
						&& v2.getValue() instanceof Operable) {
					v.setValue(Operable.class.cast(v1.getValue()).divide(
							v2.getValue()));
				}
			}
		},
		MODULES {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					if (v1.getTypeNumber() == Value.TypeNumber.INTEGER
							&& v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
						v.setValue(new Integer(v1.getNumberIntegerValue()
								% v2.getNumberIntegerValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.FLOAT)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.FLOAT)) {
						v.setValue(new Float(v1.getNumberFloatValue()
								% v2.getNumberFloatValue()));
					} else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1
							.getTypeNumber() == Value.TypeNumber.LONG)
							&& (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2
									.getTypeNumber() == Value.TypeNumber.LONG)) {
						v.setValue(new Long(v1.getNumberLongValue()
								% v2.getNumberLongValue()));
					} else {
						v.setValue(new Double(v1.getNumberDoubleValue()
								% v2.getNumberDoubleValue()));
					}
				} else if (v1.getType() == Value.Type.OBJECT
						&& v2.getType() == Value.Type.OBJECT
						&& v1.getValue() instanceof Operable
						&& v2.getValue() instanceof Operable) {
					v.setValue(Operable.class.cast(v1.getValue()).module(
							v2.getValue()));
				}
			}
		},
		AND {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				v.setValue(v1.getBooleanValue() && v2.getBooleanValue());
			}
		},
		OR {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				v.setValue(v1.getBooleanValue() || v2.getBooleanValue());
			}
		},
		EQUAL {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.BOOLEAN
						&& v2.getType() == Value.Type.BOOLEAN) {
					v.setValue(v1.getBooleanValue() == v2.getBooleanValue());
				} else if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() == v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					v.setValue(v1.getStringValue().equals(v2.getStringValue()));
				} else if (v1.getValue() == null || v2.getValue() == null) {
                                        v.setValue(v1.getValue() == v2.getValue());
				} else {
                                        v.setValue(v1.getValue().equals(v2.getValue()));
                                }
			}
		},
		NOT_EQUAL {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.BOOLEAN
						&& v2.getType() == Value.Type.BOOLEAN) {
					v.setValue(v1.getBooleanValue() != v2.getBooleanValue());
				} else if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() != v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					v.setValue(!v1.getStringValue().equals(
									v2.getStringValue()));
				} else if (v1.getValue() == null || v2.getValue() == null) {
                                        v.setValue(v1.getValue() != v2.getValue());
				} else {
                                        v.setValue(!v1.getValue().equals(v2.getValue()));
                                }
			}
		},
		LESS {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() < v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					v.setValue(v1.getStringValue().compareTo(
							v2.getStringValue()) < 0);
				}
			}
		},
		GREATER {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() > v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					v.setValue(v1.getStringValue().compareTo(
							v2.getStringValue()) > 0);
				}
			}
		},
		LESS_EQUAL {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() <= v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					int c = v1.getStringValue().compareTo(v2.getStringValue());
					v.setValue(c <= 0);
				}
			}
		},
		GREATER_EQUAL {
			@Override
			public void compare(Value v, Value v1, Value v2)
					throws CajuScriptException {
				if (v1.getType() == Value.Type.NUMBER
						&& v2.getType() == Value.Type.NUMBER) {
					v.setValue(v1.getNumberValue() >= v2.getNumberValue());
				} else if ((v1.getType() == Value.Type.STRING || (v2.getType() == Value.Type.STRING))) {
					int c = v1.getStringValue().compareTo(v2.getStringValue());
					v.setValue(c >= 0);
				}
			}
		};
		public abstract void compare(Value v, Value v1, Value v2)
				throws CajuScriptException;
	}
}
