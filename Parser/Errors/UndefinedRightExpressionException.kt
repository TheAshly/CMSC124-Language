class UndefinedRightExpressionException(keyword: Any?, line: Int) : Exception("[Line $line] " + "Expecting expression after \"$keyword\".")
