import kotlin.enums.enumEntries

class Scanner {

    var head: Int = 0
    var lexeme: String = ""
    var line: String = ""
    var lineNum: Int = 1 //Temp Line variable

    fun checkChar(): Char? {
        if (line.length <= head){
            return null
        } else {
            return line[head]
        }
    }

//    fun checkString(): String? {
//        try {
//            return line
//        } catch (e: Exception) {
//            return null;
//        }
//    }

    fun scanLine(line: String) {
        this.head = 0
        this.line = line
        this.lineNum = 1 //Temp Line variable

        while (true) {
            var char = checkChar()
//            var string = checkString()

            if (char in Literals.UPPERCASE) {
                scanTitleCase()
            } else if (char in Literals.LOWERCASE) {
                scanLowerCase()
            } else if (char in Literals.NUMBER) {
                scanNumber()
            } else if (char == Literals.STATEMENT) {
                scanStatement()
            } else if (char == Literals.COMMENT || char == null) {
                println("Token: Type=EOF, Lexeme=, Literal=null, Line=1")
                break
            } else if (char == Literals.DECIMAL) {
                println("Token: Type=EOL, Lexeme=\".\", Literal=null, Line=1")
                println("Token: Type=EOF, Lexeme=, Literal=null, Line=1")

                break
            } else if (char == Literals.SPACE) {
                head++
                continue
            }else {
                println("Grammatical Exception: That symbol doesn't exist in the Alphabet.")
                break
            }
        }
    }

    fun scanTitleCase() {
        var identifier = true
        var checker = true
        var index = head

        do {
            var char = line[head]
            if(char !in Literals.LOWERCASE && head==index){
                if(char == Literals.DECIMAL){
                    head--
                    break
                } else {
                    checker = false
                }
            }
            lexeme += char
            head++
        } while(checkChar() != ' ' && checkChar() != null)
        if(checker){
            for (word in enumEntries<Uppercase>()){
                if(lexeme == word.token){
                    println("Token: Type=${word.name}, Lexeme=${lexeme}, Literal=null, Line=1")
                    identifier = false
                    break
                }
            }
            if(identifier){
                println("Token: Type=IDENTIFIER, Lexeme=${lexeme}, Literal=${lexeme}, Line=1")
            }
        } else {
            println("Token: Type=ERROR, Lexeme=${lexeme}, Literal=null, Line=1")

        }

        lexeme=""
        head++
    }

    fun scanLowerCase() {
        var error = true
        var checker = true

        do {
            var char = line[head]
            if(char !in Literals.LOWERCASE){
                if(char == Literals.DECIMAL){
                    head--
                    break
                } else {
                    checker = false
                }
            }
            lexeme += char
            head++
        } while(checkChar() != ' ' && checkChar() != null)
        if(checker){
            for (word in enumEntries<Lowercase>()){
                if(lexeme == word.token){
                    println("Token: Type=${word.name}, Lexeme=${lexeme}, Literal=null, Line=1")
                    error = false
                    break
                }
            }
            if(error){
                println("Token: Type=ERROR, Lexeme=${lexeme}, Literal=null, Line=1")
            }
        } else {
            println("Token: Type=ERROR, Lexeme=${lexeme}, Literal=null, Line=1")

        }

        lexeme=""
        head++
    }

    fun scanNumber() {
        var checker = true
        var decimal = false
        do {
            var num = line[head]
            if (num !in Literals.NUMBER){
                if(num == Literals.DECIMAL){
                    if(!decimal){
                        decimal = true
                    } else {
                        head--
                        break
                    }
                } else {
                    checker = false
                }
            }
            lexeme += num
            head++
        } while (checkChar() != ' ' && checkChar() != null)
        if (checker){
            println("Token: Type=NUMBER, Lexeme=${lexeme}, Literal=${lexeme}, Line=1")
        } else {
            println("Token: Type=ERROR, Lexeme=${lexeme}, Literal=null, Line=1")
        }

        lexeme=""
        head++
    }

    fun scanStatement() {
        var endquote = false

        do {
            var char = line[head]
            if(char == Literals.STATEMENT){
                if(!endquote){
                    endquote = true
                    head++
                    continue
                } else {
                    break
                }
            }
            lexeme += char
            head++
        } while(checkChar() != null)
        println("Token: Type=STRING, Lexeme=\"${lexeme}\", Literal=${lexeme}, Line=1")



        lexeme=""
        head++
    }

}

