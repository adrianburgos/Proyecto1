//codigo de usuario
//paquetes e importaciones
package Analisis;
import java_cup.runtime.*;
import Reportes.*;

//opciones y declaraciones
%%
%{
    //Codigo de usuario en sitaxis java por ejemplo una variable global
    public static String x;
%}
%public
%class LexicoHaskellTerminal
%cupsym SimbolosHaskellTerminal
%ignorecase
%cup
%char
%column
%line
%full
%unicode

//expresiones regulares
numero = [0-9]+
iden = [A-Za-z_][A-Za-z_0-9]*
finLinea = \r|\n|\r\n
enBlanco = {finLinea} | [ \t\f]
%%

//reglas lexicas
//---------------palabras reservadas y simbolos---------------
<YYINITIAL> 
{
"calcular"  { return new Symbol(SimbolosHaskellTerminal.calcular, yycolumn, yyline, yytext()); }
"let"       { return new Symbol(SimbolosHaskellTerminal.let, yycolumn, yyline, yytext()); }
"succ"      { return new Symbol(SimbolosHaskellTerminal.succ, yycolumn, yyline, yytext()); }
"decc"      { return new Symbol(SimbolosHaskellTerminal.decc, yycolumn, yyline, yytext()); }
"min"       { return new Symbol(SimbolosHaskellTerminal.min, yycolumn, yyline, yytext()); }
"max"       { return new Symbol(SimbolosHaskellTerminal.max, yycolumn, yyline, yytext()); }
"sum"       { return new Symbol(SimbolosHaskellTerminal.sum, yycolumn, yyline, yytext()); }
"product"   { return new Symbol(SimbolosHaskellTerminal.product, yycolumn, yyline, yytext()); }
"revers"    { return new Symbol(SimbolosHaskellTerminal.revers, yycolumn, yyline, yytext()); }
"impr"      { return new Symbol(SimbolosHaskellTerminal.impr, yycolumn, yyline, yytext()); }
"par"       { return new Symbol(SimbolosHaskellTerminal.par, yycolumn, yyline, yytext()); }
"asc"       { return new Symbol(SimbolosHaskellTerminal.asc, yycolumn, yyline, yytext()); }
"desc"      { return new Symbol(SimbolosHaskellTerminal.desc, yycolumn, yyline, yytext()); }
"length"    { return new Symbol(SimbolosHaskellTerminal.length, yycolumn, yyline, yytext()); }
"if"        { return new Symbol(SimbolosHaskellTerminal._if, yycolumn, yyline, yytext()); }
"then"      { return new Symbol(SimbolosHaskellTerminal._then, yycolumn, yyline, yytext()); }
"else"      { return new Symbol(SimbolosHaskellTerminal._else, yycolumn, yyline, yytext()); }
"end"       { return new Symbol(SimbolosHaskellTerminal._end, yycolumn, yyline, yytext()); }
"case"      { return new Symbol(SimbolosHaskellTerminal._case, yycolumn, yyline, yytext()); }

"++"        { return new Symbol(SimbolosHaskellTerminal.masmas, yycolumn, yyline, yytext()); }

"+"         { return new Symbol(SimbolosHaskellTerminal.mas, yycolumn, yyline, yytext()); }
"-"         { return new Symbol(SimbolosHaskellTerminal.menos, yycolumn, yyline, yytext()); }
"*"         { return new Symbol(SimbolosHaskellTerminal.por, yycolumn, yyline, yytext()); }
"/"         { return new Symbol(SimbolosHaskellTerminal.dividido, yycolumn, yyline, yytext()); }
"'mod'"     { return new Symbol(SimbolosHaskellTerminal.mod, yycolumn, yyline, yytext()); }
"'sqrt'"    { return new Symbol(SimbolosHaskellTerminal.sqrt, yycolumn, yyline, yytext()); }
"'pot'"     { return new Symbol(SimbolosHaskellTerminal.pot, yycolumn, yyline, yytext()); }
"||"        { return new Symbol(SimbolosHaskellTerminal.or, yycolumn, yyline, yytext()); }
"&&"        { return new Symbol(SimbolosHaskellTerminal.and, yycolumn, yyline, yytext()); }
"<="        { return new Symbol(SimbolosHaskellTerminal.menorigual, yycolumn, yyline, yytext()); }
">="        { return new Symbol(SimbolosHaskellTerminal.mayorigual, yycolumn, yyline, yytext()); }
"<"         { return new Symbol(SimbolosHaskellTerminal.menor, yycolumn, yyline, yytext()); }
">"         { return new Symbol(SimbolosHaskellTerminal.mayor, yycolumn, yyline, yytext()); }
"!="        { return new Symbol(SimbolosHaskellTerminal.diferente, yycolumn, yyline, yytext()); }
"=="        { return new Symbol(SimbolosHaskellTerminal.igualigual, yycolumn, yyline, yytext()); }

"="         { return new Symbol(SimbolosHaskellTerminal.igual, yycolumn, yyline, yytext()); }
"$"         { return new Symbol(SimbolosHaskellTerminal.dolar, yycolumn, yyline, yytext()); }
"%"         { return new Symbol(SimbolosHaskellTerminal.porcentaje, yycolumn, yyline, yytext()); }
"{"         { return new Symbol(SimbolosHaskellTerminal.allave, yycolumn, yyline, yytext()); }
"}"         { return new Symbol(SimbolosHaskellTerminal.cllave, yycolumn, yyline, yytext()); }
"["         { return new Symbol(SimbolosHaskellTerminal.acorchete, yycolumn, yyline, yytext()); }
"]"         { return new Symbol(SimbolosHaskellTerminal.ccorchete, yycolumn, yyline, yytext()); }
"("         { return new Symbol(SimbolosHaskellTerminal.aparentesis, yycolumn, yyline, yytext()); }
")"         { return new Symbol(SimbolosHaskellTerminal.cparentesis, yycolumn, yyline, yytext()); }
","         { return new Symbol(SimbolosHaskellTerminal.coma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
{numero}         { return new Symbol(SimbolosHaskellTerminal.numero, yycolumn, yyline, yytext()); }
{iden}           { return new Symbol(SimbolosHaskellTerminal.iden, yycolumn, yyline, yytext()); }

{enBlanco}       { /* ignore */ }
}


//-----------------errores lexicos-----------------------------
[^]                            {ErroresHaskell.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}