//codigo de usuario
//paquetes e importaciones
package Analisis.terminal;
import java_cup.runtime.*;
import Reportes.*;

//opciones y declaraciones
%%
%{
    //Codigo de usuario en sitaxis java por ejemplo una variable global
    StringBuffer string = new StringBuffer();
%}
%public
%class LexicoHaskellTerminal
%cupsym symsHT
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
finLinea = \r|\n|\r\n
enBlanco = {finLinea} | [ \t\f]


%state STRING
%%

//reglas lexicas
//---------------palabras reservadas y simbolos---------------
<YYINITIAL> 
{
"calcular"  { return new Symbol(symsHT.calcular, yycolumn, yyline, yytext()); }
"let"       { return new Symbol(symsHT.let, yycolumn, yyline, yytext()); }
"succ"      { return new Symbol(symsHT.succ, yycolumn, yyline, yytext()); }
"decc"      { return new Symbol(symsHT.decc, yycolumn, yyline, yytext()); }
"min"       { return new Symbol(symsHT.min, yycolumn, yyline, yytext()); }
"max"       { return new Symbol(symsHT.max, yycolumn, yyline, yytext()); }
"sum"       { return new Symbol(symsHT.sum, yycolumn, yyline, yytext()); }
"product"   { return new Symbol(symsHT.product, yycolumn, yyline, yytext()); }
"revers"    { return new Symbol(symsHT.revers, yycolumn, yyline, yytext()); }
"impr"      { return new Symbol(symsHT.impr, yycolumn, yyline, yytext()); }
"par"       { return new Symbol(symsHT.par, yycolumn, yyline, yytext()); }
"asc"       { return new Symbol(symsHT.asc, yycolumn, yyline, yytext()); }
"desc"      { return new Symbol(symsHT.desc, yycolumn, yyline, yytext()); }
"length"    { return new Symbol(symsHT.length, yycolumn, yyline, yytext()); }

"++"        { return new Symbol(symsHT.masmas, yycolumn, yyline, yytext()); }

"+"         { return new Symbol(symsHT.mas, yycolumn, yyline, yytext()); }
"-"         { return new Symbol(symsHT.menos, yycolumn, yyline, yytext()); }
"*"         { return new Symbol(symsHT.por, yycolumn, yyline, yytext()); }
"/"         { return new Symbol(symsHT.dividido, yycolumn, yyline, yytext()); }
"'mod'"     { return new Symbol(symsHT.mod, yycolumn, yyline, yytext()); }
"'sqrt'"    { return new Symbol(symsHT.sqrt, yycolumn, yyline, yytext()); }
"'pot'"     { return new Symbol(symsHT.pot, yycolumn, yyline, yytext()); }
"!!"        { return new Symbol(symsHT.poslista, yycolumn, yyline, yytext()); }

"="         { return new Symbol(symsHT.igual, yycolumn, yyline, yytext()); }
"$"         { return new Symbol(symsHT.dolar, yycolumn, yyline, yytext()); }
"%"         { return new Symbol(symsHT.porcentaje, yycolumn, yyline, yytext()); }
"{"         { return new Symbol(symsHT.allave, yycolumn, yyline, yytext()); }
"}"         { return new Symbol(symsHT.cllave, yycolumn, yyline, yytext()); }
"["         { return new Symbol(symsHT.acorchete, yycolumn, yyline, yytext()); }
"]"         { return new Symbol(symsHT.ccorchete, yycolumn, yyline, yytext()); }
"("         { return new Symbol(symsHT.aparentesis, yycolumn, yyline, yytext()); }
")"         { return new Symbol(symsHT.cparentesis, yycolumn, yyline, yytext()); }
","         { return new Symbol(symsHT.coma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
{caracter}          { return new Symbol(symsHT.caracter, yycolumn, yyline, yytext().replace("'", "")); }
{cadena}            { return new Symbol(symsHT.cadena, yycolumn, yyline, yytext().replace("\"", "")); }
{numero}            { return new Symbol(symsHT.numero, yycolumn, yyline, yytext()); }
{iden}              { return new Symbol(symsHT.iden, yycolumn, yyline, yytext().toLowerCase()); }

{enBlanco}  { /* ignore */ }
}


//-----------------errores lexicos-----------------------------
[^]         {ErroresHaskell.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}