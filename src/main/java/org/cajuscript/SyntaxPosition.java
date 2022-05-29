/*
 * SyntaxPosition.java
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

import java.util.regex.Pattern;
import org.cajuscript.parser.Operation.Operator;

/**
 * Sintax matchers return an instance from this class with useful data to
 * manipulate dynamically the sintax.
 * 
 * @author eduveks
 */
public class SyntaxPosition {

    private int start = -1;
    private int end = -1;
    private String group = "";
    private String allContent = "";
    private Operator operator = null;
    private int pattern = 0;

    /**
     * Newly instance to save useful data to manipulate dynamically the sintax.
     *
     * @param syntax
     *            Current syntax.
     * @param pattern
     *            Parttern was matcher.
     */
    public SyntaxPosition(Syntax syntax, Pattern pattern) {
        this.pattern = pattern.pattern().hashCode();
        operator = getOperator(syntax);
    }

    private Operator getOperator(Syntax syntax) {
        if (pattern == syntax.getOperatorAnd().pattern().hashCode()) {
            return Operator.AND;
        }
        if (pattern == syntax.getOperatorOr().pattern().hashCode()) {
            return Operator.OR;
        }
        if (pattern == syntax.getOperatorEqual().pattern().hashCode()) {
            return Operator.EQUAL;
        }
        if (pattern == syntax.getOperatorNotEqual().pattern().hashCode()) {
            return Operator.NOT_EQUAL;
        }
        if (pattern == syntax.getOperatorGreater().pattern().hashCode()) {
            return Operator.GREATER;
        }
        if (pattern == syntax.getOperatorLess().pattern().hashCode()) {
            return Operator.LESS;
        }
        if (pattern == syntax.getOperatorGreaterEqual().pattern().hashCode()) {
            return Operator.GREATER_EQUAL;
        }
        if (pattern == syntax.getOperatorLessEqual().pattern().hashCode()) {
            return Operator.LESS_EQUAL;
        }
        if (pattern == syntax.getOperatorAddition().pattern().hashCode()) {
            return Operator.ADDITION;
        }
        if (pattern == syntax.getOperatorSubtraction().pattern().hashCode()) {
            return Operator.SUBTRACTION;
        }
        if (pattern == syntax.getOperatorMultiplication().pattern().hashCode()) {
            return Operator.MULTIPLICATION;
        }
        if (pattern == syntax.getOperatorDivision().pattern().hashCode()) {
            return Operator.DIVISION;
        }
        if (pattern == syntax.getOperatorModules().pattern().hashCode()) {
            return Operator.MODULES;
        }
        return null;
    }

    /**
     * Get end of text that was caught from the matcher pattern.
     *
     * @return End index.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Set end of text that was caught from the matcher pattern.
     *
     * @param end
     *            End index.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Get start of text that was caught from the matcher pattern.
     *
     * @return start index.
     */
    public int getStart() {
        return start;
    }

    /**
     * Set start of text that was caught from the matcher pattern.
     *
     * @param start
     *            Start index.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Get text was caught from the matcher pattern.
     *
     * @return Text caught.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Set text group was caught from the matcher pattern.
     *
     * @param group
     *            Text group caught.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Get all content was caught from the matcher pattern.
     *
     * @return Content caught.
     */
    public String getAllContent() {
        return allContent;
    }

    /**
     * Set all content was caught from the matcher pattern.
     *
     * @param allContent
     *            Content caught.
     */
    public void setAllContent(String allContent) {
        this.allContent = allContent;
    }

    /**
     * Get operator from the pattern.
     *
     * @return Operator
     */
    public Operator getOperator() {
        return operator;
    }
    /**
     * Get pattern hash code.
     *
     * @return Hash code
     */
    public int getPatternHashCode() {
        return pattern;
    }

    /**
     * Get pattern hash code.
     * @param pattern Pattern
     * @return Hash code
     */
    public static int getPatternHashCode(Pattern pattern) {
        return pattern.pattern().hashCode();
    }
}
