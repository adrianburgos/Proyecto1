//codigo de usuario
//paquetes e importaciones
package Analisis.haskell;
import java_cup.runtime.*;
import Reportes.*;

//opciones y declaraciones
%%
%{
    //Codigo de usuario en sitaxis java por ejemplo una variable global
    StringBuffer string = new StringBuffer();
%}
%public
%class LexicoHaskell
%cupsym symsH
%ignorecase
%cup
%char
%column
%line
%full
%unicode

//expresiones regulares
numero = [0-9]+("."[0-9]+)?
iden = [A-Za-z_][A-Za-z_0-9]*
caracter = "'"."'"
cadena = \"(\\.|[^\"])*\"
enter = \n
finLinea = \r|\n|\r\n
enBlanco = {finLinea} | [ \t\f]
comenBloque = #\/(\\.|[^\/#])*\/#
comenLinea = #(\\.|[^\n])*\n

%state STRING
%%

//reglas lexicas
//---------------palabras reservadas y simbolos---------------
<YYINITIAL> 
{
"calcular"  { return new Symbol(symsH.calcular, yycolumn, yyline, yytext()); }
"let"       { return new Symbol(symsH.let, yycolumn, yyline, yytext()); }
"succ"      { return new Symbol(symsH.succ, yycolumn, yyline, yytext()); }
"decc"      { return new Symbol(symsH.decc, yycolumn, yyline, yytext()); }
"min"       { return new Symbol(symsH.min, yycolumn, yyline, yytext()); }
"max"       { return new Symbol(symsH.max, yycolumn, yyline, yytext()); }
"sum"       { return new Symbol(symsH.sum, yycolumn, yyline, yytext()); }
"product"   { return new Symbol(symsH.product, yycolumn, yyline, yytext()); }
"revers"    { return new Symbol(symsH.revers, yycolumn, yyline, yytext()); }
"impr"      { return new Symbol(symsH.impr, yycolumn, yyline, yytext()); }
"par"       { return new Symbol(symsH.par, yycolumn, yyline, yytext()); }
"asc"       { return new Symbol(symsH.asc, yycolumn, yyline, yytext()); }
"desc"      { return new Symbol(symsH.desc, yycolumn, yyline, yytext()); }
"length"    { return new Symbol(symsH.length, yycolumn, yyline, yytext()); }
"if"        { return new Symbol(symsH._if, yycolumn, yyline, yytext()); }
"then"      { return new Symbol(symsH._then, yycolumn, yyline, yytext()); }
"else"      { return new Symbol(symsH._else, yycolumn, yyline, yytext()); }
"end"       { return new Symbol(symsH._end, yycolumn, yyline, yytext()); }
"case"      { return new Symbol(symsH._case, yycolumn, yyline, yytext()); }

"++"        { return new Symbol(symsH.masmas, yycolumn, yyline, yytext()); }

"+"         { return new Symbol(symsH.mas, yycolumn, yyline, yytext()); }
"-"         { return new Symbol(symsH.menos, yycolumn, yyline, yytext()); }
"*"         { return new Symbol(symsH.por, yycolumn, yyline, yytext()); }
"/"         { return new Symbol(symsH.dividido, yycolumn, yyline, yytext()); }
"'mod'"     { return new Symbol(symsH.mod, yycolumn, yyline, yytext()); }
"'sqrt'"    { return new Symbol(symsH.sqrt, yycolumn, yyline, yytext()); }
"'pot'"     { return new Symbol(symsH.pot, yycolumn, yyline, yytext()); }
"||"        { return new Symbol(symsH.or, yycolumn, yyline, yytext()); }
"&&"        { return new Symbol(symsH.and, yycolumn, yyline, yytext()); }
"<="        { return new Symbol(symsH.menorigual, yycolumn, yyline, yytext()); }
">="        { return new Symbol(symsH.mayorigual, yycolumn, yyline, yytext()); }
"<"         { return new Symbol(symsH.menor, yycolumn, yyline, yytext()); }
">"         { return new Symbol(symsH.mayor, yycolumn, yyline, yytext()); }
"!="        { return new Symbol(symsH.diferente, yycolumn, yyline, yytext()); }
"=="        { return new Symbol(symsH.igualigual, yycolumn, yyline, yytext()); }
"!!"        { return new Symbol(symsH.poslista, yycolumn, yyline, yytext()); }

"="         { return new Symbol(symsH.igual, yycolumn, yyline, yytext()); }
"$"         { return new Symbol(symsH.dolar, yycolumn, yyline, yytext()); }
"{"         { return new Symbol(symsH.allave, yycolumn, yyline, yytext()); }
"}"         { return new Symbol(symsH.cllave, yycolumn, yyline, yytext()); }
"["         { return new Symbol(symsH.acorchete, yycolumn, yyline, yytext()); }
"]"         { return new Symbol(symsH.ccorchete, yycolumn, yyline, yytext()); }
"("         { return new Symbol(symsH.aparentesis, yycolumn, yyline, yytext()); }
")"         { return new Symbol(symsH.cparentesis, yycolumn, yyline, yytext()); }
","         { return new Symbol(symsH.coma, yycolumn, yyline, yytext()); }
":"         { return new Symbol(symsH.dospuntos, yycolumn, yyline, yytext()); }
";"         { return new Symbol(symsH.puntoycoma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
{caracter}          { return new Symbol(symsH.caracter, yycolumn, yyline, yytext()); }
{cadena}            { return new Symbol(symsH.cadena, yycolumn, yyline, yytext().replace("\"", "")); }
{numero}            { return new Symbol(symsH.numero, yycolumn, yyline, yytext()); }
{iden}              { return new Symbol(symsH.iden, yycolumn, yyline, yytext()); }
{enter}              { return new Symbol(symsH.enter, yycolumn, yyline, yytext()); }

{enBlanco}  { /* ignore */ }
}


//-----------------errores lexicos-----------------------------
[^]         {ErroresHaskell.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}
