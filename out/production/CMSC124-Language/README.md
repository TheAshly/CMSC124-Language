# TYPEWRITER

## Creator
[Your name]

Keith Ashly M. Domingo\
John Clyde C. Aparicio\
Rene Andre B. Jocsing

## Language Overview
[Provide a brief description of your programming language - what it's designed for, its main characteristics]

The language takes itself from a lot of references such as java/C/python but strays from their syntaxing, this truly elegant language is made to support people who find it difficult to handle technicalities and coding but excel in understanding proper language and grammar.
“Say goodbye to being a Master Coder, now be an Eloquent Writer.” It closely mimics how common sentences are formed and structured, not on a 1 to 1 basis, as it would prove difficult to do but as it closely mirrors proper grammar, one must only need a proper reading comprehension skill to master it.

## Keywords
[List all reserved words that cannot be used as identifiers - include the keyword and a brief description of its purpose]
```
Dear - function call.
Sincerely - returns a value.
If, Else, and Otherwise - for if else statements.
For - for loops.
While - while loops.
Abandon - break loops.
Recommence - continue to the next iteration of loops.
Append - concatenation.
```

## Operators
[List all operators organized by category (arithmetic, comparison, logical, assignment, etc.)]
```
or & and - Logical Operators
equals & refer - Assignment Operator for Numeric and Sequence type respectively.
not & invert- Unary Operators
equivaling, exceeding, & below - Comparison Operators
add, subtract, multiply, over, modulo - Arithmetic Operators
```

## Literals
[Describe the format and syntax for each type of literal value (e.g., numbers, strings, characters, etc.) your language supports]
```
Int - numerical values.
Decimal- float version of int.
Proposition (boolean) - It’s factual / It’s faulty (case grammar sensitive).
Sentences (string) - enclosed in double quotation marks.
Letter (char) - also enclosed in double quotes.
Null - null/empty values
```

## Identifiers
[Define the rules for valid identifiers (variable names, function names, etc.) and whether they are case-sensitive]

For variables and function names:
> Always start with a capital letter, because they are nouns.
> Doesn’t accept symbols nor numbers.

## Comments
[Describe the syntax for comments and whether nested comments are supported]
```
> - single line comments
```
## Syntax Style
[Describe whether whitespace is significant, how statements are terminated, and what delimiters are used for blocks and grouping]

Code ends with a period and code blocks use comma and indention.\
Keywords must be in lowercase, unless it’s the first word of the sentence then there’s leniency.\
Identifiers must be in Titlecase.\
Use parenthesis () for formatting.\
“Is” signifies a start of a boolean expression\
“In” signifies a range\
“to” signifies an assignment\
“it” creates a placeholder identifier in loops


## Grammar
```
expression 	→ comparison { ( "or" | "and" ) comparison }
comparison  	→ term [“is” ("unlike" | "equaling" | ("exceeding"|"below")["or" "equaling"] ) term ]
term       	→ factor { ( "add" | "subtract"|”multiply”|”over”|”modulo”) factor }
factor		→ ( "invert" | "not" ) factor | primary
primary    	→ NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
```

## Sample Code
[Provide a few examples of valid code in your language to demonstrate the syntax and features]
```
Sentence refers to “Hello World”.
Number equals to 1.
Person refers to “Clyde”.
> This is a  comment
I state, “Hello to (person)”, referring to Number.
While it’s factual,
	I state, “This runs because it’s factual”.
	Increment Number.
	I states, “(number)”, referring to Number.
	If Number is equal to 10,
		Let’s move on.

	Otherwise if number is equal to 5,
		Please repeat what’s stated.
```


## Design Rationale
[Explain the reasoning behind your design choices]

This is the Typewriter programming language, where you can pour your heart into making an "elegant" literature without sacrificing both features and functionalities of being a programming language. Its purpose is to endure the unknowingly "poor" and "not elegant" style of programmers, who wish to lean back into the basics of learning grammar in their grade schooler's time. Or maybe for someone who wishes to shift to Literature, a very "elegant" course, but gets inflicted on having the Computer Science's monthly salary in companies. A very "elegant" solution for every "not elegant" user. The Typewriter language highlights an "elegant" value for prioritizing readability over one-dollar words, ensuring that even grade schoolers (jk) can read and understand code without having to overcomplicate over puny and "not so very elegant" symbols. The new design of the language can enable faster prototyping for engineers, all while learning more grammar without sacrificing value for execution speed. This is a very "elegant" way to reduce the gap between development and development of programs. The syntax of this language is very simple, yet so concise that programmers have to learn basic grammar in order to create code as mesmerizing and as "elegant" as the language itself. Unlike other languages, this "elegant" language offers a top of the line readability and parallelization for computations, making it the high-end language with a syntax similar to Python and an efficiency rivaling to C.
