parser grammar Sisyphus;

@header { package info.colinhan.sisyphus.parser; }
options {
    tokenVocab=SisyphusLexer;
}

diagram: block;
statement: actionNode | ifStatement | whileStatement;

actionNode: ActionStart actionDefinition LiteralEndOfStatement;
actionDefinition: ActionName positionedParameter? (LiteralEndOfLine namedParameter)*;
positionedParameter: literal;
namedParameter: ParameterName ParameterColon literal;

ifStatement: 'if' '(' expression ')' 'then'? EndOfLine+ block (EndOfLine+ elseifStatement)* (EndOfLine+ elseStatement)? EndOfLine+ 'endif';
elseifStatement: 'elseif' '(' expression ')' 'then'? EndOfLine+ block;
elseStatement: 'else' EndOfLine+ block;

whileStatement: 'while' '(' Id 'in' expression ')' EndOfLine+ block EndOfLine+ 'endwhile';

compare: Equale | NotEquale | GreaterThan | LessThan | GreaterThanOrEquale | LessThanOrEquale;

expression: logicalExpression;
logicalExpression: orExpression;
orExpression: andExpression (Or andExpression)*;
andExpression: equalityExpression (And equalityExpression)*;
equalityExpression: comparisonExpression (compare comparisonExpression)?;
comparisonExpression: primaryExpression;
primaryExpression: '(' logicalExpression ')' | Not primaryExpression | expressionValue;
expressionValue: reference | compReference | constant;
constant: QuotedString | ExpressionLiteral;

block: statement (EndOfLine+ statement)*;

reference: StartOfRef ReferenceId;
compReference:  StartOfCompRef CompReferenceId (CompReferenceColon CompReferenceDefaultValue?)? EndOfCompReference;

literal: (literalAtom | LiteralString)+;
literalAtom: referenceInLiteral | compReferenceInLiteral;
referenceInLiteral: StartOfRefInLiteral ReferenceId;
compReferenceInLiteral:  StartOfCompRefInLiteral CompReferenceId (CompReferenceColon CompReferenceDefaultValue?)? EndOfCompReference;
