fun main(){
    val scanner = Scanner()

    while(true) {
        print(">")
        var input = readln()
        if (input == "exit"){
            break
        }
        println(input)
        scanner.scanLine(input)
        println()
    }
}