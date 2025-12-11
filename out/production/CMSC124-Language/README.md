# About `Typewriter`

### Creators

**Keith Ashly M. Domingo** (TheAshly/FakeThird)\
**John Clyde C. Aparicio** (Cl4-Bisk)

### Language Overview

`Typewriter` is a strong and dynamically-typed language that takes itself from references such as Java/C/Python but strays from their syntaxes. 
`Typewriter` aims to support first time coders and non-coders as the language it not built on efficiency but readability.
"It's as simple as writing a Letter", is our motto, the language closely mirrors how common sentences are formed and structured,
such as end the code line with a period instead of a semi-colon, although coders who are also having a hard time handling technicalities could also use the
language as only proper understanding of grammar and writing is needed.
---
## Reserved Keywords

### Constructs
This **reserved keywords** are usually used at the start of a code line and ends with a comma/period, they can also begin with an uppercased letter, only when they are called at the start of a code line.

| Keyword                   | Description              |
|:--------------------------|:-------------------------|
| `dear`                    | Function Declaration     |
| `sincerely`               | Return Value in Function |
| `retrieve`                | Function Call            |
| `i state`                 | Print Statement          |
| `you state `              | User Input               |
| `if, else, and otherwise` | Conditional Statements   |
| `for`                     | For Loops                |
| `while`                   | While Loops              |
| `abandon`                 | Break Loop               |
| `recommence`              | Continue Loop            |

### Operators
This **reserved keywords** are usually used between line codes and is always lowercased all throughout, unless it is called at the beginning at then it can either start with an uppercase or lowercase.

| Operator                       | Description             |
|:-------------------------------|:------------------------|
| `refers to`                    | Assignment for Strings  |
| `equals to`                    | Assignment for Int      |
| `correlates to`                | Assignment for Boolean  |
| `or/and`                       | Boolean Comparators     |
| `equaling/unlike`              | Literal Comparators     |
| `exceeding/below`              | Numerical Comparator    |
| `not/negative`                 | Unary Operators         |
| `plus/minus/times/over/modulo` | Arithmetic Comparators  |

---

### Literal and Types
- **Numerical -** Literal Representation for Int and Double Values.
- **Proposition -** Literal Representation for Boolean Values.
- **Statement -** Literal Representation for String Values.
- **Letter -** Literal Representation for Char Values.
- **Empty -** Literal Representation for the null value.
- **Indention/Spaces -** Literals for indents such as \n and for whitespaces.
- **Letters -** Literal Representation of a Function.

---

### Comments
```
Only has single line comments, in which you use ">".
e.g. > This is a comment.
```

---

### Syntax Style

- Code lines ends with a period and code blocks use comma and indention, indentions are important and can not be miscounted.
- Keywords must be in lowercase, unless it’s the first word of the sentence then there’s leniency.
- Identifiers and Function Declarations must be in Title Case, with no symbols and is case sensitive.
- Use parenthesis () for formatting.
- Retyping of an Identifier is allowed but redeclaration of functions are not allowed, and having a function of the same name as an Identifier is also allowed.

---

### Context-Free Grammar (CFG)
```
program         → statement "." { NEWLINE statement "." }

statement            → function_stmt
                        | return_stmt
                        | if_stmt
                        | loop_stmt
                        | loop_control
                        | printing_stmt
                        | assigning_stmt
                        | expression
                        | commenting_stmt
                        

function_stmt   → "dear" FUNCTION ("," | [":" {IDENTIFIER ","}]) NEWLINE code_block

return_stmt     → "sincerely," expression

if_stmt         → "If" expression "," NEWLINE code_block [else_stmt]
else_stmt       → "Otherwise" expression "," NEWLINE code_block NEWLINE else_stmt | "Else" "," NEWLINE code_block

loop_stmt       → "While" expression "," NEWLINE code_block | "For" for_condition "," NEWLINE code_block
for_condition   → (IDENTIFIER|NUMBER) ("up to" | "down to") (IDENTIFIER|NUMBER) | "range of" expression

code_block      → INDENT stmt "." { NEWLINE INDENT stmt "." }

loop_control    → "Recommence" | "Abandon"

printing_stmt   → "I state," "“" STRING {["(" [REFERENCE] ")" STRING]} "“" [", referring to" IDENTIFIER {"," IDENTIFIER}]

assigning_stmt  → IDENTIFIER ("refers to" | "equals to" | "correlates to") expression 

expression 	    → comparison { ( "or" | "and" ) comparison }
comparison      → term [“is” ("unlike" | "equaling" | ("exceeding"|"below")["or" "equaling"] ) term ]
term       	    → factor { ( "add" | "subtract"|”multiply”|”over”|”modulo”) factor }
factor		    → ( "invert" | "not" ) factor | primary
primary    	    → NUMBER | STRING | PREPOSITION | EMPTY | IDENTIFIER | "retrieve" FUNCTION ("," | ":" {expression ","}]) | "your statement"

commenting_stmt → ">" STRING NEWLINE
```

### Sample Code: Typewriter
```
Sentence refers to “Hello World”.
Number equals to 1.
Person refers to "Clyde".
> This is a comment
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

---

### Running Typewriter

Run this command on your cmd while in the directory of the command:
```
kotlinc src/*. kt -include-runtime -d typewrite.jar
```

Then put the path to the directory in your system's Environment Variables Path.
```
C:\Users\Name\Downloads\...\CMSC124-Language
```
Then you can read files with this command (while in the directory of the file):
```
typewrite test/example.txt
```

---

### Error Handling

`Typewriter` provides clear error messages and what line they are found, all of which you can find in the **Exceptions Folder**.

---


### Design Rationale

`Typewriter` is a programming language where you can pour your heart into writing a literature without sacrificing both features and functionalities of a being a programming language.
The language highlights a value for prioritizing readability over one-dollar-words and jargons that only those who are taking **Computer Science** can understand, 
ensuring that even those who have no prior coding experience can easily be explained to how or what does the code do without having to overcomplicate over the vast use of symbols.
The new design of the language can enable faster learning curve for beginners, all while learning coding practices without sacrificing value for execution speed.
This is an elegant way to reduce the gap between those in the know and those who can't, as the syntax of the language is very simple, yet so concise
that users don't need to memorize or learn a lot of information to use the language. In the end, the main reason of choosing this project was all about the heart,
the codes we are writing these days are so technical with no sense of humanity between the screen and the writer; That's where `Typewriter` comes in , and uses terms we are
all but familiar with when writing a thought or letter, Dear, Sincerely, State, to name a few. It adds back the person and the emotions behind the screen,
reminiscing a time, where text was written with click and a clank, as a reminder that even
through it all, the code we are writing is always meant for someone to use or to read, just like we say, `"It's as easy as writing a letter."`
---
### License
**Educational Use Only**

This project was created as an academic project for the CMSC 124 - Design and Implementation of Programming Languages.

- Free for **educational purposes** (learning, teaching, academic research)
- Free for **personal, non-commercial use**

**Copyright © 2025 Keith Ashly M. Domingo and John Clyde C. Aparicio**

---

