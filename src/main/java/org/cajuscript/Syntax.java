/*
 * Syntax.java
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
import java.util.regex.Matcher;

/**
 * Script syntax.
 * <p>Java Basic style:</p>
 * <p><blockquote><pre>
 *      Syntax syntaxJ = new Syntax();
 *      syntaxJ.setIf(Pattern.compile("if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
 *      syntaxJ.setElseIf(Pattern.compile("\\}\\s*else\\s+if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
 *      syntaxJ.setElse(Pattern.compile("\\}\\s*else\\s*\\{"));
 *      syntaxJ.setIfEnd(Pattern.compile("\\}"));
 *      syntaxJ.setLoop(Pattern.compile("while\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
 *      syntaxJ.setLoopEnd(Pattern.compile("\\}"));
 *      syntaxJ.setTry(Pattern.compile("try\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
 *      syntaxJ.setTryCatch(Pattern.compile("\\}\\s*catch\\s*\\{"));
 *      syntaxJ.setTryFinally(Pattern.compile("\\}\\s*finally\\s*\\{"));
 *      syntaxJ.setTryEnd(Pattern.compile("\\}"));
 *      syntaxJ.setFunction(Pattern.compile("function\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
 *      syntaxJ.setFunctionEnd(Pattern.compile("\\}"));
 *      syntaxJ.setNull(Pattern.compile("null"));
 *      syntaxJ.setReturn(Pattern.compile("return"));
 *      syntaxJ.setImport(Pattern.compile("import\\s+"));
 *      syntaxJ.setRootContext(Pattern.compile("root\\."));
 *      syntaxJ.setContinue(Pattern.compile("continue"));
 *      syntaxJ.setBreak(Pattern.compile("break"));
 *      globalSyntaxs.put("CajuJava", syntaxJ);
 *      // Registering:
 *      CajuScript.addGlobalSyntax("CajuJava", syntaxJ);
 *      // To use set the first line of script: caju.syntax CajuJava
 * </pre></blockquote></p>
 * <p>Syntax Basic style:</p>
 * <p><blockquote><pre>
 *      Syntax syntaxB = new Syntax();
 *      syntaxB.setIf(Pattern.compile("^[\\s+i|i]f\\s*([\\s+|[\\s*\\(]].+)\\s*"));
 *      syntaxB.setElseIf(Pattern.compile("^[\\s+e|e]lseif\\s*([\\s+|[\\s*\\(]].+)\\s*"));
 *      syntaxB.setElse(Pattern.compile("^[\\s+e|e]ls[e\\s+|e]$"));
 *      syntaxB.setIfEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
 *      syntaxB.setLoop(Pattern.compile("^[\\s+w|w]hile\\s*([\\s+|[\\s*\\(]].+)\\s*"));
 *      syntaxB.setLoopEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
 *      syntaxB.setTry(Pattern.compile("^[\\s+t|t]ry\\s*([\\s+|[\\s*\\(]].+)\\s*"));
 *      syntaxB.setTryCatch(Pattern.compile("^[\\s+c|c]atc[h\\s+|h]$"));
 *      syntaxB.setTryFinally(Pattern.compile("^[\\s+f|f]inall[y\\s+|y]$"));
 *      syntaxB.setTryEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
 *      syntaxB.setFunction(Pattern.compile("^[\\s+f|f]unction\\s*([\\s+|[\\s*\\(]].+)\\s*"));
 *      syntaxB.setFunctionEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
 *      syntaxB.setNull(Pattern.compile("null"));
 *      syntaxB.setReturn(Pattern.compile("return"));
 *      syntaxB.setImport(Pattern.compile("import\\s+"));
 *      syntaxB.setRootContext(Pattern.compile("root\\."));
 *      syntaxB.setContinue(Pattern.compile("continue"));
 *      syntaxB.setBreak(Pattern.compile("break"));
 *      // Registering:
 *      CajuScript.addGlobalSyntax("CajuBasic", syntaxB);
 *      // To use set the first line of script: caju.syntax CajuBasic
 * </pre></blockquote></p>
 * @author eduveks
 */
public class Syntax {
    private Pattern operatorAddition = Pattern.compile("\\+");
    private Pattern operatorSubtraction = Pattern.compile("\\-");
    private Pattern operatorMultiplication = Pattern.compile("\\*");
    private Pattern operatorDivision = Pattern.compile("\\/");
    private Pattern operatorModules = Pattern.compile("\\%");
    private Pattern operatorAnd = Pattern.compile("\\&");
    private Pattern operatorOr = Pattern.compile("\\|");
    private Pattern operatorEqual = Pattern.compile("\\=");
    private Pattern operatorNotEqual = Pattern.compile("\\!");
    private Pattern operatorLess = Pattern.compile("\\<");
    private Pattern operatorGreater = Pattern.compile("\\>");
    private Pattern operatorLessEqual = Pattern.compile("\\<\\s*\\=");
    private Pattern operatorGreaterEqual = Pattern.compile("\\>\\s*\\=");
    private Pattern number = Pattern.compile("\\-*\\d+[\\.\\d+]*");
    private Pattern ifStart = Pattern.compile("([^\\?\\@\\#\\^]+)\\?");
    private Pattern elseIfStart = Pattern.compile("\\?\\s*(.+)\\s*\\?");
    private Pattern elseStart = Pattern.compile("\\?\\s*\\?");
    private Pattern ifEnd = Pattern.compile("\\?");
    private Pattern loopStart = Pattern.compile("([^\\?\\@\\#\\^]+)\\@");
    private Pattern loopEnd = Pattern.compile("\\@");
    private Pattern functionStart = Pattern.compile("([^\\?\\@\\#\\^]+)\\s*(.+)\\s*\\#");
    private Pattern functionEnd = Pattern.compile("\\#");
    private Pattern tryStart = Pattern.compile("([^\\?\\@\\#\\^]+)\\^");
    private Pattern tryCatchStart = Pattern.compile("\\^\\s*\\^");
    private Pattern tryFinallyStart = Pattern.compile("\\^\\s*\\~\\s*\\^");
    private Pattern tryEnd = Pattern.compile("\\^");
    private Pattern _return = Pattern.compile("\\~");
    private Pattern _import = Pattern.compile("\\$");
    private Pattern _null = Pattern.compile("\\$");
    private Pattern _break = Pattern.compile("\\!\\s*\\!");
    private Pattern _continue = Pattern.compile("\\.\\s*\\.");
    private Pattern rootContext = Pattern.compile("\\\\");
    private Pattern label = Pattern.compile("\\s*([^\\s\\(\\:]+)\\s*\\:");
    private Pattern[] comments = new Pattern[]{Pattern.compile("\\\\\\s*\\\\"), Pattern.compile("\\-\\s*\\-"), Pattern.compile("\\/\\s*\\/")};
    private Pattern group = Pattern.compile("\\(([[^\\(\\)]|[.]]*)\\)");
    private Pattern functionCall = Pattern.compile("[\\w|\\.]+[\\w|\\.|\\s]*\\([[^\\(\\)]|[.]]*\\)");
    private Pattern functionCallPathSeparator = Pattern.compile("\\.");
    private Pattern functionCallParametersBegin = Pattern.compile("\\(");
    private Pattern functionCallParametersSeparator = Pattern.compile("\\,");
    private Pattern functionCallParametersEnd = Pattern.compile("\\)");
    private Pattern array = Pattern.compile("(\\[([[^\\[\\]]|[.]]*)\\])");
    private Pattern arrayCall = Pattern.compile("[\\w|\\.]+[\\w|\\.|\\s]*\\[[[^\\(\\)]|[.]]*\\]");
    private Pattern arrayCallParametersBegin = Pattern.compile("\\[");
    private Pattern arrayCallParametersSeparator = Pattern.compile("\\,");
    private Pattern arrayCallParametersEnd = Pattern.compile("\\]");

    /**
     * Create new Syntax.
     */
    public Syntax() {

    }

    /**
     * Get If. Default "([^\\?\\@\\#\\^]+)\\?".
     * Basic: "^[\\s+i|i]f\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @return If.
     */
    public Pattern getIf() {
        return ifStart;
    }

    /**
     * Get If end. Default "\\?".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @return If end.
     */
    public Pattern getIfEnd() {
        return ifEnd;
    }

    /**
     * Get Else If. Default "\\?\\s*(.+)\\s*\\?".
     * Basic: "^[\\s+e|e]lseif\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "\\}\\s*else\\s+if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @return Else If.
     */
    public Pattern getElseIf() {
        return elseIfStart;
    }

    /**
     * Get Else. Default "\\?\\s*\\?".
     * Basic: "^[\\s+e|e]ls[e\\s+|e]$".
     * Java: "\\}\\s*else\\s*\\{".
     * @return Else.
     */
    public Pattern getElse() {
        return elseStart;
    }

    /**
     * Get Loop. Default "([^\\?\\@\\#\\^]+)\\@".
     * Basic: "^[\\s+w|w]hile\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "while\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @return Loop.
     */
    public Pattern getLoop() {
        return loopStart;
    }

    /**
     * Get Loop end. Default "\\@".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @return Loop end.
     */
    public Pattern getLoopEnd() {
        return loopEnd;
    }

    /**
     * Get Function. Default "([^\\?\\@\\#\\^]+)\\s*(.+)\\s*\\#".
     * Basic: "^[\\s+f|f]unction\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "function\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @return Function.
     */
    public Pattern getFunction() {
        return functionStart;
    }

    /**
     * Get Function end. Default "\\#".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @return Function end.
     */
    public Pattern getFunctionEnd() {
        return functionEnd;
    }

    /**
     * Get Try. Default "([^\\?\\@\\#\\^]+)\\^".
     * Basic: "^[\\s+t|t]ry\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "try\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @return Try.
     */
    public Pattern getTry() {
        return tryStart;
    }

    /**
     * Get Try end. Default "\\^".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @return Try end.
     */
    public Pattern getTryEnd() {
        return tryEnd;
    }

    /**
     * Get Catch. Default "\\^\\s*\\^".
     * Basic: "^[\\s+c|c]atc[h\\s+|h]$".
     * Java: "\\}\\s*catch\\s*\\{".
     * @return Catch.
     */
    public Pattern getTryCatch() {
        return tryCatchStart;
    }

    /**
     * Get Finally. Default "\\^\\s*\\~\\s*\\^".
     * Basic: "^[\\s+f|f]inall[y\\s+|y]$".
     * Java: "\\}\\s*finally\\s*\\{".
     * @return Finally.
     */
    public Pattern getTryFinally() {
        return tryFinallyStart;
    }

    /**
     * Get Import. Default "\\$".
     * Basic: "import\\s+".
     * Java: "import\\s+".
     * @return Import.
     */
    public Pattern getImport() {
        return _import;
    }

    /**
     * Get Null. Default "\\$".
     * Basic: "null".
     * Java: "null".
     * @return Null.
     */
    public Pattern getNull() {
        return _null;
    }

    /**
     * Get Return. Default "\\~".
     * Basic: "return".
     * Java: "return".
     * @return Return.
     */
    public Pattern getReturn() {
        return _return;
    }

    /**
     * Get Break. Default "\\!\\s*\\!".
     * Basic: "break".
     * Java: "break".
     * @return Break.
     */
    public Pattern getBreak() {
        return _break;
    }

    /**
     * Get Continue. Default "\\.\\s*\\.".
     * Basic: "continue".
     * Java: "continue".
     * @return Continue.
     */
    public Pattern getContinue() {
        return _continue;
    }

    /**
     * Get Root context. Default: "\\.".
     * Basic: "root\\.".
     * Java: "root\\.".
     * @return Root context.
     */
    public Pattern getRootContext() {
        return rootContext;
    }

    /**
     * Get Addition operator. Default: "\\+".
     * @return Addition operator.
     */
    public Pattern getOperatorAddition() {
        return operatorAddition;
    }

    /**
     * Get Subtraction operator. Default: "\\-".
     * @return Subtraction operator.
     */
    public Pattern getOperatorSubtraction() {
        return operatorSubtraction;
    }

    /**
     * Get Multiplication operator. Default: "\\*".
     * @return Multiplication operator.
     */
    public Pattern getOperatorMultiplication() {
        return operatorMultiplication;
    }

    /**
     * Get Division operator. Default: "\\/".
     * @return Division operator.
     */
    public Pattern getOperatorDivision() {
        return operatorDivision;
    }

    /**
     * Get Modules operator. Default: "\\%".
     * @return Modules operator.
     */
    public Pattern getOperatorModules() {
        return operatorModules;
    }

    /**
     * Get And operator. Default: "\\&".
     * @return And operator.
     */
    public Pattern getOperatorAnd() {
        return operatorAnd;
    }

    /**
     * Get Or operator. Default: "\\|".
     * @return Or operator.
     */
    public Pattern getOperatorOr() {
        return operatorOr;
    }

    /**
     * Get Equal operator. Default: "\\=".
     * @return Equal operator.
     */
    public Pattern getOperatorEqual() {
        return operatorEqual;
    }

    /**
     * Get Not Equal operator. Default: "\\!".
     * @return Not Equal operator.
     */
    public Pattern getOperatorNotEqual() {
        return operatorNotEqual;
    }

    /**
     * Get Less operator. Default: "\\&lt;".
     * @return Less operator.
     */
    public Pattern getOperatorLess() {
        return operatorLess;
    }

    /**
     * Get Greater operator. Default: "\\&gt;".
     * @return Greater operator.
     */
    public Pattern getOperatorGreater() {
        return operatorGreater;
    }

    /**
     * Get Less Equal operator. Default: "\\&lt;\\s*\\=".
     * @return Less Equal operator.
     */
    public Pattern getOperatorLessEqual() {
        return operatorLessEqual;
    }

    /**
     * Get Greater Equal operator. Default: "\\&gt;\\s*\\=".
     * @return Greater Equal operator.
     */
    public Pattern getOperatorGreaterEqual() {
        return operatorGreaterEqual;
    }

    /**
     * Get valid number. Default: "\\-*\\d+[\\.\\d+]*"
     * @return Valid number.
     */
    public Pattern getNumber() {
        return number;
    }
    
    /**
     * Get label signal. Default: "\\:".
     * @return Label signal.
     */
    public Pattern getLabel() {
        return label;
    }

    /**
     * Get comments. Default: "\\\\", "\\-\\s*\\-", "\\/\\s*\\/".
     * @return Comments.
     */
    public Pattern[] getComments() {
        return comments;
    }

    /**
     * Get command group. Default: "\\(([[^\\(\\)]|[.]]*)\\)".
     * @return Command group.
     */
    public Pattern getGroup() {
        return group;
    }

    /**
     * Get function call. Default: "[\\w|\\.]+[\\w|\\.|\\s]*\\([[^\\(\\)]|[.]]*\\)".
     * @return Function call.
     */
    public Pattern getFunctionCall() {
        return functionCall;
    }

    /**
     * Get function call path separator. Default: "\\.".
     * @return Function call path separator.
     */
    public Pattern getFunctionCallPathSeparator() {
        return functionCallPathSeparator;
    }

    /**
     * Get function call parameters begin. Default: "\\(".
     * @return Function call parameters begin.
     */
    public Pattern getFunctionCallParametersBegin() {
        return functionCallParametersBegin;
    }

    /**
     * Get function call parameters end. Default: "\\)".
     * @return Function call parameters end.
     */
    public Pattern getFunctionCallParametersEnd() {
        return functionCallParametersEnd;
    }

    /**
     * Get function call parameters separator. Default "\\,".
     * @return Function call parameters separator.
     */
    public Pattern getFunctionCallParametersSeparator() {
        return functionCallParametersSeparator;
    }

    /**
     * Get array. Default: "\\{([[^\\{\\}]|[.]]*)\\}".
     * @return Array.
     */
    public Pattern getArray() {
        return array;
    }

    /**
     * Get array. Default: "[\\w|\\.]+[\\w|\\.|\\s]*\\[[[^\\(\\)]|[.]]*\\]".
     * @return Array call.
     */
    public Pattern getArrayCall() {
        return arrayCall;
    }

    /**
     * Get array call parameters begin. Default: "\\[".
     * @return Array call parameters begin.
     */
    public Pattern getArrayCallParametersBegin() {
        return arrayCallParametersBegin;
    }

    /**
     * Get array call parameters end. Default: "\\]".
     * @return Array call parameters end.
     */
    public Pattern getArrayCallParametersEnd() {
        return arrayCallParametersEnd;
    }

    /**
     * Get array call parameters separator. Default "\\,".
     * @return Array call parameters separator.
     */
    public Pattern getArrayCallParametersSeparator() {
        return arrayCallParametersSeparator;
    }

    /**
     * Set If. Default "([^\\?\\@\\#\\^]+)\\?".
     * Basic: "^[\\s+i|i]f\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @param ifStart If.
     */
    public void setIf(Pattern ifStart) {
        this.ifStart = ifStart;
    }

    /**
     * Set If end. Default "\\?".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @param ifEnd If end.
     */
    public void setIfEnd(Pattern ifEnd) {
        this.ifEnd = ifEnd;
    }

    /**
     * Set Else If. Default "\\?\\s*(.+)\\s*\\?".
     * Basic: "^[\\s+e|e]lseif\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "\\}\\s*else\\s+if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @param elseIfStart Else If.
     */
    public void setElseIf(Pattern elseIfStart) {
        this.elseIfStart = elseIfStart;
    }

    /**
     * Set Else. Default "\\?\\s*\\?".
     * Basic: "^[\\s+e|e]ls[e\\s+|e]$".
     * Java: "\\}\\s*else\\s*\\{".
     * @param elseStart Else.
     */
    public void setElse(Pattern elseStart) {
        this.elseStart = elseStart;
    }

    /**
     * Set Loop. Default "([^\\?\\@\\#\\^]+)\\@".
     * Basic: "^[\\s+w|w]hile\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "while\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @param loopStart Loop.
     */
    public void setLoop(Pattern loopStart) {
        this.loopStart = loopStart;
    }

    /**
     * Set Loop end. Default "\\@".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @param loopEnd Loop end.
     */
    public void setLoopEnd(Pattern loopEnd) {
        this.loopEnd = loopEnd;
    }

    /**
     * Set Function. Default "([^\\?\\@\\#\\^]+)\\s*(.+)\\s*\\#".
     * Basic: "^[\\s+f|f]unction\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "function\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @param functionStart Function.
     */
    public void setFunction(Pattern functionStart) {
        this.functionStart = functionStart;
    }

    /**
     * Set Function end. Default "\\#".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @param functionEnd Function end.
     */
    public void setFunctionEnd(Pattern functionEnd) {
        this.functionEnd = functionEnd;
    }

    /**
     * Set Try. Default "([^\\?\\@\\#\\^]+)\\^".
     * Basic: "^[\\s+t|t]ry\\s*([\\s+|[\\s*\\(]].+)\\s*".
     * Java: "try\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{".
     * @param tryStart Try.
     */
    public void setTry(Pattern tryStart) {
        this.tryStart = tryStart;
    }

    /**
     * Set Try end. Default "\\^".
     * Basic: "^[\\s+e|e]n[d\\s+|d]$".
     * Java: "\\}".
     * @param tryEnd Try end.
     */
    public void setTryEnd(Pattern tryEnd) {
        this.tryEnd = tryEnd;
    }

    /**
     * Set Catch. Default "\\^\\s*\\^".
     * Basic: "^[\\s+c|c]atc[h\\s+|h]$".
     * Java: "\\}\\s*catch\\s*\\{".
     * @param catchStart Catch.
     */
    public void setTryCatch(Pattern catchStart) {
        this.tryCatchStart = catchStart;
    }

    /**
     * Set Finally. Default "\\^\\s*\\~\\s*\\^".
     * Basic: "^[\\s+f|f]inall[y\\s+|y]$".
     * Java: "\\}\\s*finally\\s*\\{".
     * @param finallyStart Finally.
     */
    public void setTryFinally(Pattern finallyStart) {
        this.tryFinallyStart = finallyStart;
    }

    /**
     * Set Import. Default "\\$".
     * Basic: "import\\s+".
     * Java: "import\\s+".
     * @param i Import.
     */
    public void setImport(Pattern i) {
        this._import = i;
    }

    /**
     * Set Null. Default "\\$".
     * Basic: "null".
     * Java: "null".
     * @param n Null.
     */
    public void setNull(Pattern n) {
        this._null = n;
    }

    /**
     * Set Return. Default "\\~".
     * Basic: "return".
     * Java: "return".
     * @param r Return.
     */
    public void setReturn(Pattern r) {
        this._return = r;
    }

    /**
     * Set Break. Default "\\!\\s*\\!".
     * Basic: "break".
     * Java: "break".
     * @param b Break.
     */
    public void setBreak(Pattern b) {
        this._break = b;
    }

    /**
     * Set Continue. Default "\\.\\s*\\.".
     * Basic: "continue".
     * Java: "continue".
     * @param c Continue.
     */
    public void setContinue(Pattern c) {
        this._continue = c;
    }

    /**
     * Set Root context. Default: "\\.".
     * Basic: "root\\.".
     * Java: "root\\.".
     * @param c Root Context.
     */
    public void setRootContext(Pattern c) {
        this.rootContext = c;
    }

    /**
     * Set Addition operator. Default: "\\+".
     * @param operatorAddition Addition operator.
     */
    public void setOperatorAddition(Pattern operatorAddition) {
        this.operatorAddition = operatorAddition;
    }

    /**
     * Set Subtraction operator. Default: "\\-".
     * @param operatorSubtraction Subtraction operator.
     */
    public void setOperatorSubtraction(Pattern operatorSubtraction) {
        this.operatorSubtraction = operatorSubtraction;
    }

    /**
     * Set Multiplication operator. Default: "\\*".
     * @param operatorMultiplication Multiplication operator.
     */
    public void setOperatorMultiplication(Pattern operatorMultiplication) {
        this.operatorMultiplication = operatorMultiplication;
    }

    /**
     * Set Division operator. Default: "\\/".
     * @param operatorDivision Division operator.
     */
    public void setOperatorDivision(Pattern operatorDivision) {
        this.operatorDivision = operatorDivision;
    }

    /**
     * Set Modules operator. Default: "\\%".
     * @param operatorModules Modules operator.
     */
    public void setOperatorModules(Pattern operatorModules) {
        this.operatorModules = operatorModules;
    }

    /**
     * Set And operator. Default: "\\&".
     * @param operatorAnd And operator.
     */
    public void setOperatorAnd(Pattern operatorAnd) {
        this.operatorAnd = operatorAnd;
    }

    /**
     * Set Or operator. Default: "\\|".
     * @param operatorOr Or operator.
     */
    public void setOperatorOr(Pattern operatorOr) {
        this.operatorOr = operatorOr;
    }

    /**
     * Set Equal operator. Default: "\\=".
     * @param operatorEqual Equal operator.
     */
    public void setOperatorEqual(Pattern operatorEqual) {
        this.operatorEqual = operatorEqual;
    }

    /**
     * Set Not Equal operator. Default: "\\!".
     * @param operatorNotEqual Not Equal operator.
     */
    public void setOperatorNotEqual(Pattern operatorNotEqual) {
        this.operatorNotEqual = operatorNotEqual;
    }

    /**
     * Set Less operator. Default: "\\&lt;".
     * @param operatorLess Less operator.
     */
    public void setOperatorLess(Pattern operatorLess) {
        this.operatorLess = operatorLess;
    }

    /**
     * Set Greater operator. Default: "\\&gt;".
     * @param operatorGreater Greater operator.
     */
    public void setOperatorGreater(Pattern operatorGreater) {
        this.operatorGreater = operatorGreater;
    }

    /**
     * Set Less Equal operator. Default: "\\&lt;\\s*\\=".
     * @param operatorLessEqual Less Equal operator.
     */
    public void setOperatorLessEqual(Pattern operatorLessEqual) {
        this.operatorLessEqual = operatorLessEqual;
    }

    /**
     * Set Greater Equal operator. Default: "\\&gt;\\s*\\=".
     * @param operatorGreaterEqual Greater Equal operator.
     */
    public void setOperatorGreaterEqual(Pattern operatorGreaterEqual) {
        this.operatorGreaterEqual = operatorGreaterEqual;
    }

    /**
     * Set valid number. Default: "\\-*\\d+[\\.\\d+]*"
     * @param number Valid number.
     */
    public void setNumber(Pattern number) {
        this.number = number;
    }

    /**
     * Set label signal. Default: "\\:".
     * @param label Label signal.
     */
    public void setLabel(Pattern label) {
        this.label = label;
    }

    /**
     * Set comments. Default: "\\\\", "\\-\\s*\\-", "\\/\\s*\\/".
     * @param comments Comments.
     */
    public void setComments(Pattern[] comments) {
        this.comments = comments;
    }

    /**
     * Set command group. Default: "\\(([[^\\(\\)]|[.]]*)\\)".
     * @param group Command group.
     */
    public void setGroup(Pattern group) {
        this.group = group;
    }

    /**
     * Set function call. Default: "[\\w|\\.]+[\\w|\\.|\\s]*\\([[^\\(\\)]|[.]]*\\)".
     * @param functionCall Function call.
     */
    public void setFunctionCall(Pattern functionCall) {
        this.functionCall = functionCall;
    }

    /**
     * Set function call path separator. Default: "\\.".
     * @param functionCallPathSeparator Function call path separator.
     */
    public void setFunctionCallPathSeparator(Pattern functionCallPathSeparator) {
        this.functionCallPathSeparator = functionCallPathSeparator;
    }

    /**
     * Set function call parameters begin. Default: "\\(".
     * @param functionCallParametersBegin Function call parameters begin.
     */
    public void setFunctionCallParametersBegin(Pattern functionCallParametersBegin) {
        this.functionCallParametersBegin = functionCallParametersBegin;
    }

    /**
     * Set function call parameters end. Default: "\\)".
     * @param functionCallParametersEnd Function call parameters end.
     */
    public void setFunctionCallParametersEnd(Pattern functionCallParametersEnd) {
        this.functionCallParametersEnd = functionCallParametersEnd;
    }

    /**
     * Set function call parameters separator. Default "\\,".
     * @param functionCallParametersSeparator Function call parameters separator.
     */
    public void setFunctionCallParametersSeparator(Pattern functionCallParametersSeparator) {
        this.functionCallParametersSeparator = functionCallParametersSeparator;
    }
    
    /**
     * Set array. Default: "\\{([[^\\{\\}]|[.]]*)\\}".
     * @param array Array.
     */
    public void setArray(Pattern array) {
        this.array = array;
    }

    /**
     * Set array call. Default: "[\\w|\\.]+[\\w|\\.|\\s]*\\[[[^\\(\\)]|[.]]*\\]".
     * @param arrayCall Array call.
     */
    public void setArrayCall(Pattern arrayCall) {
        this.arrayCall = arrayCall;
    }

    /**
     * Set array call parameters begin. Default: "\\[".
     * @param arrayCallParametersBegin Array call parameters begin.
     */
    public void setArrayCallParametersBegin(Pattern arrayCallParametersBegin) {
        this.arrayCallParametersBegin = arrayCallParametersBegin;
    }

    /**
     * Set array call parameters end. Default: "\\]".
     * @param arrayCallParametersEnd Array call parameters end.
     */
    public void setArrayCallParametersEnd(Pattern arrayCallParametersEnd) {
        this.arrayCallParametersEnd = arrayCallParametersEnd;
    }

    /**
     * Set array call parameters separator. Default "\\,".
     * @param arrayCallParametersSeparator Array call parameters separator.
     */
    public void setArrayCallParametersSeparator(Pattern arrayCallParametersSeparator) {
        this.arrayCallParametersSeparator = arrayCallParametersSeparator;
    }

    /**
     * Matcher position.
     * @param line Command line.
     * @param pattern Pattern.
     * @return Position.
     */
    public SyntaxPosition matcherPosition(String line, Pattern pattern) {
        SyntaxPosition position = null;
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            position = new SyntaxPosition(this, pattern);
            position.setAllContent(matcher.group());
            if (matcher.groupCount() > 0) {
                position.setGroup(matcher.group(1));
            } else {
                position.setGroup(matcher.group());
            }
            position.setStart(matcher.start());
            position.setEnd(matcher.end());
            return position;
        } else {
            position = new SyntaxPosition(this, pattern);
            position.setStart(-1);
            position.setEnd(-1);
            return position;
        }
    }

    /**
     * Find the last operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition matcherLastPosition(String script, Pattern pattern) {
        SyntaxPosition syntaxPosition = null;
        SyntaxPosition syntaxPositionFinal = new SyntaxPosition(this, Pattern.compile(""));
        while (true) {
            syntaxPosition = matcherPosition(script, pattern);
            if (syntaxPosition.getStart() != -1) {
                script = script.substring(syntaxPosition.getEnd());
                syntaxPositionFinal = syntaxPosition;
            } else {
                return syntaxPositionFinal;
            }
        }
    }

    /**
     * Matcher equals.
     * @param line Command line.
     * @param pattern Pattern.
     * @return Is equals.
     */
    public boolean matcherEquals(String line, Pattern pattern) {
        Boolean b = null;
        Matcher matcher = pattern.matcher(line);
        b = Boolean.valueOf(matcher.matches());
        return b;
    }

    /**
     * Find the first logical operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition firstLogicalOperator(String script) {
        return firstOperator(script, getOperatorAnd(), getOperatorOr());
    }

    /**
     * Find the last logical operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition lastLogicalOperator(String script) {
        return lastOperator(script, getOperatorAnd(), getOperatorOr());
    }

    /**
     * Find the first conditional operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition firstConditionalOperator(String script) {
        return firstOperator(script, getOperatorEqual(), getOperatorNotEqual(),
                getOperatorGreater(), getOperatorLess(), getOperatorGreaterEqual(), getOperatorLessEqual());
    }

    /**
     * Find the last conditional operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition lastConditionalOperator(String script) {
        return lastOperator(script, getOperatorEqual(), getOperatorNotEqual(),
                getOperatorGreater(), getOperatorLess(), getOperatorGreaterEqual(), getOperatorLessEqual());
    }
    
    /**
     * Find the first math operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition firstMathOperator(String script) {
        return firstOperator(script, getOperatorAddition(), getOperatorSubtraction(),
                getOperatorMultiplication(), getOperatorDivision(), getOperatorModules());
    }

    /**
     * Find the last math operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition lastMathOperator(String script) {
        return lastOperator(script, getOperatorAddition(), getOperatorSubtraction(),
                getOperatorMultiplication(), getOperatorDivision(), getOperatorModules());
    }

    /**
     * Find the first operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition firstOperator(String script, Pattern... patterns) {
        SyntaxPosition first = null;
        SyntaxPosition[] syntaxPositions = new SyntaxPosition[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            syntaxPositions[i] = matcherPosition(script, patterns[i]);
        }
        first = new SyntaxPosition(this, Pattern.compile(""));
        for (int i = 0; i < syntaxPositions.length; i++) {
            if ((syntaxPositions[i].getStart() > -1 && (first.getStart() == -1 || syntaxPositions[i].getStart() < first.getStart()))
                    || (syntaxPositions[i].getStart() == first.getStart() && syntaxPositions[i].getEnd() > first.getEnd())) {
                first = syntaxPositions[i];
            }
        }
        return first;
    }

    /**
     * Find the last operator.
     * @param script Script where find the operator.
     * @return Position.
     */
    public SyntaxPosition lastOperator(String script, Pattern... patterns) {
        SyntaxPosition syntaxPositionFinal = null;
        syntaxPositionFinal = new SyntaxPosition(this, Pattern.compile(""));
        while (true) {
            SyntaxPosition syntaxPosition = firstOperator(script, patterns);
            String spaces = "";
            if (syntaxPosition.getStart() != -1) {
                for (int k = syntaxPosition.getStart(); k < syntaxPosition.getEnd(); k++) {
                    spaces = spaces.concat(" ");
                }
            }
            if (syntaxPosition.getStart() != -1) {
                script = script.substring(0, syntaxPosition.getStart()) + spaces + script.substring(syntaxPosition.getEnd());
                syntaxPositionFinal = syntaxPosition;
            } else {
                return syntaxPositionFinal;
            }
        }
    }
}