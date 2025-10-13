import kotlin.enums.enumEntries

class Scanner {

    // Initializing Scanner Variables
    var head: Int = 0       // Index in String
    var lexeme: String = "" // Word to be Checked
    var line: String = ""   // Line to be Checked
    var lineNum: Int = 1    // Line Number of Code

    // Creates a mutable list of tokens, which will be stored again in another array to separate lines
    // but right now is not being used so commented
    // var tokenList = mutableListOf<MutableList<Token>>()
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
    fun checkReservedWord(word: String): String{
        var editdistance: Int? = null
        var reservedword = ""
        for (reserved in enumEntries<ReservedWords>()) {
            val placeholder = countEditDistance(word, reserved.token)
            if(editdistance == null){
                editdistance = placeholder
                reservedword = reserved.token
            } else if(editdistance > placeholder){
                editdistance = placeholder
                reservedword = reserved.token
            }
        }
        return reservedword
    }

    // Checks the code line by line
    fun scanLine(line: String) {
        this.head = 0                   // Initialized the index to start of the line string
        this.line = line                // Variable Line initialized to the line String
        this.lineNum = 1                // Temp Line variable, as we're not changing lines
        this.tokens = mutableListOf()   // Initialized a new mutable Token list

        this.firstword = true           // Initialized to signify we're at the first word again

        // Checks the first symbol in the word and scans accordingly based on it
        while (true) {
            val char = checkChar()
            if (char in Character.UPPERCASE || char in Character.LOWERCASE) {
                scanLexeme()
            } else if (char in Character.NUMBER) {
                scanNumber()
            } else if (char == Character.STATEMENT) {
                head++ // Does not include the first (") in the lexeme
                scanStatement()
            } else if (char == Character.COMMENT || char == null) {
                tokens.add(Token("end", "EOF", null, lineNum))
                break
            } else if (char == Character.PERIOD) {
                //  println("Token: Type=EOL, Lexeme=\".\", Literal=null, Line=1")
                tokens.add(Token("end", "EOF", null, lineNum))
                break
            } else if (char == Character.SPACE) {
                head++
                continue
            }else {
                println("[Line $lineNum] Typesetting Error: Character/Symbol not found in the alphabet.")
                break
            }
            System.gc()
            firstword = false
            this.lexeme = ""
        }

        // For Printing Tokens if Needed to Check, Uncomment if you want
//         for(i in tokens){
//             println("Token: Type=${i.type}, Lexeme=${i.lexeme}, Literal=${i.literal}, Line=${i.line}")
//         }

        // Calls Scanner, pushes the Token List to the parser to be parsed
        val parser = Parser()
        parser.parseTokens(tokens)
    }

    // Checks the current word, if it's a Reserved Keyword, Identifier, or Else
    fun scanLexeme() {
        val index = head        // Saves the index of the first letter of the word
        var identifier = false  // Initializing Checker if it's an Identifier
        var word = true         // Initializing Checker if it follows Proper Word Format (No Symbol or Numbers)
        do {
            // Checks next character until it hits end of line or a whitespace
            val char: Char? = checkChar()

            // Checks if it is an identifier with a Titlecase format or
            // if it has any symbols showing it doesn't follow proper word format
            if(char !in Character.LOWERCASE){
                if(char in Character.UPPERCASE){
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

            // Adds character to the current lexeme and iterates to the next character
            lexeme += char
            head++
        } while(checkChar() !in setOf(Character.PERIOD, Character.COMMA, Character.COMMENT, Character.SPACE,null))

        // If it followed proper word format it goes here otherwise it adds an error Token
        if(word){

            // Runs the checker to what Reserved word is most similar to the current word
            val reservedWord: String = checkReservedWord(lexeme)

            // First checks its equal to the Keyword, otherwise checks if at least its similar, if also not applicable
            // checks if it can be an identifier, if not then pushes an Error Token
            if(lexeme.equals(reservedWord, ignoreCase = true)){
                // firstword is used here to be lenient when the word's a reserved but Titlecase
                if(firstword || lexeme == reservedWord) {
                    tokens.add(Token(reservedWord, lexeme, null, lineNum))
                } else {
                    tokens.add(Token("[Line $lineNum] Capitalization Exception: Did you mean $reservedWord?", lexeme, null, lineNum))
                }
            } else if(countEditDistance(lexeme, reservedWord) <= reservedWord.length.floorDiv(2)) {
                    tokens.add(Token("[Line $lineNum] Spelling Exception: Did you mean $reservedWord?", lexeme, null, lineNum))
            }  else {
                if(identifier){
                    tokens.add(Token("Identifier", lexeme, null, lineNum))
                } else {
                    tokens.add(Token("[Line $lineNum] Formatting Error: Reserved Keyword not found in dictionary.", lexeme, null, lineNum))
                }
            }
        } else{
            tokens.add(Token("[Line $lineNum] Typesetting Error: String doesn't follow proper word structure.", lexeme, null, lineNum))
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
            if (num !in Character.NUMBER){
                if(num == Character.DECIMAL){
                    if(!decimal){
                        decimal = true
                    } else {
                        break
                    }
                } else {
                    checker = false
                }
            }

            // Adds character to the current lexeme and iterates to the next character
            lexeme += num
            head++
        } while (checkChar() !in setOf(Character.COMMA, Character.COMMENT, Character.SPACE, null))

        // If it followed Proper Numeric Format it goes here otherwise it adds an Error Token
        if (checker){
            tokens.add(Token("Numeric", lexeme, lexeme, lineNum))
        } else {
            tokens.add(Token("[Line $lineNum] Numeric Error: Not a proper number.", lexeme, null, lineNum))
        }
    }

    // Check if it's a statement (A long String)
    fun scanStatement() {
        var endQuote = false // Initializing Checker for the ending quotation (")
        var ended = false    // Initializing Checker for making sure they end the Statement properly
        var checker = true   // Initializing Checker to see if they did end the statement
        do {
            // Checks next character until it hits end of line or a whitespace
            val char: Char? = checkChar()

            // Checks if the character is a ("), and activates both Checker
            // iterates to not include the ending quote in the lexeme
            if(char == Character.STATEMENT){
                if(!endQuote){
                    endQuote = true
                    ended = true
                    head++
                    continue
                }
            }

            // Adds character to the current lexeme and iterates to the next character
            lexeme += char
            head++

            // Checks if the there is still a character after the ending quotation
            if(ended) checker = false
        } while(!ended || checkChar() !in setOf(Character.PERIOD, Character.COMMA, Character.COMMENT,  Character.SPACE, null))

        // If the statement ended properly it goes here otherwise it pushes an error token
        if (checker){
            tokens.add(Token("String", "\"$lexeme\"", lexeme, lineNum))
        } else {
            tokens.add(Token("[Line $lineNum] Statement Error: Did not properly end the statement.", lexeme, null, lineNum))
        }
    }
}

