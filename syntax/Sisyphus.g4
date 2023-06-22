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

expression: Dolar Id;
block: statement (EndOfLine+ statement)*;

literal: (literalAtom | LiteralString)+;
literalAtom: reference | compReference;
reference: StartOfRef ReferenceId;
compReference:  StartOfCompRef CompReferenceId (CompReferenceColon CompReferenceDefaultValue?)? EndOfCompReference;
