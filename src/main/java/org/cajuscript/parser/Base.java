/*
 * Base.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.cajuscript.CajuScript;
import org.cajuscript.Context;
import org.cajuscript.Syntax;
import org.cajuscript.SyntaxPosition;
import org.cajuscript.Value;
import org.cajuscript.CajuScriptException;
import org.cajuscript.compiler.Executable;
import org.cajuscript.parser.Operation.Operator;

/**
 * Base to do script parse.
 * @author eduveks
 */
public class Base implements Element, java.io.Serializable, Cloneable {
    protected List<Element> elements = new ArrayList<Element>();
    protected LineDetail baseLineDetail = null;
    private static long varsGroupCounter = 0;
    private static long varsMathCounter = 0;
    protected Executable executable = null;
    
    /**
     * Base
     * @param line Line detail
     */
    public Base(LineDetail line) {
        this.baseLineDetail = line;
    }

    /**
     * Base
     * @param executable Executable
     */
    public Base(Executable executable) {
        this.executable = executable;
    }
    
    /**
     * Elements
     * @return All child elements
     */
    public List<Element> getElements() {
        return elements;
    }
    
    /**
     * Append child element.
     * @param element Element
     */
    public void addElement(Element element) {
        elements.add(element);
    }
    
    /**
     * Remove child element.
     * @param element Element
     */
    public void removeElement(Element element) {
        elements.remove(element);
    }
    
    /**
     * Get line detail.
     * @return Line detail
     */
    public LineDetail getLineDetail() {
        return baseLineDetail;
    }
    
    /**
     * If element can return value.
     * @param element Element
     * @return If element can return.
     */
    public boolean canElementReturn(Element element) {
        if (!(element instanceof Command) && !(element instanceof Operation) && !(element instanceof Variable)) {
            return true;
        }
        return false;
    }
    
    /**
     * Executed this element and all childs elements.
     * @param caju CajuScript
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        for (Element element : elements) {
            Value v = element.execute(caju, context, syntax);
            if (v != null && canElementReturn(element)) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Script parse.
     * @param caju CajuScript instance
     * @param script Script to be parsed
     * @param syntax Syntax style of the script
     * @throws org.cajuscript.CajuScriptException Errors ocurred on parsing
     */
    public void parse(CajuScript caju, String script, Syntax syntax) throws CajuScriptException {
        parse(null, caju, null, script, syntax);
    }
    private LineDetail parseLastLineDetail = null;
    private void parse(Element base, CajuScript caju, LineDetail lineDetail, String script, Syntax syntax) throws CajuScriptException {
        if (base == null) {
            base = this;
        }
        String[] lines = script.split(CajuScript.SUBLINE_LIMITER);
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y].trim();
            lineDetail = loadLineDetail(line);
            line = lineDetail.getContent().trim();
            String label = "";
            SyntaxPosition syntaxPosition = null;
            int sublineCountCall = 0;
            for (int z = y + 1; z < lines.length; z++) {
                String subline = lines[z].trim();
                LineDetail sublineDetail = loadLineDetail(subline);
                String sublineContent = sublineDetail.getContent().trim();
                if (syntax.lastOperator(
                        line,
                        syntax.getFunctionCallParametersBegin(),
                        syntax.getArrayCallParametersBegin()
                ).getEnd() == line.length()) {
                    sublineCountCall += 1;
                }
                if (sublineCountCall > 0 || sublineContent.isEmpty()
                        || syntax.firstMathOperator(sublineContent).getStart() == 0
                        || syntax.lastMathOperator(line).getEnd() == line.length()
                        || syntax.firstLogicalOperator(sublineContent).getStart() == 0
                        || syntax.lastLogicalOperator(line).getEnd() == line.length()
                        || syntax.firstConditionalOperator(sublineContent).getStart() == 0
                        || syntax.lastConditionalOperator(line).getEnd() == line.length()
                        || syntax.firstOperator(
                                sublineContent,
                                syntax.getFunctionCallPathSeparator(),
                                syntax.getFunctionCallParametersSeparator(),
                                syntax.getArrayCallParametersSeparator()
                            ).getStart() == 0
                        || syntax.lastOperator(
                                line,
                                syntax.getFunctionCallPathSeparator(),
                                syntax.getFunctionCallParametersSeparator(),
                                syntax.getArrayCallParametersSeparator()
                            ).getEnd() == line.length()
                ) {
                    if (syntax.firstOperator(
                            sublineContent,
                            syntax.getFunctionCallParametersEnd(),
                            syntax.getArrayCallParametersEnd()
                    ).getStart() == 0) {
                        sublineCountCall -= 1;
                    }
                    lineDetail.appendContent(sublineContent);
                    line += sublineContent;
                    y++;
                } else {
                    break;
                }
            }
            if ((syntaxPosition = syntax.matcherPosition(line, syntax.getLabel())).getStart() == 0) {
                label = syntaxPosition.getGroup();
                line = line.substring(syntaxPosition.getEnd()).trim();
            }
            if ((syntaxPosition = syntax.matcherPosition(line, syntax.getReturn())).getStart() == 0) {
                if (syntax.matcherEquals(line, syntax.getReturn())) {
                    base.addElement(new Return(lineDetail));
                } else {
                    Return r = new Return(lineDetail);
                    r.setValue(evalValue(base, caju, lineDetail, syntax, line.substring(syntaxPosition.getEnd())));
                    base.addElement(r);
                }
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getIf())).getStart() == 0) {
                SyntaxPosition syntaxPositionIf = syntaxPosition;
                String scriptIFCondition = syntaxPositionIf.getGroup();
                StringBuilder scriptIF = new StringBuilder();
                List<String> ifsConditions = new ArrayList<String>();
                List<String> ifsStatements = new ArrayList<String>();
                int ifLevel = 0;
                boolean ifClosed = false;
                for (int z = y + 1; z < lines.length; z++) {
                    y++;
                    String originalIFline = lines[z];
                    String scriptIFline = originalIFline.trim();
                    LineDetail _lineDetail = loadLineDetail(scriptIFline);
                    scriptIFline = _lineDetail.getContent().trim();
                    SyntaxPosition syntaxPositionElseIf = null;
                    if (ifLevel == 0 && (syntaxPositionElseIf = syntax.matcherPosition(scriptIFline, syntax.getElseIf())).getStart() == 0) {
                        ifsConditions.add(scriptIFCondition);
                        ifsStatements.add(scriptIF.toString());
                        String condition = syntaxPositionElseIf.getGroup();
                        if (condition.trim().length() == 0) {
                            condition = "true";
                        }
                        scriptIFCondition = condition;
                        scriptIF.delete(0, scriptIF.length());
                        continue;
                    } else if (ifLevel == 0 && (syntaxPositionElseIf = syntax.matcherPosition(scriptIFline, syntax.getElse())).getStart() == 0) {
                        ifsConditions.add(scriptIFCondition);
                        ifsStatements.add(scriptIF.toString());
                        scriptIFCondition = "true";
                        scriptIF.delete(0, scriptIF.length());
                        continue;
                    } else if (isStatementBegins(scriptIFline, syntax)) {
                        ifLevel++;
                    } else if (isStatementEnds(scriptIFline, syntax)) {
                        if (ifLevel == 0) {
                            ifsConditions.add(scriptIFCondition);
                            ifsStatements.add(scriptIF.toString());
                            ifClosed = true;
                            break;
                        }
                        ifLevel--;
                    }
                    scriptIF.append(originalIFline);
                    scriptIF.append(CajuScript.SUBLINE_LIMITER);
                }
                if (ifLevel != 0 || !ifClosed) {
                    throw CajuScriptException.create(caju, caju.getContext(), "\"If\" statement sintax error, maybe any \"if\" statement was not closed.");
                }
                IfGroup ifGroup = new IfGroup(lineDetail);
                for (int i = 0; i < ifsConditions.size(); i++) {
                    String _ifCondition = ifsConditions.get(i);
                    String _ifContent = ifsStatements.get(i);
                    _ifCondition = _ifCondition.trim();
                    If _if = new If(lineDetail);
                    Variable var = new Variable(lineDetail);
                    var.setValue(evalValue(var, caju, lineDetail, syntax, _ifCondition));
                    _if.setCondition(var);
                    parse(_if, caju, lineDetail, _ifContent, syntax);
                    ifGroup.addElement(_if);
                }
                base.addElement(ifGroup);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getLoop())).getStart() == 0) {
                SyntaxPosition syntaxPositionLoop = syntaxPosition;
                String scriptLOOPCondition = syntaxPositionLoop.getGroup();
                StringBuilder scriptLOOP = new StringBuilder();
                int loopLevel = 0;
                boolean loopClosed = false;
                for (int z = y + 1; z < lines.length; z++) {
                    y++;
                    String originalLOOPline = lines[z];
                    String scriptLOOPline = originalLOOPline.trim();
                    LineDetail _lineDetail = loadLineDetail(scriptLOOPline);
                    scriptLOOPline = _lineDetail.getContent().trim();
                    if (isStatementBegins(scriptLOOPline, syntax)) {
                        loopLevel++;
                    } else if (isStatementEnds(scriptLOOPline, syntax)) {
                        if (loopLevel == 0) {
                            loopClosed = true;
                            break;
                        }
                        loopLevel--;
                    }
                    scriptLOOP.append(originalLOOPline);
                    scriptLOOP.append(CajuScript.SUBLINE_LIMITER);
                }
                if (loopLevel != 0 || !loopClosed) {
                    throw CajuScriptException.create(caju, caju.getContext(), "\"Loop\" statement sintax error, maybe any \"loop\" statement was not closed.");
                }
                Loop loop = new Loop(lineDetail);
                loop.setLabel(label);
                Variable var = new Variable(lineDetail);
                var.setValue(evalValue(var, caju, lineDetail, syntax, scriptLOOPCondition));
                loop.setCondition(var);
                parse(loop, caju, lineDetail, scriptLOOP.toString(), syntax);
                base.addElement(loop);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getFunction())).getStart() == 0) {
                String scriptFuncDef = syntaxPosition.getGroup();
                StringBuilder scriptFUNC = new StringBuilder();
                int funcLevel = 0;
                boolean funcClosed = false;
                for (int z = y + 1; z < lines.length; z++) {
                    y++;
                    String originalFUNCline = lines[z];
                    String scriptFUNCline = originalFUNCline.trim();
                    LineDetail _lineDetail = loadLineDetail(scriptFUNCline);
                    scriptFUNCline = _lineDetail.getContent().trim();
                    if (syntax.matcherPosition(scriptFUNCline, syntax.getFunction()).getStart() == 0) {
                        funcLevel++;
                    } else if (isStatementBegins(scriptFUNCline, syntax)) {
                        funcLevel++;
                    } else if (isStatementEnds(scriptFUNCline, syntax)) {
                        if (funcLevel == 0) {
                            funcClosed = true;
                            break;
                        }
                        funcLevel--;
                    }
                    scriptFUNC.append(originalFUNCline);
                    scriptFUNC.append(CajuScript.SUBLINE_LIMITER);
                }
                if (funcLevel != 0 || !funcClosed) {
                    throw CajuScriptException.create(caju, caju.getContext(), "\"Function\" statement sintax error, maybe any \"function\" statement was not closed.");
                }
                Function func = new Function(lineDetail);
                func.setDefinition(scriptFuncDef, syntax);
                parse(func, caju, lineDetail, scriptFUNC.toString(), syntax);
                caju.setFunc(func.getName(), func);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getTry())).getStart() == 0) {
                String scriptTRYCATCHerrorVar = syntaxPosition.getGroup();
                StringBuilder scriptTRY = new StringBuilder();
                StringBuilder scriptCATCH = new StringBuilder();
                StringBuilder scriptFINALLY = new StringBuilder();
                boolean isTry = true;
                boolean isCatch = false;
                boolean isFinally = false;
                int tryLevel = 0;
                boolean tryClosed = false;
                for (int z = y + 1; z < lines.length; z++) {
                    y++;
                    String originalTRYCATCHline = lines[z];
                    String scriptTRYCATCHline = originalTRYCATCHline.trim();
                    LineDetail _lineDetail = loadLineDetail(scriptTRYCATCHline);
                    scriptTRYCATCHline = _lineDetail.getContent().trim();
                    if (tryLevel == 0 && syntax.matcherPosition(scriptTRYCATCHline, syntax.getTryCatch()).getStart() == 0) {
                        isTry = false;
                        isCatch = true;
                        isFinally = false;
                        continue;
                    } else if (tryLevel == 0 && syntax.matcherPosition(scriptTRYCATCHline, syntax.getTryFinally()).getStart() == 0) {
                        isTry = false;
                        isCatch = false;
                        isFinally = true;
                        continue;
                    } else if (isStatementBegins(scriptTRYCATCHline, syntax)) {
                        tryLevel++;
                    } else if (isStatementEnds(scriptTRYCATCHline, syntax)) {
                        if (tryLevel == 0) {
                            tryClosed = true;
                            break;
                        }
                        tryLevel--;
                    }
                    if (isTry) {
                        scriptTRY.append(originalTRYCATCHline);
                        scriptTRY.append(CajuScript.SUBLINE_LIMITER);
                    } else if (isCatch) {
                        scriptCATCH.append(originalTRYCATCHline);
                        scriptCATCH.append(CajuScript.SUBLINE_LIMITER);
                    } else if (isFinally) {
                        scriptFINALLY.append(originalTRYCATCHline);
                        scriptFINALLY.append(CajuScript.SUBLINE_LIMITER);
                    }
                }
                if (tryLevel != 0 || !tryClosed) {
                    throw CajuScriptException.create(caju, caju.getContext(), "\"Try\" statement sintax error, maybe any \"try\" statement was not closed.");
                }
                TryCatch tryCatch = new TryCatch(lineDetail);
                Variable error = new Variable(lineDetail);
                error.setKey(scriptTRYCATCHerrorVar.trim());
                Base _try = new Base(lineDetail);
                parse(_try, caju, lineDetail, scriptTRY.toString(), syntax);
                Base _catch = new Base(lineDetail);
                parse(_catch, caju, lineDetail, scriptCATCH.toString(), syntax);
                Base _finally = new Base(lineDetail);
                parse(_finally, caju, lineDetail, scriptFINALLY.toString(), syntax);
                tryCatch.setError(error);
                tryCatch.setTry(_try);
                tryCatch.setCatch(_catch);
                tryCatch.setFinally(_finally);
                base.addElement(tryCatch);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getOperatorEqual())).getStart() > 0) {
                try {
                    int p = syntaxPosition.getStart();
                    String keys = line.substring(0, p);
                    String value = line.substring(syntaxPosition.getEnd(), line.length());
                    
                    SyntaxPosition syntaxPositionOperator = syntax.lastMathOperator(keys);
                    if (syntaxPositionOperator.getEnd() == keys.length()) {
                        p = syntaxPositionOperator.getStart();
                    }
                    String[] allKeys = keys.substring(0, p).replaceAll(" ", "").split(",");
                    for (String key : allKeys) {
                        Variable var = new Variable(lineDetail);
                        var.setType(label);
                        var.setKey(key);
                        if (syntaxPositionOperator.getOperator() != null) {
                            Command v = new Command(lineDetail);
                            v.setCommand(key);
                            Operation operation = new Operation(lineDetail);
                            operation.setCommands(v, syntaxPositionOperator.getOperator(), evalValue(base, caju, lineDetail, syntax, value));
                            var.setValue(operation);
                        } else {
                            var.setValue(evalValue(base, caju, lineDetail, syntax, value));
                        }
                        base.addElement(var);
                    }
                } catch (CajuScriptException e) {
                    throw e;
                } catch (Exception e) {
                    throw CajuScriptException.create(caju, caju.getContext(), "Incorrect definition", e);
                }
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getImport())).getStart() == 0) {
                String path = line.substring(syntaxPosition.getEnd()).trim();
                Import i = new Import(lineDetail);
                i.setPath(path);
                base.addElement(i);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getContinue())).getStart() == -1
                    && (syntaxPosition = syntax.matcherPosition(line, syntax.getBreak())).getStart() == 0) {
                Break b = new Break(lineDetail);
                b.setLabel(line.substring(syntaxPosition.getEnd()).trim());
                base.addElement(b);
            } else if ((syntaxPosition = syntax.matcherPosition(line, syntax.getBreak())).getStart() == -1
                    && (syntaxPosition = syntax.matcherPosition(line, syntax.getContinue())).getStart() == 0) {
                Continue c = new Continue(lineDetail);
                c.setLabel(line.substring(syntaxPosition.getEnd()).trim());
                base.addElement(c);
            } else {
                if (line.length() != 0) {
                    base.addElement(evalValue(base, caju, lineDetail, syntax, line));
                }
            }
        }
    }
    
    private LineDetail loadLineDetail(String line) {
        if (line.startsWith(CajuScript.LINE_DETAIL_START)) {
            int indexLineInfoEnd = line.indexOf(CajuScript.LINE_DETAIL_END);
            int lineNumber = Integer.parseInt(line.substring(CajuScript.LINE_DETAIL_START.length(), indexLineInfoEnd));
            line = line.substring(indexLineInfoEnd + CajuScript.LINE_DETAIL_END.length());
            parseLastLineDetail = new LineDetail(lineNumber, line);
            return parseLastLineDetail;
        } else {
            return new LineDetail(parseLastLineDetail.getNumber(), line);
        }
    }
    
    private boolean isStatementBegins(String line, Syntax syntax) {
        if (syntax.matcherPosition(line, syntax.getIf()).getStart() == 0) {
            return true;
        } else if (syntax.matcherPosition(line, syntax.getLoop()).getStart() == 0) {
            return true;
        } else if (syntax.matcherPosition(line, syntax.getTry()).getStart() == 0) {
            return true;
        } else if (syntax.matcherPosition(line, syntax.getFunction()).getStart() == 0) {
            return true;
        }
        return false;
    }
    
    private boolean isStatementEnds(String line, Syntax syntax) {
        if (syntax.matcherEquals(line, syntax.getIfEnd())) {
            return true;
        } else if (syntax.matcherEquals(line, syntax.getLoopEnd())) {
            return true;
        } else if (syntax.matcherEquals(line, syntax.getTryEnd())) {
            return true;
        } else if (syntax.matcherEquals(line, syntax.getFunctionEnd())) {
            return true;
        }
        return false;
    }
    
    private Element condition(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script) throws CajuScriptException {
        try {
            script = script.trim();
            SyntaxPosition syntaxPositionLogical = syntax.firstLogicalOperator(script);
            if (syntaxPositionLogical.getStart() > -1) {
                Operation o = new Operation(lineDetail);
                o.setCommands(condition(base, caju, lineDetail, syntax, script.substring(0, syntaxPositionLogical.getStart())), syntaxPositionLogical.getOperator(), condition(base, caju, lineDetail, syntax, script.substring(syntaxPositionLogical.getEnd())));
                return o;
            } else if (script.length() != 0) {
                SyntaxPosition syntaxPosition = syntax.firstConditionalOperator(script);
                if (syntaxPosition.getStart() > -1) {
                    Element e1 = evalValue(base, caju, lineDetail, syntax, script.substring(0, syntaxPosition.getStart()));
                    Element e2 = evalValue(base, caju, lineDetail, syntax, script.substring(syntaxPosition.getEnd()));
                    Operation o = new Operation(lineDetail);
                    o.setCommands(e1, syntaxPosition.getOperator(), e2);
                    return o;
                } else {
                    return evalValue(base, caju, lineDetail, syntax, script);
                }
            }
            throw CajuScriptException.create(caju, caju.getContext(), "Sintax error");
        } catch (CajuScriptException e) {
            throw e;
        } catch (Exception e) {
            throw CajuScriptException.create(caju, caju.getContext(), "Sintax error", e);
        }
    }

    private Element evalValue(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script) throws CajuScriptException {
        return evalValueGroup(base, caju, lineDetail, syntax, script, false);
    }

    private Element evalValue(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script, boolean isArray) throws CajuScriptException {
        return evalValueGroup(base, caju, lineDetail, syntax, script, true);
    }
    
    private Element evalValueSingle(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script) throws CajuScriptException {
        try {
            script = script.trim();
            SyntaxPosition firstOperator = syntax.firstMathOperator(script);
            if (firstOperator.getStart() > -1 && !syntax.matcherEquals(script, syntax.getNumber())) {
                if (firstOperator.getStart() == 0) {
                    firstOperator = syntax.firstMathOperator(script.substring(firstOperator.getEnd()));
                }
                SyntaxPosition priorityOperator = syntax.firstOperator(script, syntax.getOperatorMultiplication(), syntax.getOperatorDivision(), syntax.getOperatorModules());
                if (priorityOperator.getStart() > firstOperator.getStart()) {
                    SyntaxPosition syntaxOperator1 = syntax.lastMathOperator(script.substring(0, priorityOperator.getStart()));
                    String script1 = syntaxOperator1.getStart() > -1 ? script.substring(0, syntaxOperator1.getStart()) : "";
                    Operator operator1 = syntaxOperator1.getOperator();
                    String scriptValue1 = script.substring(syntaxOperator1.getEnd() > -1 ? syntaxOperator1.getEnd() : 0, priorityOperator.getStart());
                    Operation.Operator operator = priorityOperator.getOperator();
                    SyntaxPosition syntaxOperator2 = syntax.firstMathOperator(script.substring(priorityOperator.getEnd()));
                    String scriptValue2 = script.substring(priorityOperator.getEnd(), syntaxOperator2.getStart() > -1 ? syntaxOperator2.getStart() : script.length());
                    Operator operator2 = syntaxOperator2.getOperator();
                    String script2 = syntaxOperator2.getEnd() > -1 ? script.substring(syntaxOperator2.getEnd()) : "";
                    Command c1 = new Command(lineDetail);
                    c1.setCommand(scriptValue1);
                    Command c2 = new Command(lineDetail);
                    c2.setCommand(scriptValue2);
                    Operation o = new Operation(lineDetail);
                    o.setCommands(c1, operator, c2);
                    if (scriptValue1.length() == 0 && scriptValue2.length() == 0) {
                        return o;
                    }
                    Element e1 = o;
                    if (operator1 != null) {
                        Operation o1 = new Operation(lineDetail);
                        o1.setCommands(evalValueSingle(base, caju, lineDetail, syntax, script1), operator1, o);
                        if (operator2 == null) {
                            return o1;
                        }
                        e1 = o1;
                    }
                    Operation o2 = new Operation(lineDetail);
                    o2.setCommands(e1, operator2, evalValueSingle(base, caju, lineDetail, syntax, script2));
                    return o2;
                }
                Command value1 = new Command(lineDetail);
                value1.setCommand(script.substring(0, firstOperator.getStart()));
                String scriptFinal = script.substring(firstOperator.getEnd());
                SyntaxPosition value2Operator = syntax.firstMathOperator(scriptFinal);
                if (value2Operator.getStart() > -1) {
                    Command value2 = new Command(lineDetail);
                    value2.setCommand(scriptFinal.substring(0, value2Operator.getStart()));
                    if (varsMathCounter == Long.MAX_VALUE) {
                        varsMathCounter = 0;
                    }
                    Operation o = new Operation(lineDetail);
                    o.setCommands(value1, firstOperator.getOperator(), value2);
                    String varParamKey = CajuScript.CAJU_VARS_MATH.concat(caju.nextVarsCounter()).concat(Long.toString(varsMathCounter));
                    varsMathCounter++;
                    Variable varParam = new Variable(lineDetail);
                    varParam.setKey(varParamKey);
                    varParam.setValue(o);
                    base.addElement(varParam);
                    return evalValueSingle(base, caju, lineDetail, syntax, varParamKey.concat(scriptFinal.substring(value2Operator.getStart())));
                } else {
                    Operation operation = new Operation(lineDetail);
                    Command value2 = new Command(lineDetail);
                    value2.setCommand(script.substring(firstOperator.getEnd()));
                    operation.setCommands(value1, firstOperator.getOperator(), value2);
                    return operation;
                }
            } else {
                Command cmd = new Command(lineDetail);
                cmd.setCommand(script.trim());
                return cmd;
            }
        } catch (CajuScriptException e) {
            throw e;
        } catch (Exception e) {
            throw CajuScriptException.create(caju, caju.getContext(), "Sintax error", e);
        }
    }

    private Element evalValueGroup(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script) throws CajuScriptException {
        return evalValueGroup(base, caju, lineDetail, syntax, script, false);
    }

    private Element evalValueGroup(Element base, CajuScript caju, LineDetail lineDetail, Syntax syntax, String script, boolean isArray) throws CajuScriptException {
        if (varsGroupCounter == Long.MAX_VALUE) {
            varsGroupCounter = 0;
        }
        String scriptBackup = script;
        if (isArray) {
            SyntaxPosition arrayBegin = syntax.matcherPosition(script, syntax.getArrayCallParametersBegin());
            SyntaxPosition arrayEnd = syntax.matcherLastPosition(script, syntax.getArrayCallParametersEnd());
            if (arrayBegin.getEnd() > -1 && arrayEnd.getStart() > -1) {
                script = script.substring(arrayBegin.getEnd(), arrayEnd.getStart());
            }
        }
        SyntaxPosition syntaxPosition = null;
        if ((syntaxPosition = syntax.matcherPosition(script, syntax.getFunctionCall())).getStart() > -1 || (syntaxPosition = syntax.matcherPosition(script, syntax.getArrayCall())).getStart() > -1) {
            Pattern callParametersBegin = null;
            Pattern callParametersEnd = null;
            Pattern callParametersSeparator = null;
            if (syntaxPosition.getPatternHashCode() == SyntaxPosition.getPatternHashCode(syntax.getFunctionCall())) {
                callParametersBegin = syntax.getFunctionCallParametersBegin();
                callParametersEnd = syntax.getFunctionCallParametersEnd();
                callParametersSeparator = syntax.getFunctionCallParametersSeparator();
            } else if (syntaxPosition.getPatternHashCode() == SyntaxPosition.getPatternHashCode(syntax.getArrayCall())) {
                callParametersBegin = syntax.getArrayCallParametersBegin();
                callParametersEnd = syntax.getArrayCallParametersEnd();
                callParametersSeparator = syntax.getArrayCallParametersSeparator();
            }
            String varKey = CajuScript.CAJU_VARS_GROUP.concat(caju.nextVarsCounter()).concat(Long.toString(varsGroupCounter));
            varsGroupCounter++;
            Variable var = new Variable(lineDetail);
            var.setKey(varKey);
            Command c = new Command(lineDetail);
            String cmd = syntaxPosition.getGroup();
            String functionName = cmd.substring(0, syntax.matcherPosition(cmd, callParametersBegin).getStart());
            SyntaxPosition syntaxFixOperator = syntax.lastLogicalOperator(functionName);
            if (syntaxFixOperator.getEnd() > -1) {
                syntaxPosition.setStart(syntaxPosition.getStart() + syntaxFixOperator.getEnd());
                cmd = cmd.substring(syntaxFixOperator.getEnd());
            }
            syntaxFixOperator = syntax.lastConditionalOperator(functionName);
            if (syntaxFixOperator.getEnd() > -1) {
                syntaxPosition.setStart(syntaxPosition.getStart() + syntaxFixOperator.getEnd());
                cmd = cmd.substring(syntaxFixOperator.getEnd());
            }
            syntaxFixOperator = syntax.lastMathOperator(functionName);
            if (syntaxFixOperator.getEnd() > -1) {
                syntaxPosition.setStart(syntaxPosition.getStart() + syntaxFixOperator.getEnd());
                cmd = cmd.substring(syntaxFixOperator.getEnd());
            }
            String cmdBase = cmd;
            SyntaxPosition syntaxParameterBegin = syntax.matcherPosition(cmdBase, callParametersBegin);
            SyntaxPosition syntaxParameterEnd = syntax.matcherPosition(cmdBase, callParametersEnd);
            if (syntaxParameterBegin.getStart() > -1 && syntaxParameterBegin.getStart() < syntaxParameterEnd.getStart()) {
                String params = cmdBase.substring(syntaxParameterBegin.getEnd(), syntaxParameterEnd.getStart());
                cmd = cmd.substring(0, syntaxParameterBegin.getEnd());
                while(true) {
                    SyntaxPosition syntaxPositionParam = syntax.matcherPosition(params, callParametersSeparator);
                    int lenParamSeparatorStart = syntaxPositionParam.getStart();
                    int lenParamSeparatorEnd = syntaxPositionParam.getEnd();
                    if (lenParamSeparatorEnd == -1) {
                        lenParamSeparatorStart = params.length();
                        lenParamSeparatorEnd = params.length();
                    }
                    
                    if (params.trim().length() != 0) {
                        if (varsGroupCounter == Long.MAX_VALUE) {
                            varsGroupCounter = 0;
                        }
                        String varParamKey = CajuScript.CAJU_VARS_GROUP.concat(caju.nextVarsCounter()).concat(Long.toString(varsGroupCounter));
                        varsGroupCounter++;
                        Variable varParam = new Variable(lineDetail);
                        String paramCmdContent = params.substring(0, lenParamSeparatorStart);
                        if ((syntaxPosition = syntax.matcherPosition(paramCmdContent, syntax.getLabel())).getStart() > -1) {
                            varParam.setType(syntaxPosition.getGroup());
                            paramCmdContent = paramCmdContent.substring(syntaxPosition.getEnd()).trim();
                        }
                        varParam.setKey(varParamKey);
                        varParam.setValue(evalValueGroup(base, caju, lineDetail, syntax, paramCmdContent));
                        base.addElement(varParam);
                        if (syntaxPositionParam.getStart() == -1) {
                            cmd = cmd.concat(varParamKey);
                        } else {
                            cmd = cmd.concat(varParamKey).concat(params.substring(lenParamSeparatorStart, lenParamSeparatorEnd));
                        }
                        
                        params = params.substring(lenParamSeparatorEnd);
                    }
                    if (syntaxPositionParam.getStart() == -1) {
                        break;
                    }
                }
                cmd = cmd.concat(cmdBase.substring(syntaxParameterEnd.getStart()));
            }
            c.setCommand(cmd);
            var.setValue(c);
            base.addElement(var);
            int cmdReplacementStart = script.indexOf(cmdBase);
            if (cmdReplacementStart > -1) {
                script = script.substring(0, cmdReplacementStart) +
                        varKey +
                        script.substring(cmdReplacementStart + cmdBase.length());
            }
            return evalValueGroup(base, caju, lineDetail, syntax, script);
        } else if ((syntaxPosition = syntax.matcherPosition(script, syntax.getGroup())).getStart() > -1 || (syntaxPosition = syntax.matcherPosition(script, syntax.getArray())).getStart() > -1) {
            String varKey = CajuScript.CAJU_VARS_GROUP.concat(caju.nextVarsCounter()).concat(Long.toString(varsGroupCounter));
            varsGroupCounter++;
            Variable var = new Variable(lineDetail);
            var.setKey(varKey);
            var.setValue(evalValue(base, caju, lineDetail, syntax, syntaxPosition.getGroup(), syntaxPosition.getPatternHashCode() == SyntaxPosition.getPatternHashCode(syntax.getArray())));
            base.addElement(var);
            return evalValueGroup(base, caju, lineDetail, syntax, script.replace((CharSequence)syntaxPosition.getAllContent(), (CharSequence)varKey));
        } else {
            if (isArray) {
                script = scriptBackup;
            }
            if ((syntaxPosition = syntax.firstConditionalOperator(script)).getStart() > -1
                || (syntaxPosition = syntax.firstLogicalOperator(script)).getStart() > -1) {
                return condition(base, caju, lineDetail, syntax, script);
            }
            return evalValueSingle(base, caju, lineDetail, syntax, script);
        }
    }
    
    /**
     * Cleaning forced in memory allocation.
     */
    public void clear() {
        for (Element element : elements) {
            element.clear();
        }
    }
    
    /**
     * Clone parser.
     * @return Parser cloned.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error ("Cannot clone this object.");
        }
    }
    
    /**
     * Clone parser with serialization.
     * @return Parser cloned.
     */
    public Object cloneSerialization() {
        java.io.ObjectOutputStream oos = null;
        java.io.ObjectInputStream ois = null;
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(bos.toByteArray());
            ois = new java.io.ObjectInputStream(bin);
            return ois.readObject();
        } catch(Exception e) {
            throw new Error ("Cannot clone this object.", e);
        } finally {
            try {
                oos.close();
                ois.close();
            } catch(Exception e) { }
        }
    }
}
