grammar Day18;

expr: NUM | expr (MULT | ADD) expr | '(' expr ')';
addExpr: NUM | addExpr ADD addExpr | addExpr MULT addExpr | '(' addExpr ')';

NUM: [0-9]+;
ADD: '+';
MULT: '*';
SPACE: ' ' -> channel(HIDDEN);