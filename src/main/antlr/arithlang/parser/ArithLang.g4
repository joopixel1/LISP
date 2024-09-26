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
		  n=numexp      { $ast = $n.ast; }
        | a=addexp      { $ast = $a.ast; }
        | s=subexp      { $ast = $s.ast; }
        | m=multexp     { $ast = $m.ast; }
        | p=powexp      { $ast = $p.ast; }
        | d=divexp      { $ast = $d.ast; }
        | i=intdivexp   { $ast = $i.ast; }
        | v=varexp      { $ast = $v.ast; }
        | l=letexp      { $ast = $l.ast; }
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





 // Lexical Specification of this Programming Language
 //  - lexical specification rules start with uppercase
 Dot : '.' ;

 Let: 'let';

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
