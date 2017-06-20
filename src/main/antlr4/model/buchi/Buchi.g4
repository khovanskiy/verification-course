grammar Buchi;

@header {
import java.util.*;
import model.ltl.*;
import static model.ltl.BinaryOperation.*;
}

compilationUnit returns [List<State> list]
@init
{
    $list = new ArrayList<State>();
}:
'never' '{' (((stateDefinition {$list.add($stateDefinition.s);})* | empty) '}' ) EOF;

stateDefinition returns [State s]
@init
{
    List<Transition> list = new ArrayList<>();
}:
    stateName ':' ('skip' | empty | ('if' (transition {list.add($transition.t);})+ 'fi' ';'))
    {
        String name = $stateName.text;
        $s = new State(name, list);
    }
    ;

stateName: StringLiteral;

transition returns [Transition t]:
    '::' expression '->' 'goto' stateName
    {
        $t = new Transition($expression.f, $stateName.text);
    }
    ;

expression returns [Formula<String> f]:
        |   '(' expression ')'
        {
            $f = $expression.f;
        }
        |   literal
        {
            $f = LTL.var($literal.text);
        }
        |   unaryOperator='!' expression
        {
            $f = LTL.not($expression.f);
        }
        |   left=expression binaryOperator='&&' right=expression
        {
            $f = LTL.and($left.f, $right.f);
        }
        |   left=expression binaryOperator='||' right=expression
        {
            $f = LTL.or($left.f, $right.f);
        }
        |   unit
        {
            $f = LTL.t();
        }
        ;

empty
    : False;

unit
    : Unit;

literal
    : StringLiteral;

WS  :	[ \t\r\n\f] -> skip;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

False
    :   'false;'
    ;

StringLiteral
	:	[a-zA-Z][a-zA-Z0-9_]*
	;

Unit
    :   '1'
    ;


