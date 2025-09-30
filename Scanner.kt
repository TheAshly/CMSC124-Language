class Tokens {
    companion object {
        val UPPERCASE = (('A'..'Z') + 'Ñ').toSet()
        val LOWERCASE = (('a'..'z') + 'ñ').toSet()
        val COMMENT = '>'
        val EOL = '.'
        val NUMBER = (('0'..'9')).toSet()


        val UNARY = setOf("not", "positive", "negative")
        val LOGICAL = setOf("or", "and")
        val STRINGTYPE = "refer"
        val INTTYPE = "equals"
        val COMPARISON = setOf("equal", "greater than", "less than", "not equal",
                                "greater than or equal", "less than equal")
        val ARITHMETIC = setOf("add", "subtract", "multiply", "divide", "modulo")
        val BOOLEAN = setOf("factual", "faulty")
        val BOOLEANSIGNIFIER = "it is"
        val RANGESIGNIFIER = "in"
        val ASSIGNCOMPARE = "to"

        val CONTINUE = "Please repeat what is stated"
        val BREAK = "Lets move on"
        val INCREMENT = "Increment"
        val DECREMENT = "Decrement"
        val KEYWORDS = setOf("Dear", "If", "Else", "Otherwise if", "For", "While", "Sincerely")
    }
}

class Scanner {

    var head: Int = 0
    var lexeme: String = ""
    var line: String = ""
    var lineNum: Int = 1 //Temp Line variable

    fun checkChar(): Char? {
        try {
            return line[head]
        } catch (e: Exception) {
            return null;
        }
    }

    fun scanLine(line: String) {
        this.head = 0
        this.line = line
        this.lineNum = 1 //Temp Line variable

        while (true) {
            var char = checkChar()

            if (char in Tokens.UPPERCASE) {
                titleScanner()
            } else if (char in Tokens.LOWERCASE) {
                lowercaseScanner()
            } else if (char in Tokens.NUMBER) {
                numberScanner()
            } else if (char == Tokens.COMMENT || char == null) {
                print("Token: Type=EOF, Lexeme=, Literal=null, Line=1")
                break
            } else if (char == Tokens.EOL) {
                print("Token: Type=EOL, Lexeme=\".\", Literal=null, Line=1")
                print("Token: Type=EOF, Lexeme=, Literal=null, Line=1")
                break
            }else {

            }
        }
    }

    fun titleScanner() {
        var checker = true
        var index = head
        do {
            var char = line[head]
            if(char !in Tokens.LOWERCASE && head-index!=0){
                checker = false
            }
            lexeme += char
            head++
        } while(checkChar() != ' ' && checkChar() != null)
        if(checker){
            println("TitleCaseWord")
        } else {
            println("NotTitleCaseWord")
        }

        lexeme=""
        head++
    }

    fun lowercaseScanner() {
        var checker = true
        do {
            var char = line[head]
            if(char !in Tokens.LOWERCASE){
                checker = false
            }
            lexeme += char
            head++
        } while(checkChar() != ' ' && checkChar() != null)
        if(checker){
            println("LowerCaseWord")
        } else {
            println("NotLowerCaseWord")
        }

        lexeme=""
        head++
    }

    fun numberScanner() {

    }
}

fun main(){
    val scanner = Scanner()
    while(true) {
        print(">")
        var input = readln()
        println(input)
        scanner.scanLine(input)
        println()
    }
}