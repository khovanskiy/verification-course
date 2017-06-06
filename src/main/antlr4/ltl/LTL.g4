grammar LTL;

@header{
    import static ltl.BinaryOperation.*;
}

ltl returns [Formula f]
    :   'X' ltl
    {
        $f = new Next($ltl.f);
    }
    |   'G' ltl
    {
        $f = LTL.globally($ltl.f);
    }
    |   'F' ltl
    {
        $f = LTL.future($ltl.f);
    }
    |   binary
    {
        $f = $binary.f;
    }
    ;

binary returns [Formula f]
    :   l=binary 'U' r=binary
    {
        $f = new BinaryFormula(U, $l.f, $r.f);
    }
    |   l=binary 'R' r=binary
    {
        $f = new BinaryFormula(R, $l.f, $r.f);
    }
    |   prop
    {
        $f = $prop.f;
    }
    ;

prop returns [Formula f]
    :   l=prop '&' r=prop
    {
        $f = new BinaryFormula(AND, $l.f, $r.f);
    }
    |   l=prop '|' r=prop
    {
        $f = new BinaryFormula(OR, $l.f, $r.f);
    }
    |   <assoc=right> l=prop '->' r=prop
    {
        $f = LTL.impl($l.f, $r.f);
    }
    |   l=prop '<->' r=prop
    {
        $f = LTL.eq($l.f, $r.f);
    }
    |   primary
    {
        $f = $primary.f;
    }
    ;

primary returns [Formula f]
    :   constant
    {
        $f = $constant.f;
    }
    |   '(' ltl ')'
    {
        $f = $ltl.f;
    }
    |   '!' primary
    {
        $f = new Not($primary.f);
    }
    ;

constant returns [Formula f]
    :   TRUE
    {
        $f = LTL.TRUE;
    }
    |   FALSE
    {
        $f = LTL.FALSE;
    }
    |   StringLiteral
    {
        String name = $StringLiteral.text;
        $f = new Variable(name.substring(1, name.length() - 1));
    }
    ;

DIS     :   '|'     ;
CON     :   '&'     ;
NOT     :   '!'     ;
IMPL    :   '->'    ;
EQ      :   '<->'   ;
UNTIL   :   'U'     ;
RELEASE :   'R'     ;
NEXT    :   'X'     ;
GLOB    :   'G'     ;
FUTURE  :   'F'     ;
TRUE    :   'true'  ;
FALSE   :   'false' ;
LP      :   '('     ;
RP      :   ')'     ;

StringLiteral
    :   SingleQuoteSeq | DoubleQuoteSeq
    ;

fragment
SingleQuoteSeq
    :   '\'' (~['\\\r\n] | EscapeSeq) '\''
    ;

fragment
DoubleQuoteSeq
    :   '"' (~["\\\r\n] | EscapeSeq) '"'
    ;

fragment
EscapeSeq
    :   '\\' ['"abfnrtv\\]
    ;

WS  :	[ \t\r\n\f] -> skip;