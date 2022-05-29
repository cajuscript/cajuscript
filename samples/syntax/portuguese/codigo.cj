caju.syntax: PT

importa java.lang

// IF = SE
x = 10
se x < 10 e x > 0 faz
    System.out.println("X menor que 10!")
ou se x > 10 e x ! 10 faz
    System.out.println("X maior que 10!")
ou se x = 10 ou x ! 0 faz
    System.out.println("X igual 10!")
ou senao faz
    System.out.println("X menor que 1!")
fim

// LOOP = ENQUANTO
x = 0
enquanto x < 100 e x >= 0 faz
    System.out.println(x)
    x += 1
    se x = 10 faz
        para
    ou senao faz
        continua
    fim
fim

// FUNCTION = FUNCAO
x = 5
funcao addWithX(v1, v2) faz
    retorna raiz.x + v1 + v2
fim
x = addWithX(10, 20)
System.out.println("X = "+ x)

// TRY/CATCH = TENTA/PEGA
tenta e faz
    "".substring(0, -1)
pega o erro
    System.out.println("Erro: "+ e.getMessage())
finalmente
    System.out.println("Finalmente...")
fim

System.out.println("\n\nSyntax em portugues executada com sucesso!\n")

Thread.sleep(10000)