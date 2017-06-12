grammar LTL;

@header{
    import static ltl.BinaryOperation.*;
}

ltl returns [Formula<String> f]
    :   'X' ltl
    {
        $f = LTL.next($ltl.f);
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

binary returns [Formula<String> f]
    :   l=binary 'U' r=binary
    {
        $f = LTL.until($l.f, $r.f);
    }
    |   l=binary 'R' r=binary
    {
        $f = LTL.release($l.f, $r.f);
    }
    |   prop
    {
        $f = $prop.f;
    }
    ;

prop returns [Formula<String> f]
    :   l=prop '&' r=prop
    {
        $f = LTL.and($l.f, $r.f);
    }
    |   l=prop '|' r=prop
    {
        $f = LTL.or($l.f, $r.f);
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

primary returns [Formula<String> f]
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
        $f = LTL.not($primary.f);
    }
    ;

constant returns [Formula<String> f]
    :   TRUE
    {
        $f = LTL.t();
    }
    |   FALSE
    {
        $f = LTL.f();
    }
    |   StringLiteral
    {
        String name = $StringLiteral.text;
        $f = LTL.var(name.substring(1, name.length() - 1));
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