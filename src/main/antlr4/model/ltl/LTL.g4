grammar LTL;

@header{
import static model.ltl.BinaryOperation.*;
}

ltl returns [Formula<String> f]
    :   
    |   primary
    {
        $f = $primary.f;
    }
    |   NEXT ltl
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
    |   l=ltl RELEASE r=ltl
    {
      $f = LTL.release($l.f, $r.f);
    }
    |   l=ltl UNTIL r=ltl
    {
    $f = LTL.until($l.f, $r.f);
    }
    |   l=ltl CON r=ltl
    {
        $f = LTL.and($l.f, $r.f);
    }
    |   l=ltl DIS r=ltl
    {
        $f = LTL.or($l.f, $r.f);
    }
    |   <assoc=right> l=ltl IMPL r=ltl
    {
        $f = LTL.impl($l.f, $r.f);
    }
    |   l=ltl EQ r=ltl
    {
        $f = LTL.eq($l.f, $r.f);
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
    |   NOT ltl
    {
        $f = LTL.not($ltl.f);
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
    :   '\'' (~['\\\r\n] | EscapeSeq)+ '\''
    ;

fragment
DoubleQuoteSeq
    :   '"' (~["\\\r\n] | EscapeSeq)+ '"'
    ;

fragment
EscapeSeq
    :   '\\' ['"abfnrtv\\]
    ;

WS  :	[ \t\r\n\f] -> skip;

ErrorChar : . ;
