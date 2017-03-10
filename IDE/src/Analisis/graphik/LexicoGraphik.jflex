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
dec = [0-9]+"."[0-9]+
iden = [A-Za-z_][A-Za-z_0-9]*
car = "'"."'"
cadena = \"(\\.|[^\"])*\"
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
"var"           { return new Symbol(symsG.var, yycolumn, yyline, yytext()); }
"nuevo"         { return new Symbol(symsG.nuevo, yycolumn, yyline, yytext()); }
"entero"        { return new Symbol(symsG.entero, yycolumn, yyline, yytext()); }
"decimal"       { return new Symbol(symsG.decimal, yycolumn, yyline, yytext()); }
"caracter"      { return new Symbol(symsG.caracter, yycolumn, yyline, yytext()); }
"cadena"        { return new Symbol(symsG.cadena, yycolumn, yyline, yytext()); }
"bool"          { return new Symbol(symsG.bool, yycolumn, yyline, yytext()); }
"void"          { return new Symbol(symsG.vacio, yycolumn, yyline, yytext()); }
"publico"       { return new Symbol(symsG.publico, yycolumn, yyline, yytext()); }
"protegido"     { return new Symbol(symsG.protegido, yycolumn, yyline, yytext()); }
"privado"       { return new Symbol(symsG.privado, yycolumn, yyline, yytext()); }
"importar"      { return new Symbol(symsG.importar, yycolumn, yyline, yytext()); }
".gk"           { return new Symbol(symsG.extgk, yycolumn, yyline, yytext()); }
"als"           { return new Symbol(symsG.als, yycolumn, yyline, yytext()); }
"hereda"        { return new Symbol(symsG.hereda, yycolumn, yyline, yytext()); }

"if"            { return new Symbol(symsG._if, yycolumn, yyline, yytext()); }
"then"          { return new Symbol(symsG._then, yycolumn, yyline, yytext()); }
"else"          { return new Symbol(symsG._else, yycolumn, yyline, yytext()); }
"end"           { return new Symbol(symsG._end, yycolumn, yyline, yytext()); }
"case"          { return new Symbol(symsG._case, yycolumn, yyline, yytext()); }

"++"            { return new Symbol(symsG.masmas, yycolumn, yyline, yytext()); }
"--"            { return new Symbol(symsG.menosmenos, yycolumn, yyline, yytext()); }

"+"             { return new Symbol(symsG.mas, yycolumn, yyline, yytext()); }
"-"             { return new Symbol(symsG.menos, yycolumn, yyline, yytext()); }
"*"             { return new Symbol(symsG.por, yycolumn, yyline, yytext()); }
"/"             { return new Symbol(symsG.dividido, yycolumn, yyline, yytext()); }
"^"             { return new Symbol(symsG.potencia, yycolumn, yyline, yytext()); }
"||"            { return new Symbol(symsG.or, yycolumn, yyline, yytext()); }
"&&"            { return new Symbol(symsG.and, yycolumn, yyline, yytext()); }
"&|"            { return new Symbol(symsG.xor, yycolumn, yyline, yytext()); }
"!"             { return new Symbol(symsG.not, yycolumn, yyline, yytext()); }
"<="            { return new Symbol(symsG.menorigual, yycolumn, yyline, yytext()); }
">="            { return new Symbol(symsG.mayorigual, yycolumn, yyline, yytext()); }
"<"             { return new Symbol(symsG.menor, yycolumn, yyline, yytext()); }
">"             { return new Symbol(symsG.mayor, yycolumn, yyline, yytext()); }
"!="            { return new Symbol(symsG.diferente, yycolumn, yyline, yytext()); }
"=="            { return new Symbol(symsG.igualigual, yycolumn, yyline, yytext()); }

"?"             { return new Symbol(symsG.interrogacion, yycolumn, yyline, yytext()); }
":"             { return new Symbol(symsG.dospuntos, yycolumn, yyline, yytext()); }
"="             { return new Symbol(symsG.igual, yycolumn, yyline, yytext()); }
"{"             { return new Symbol(symsG.allave, yycolumn, yyline, yytext()); }
"}"             { return new Symbol(symsG.cllave, yycolumn, yyline, yytext()); }
"["             { return new Symbol(symsG.acorchete, yycolumn, yyline, yytext()); }
"]"             { return new Symbol(symsG.ccorchete, yycolumn, yyline, yytext()); }
"("             { return new Symbol(symsG.aparentesis, yycolumn, yyline, yytext()); }
")"             { return new Symbol(symsG.cparentesis, yycolumn, yyline, yytext()); }
","             { return new Symbol(symsG.coma, yycolumn, yyline, yytext()); }
"."             { return new Symbol(symsG.punto, yycolumn, yyline, yytext()); }
";"             { return new Symbol(symsG.puntoycoma, yycolumn, yyline, yytext()); }

//-----------------expresiones regulares-----------------------
{car}           { return new Symbol(symsG.caracter, yycolumn, yyline, yytext()); }
{cadena}        { return new Symbol(symsG.cadena, yycolumn, yyline, yytext().replace("\"", "")); }
{numero}        { return new Symbol(symsG.numero, yycolumn, yyline, yytext()); }
{dec}           { return new Symbol(symsG.dec, yycolumn, yyline, yytext()); }
{iden}          { return new Symbol(symsG.iden, yycolumn, yyline, yytext()); }

{enBlanco}  { /* ignore */ }
{comenBloque}  { System.out.println("Comentario bloque"); }
{comenLinea}  { System.out.println("Comentario liena"); }
}


//-----------------errores lexicos-----------------------------
[^]         {ErroresGraphik.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}

