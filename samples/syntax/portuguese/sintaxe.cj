$java.util.regex

syntaxPT = org.cajuscript.Syntax()
syntaxPT.setIf(Pattern.compile("^[\\s+s|s]e\\s*([\\s+|[\\s*\\(]].+)\\s*faz"))
syntaxPT.setElseIf(Pattern.compile("^[\\s+o|o]u\s+se\\s*([\\s+|[\\s*\\(]].+)\\s*faz"))
syntaxPT.setElse(Pattern.compile("^[\\s+o|o]u\\s+senao\\s+fa[z\\s+|z]$"))
syntaxPT.setIfEnd(Pattern.compile("^[\\s+f|f]i[m\\s+|m]$"))
syntaxPT.setLoop(Pattern.compile("^[\\s+e|e]nquanto\\s*([\\s+|[\\s*\\(]].+)\\s*faz"))
syntaxPT.setLoopEnd(Pattern.compile("^[\\s+f|f]i[m\\s+|m]$"))
syntaxPT.setTry(Pattern.compile("^[\\s+t|t]enta\\s*([\\s+|[\\s*\\(]].+)\\s*faz"))
syntaxPT.setTryCatch(Pattern.compile("^[\\s+p|p]ega\\s+o\\s+err[o\\s+|o]$"))
syntaxPT.setTryFinally(Pattern.compile("^[\\s+f|f]inalment[e\\s+|e]$"))
syntaxPT.setTryEnd(Pattern.compile("^[\\s+f|f]i[m\\s+|m]$"))
syntaxPT.setFunction(Pattern.compile("^[\\s+f|f]uncao\\s*([\\s+|[\\s*\\(]].+)\\s*faz"))
syntaxPT.setFunctionEnd(Pattern.compile("^[\\s+f|f]i[m\\s+|m]$"))
syntaxPT.setNull(Pattern.compile("nulo"))
syntaxPT.setReturn(Pattern.compile("retorna"))
syntaxPT.setImport(Pattern.compile("importa\\s+"))
syntaxPT.setRootContext(Pattern.compile("raiz\\."))
syntaxPT.setContinue(Pattern.compile("continua"))
syntaxPT.setBreak(Pattern.compile("para"))
syntaxPT.setOperatorOr(Pattern.compile("[\\s+|)\\s*]ou[\\s+|(\\s*]"))
syntaxPT.setOperatorAnd(Pattern.compile("[\\s+|)\\s*]e[\\s+|(\\s*]"))

org.cajuscript.CajuScript.addGlobalSyntax("PT", syntaxPT)

$"codigo.cj"