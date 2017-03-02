//codigo de usuario
//paquetes e importaciones
package Analisis;
import java_cup.runtime.*;

//opciones y declaraciones
%%
%{
    //Codigo de usuario en sitaxis java por ejemplo una variable global
    public static String x;
%}
%public
%class LexicoHaskell
%cupsym SimbolosHaskell
%ignorecase
%cup
%char
%column
%line
%full
%unicode

//expresiones regulares
numero = [0-9]+("."[0-9]+)?;
%%

//reglas lexicas
//---------------palabras reservadas y simbolos---------------
<YYINITIAL> Const.calcular    { return new Symbol(SimbolosHaskell.calcular, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.let         { return new Symbol(SimbolosHaskell.let, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.succ        { return new Symbol(SimbolosHaskell.succ, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.decc        { return new Symbol(SimbolosHaskell.decc, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.min         { return new Symbol(SimbolosHaskell.min, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.max         { return new Symbol(SimbolosHaskell.max, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.sum         { return new Symbol(SimbolosHaskell.sum, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.product     { return new Symbol(SimbolosHaskell.product, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.revers      { return new Symbol(SimbolosHaskell.revers, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.impr        { return new Symbol(SimbolosHaskell.impr, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.par         { return new Symbol(SimbolosHaskell.par, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.asc         { return new Symbol(SimbolosHaskell.asc, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.desc        { return new Symbol(SimbolosHaskell.desc, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.length      { return new Symbol(SimbolosHaskell.length, yycolumn, yyline, yytext()); }
<YYINITIAL> Const._if         { return new Symbol(SimbolosHaskell._if, yycolumn, yyline, yytext()); }
<YYINITIAL> Const._then       { return new Symbol(SimbolosHaskell._then, yycolumn, yyline, yytext()); }
<YYINITIAL> Const._else       { return new Symbol(SimbolosHaskell._else, yycolumn, yyline, yytext()); }
<YYINITIAL> Const._end        { return new Symbol(SimbolosHaskell._end, yycolumn, yyline, yytext()); }
<YYINITIAL> Const._case       { return new Symbol(SimbolosHaskell._case, yycolumn, yyline, yytext()); }

<YYINITIAL> Const.masmas      { return new Symbol(SimbolosHaskell.masmas, yycolumn, yyline, yytext()); }

<YYINITIAL> Const.mas         { return new Symbol(SimbolosHaskell.mas, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.menos       { return new Symbol(SimbolosHaskell.menos, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.por         { return new Symbol(SimbolosHaskell.por, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.dividido    { return new Symbol(SimbolosHaskell.dividido, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.mod         { return new Symbol(SimbolosHaskell.mod, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.sqrt        { return new Symbol(SimbolosHaskell.sqrt, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.pot         { return new Symbol(SimbolosHaskell.pot, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.or          { return new Symbol(SimbolosHaskell.or, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.and         { return new Symbol(SimbolosHaskell.and, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.menorigual  { return new Symbol(SimbolosHaskell.menorigual, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.mayorigual  { return new Symbol(SimbolosHaskell.mayorigual, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.menor       { return new Symbol(SimbolosHaskell.menor, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.mayor       { return new Symbol(SimbolosHaskell.mayor, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.diferente   { return new Symbol(SimbolosHaskell.diferente, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.igualigual  { return new Symbol(SimbolosHaskell.igualigual, yycolumn, yyline, yytext()); }

<YYINITIAL> Const.igual       { return new Symbol(SimbolosHaskell.igual, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.dolar       { return new Symbol(SimbolosHaskell.dolar, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.porcentaje  { return new Symbol(SimbolosHaskell.porcentaje, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.allave      { return new Symbol(SimbolosHaskell.allave, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.cllave      { return new Symbol(SimbolosHaskell.cllave, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.acorchete   { return new Symbol(SimbolosHaskell.acorchete, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.ccorchete   { return new Symbol(SimbolosHaskell.ccorchete, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.aparentesis { return new Symbol(SimbolosHaskell.aparentesis, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.cparentesis { return new Symbol(SimbolosHaskell.cparentesis, yycolumn, yyline, yytext()); }
<YYINITIAL> Const.coma        { return new Symbol(SimbolosHaskell.coma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
<YYINITIAL> {numero}         { return new Symbol(SimbolosHaskell.numero, yycolumn, yyline, yytext()); }

[\t\r\n\f]                   {/*Espacios en blanco, son ignorados*/}

//-----------------errores lexicos-----------------------------
.                            {ErroresHaskell.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}