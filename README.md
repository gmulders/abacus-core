Abacus
======

Abacus is a library for parsing and evaluating expressions. It was designed so that simple expressions can be evaluated or translated to a different language. For more info on translators see [translator-java]() and [translator-javascript]().

Given an expression it builds an abstract syntax tree (AST) on which it can perform operations. For instance it can simplify it, check it on semantical errors, evaluate it or translate it to some language.

## Table of contents

- [Quick start](#quick-start)
- [Examples](#examples)
- [Documentation](#documentation)
- [TODO](#todo)
- [Copyright](#copyright)

Quick start
-----------

For those who can't wait to see it running, try copy pasting the following code:

	SimpleSymbolTable sym = new SimpleSymbolTable();

	AbstractNode tree;
	try {
		NodeFactory nodeFactory = new AbacusNodeFactory();
		Lexer lexer = new AbacusLexer(expression);

		Parser parser = new Parser(lexer, nodeFactory);
		tree = parser.parse();

		SemanticsChecker semanticsChecker = new SemanticsChecker(sym);
		semanticsChecker.check(tree);

		Simplifier simplifier = new Simplifier(sym, nodeFactory);
		tree = simplifier.simplify(tree);

	} catch (CompilerException | SemanticsCheckException | SimplificationException e) {
		// Handle exception.
	}

	Object value;
	try {
		Evaluator evaluator = new Evaluator(sym);
		value = evaluator.evaluate(tree);
	} catch (EvaluationException e) {
		// Handle exception.
	}

Examples
--------
Some examples of expressions Abacus can handle are:

- `3*x`
- `6+7`
- `x > 1 ? "Singular" : "Plural"`
- `"Hello" + " " + "World!"`
- `true != false`
- `(2*pi*sigma^2)^-(1/2)*e^-(x-mu)^2/2*sigma^2`
- `2*pi*r`

Documentation
-------------

###Supported types
The following types are supported:

- String
- Integer
- Decimal
- Boolean
- Date (TODO)

###Operations
The following operations are defined:

#### addition `+`
- Operands: either two numbers or two strings
- Precedence: 5
- Return type:

| lhs \ rhs   | String  | Integer | Decimal | Unknown |
|-------------|---------|---------|---------|---------|
| **String**  | String  | -       | -       | String  |
| **Integer** | -       | Integer | Decimal | Integer |
| **Decimal** | -       | Decimal | Decimal | Decimal |
| **Unknown** | String  | Integer | Decimal | Unknown |

#### substraction `-`
- Operands: two numbers
- Precedence: 5
- Return type:

| lhs \ rhs   | Integer | Decimal | Unknown |
|-------------|---------|---------|---------|
| **Integer** | Integer | Decimal | Integer |
| **Decimal** | Decimal | Decimal | Decimal |
| **Unknown** | Integer | Decimal | Unknown |

#### multiplication `*`
- Operands: two numbers
- Precedence: 4
- Return type:

| lhs \ rhs   | Integer | Decimal | Unknown |
|-------------|---------|---------|---------|
| **Integer** | Integer | Decimal | Integer |
| **Decimal** | Decimal | Decimal | Decimal |
| **Unknown** | Integer | Decimal | Unknown |

#### division `/`
- Operands: two numbers
- Precedence: 4
- Return type:

| lhs \ rhs   | Integer | Decimal | Unknown |
|-------------|---------|---------|---------|
| **Integer** | Integer | Decimal | Integer |
| **Decimal** | Decimal | Decimal | Decimal |
| **Unknown** | Integer | Decimal | Unknown |

#### modulo `%`
- Operands: two numbers
- Precedence: 4
- Return type:
  - Integer

#### power `^`
- Operands: two numbers
- Precedence: 4
- Return type: Decimal

#### unary positive `+`
- Operands: one number
- Precedence: 2
- Return type: the same type as the operand

#### unary negative `-`
- Operands: one number
- Precedence: 2
- Return type: the same type as the operand

#### unary not `!`
- Operands: Boolean
- Precedence: 2
- Return type: Boolean

#### smaller `<`
- Operands: either two numbers or two operands of the same type
- Precedence: 6
- Return type: Boolean

#### smaller or equals `<=`
- Operands: either two numbers or two operands of the same type
- Precedence: 6
- Return type: Boolean

#### equals `==`
- Operands: either two numbers or two operands of the same type
- Precedence: 7
- Return type: Boolean

#### unequals `!=`
- Operands: either two numbers or two operands of the same type
- Precedence: 7
- Return type: Boolean

#### greater or equals `>=`
- Operands: either two numbers or two operands of the same type
- Precedence: 6
- Return type: Boolean

#### greater `>`
- Operands: either two numbers or two operands of the same type
- Precedence: 6
- Return type: Boolean

#### logical and `&&`
- Operands: two Booleans
- Precedence: 8
- Return type: Boolean

#### logical or `||`
- Operands: two Booleans
- Precedence: 8
- Return type: Boolean

#### ternary if `? :`
- Operands: a boolean and two operands of the same type
- Precedence: 10
- Return type:

| if \ else   | String  | Integer | Decimal | Date    | Unknown |
|-------------|---------|---------|---------|---------|---------|
| **String**  | String  | -       | -       | -       | String  |
| **Integer** | -       | Integer | Decimal | -       | Integer |
| **Decimal** | -       | Decimal | Decimal | -       | Decimal |
| **Boolean** | -       | -       | -       | -       | Boolean |
| **Date**    | -       | -       | -       | Date    | Date    |
| **Unknown** | String  | Integer | Decimal | Date    | Unknown |

Note: The interpreter does not do a complete check of the AST before evaluating it. Because of this it _may_ be possible
that the type of the non-applicable branch (if the condition evaluated to false the if-branch otherwise the else-branch)
is unknown while it could have been known otherwise. In this case we also infer the type using the above table. There
are a couple of situation where the behaviour is slightly different.

- The applicable branch has type Integer, while the non applicable brach has type Decimal. The inferred type when using
  the interpreter is Integer, otherwise it would have been Decimal. For example: `true ? 1 : (false ? 2.0 : 3.0)`.
- When both branches have non compatible types, i.e. String and Integer, the interpreter does not throw an exception.
  For example: `true ? 'Hello World!' : (false ? 2.0 : 3.0)`.

#### assignment `=`
- Operands: left a variable, right any type
- Precedence: 1
- Return type: the same type as the right operand

###Language definition

We will define the language in two parts. The first part is a list of tokens the lexer recognises. The second part is a
Backus-Naur Form*ish* description using the tokens.

#### Tokens

The lexer recognises the following tokens:

	END_OF_INPUT      = (Not really a character, just the end of the input. \0 if you like.)
	END_OF_EXPRESSION = ";"
	WHITE_SPACE       = " " | "\t" | "\n" | "\r"
	NEW_LINE          = "\n" | "\r\n"
	COMMA             = ","
	IDENTIFIER        = [a-zA-Z][a-zA-Z0-9]*
	LEFT_PARENTHESIS  = "("
	RIGHT_PARENTHESIS = ")"
	STRING            = \'(\\.|[^\\'])*\'  (escaping of special characters is possible)
	DECIMAL           = ([0-9]+ \. [0-9]* | \. [0-9]+)
	INTEGER           = 0 | [1-9][0-9]*
	BOOLEAN_AND       = "&&"
	BOOLEAN_OR        = "||"
	PLUS              = "+"
	MINUS             = "-"
	MULTIPLY          = "*"
	DIVIDE            = "/"
	NEQ               = "!="
	NOT               = "!"
	LEQ               = "<="
	LT                = "<"
	GEQ               = ">="
	GT                = ">"
	EQ                = "=="
	IF                = "?"
	COLON             = ":"
	PERCENT           = "%"
	POWER             = "^"
	ASSIGNMENT        = "="
	NULL              = "null"

#### Definition

	<statement-list>   := <statement> | <statement> <statement-list>
	<statement>        := <assignment> <eos>
	<assignment>       := <expression> | <expression> ASSIGNMENT <assignment>
	<expression>       := <conditional>
	<conditional>      := <booleanoperation> | <booleanoperation> IF <expression> COLON <expression>
	<booleanoperation> := <comparison> | <comparison> ( BOOLEAN_AND | BOOLEAN_OR ) <booleanoperation>
	<comparison>       := <addition> | <addition> ( EQ | NEQ | LT | LEQ | GEQ | GT ) <comparison>
	<addition>         := <term> | <term> ( PLUS | MINUS ) <comparison>
	<term>             := <power> | <power> ( MULTIPLY | DIVIDE | PERCENT ) <term>
	<power>            := <unary> | <unary> ( POWER ) <power>
	<unary>            := ( epsilon | PLUS | MINUS | NOT ) <factor>
	<factor>           := DECIMAL | INTEGER | STRING | ( LEFT_PARENTHESIS <expression> RIGHT_PARENTHESIS )
	                         | IDENTIFIER ( epsilon | LEFT_PARENTHESIS <expression-list> RIGHT_PARENTHESIS )
	<expression-list>  := <expression> | <expression> <expression-list>
	
	<eos> := ; | end of input

TODO
----
- Parse dates. Probably using the following syntax; `D'yyyy-MM-dd'`.
- Apply typechecking on the rhs of an `OrNode`, on the rhs of an `AndNode` and on the rhs of an `IfNode` in the
  `Interperter`. For instance, the `Interpreter` will evaluate the expression `true || 9.6` to true, while the
  `Evaluator` will throw an exception beacuse the rhs is of type Decimal. In this case the checking can be done easilly.
  In contrast; in the case `true || (a * b)` we cannot infer the type of the rhs (without interpreting), but we _do_
  know that the type of the rhs cannot ever become `Boolean`. So (without interpreting the rhs) we should be able to
  deduct that an exception should be thrown. For the `IfNode` this also means that the type cannot always be inferred.
- Translate comments in code to English.

Copyright
---------

	Copyright 2011 Geert Mulders

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.