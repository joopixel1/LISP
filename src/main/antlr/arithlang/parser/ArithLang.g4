grammar ArithLang;

@header {
    import java.util.HashMap;
    import java.util.ArrayList;
 }






 // Grammar of this Programming Language
 //  - grammar rules start with lowercase
 program returns [Program ast] : 
		e=exp { $ast = new Program($e.ast); }
		;

 exp returns [Exp ast]:
        // arithexp
		  n=numexp          { $ast = $n.ast; }
        | a=addexp          { $ast = $a.ast; }
        | s=subexp          { $ast = $s.ast; }
        | m=multexp         { $ast = $m.ast; }
        | p=powexp          { $ast = $p.ast; }
        | d=divexp          { $ast = $d.ast; }
        | i=intdivexp       { $ast = $i.ast; }
        // varlang
        | v=varexp          { $ast = $v.ast; }
        | l=letexp          { $ast = $l.ast; }
        // deflang
        | f=defexp          { $ast = $f.ast; }
        // funclang
        | lambda=lambdaexp  { $ast = $lambda.ast; }
        | call=callexp      { $ast = $call.ast; }
        | lambda=lambdaexp  { $ast = $lambda.ast; }
        // conditional functions
        | br=branchexp      { $ast = $br.ast; }
        | equal=equalexp    { $ast = $equal.ast; }
        | gt=gtexp          { $ast = $gt.ast; }
        | lt=ltexp          { $ast = $lt.ast; }
        | and=andexp        { $ast = $and.ast; }
        | or=orexp          { $ast = $or.ast; }
		| bl=boolexp        { $ast = $bl.ast; }
        // pair and its functions
        | first=firstexp    { $ast = $first.ast; }
        | second=secondexp  { $ast = $second.ast; }
        | pair=pairexp      { $ast = $pair.ast; }
        // lists and its functions
        | list=listexp      { $ast = $list.ast; }
        // TODO: list functions
        ;

 numexp returns [NumExp ast]:
 		  n0=Number { $ast = new NumExp( Integer.parseInt($n0.text)); }
  		| '-' n0=Number { $ast = new NumExp(-Integer.parseInt($n0.text)); }
  		| n0=Number Dot n1=Number { $ast = new NumExp(Double.parseDouble(      $n0.text+"."+$n1.text)); }
  		| '-' n0=Number Dot n1=Number { $ast = new NumExp(Double.parseDouble("-" + $n0.text+"."+$n1.text)); }
 		| '(' n0=Number ')' { $ast = new NumExp( Integer.parseInt($n0.text)); }
  		| '(' '-' n0=Number ')' { $ast = new NumExp(-Integer.parseInt($n0.text)); }
  		| '(' n0=Number Dot n1=Number ')' { $ast = new NumExp(Double.parseDouble(      $n0.text+"."+$n1.text)); }
  		| '(' '-' n0=Number Dot n1=Number ')' { $ast = new NumExp(Double.parseDouble("-" + $n0.text+"."+$n1.text)); }
  		;
  
 addexp returns [AddExp ast]
        locals [ArrayList<Exp> list]
 		@init { $list = new ArrayList<Exp>(); } :
 		'(' '+'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new AddExp($list); }
 		;
 
 subexp returns [SubExp ast]  
        locals [ArrayList<Exp> list]
 		@init { $list = new ArrayList<Exp>(); } :
 		'(' '-'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+ 
 		')' { $ast = new SubExp($list); }
 		;

 multexp returns [MultExp ast] 
        locals [ArrayList<Exp> list]
 		@init { $list = new ArrayList<Exp>(); } :
 		'(' '*'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+ 
 		')' { $ast = new MultExp($list); }
 		;

 divexp returns [DivExp ast] 
        locals [ArrayList<Exp> list]
 		@init { $list = new ArrayList<Exp>(); } :
 		'(' '/'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+ 
 		')' { $ast = new DivExp($list); }
 		;

 intdivexp returns [IntDivExp ast]
        locals [ArrayList<Exp> list]
  		@init { $list = new ArrayList<Exp>(); } :
  		'(' '//'
  		      e=exp { $list.add($e.ast); }
  		    ( e=exp { $list.add($e.ast); } )+
  		')' { $ast = new IntDivExp($list); }
  		;

 powexp returns [PowExp ast]
        locals [ArrayList<Exp> list]
  		@init { $list = new ArrayList<Exp>(); } :
  		'(' '^'
  		      e=exp { $list.add($e.ast); }
  		    ( e=exp { $list.add($e.ast); } )+
  		')' { $ast = new PowExp($list); }
  		;

 varexp returns [VarExp ast]:
        id=Identifier { $ast = new VarExp($id.text); }
        | '(' id=Identifier ')' { $ast = new VarExp($id.text); }
        ;

 letexp returns [LetExp ast]
        locals [
            HashMap<String, Exp> exps = new HashMap<String, Exp>();
        ] :
        '(' Let
            '('
  		        ( '(' id=Identifier e=exp ')' { $exps.put($id.text, $e.ast); } )+
            ')'
            body=exp
        ')' {$ast = new LetExp($exps, $body.ast); }
        ;

 defexp returns [DefExp ast]:
        '('
            Define id=Identifier e=exp
        ')' { $ast = new DefExp($id.text, $e.ast); }
        ;

 lambdaexp returns [LambdaExp ast]
        locals [
            ArrayList<String> params = new ArrayList<String>();
        ] :
        '(' Lambda
            '('
  		        ( id=Identifier { $params.add($id.text); } )*
            ')'
            body=exp
        ')' {$ast = new LambdaExp($params, $body.ast); }
        ;

 callexp returns [CallExp ast]
        locals [
            ArrayList<Exp> args = new ArrayList<Exp>();
        ] :
        '('
            l=exp
            ( e=exp { $args.add($e.ast); } )*
        ')' {$ast = new CallExp($l.ast, $args); }
        ;

 branchexp returns [IfExp ast]:
        '('
            cond=exp
            '?'
            t_eval=exp
            ':'
            f_eval=exp
        ')' {$ast = new IfExp($cond.ast, $t_eval.ast, $f_eval.ast); }
        ;

 equalexp returns [EqualExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
 		'(' '='
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new EqualExp($list); }
 		;

 gtexp returns [GtExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
 		'(' '>'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new GtExp($list); }
 		;

 ltexp returns [LtExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
 		'(' '<'
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new LtExp($list); }
 		;

 andexp returns [AndExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
 		'(' And
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new AndExp($list); }
 		;

 orexp returns [OrExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
 		'(' Or
 		      e=exp { $list.add($e.ast); }
 		    ( e=exp { $list.add($e.ast); } )+
 		')' { $ast = new OrExp($list); }
 		;

 boolexp returns [BoolExp ast]:
 		  t=True    { $ast = new BoolExp($t.text); }
  		| f=False   { $ast = new BoolExp($f.text); }
 		;

 firstexp returns [FirstExp ast]:
 		'('
 		    First e=exp
 		')' { $ast = new FirstExp($e.ast); }
 		;

 secondexp returns [SecondExp ast]:
 		'('
 		    Second e=exp
 		')' { $ast = new SecondExp($e.ast); }
 		;

 pairexp returns [PairExp ast]:
  	    '('
  		    Pair
  		    f=exp
  		    s=exp
  		')' { $ast = new PairExp($f.ast, $s.ast); }
  		;

  listexp returns [ListExp ast]
        locals [
            ArrayList<Exp> list = new ArrayList<Exp>();
        ] :
  		'(' List
 		    ( e=exp { $list.add($e.ast); } )*
  		')' { $ast = new ListExp($list); }
  		;




 // Lexical Specification of this Programming Language
 //  - lexical specification rules start with uppercase
 Dot : '.' ;

 Let: 'let';

 Define: 'Define';

 Lambda: 'lambda';

 If: 'if';

 And: 'and';

 Or: 'or';

 True: 'true';

 False: 'false';

 Pair: 'pair';

 First: 'first';

 Second: 'second';

 List: 'list';

 Number : DIGIT+ ;

 Identifier :   Letter LetterOrDigit*;

 Letter :   [a-zA-Z$_]
	|   ~[\u0000-\u00FF\uD800-\uDBFF] 
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|   [\uD800-\uDBFF] [\uDC00-\uDFFF] 
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}? ;

 LetterOrDigit: [a-zA-Z0-9$_]
	|   ~[\u0000-\u00FF\uD800-\uDBFF] 
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|    [\uD800-\uDBFF] [\uDC00-\uDFFF] 
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?;

 fragment DIGIT: ('0'..'9');

 AT : '@';

 ELLIPSIS : '...';

 WS  :  [ \t\r\n\u000C]+ -> skip;

 Comment :   '/*' .*? '*/' -> skip;

 Line_Comment :   '#' ~[\r\n]* -> skip;
