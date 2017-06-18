grammar LTL;

@header{
import static model.ltl.BinaryOperation.*;
}

ltl returns [Formula<String> f]
    :   NEXT ltl
    {
        $f = LTL.next($ltl.f);
    }
    |   GLOB ltl
    {
        $f = LTL.globally($ltl.f);
    }
    |   FUTURE ltl
    {
        $f = LTL.future($ltl.f);
    }
    |   binary
    {
        $f = $binary.f;
    }
    ;

binary returns [Formula<String> f]
    :   l=binary UNTIL r=binary
    {
        $f = LTL.until($l.f, $r.f);
    }
    |   l=binary RELEASE r=binary
    {
        $f = LTL.release($l.f, $r.f);
    }
    |   prop
    {
        $f = $prop.f;
    }
    ;

prop returns [Formula<String> f]
    :   l=prop CON r=prop
    {
        $f = LTL.and($l.f, $r.f);
    }
    |   l=prop DIS r=prop
    {
        $f = LTL.or($l.f, $r.f);
    }
    |   <assoc=right> l=prop IMPL r=prop
    {
        $f = LTL.impl($l.f, $r.f);
    }
    |   l=prop EQ r=prop
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
    |   LP ltl RP
    {
        $f = $ltl.f;
    }
    |   NOT primary
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