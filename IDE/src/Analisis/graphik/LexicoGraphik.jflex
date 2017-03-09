//codigo de usuario
//paquetes e importaciones
package Analisis.graphik;
import java_cup.runtime.*;
import Reportes.*;

//opciones y declaraciones
%%
%{
    //Codigo de usuario en sitaxis java por ejemplo una variable global
    StringBuffer string = new StringBuffer();
%}
%public
%class LexicoGraphik
%cupsym symsG
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
caracter = "'"."'"
cadena = \"(\\.|[^\"])*\"
enter = \n
finLinea = \r|\n|\r\n
enBlanco = {finLinea} | [ \t\f]


%state STRING
%%

//reglas lexicas
//---------------palabras reservadas y simbolos---------------
<YYINITIAL> 
{
"calcular"  { return new Symbol(symsG.calcular, yycolumn, yyline, yytext()); }
"let"       { return new Symbol(symsG.let, yycolumn, yyline, yytext()); }
"succ"      { return new Symbol(symsG.succ, yycolumn, yyline, yytext()); }
"decc"      { return new Symbol(symsG.decc, yycolumn, yyline, yytext()); }
"min"       { return new Symbol(symsG.min, yycolumn, yyline, yytext()); }
"max"       { return new Symbol(symsG.max, yycolumn, yyline, yytext()); }
"sum"       { return new Symbol(symsG.sum, yycolumn, yyline, yytext()); }
"product"   { return new Symbol(symsG.product, yycolumn, yyline, yytext()); }
"revers"    { return new Symbol(symsG.revers, yycolumn, yyline, yytext()); }
"impr"      { return new Symbol(symsG.impr, yycolumn, yyline, yytext()); }
"par"       { return new Symbol(symsG.par, yycolumn, yyline, yytext()); }
"asc"       { return new Symbol(symsG.asc, yycolumn, yyline, yytext()); }
"desc"      { return new Symbol(symsG.desc, yycolumn, yyline, yytext()); }
"length"    { return new Symbol(symsG.length, yycolumn, yyline, yytext()); }
"if"        { return new Symbol(symsG._if, yycolumn, yyline, yytext()); }
"then"      { return new Symbol(symsG._then, yycolumn, yyline, yytext()); }
"else"      { return new Symbol(symsG._else, yycolumn, yyline, yytext()); }
"end"       { return new Symbol(symsG._end, yycolumn, yyline, yytext()); }
"case"      { return new Symbol(symsG._case, yycolumn, yyline, yytext()); }

"++"        { return new Symbol(symsG.masmas, yycolumn, yyline, yytext()); }
"--"        { return new Symbol(symsG.menosmenos, yycolumn, yyline, yytext()); }

"+"         { return new Symbol(symsG.mas, yycolumn, yyline, yytext()); }
"-"         { return new Symbol(symsG.menos, yycolumn, yyline, yytext()); }
"*"         { return new Symbol(symsG.por, yycolumn, yyline, yytext()); }
"/"         { return new Symbol(symsG.dividido, yycolumn, yyline, yytext()); }
"||"        { return new Symbol(symsG.or, yycolumn, yyline, yytext()); }
"&&"        { return new Symbol(symsG.and, yycolumn, yyline, yytext()); }
"<="        { return new Symbol(symsG.menorigual, yycolumn, yyline, yytext()); }
">="        { return new Symbol(symsG.mayorigual, yycolumn, yyline, yytext()); }
"<"         { return new Symbol(symsG.menor, yycolumn, yyline, yytext()); }
">"         { return new Symbol(symsG.mayor, yycolumn, yyline, yytext()); }
"!="        { return new Symbol(symsG.diferente, yycolumn, yyline, yytext()); }
"=="        { return new Symbol(symsG.igualigual, yycolumn, yyline, yytext()); }
"!!"        { return new Symbol(symsG.poslista, yycolumn, yyline, yytext()); }

"="         { return new Symbol(symsG.igual, yycolumn, yyline, yytext()); }
"$"         { return new Symbol(symsG.dolar, yycolumn, yyline, yytext()); }
"{"         { return new Symbol(symsG.allave, yycolumn, yyline, yytext()); }
"}"         { return new Symbol(symsG.cllave, yycolumn, yyline, yytext()); }
"["         { return new Symbol(symsG.acorchete, yycolumn, yyline, yytext()); }
"]"         { return new Symbol(symsG.ccorchete, yycolumn, yyline, yytext()); }
"("         { return new Symbol(symsG.aparentesis, yycolumn, yyline, yytext()); }
")"         { return new Symbol(symsG.cparentesis, yycolumn, yyline, yytext()); }
","         { return new Symbol(symsG.coma, yycolumn, yyline, yytext()); }
":"         { return new Symbol(symsG.dospuntos, yycolumn, yyline, yytext()); }
";"         { return new Symbol(symsG.puntoycoma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
{caracter}          { return new Symbol(symsG.caracter, yycolumn, yyline, yytext()); }
{cadena}            { return new Symbol(symsG.cadena, yycolumn, yyline, yytext().replace("\"", "")); }
{numero}            { return new Symbol(symsG.numero, yycolumn, yyline, yytext()); }
{iden}              { return new Symbol(symsG.iden, yycolumn, yyline, yytext()); }
{enter}              { return new Symbol(symsG.enter, yycolumn, yyline, yytext()); }

{enBlanco}  { /* ignore */ }
}


//-----------------errores lexicos-----------------------------
[^]         {ErroresGraphik.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}

