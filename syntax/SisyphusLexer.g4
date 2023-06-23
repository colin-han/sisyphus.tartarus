lexer grammar SisyphusLexer;

If: 'if';
Then: 'then';
ElseIf: 'elseif';
Else: 'else';
EndIf: 'endif';

While: 'while';
In: 'in';
EndWhile: 'endwhile';

EndOfLine: '\n';

Semicolon: ';';
Comma: ',';
LeftBracket: '(';
RightBracket: ')';
LeftCurlyBracket: '{';
RightCurlyBracket: '}';
LeftSquareBracket: '[';
RightSquareBracket: ']';

Equale: '=';
NotEquale: '!=';
GreaterThan: '>';
LessThan: '<';
GreaterThanOrEquale: '>=';
LessThanOrEquale: '<=';

Id: [a-zA-Z][a-zA-Z0-9_]*;

WhiteSpace: [ \t\r]+ -> skip;

// -----------------------------------------
ActionStart: (':' | 'do') -> pushMode(actionMode);
StartOfCompRef: '${' -> pushMode(compReferenceMode);
StartOfRef: ('$' | '@' | '#') -> pushMode(referenceMode);

//------------------------------------------
mode actionMode;
ActionModeSpace: [ \t]+ -> skip;
ActionName: Id -> pushMode(namedParameterMode), pushMode(literalMode);

mode namedParameterMode;
ParameterModeSpace: [ \t]+ -> skip;
ParameterName: Id;
ParameterColon: ':' -> pushMode(literalMode);

//------------------------------------------
mode literalMode;
LiteralModeSpace: '\r' -> skip;
StartOfCompRefInLiteral: '${' -> pushMode(compReferenceMode);
StartOfRefInLiteral: ('$' | '@' | '#') -> pushMode(referenceMode);
LiteralString: (LiteralEsacapedCharacter | ~[\n\r$@#;])+;
fragment LiteralEsacapedCharacter: '\\' [\\$@#rnt;];
LiteralEndOfLine: '\n' -> popMode;
LiteralEndOfStatement: ';' -> popMode, popMode, popMode;

//------------------------------------------
mode referenceMode;
ReferenceId: Id -> popMode;

//------------------------------------------
mode compReferenceMode;
CompReferenceSpace: [ \t]+ -> skip;
CompReferenceId: Id;
CompReferenceColon: ':' -> pushMode(compReferenceDefaultValueMode);
EndOfCompReference: '}' -> popMode;

//------------------------------------------
mode compReferenceDefaultValueMode;
CompReferenceDefaultValue: (CompReferenceDefaultValueEscaped | ~[\n\r$;}])+ -> popMode;
fragment CompReferenceDefaultValueEscaped: '\\' [\\$rnt;}];