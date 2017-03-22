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
cad = \"(\\.|[^\"])*\"
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
"var"           { return new Symbol(symsG.var, yycolumn, yyline, yytext().toLowerCase()); }
"nuevo"         { return new Symbol(symsG.nuevo, yycolumn, yyline, yytext().toLowerCase()); }
"entero"        { return new Symbol(symsG.entero, yycolumn, yyline, yytext().toLowerCase()); }
"decimal"       { return new Symbol(symsG.decimal, yycolumn, yyline, yytext().toLowerCase()); }
"caracter"      { return new Symbol(symsG.caracter, yycolumn, yyline, yytext().toLowerCase()); }
"cadena"        { return new Symbol(symsG.cadena, yycolumn, yyline, yytext().toLowerCase()); }
"bool"          { return new Symbol(symsG.bool, yycolumn, yyline, yytext().toLowerCase()); }
"vacio"         { return new Symbol(symsG.vacio, yycolumn, yyline, yytext().toLowerCase()); }
"publico"       { return new Symbol(symsG.publico, yycolumn, yyline, yytext().toLowerCase()); }
"protegido"     { return new Symbol(symsG.protegido, yycolumn, yyline, yytext().toLowerCase()); }
"privado"       { return new Symbol(symsG.privado, yycolumn, yyline, yytext().toLowerCase()); }
"importar"      { return new Symbol(symsG.importar, yycolumn, yyline, yytext().toLowerCase()); }
".gk"           { return new Symbol(symsG.extgk, yycolumn, yyline, yytext().toLowerCase()); }
"als"           { return new Symbol(symsG.als, yycolumn, yyline, yytext().toLowerCase()); }
"hereda"        { return new Symbol(symsG.hereda, yycolumn, yyline, yytext().toLowerCase()); }
"retornar"      { return new Symbol(symsG.retornar, yycolumn, yyline, yytext().toLowerCase()); }
"llamarhk"      { return new Symbol(symsG.llamarhk, yycolumn, yyline, yytext().toLowerCase()); }
"llamar"        { return new Symbol(symsG.llamar, yycolumn, yyline, yytext().toLowerCase()); }
"inicio"        { return new Symbol(symsG.inicio, yycolumn, yyline, yytext().toLowerCase()); }
"incluir_hk"    { return new Symbol(symsG.incluirhk, yycolumn, yyline, yytext().toLowerCase()); }
"sino"          { return new Symbol(symsG.sino, yycolumn, yyline, yytext().toLowerCase()); }
"si"            { return new Symbol(symsG.si, yycolumn, yyline, yytext().toLowerCase()); }
"verdadero"     { return new Symbol(symsG.verdadero, yycolumn, yyline, yytext().toLowerCase()); }
"falso"         { return new Symbol(symsG.falso, yycolumn, yyline, yytext().toLowerCase()); }

"seleccion"     { return new Symbol(symsG.seleccion, yycolumn, yyline, yytext().toLowerCase()); }
"defecto"       { return new Symbol(symsG.defecto, yycolumn, yyline, yytext().toLowerCase()); }
"caso"          { return new Symbol(symsG.caso, yycolumn, yyline, yytext().toLowerCase()); }
"para"          { return new Symbol(symsG.para, yycolumn, yyline, yytext().toLowerCase()); }
"mientras"      { return new Symbol(symsG.mientras, yycolumn, yyline, yytext().toLowerCase()); }
"hacer"         { return new Symbol(symsG.hacer, yycolumn, yyline, yytext().toLowerCase()); }
"continuar"     { return new Symbol(symsG.continuar, yycolumn, yyline, yytext().toLowerCase()); }
"terminar"      { return new Symbol(symsG.terminar, yycolumn, yyline, yytext().toLowerCase()); }
"graphikar_funcion"     { return new Symbol(symsG.graphikarfuncion, yycolumn, yyline, yytext().toLowerCase()); }
"datos"         { return new Symbol(symsG.datos, yycolumn, yyline, yytext().toLowerCase()); }
"columna"       { return new Symbol(symsG.columna, yycolumn, yyline, yytext().toLowerCase()); }
"procesar"      { return new Symbol(symsG.procesar, yycolumn, yyline, yytext().toLowerCase()); }
"dondecada"     { return new Symbol(symsG.dondecada, yycolumn, yyline, yytext().toLowerCase()); }
"dondetodo"     { return new Symbol(symsG.dondetodo, yycolumn, yyline, yytext().toLowerCase()); }
"donde"         { return new Symbol(symsG.donde, yycolumn, yyline, yytext().toLowerCase()); }
"imprimir"      { return new Symbol(symsG.imprimir, yycolumn, yyline, yytext().toLowerCase()); }

"++"            { return new Symbol(symsG.masmas, yycolumn, yyline, yytext().toLowerCase()); }
"--"            { return new Symbol(symsG.menosmenos, yycolumn, yyline, yytext().toLowerCase()); }

"+"             { return new Symbol(symsG.mas, yycolumn, yyline, yytext().toLowerCase()); }
"-"             { return new Symbol(symsG.menos, yycolumn, yyline, yytext().toLowerCase()); }
"*"             { return new Symbol(symsG.por, yycolumn, yyline, yytext().toLowerCase()); }
"/"             { return new Symbol(symsG.dividido, yycolumn, yyline, yytext().toLowerCase()); }
"^"             { return new Symbol(symsG.pot, yycolumn, yyline, yytext().toLowerCase()); }
"||"            { return new Symbol(symsG.or, yycolumn, yyline, yytext().toLowerCase()); }
"&&"            { return new Symbol(symsG.and, yycolumn, yyline, yytext().toLowerCase()); }
"&|"            { return new Symbol(symsG.xor, yycolumn, yyline, yytext().toLowerCase()); }
"!"             { return new Symbol(symsG.not, yycolumn, yyline, yytext().toLowerCase()); }
"<="            { return new Symbol(symsG.menorigual, yycolumn, yyline, yytext().toLowerCase()); }
">="            { return new Symbol(symsG.mayorigual, yycolumn, yyline, yytext().toLowerCase()); }
"<"             { return new Symbol(symsG.menor, yycolumn, yyline, yytext().toLowerCase()); }
">"             { return new Symbol(symsG.mayor, yycolumn, yyline, yytext().toLowerCase()); }
"!="            { return new Symbol(symsG.diferente, yycolumn, yyline, yytext().toLowerCase()); }
"=="            { return new Symbol(symsG.igualigual, yycolumn, yyline, yytext().toLowerCase()); }

"?"             { return new Symbol(symsG.interroga, yycolumn, yyline, yytext().toLowerCase()); }
":"             { return new Symbol(symsG.dospuntos, yycolumn, yyline, yytext().toLowerCase()); }
"="             { return new Symbol(symsG.igual, yycolumn, yyline, yytext().toLowerCase()); }
"."             { return new Symbol(symsG.punto, yycolumn, yyline, yytext().toLowerCase()); }
","             { return new Symbol(symsG.coma, yycolumn, yyline, yytext().toLowerCase()); }
"{"             { return new Symbol(symsG.allave, yycolumn, yyline, yytext().toLowerCase()); }
"}"             { return new Symbol(symsG.cllave, yycolumn, yyline, yytext().toLowerCase()); }
"["             { return new Symbol(symsG.acorchete, yycolumn, yyline, yytext().toLowerCase()); }
"]"             { return new Symbol(symsG.ccorchete, yycolumn, yyline, yytext().toLowerCase()); }
"("             { return new Symbol(symsG.aparentesis, yycolumn, yyline, yytext().toLowerCase()); }
")"             { return new Symbol(symsG.cparentesis, yycolumn, yyline, yytext().toLowerCase()); }

//-----------------expresiones regulares-----------------------
{car}           { return new Symbol(symsG.car, yycolumn, yyline, yytext().replace("'", "")); }
{cad}           { return new Symbol(symsG.cad, yycolumn, yyline, yytext().replace("\"", "")); }
{numero}        { return new Symbol(symsG.numero, yycolumn, yyline, yytext().toLowerCase()); }
{dec}           { return new Symbol(symsG.dec, yycolumn, yyline, yytext().toLowerCase()); }
{iden}          { return new Symbol(symsG.iden, yycolumn, yyline, yytext().toLowerCase()); }

{enBlanco}  { /* ignore */ }
{comenBloque}  { System.out.println("Comentario bloque"); }
{comenLinea}  { System.out.println("Comentario liena"); }
}


//-----------------errores lexicos-----------------------------
[^]         {ErroresGraphik.agregarError("Error Lexico", "El caracter [" + yytext() + "] no es valido", yyline, yycolumn);}

