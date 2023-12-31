parser grammar TartarusParser;

@header { package info.colinhan.sisyphus.tartarus.parser; }
options {
    tokenVocab=TartarusLexer;
}

diagram: block? EOF;
statement: action | ifStatement | whileStatement | parallelStatement;

action: ActionStart actionDefinition LiteralEndOfStatement;
actionDefinition: ActionName positionedParameter? (LiteralEndOfLine namedParameter)*;
positionedParameter: literal;
namedParameter: ParameterName ParameterColon literal;

ifStatement: 'if' '(' logicalExpression ')' 'then'? EndOfLine+ block (EndOfLine+ elseifStatement)* (EndOfLine+ elseStatement)? EndOfLine+ 'end';
elseifStatement: 'elseif' '(' logicalExpression ')' 'then'? EndOfLine+ block;
elseStatement: 'else' EndOfLine+ block;

whileStatement: 'while' '(' Id 'in' arrayExpression ')' EndOfLine+ block EndOfLine+ 'end';
parallelStatement: 'parallel' '(' Id 'in' arrayExpression ')' EndOfLine+ block EndOfLine+ 'end';

compare: Equale | NotEquale | GreaterThan | LessThan | GreaterThanOrEquale | LessThanOrEquale;

logicalExpression: orExpression;
orExpression: andExpression (Or andExpression)*;
andExpression: equalityExpression (And equalityExpression)*;
equalityExpression: comparisonExpression (compare comparisonExpression)?;
comparisonExpression: primaryExpression;
primaryExpression: '(' logicalExpression ')' | Not primaryExpression | expressionValue;

arrayExpression: expressionValue | array;
array: '[' (expressionValue (',' expressionValue)*)? ']';

expressionValue: reference | constant;
constant: QuotedString | Number;

block: statement (EndOfLine+ statement)*;

reference: simpleReference | compReference;
simpleReference: StartOfRef ReferenceId;
compReference:  StartOfCompRef CompReferenceId (CompReferenceColon CompReferenceDefaultValue?)? EndOfCompReference;

literal: (literalAtom | LiteralString)+;
literalAtom: referenceInLiteral | compReferenceInLiteral;
referenceInLiteral: StartOfRefInLiteral ReferenceId;
compReferenceInLiteral:  StartOfCompRefInLiteral CompReferenceId (CompReferenceColon CompReferenceDefaultValue?)? EndOfCompReference;
