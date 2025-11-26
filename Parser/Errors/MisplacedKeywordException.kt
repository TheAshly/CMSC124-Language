class MisplacedKeywordException(keyword: Any?, line: Int) : Exception("[Line $line] " + "\"$keyword\" shouldn't be used in an expression but declared otherwise.")
