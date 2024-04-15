
**CODE Programming Language Specification**
Introduction
CODE is a strongly-typed programming language developed to teach Junior High School students the basics of programming. It was created by a group of students enrolled in a Programming Languages course. CODE is implemented as a pure interpreter.

**Sample Program**


```# Sample program in CODE
BEGIN CODE
INT x, y, z=5 
CHAR a_1='n' 
BOOL t="TRUE"
x=y=4
a_1='c'
# Comment example
DISPLAY: x & t & z & $ & a_1 & [#] & "last"
END CODE
**Output**
4TRUE5
n#last
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

_Language Grammar_
**Program Structure**
All code is placed inside BEGIN CODE and END CODE blocks.
Variable declarations follow BEGIN CODE.
Variable names are case-sensitive and start with a letter or underscore, followed by letters, underscores, or digits.
Each line contains a single statement.
Comments start with # and can be placed anywhere in the program.
Executable code follows variable declarations.
Reserved words are in uppercase and cannot be used as variable names.
$ signifies a newline or carriage return.
& serves as a concatenator.
[] are used for escape sequences.
Data Types
INT: Ordinary number without a decimal part (occupies 4 bytes).
CHAR: Single symbol.
BOOL: Represents true or false.
FLOAT: Number with a decimal part (occupies 4 bytes).
Operators
Arithmetic Operators: (), *, /, %, +, -
Comparison Operators: >, <, >=, <=, ==, <>
Logical Operators: AND, OR, NOT
Unary Operators: +, -

[-60]

**Program 1: Arithmetic Operation**

```BEGIN CODE
INT xyz, abc=100
xyz= ((abc *5)/10 + 10) * -1
DISPLAY: [[] & xyz & []]
END CODE```
**Output**
[-60]

**Program 2: Logical Operation**
```BEGIN CODE
INT a=100, b=200, c=300 
BOOL d="FALSE" 
d = (a < b AND c <>200)
DISPLAY: d
END CODE```
**Output**
TRUE

**Code Statements**
_DISPLAY:_ Writes formatted output to the device.
_SCAN:_ Allows user input for data types.

**Control Flow Structures**
_Conditional_
_If Selection_


**IF (<BOOL expression>)**
BEGIN IF
<statement>
...
<statement>

**END IF**
If-Else Selection

**IF (<BOOL expression>)**
BEGIN IF
<statement>
...
<statement>
END IF
ELSE
BEGIN IF
<statement>
...
<statement>
END IF

**If-Else with Multiple Alternatives**

IF (<BOOL expression>)
BEGIN IF
<statement>
...
<statement>
END IF
ELSE IF (<BOOL expression>)
BEGIN IF
<statement>
...
<statement>
END IF
ELSE
BEGIN IF
<statement>
...
<statement>
END IF

**Loop Control Structures**

While Loop
CODE
Copy code
WHILE (<BOOL expression>)
BEGIN WHILE
<statement>
...
<statement>
END WHILE
**
Implementation**
This interpreter is being developed in Java.

Feel free to contribute to this project and refer to the provided language specification for further details on the CODE programming language. If you have any questions or suggestions, please open an issue or pull request on GitHub. Thank you!
