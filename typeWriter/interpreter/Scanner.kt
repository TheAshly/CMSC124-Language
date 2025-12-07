package typeWriter.interpreter

import typeWriter.interpreter.constructors.Token
import typeWriter.interpreter.keywords.Character
import typeWriter.interpreter.keywords.ReservedWords
import kotlin.collections.contains
import kotlin.collections.mutableListOf


class Scanner {

    // Initializing Scanner Variables
    var head: Int = 0
    var lexeme: String = ""
    var line: String = ""
    var lineNum: Int = 1

    var tokens = mutableListOf<Token>()

    // To make sure that the first word can start either with either Upper or Lower Case
    var firstword: Boolean = true

    // Checks the current index of the line string to see if it is null
    fun checkChar(): Char? {
        if (line.length > head){
            return line[head]
        }
        return null
    }

    // Shortest Edit Path Algorithm to find which Keyword is most similar to the current
    // word being checked, accepts alpha as word to check, and omega as the keyword
    fun countEditDistance(alpha: String, omega: String): Int {
        val aLength = alpha.length
        val oLength = omega.length
        var value = 0

        val array = IntArray(oLength + 1) { amount -> amount }

        for (i in 1..aLength) {
            for (j in 0..oLength) {
                if (j==0){
                    value = array[j] + 1
                } else if(alpha[i - 1].equals(omega[j - 1], ignoreCase = true)) {
                    val diagonal = array[j-1]
                    array[j-1] = value

                    value = diagonal

                } else {
                    val diagonal = array[j-1]
                    array[j-1] = value

                    val editInsert = value + 1
                    val editDelete = array[j] + 1
                    val editReplace = diagonal + 1

                    value = minOf(editInsert, editDelete, editReplace)
                }

            }
            array[oLength] = value
        }

        return value
    }

    // Checks the Shortest Edit Path to the current word with each keyword and
    // returns the keyword with the least amount of edit
    fun checkReservedWord(word: String): Pair<String, Int>{
        var editdistance = Int.MAX_VALUE
        var reservedword = ""
        for (reserved in ReservedWords.Companion.KEYWORDS) {
            val placeholder = countEditDistance(word, reserved)
            if(editdistance > placeholder){
                editdistance = placeholder
                reservedword = reserved
            }
        }
        return Pair(reservedword, editdistance)
    }

    // Checks the code line by line
    fun scanLine(line: String): MutableList<Token> {
        this.head = 0
        this.line = line
        this.tokens = mutableListOf()

        this.firstword = true

        // Checks the first symbol in the word and scans accordingly based on it
        while (true) {
            val char = checkChar()
            if (char in Character.Companion.UPPERCASE || char in Character.Companion.LOWERCASE){
                scanLexeme()
                firstword = false
            }
            else if (char in Character.Companion.NUMBER){
                scanNumber()
                firstword = false
            }
            else if (char == Character.Companion.STATEMENT){
                scanStatement()
                firstword = false
            }
            else if (char == Character.Companion.INDENT){
                tokens.add(Token("indent", "\t", null, lineNum))
                this.firstword = true
                head++
            }
            else if (char == Character.Companion.SPACE){
                tokens.add(Token("space", " ", null, lineNum))
                head++
            }
            else if (char == Character.Companion.COLON){
                tokens.add(Token("colon", ":", null, lineNum))
                head++
            }
            else if (char == Character.Companion.COMMA){
                tokens.add(Token("comma", ",", null, lineNum))
                head++
            }
            else if (char == Character.Companion.PERIOD){
                tokens.add(Token("period", ".", null, lineNum))
                head++
                firstword = true
            }
            else if (char == Character.Companion.COMMENT || Character.Companion.NEWLINE.contains(char)){
                if (Character.Companion.NEWLINE.contains(char)) {
                    if(char == '\n')
                        lineNum++
                    head++
                } else {
                    do {
                        val comment = checkChar()
                        head++
                    }while(!Character.Companion.NEWLINE.contains(comment))

                }
            }
            else {
                if (ErrorChecker.checkCharNull(char, lineNum)){
                    tokens.add(Token("EOF", "", null, lineNum))
                    lineNum++
                    break
                }

            }

            this.lexeme = ""

            System.gc()
        }

        return tokens
    }

    // Checks the current word, if it's a Reserved Keyword, Identifier, or Else
    fun scanLexeme() {
        val index = head        // Saves the index of the first letter of the word
        var identifier = false  // Initializing Checker if it's an Identifier
        var word = true         // Initializing Checker if it follows Proper Word Format (No Symbol or Numbers)

        do {
            // Checks next character until it hits end of line or a whitespace
            val char: Char? = checkChar()

            if(char !in Character.Companion.LOWERCASE){
                if(char in Character.Companion.UPPERCASE){
                    if(index==head){
                        identifier = true
                    } else {
                        firstword = false
                        identifier = false
                    }
                } else{
                    word = false
                }
            }

            lexeme += char
            head++
        } while(checkChar() !in hashSetOf(Character.Companion.PERIOD, Character.Companion.COMMA,Character.Companion.COLON, Character.Companion.COMMENT, Character.Companion.SPACE,null))

        // If it followed proper word format it goes here otherwise it adds an error Token
        if(ErrorChecker.checkLexemeStructure(word, lineNum)){
            // Runs the checker to what Reserved word is most similar to the current word
            val reservedWord: Pair<String, Int> = checkReservedWord(lexeme)

            // First checks its equal to the Keyword, otherwise checks if at least its similar, if also not applicable
            if(ErrorChecker.checkLexemeCapitalization(firstword, lexeme, reservedWord.first, reservedWord.second, lineNum))
                when(lexeme){
                    "factual" -> tokens.add(Token("Preposition", lexeme, "true", lineNum))
                    "faulty" -> tokens.add(Token("Preposition", lexeme, "false", lineNum))
                    "nothing" -> tokens.add(Token("Null", lexeme, "nothing", lineNum))
                    else -> tokens.add(Token(reservedWord.first, lexeme, null, lineNum))
                }
            else
                if(ErrorChecker.checkKeywordExistence(identifier, lineNum))
                    tokens.add(Token("Identifier", lexeme, null, lineNum))
        }
    }

    // Checks if it's an Integer or a Decimal Number
    fun scanNumber() {
        var checker = true  // Initializing Checker if it follows Numeric Formats
        var decimal = false // Initializing Checker if it is a Decimal Value
        do {
            // Checks next character until it hits end of line or a whitespace
            val num: Char? = checkChar()

            // Checks if the character are still a number and if otherwise activates checker
            // if it uses decimal, activates decimal, if it uses decimal again, calls Error
            if (num !in Character.Companion.NUMBER){
                if(num == Character.Companion.DECIMAL){
                    if(!decimal){
                        decimal = true
                        head++
                        if(checkChar() !in Character.Companion.NUMBER){
                            head--
                            break
                        }
                        else
                            head--
                    } else {
                        break
                    }
                } else {
                    checker = false
                }
            }

            lexeme += num
            head++

        } while (checkChar() !in hashSetOf(Character.Companion.COMMA,Character.Companion.COLON, Character.Companion.COMMENT, Character.Companion.SPACE, null))

        // If it followed Proper Numeric Format it goes here otherwise it adds an Error Token
        if (ErrorChecker.checkNumberStructure(checker, lineNum))
            tokens.add(Token("Numeric", lexeme, lexeme, lineNum))
    }

    // Check if it's a statement (A long String)
    fun scanStatement() {
        var endQuote = false // Initializing Checker for the ending quotation (")
        var checker = true   // Initializing Checker to see if they did end the statement
        var referencing = false
        var reference = ""
        val placeholder = mutableListOf<Token>()

        head++

        do {
            // Checks next character until it hits end of line or a whitespace
            val char: Char? = checkChar()

            // Checks if the character is a ("), and activates both Checker
            // iterates to not include the ending quote in the lexeme
            if(char == Character.Companion.STATEMENT){
                if(!endQuote){
                    endQuote = true
                    head++
                    continue
                }
            } else if(char == Character.Companion.OPENREF){
                referencing = true
            } else if(char == Character.Companion.CLOSEREF){
                referencing = false
                reference += char
                placeholder.add(Token("Reference", reference, null, lineNum))
                reference = ""
            }

            if (referencing) reference += char
            lexeme += char
            head++

            // Checks if the there is still a character after the ending quotation
            if(endQuote) checker = false
        } while(!endQuote || checkChar() !in hashSetOf(Character.Companion.PERIOD, Character.Companion.COMMA, Character.Companion.COLON, Character.Companion.COMMENT,  Character.Companion.SPACE, null))

        // If the statement ended properly it goes here otherwise it pushes an error token
        if (ErrorChecker.checkSentenceStructure(checker, lineNum))
            tokens.add(Token("Sentence", "\"$lexeme\"", "\"$lexeme\"", lineNum))

        for(reference in placeholder){
            tokens.add(reference)
        }

    }
}

