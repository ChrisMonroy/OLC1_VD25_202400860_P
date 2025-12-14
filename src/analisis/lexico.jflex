package analisis;

import java_cup.runtime.Symbol;

%%
%{
    String cadena = "";
%}

%init{
    yyline = 1;
    yycolumn = 1;
%init}

%cup
%class scanner
%public 
%line
%char
%column
%full
%ignorecase

%state CADENA
%state CARACTER
%state COMENTARIO_MULTILINEA

// Expresiones terminales
DIGITO = [0-9]
LETRA = [a-zA-Z_]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO})*
ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+"."{DIGITO}+
BOOLEANO = true|false

BLANCOS = [\ \r\t\n\f]+
COMENTARIO_LINEA = "//".*
COMENTARIO_MULTILINEA_INICIO = "/\*"
COMENTARIO_MULTILINEA_FIN = "\*/

%%

// Ignorar espacios en blanco
<YYINITIAL> {BLANCOS} { }

// Comentarios
<YYINITIAL> {COMENTARIO_LINEA} { }
<YYINITIAL> {COMENTARIO_MULTILINEA_INICIO} { yybegin(COMENTARIO_MULTILINEA); }

<COMENTARIO_MULTILINEA> {
    {COMENTARIO_MULTILINEA_FIN} { yybegin(YYINITIAL); }
    .|\n { }
}

// Palabras reservadas
<YYINITIAL> "var"     { return new Symbol(sym.VAR, yyline, yycolumn, yytext()); }
<YYINITIAL> "if"      { return new Symbol(sym.IF, yyline, yycolumn, yytext()); }
<YYINITIAL> "else"    { return new Symbol(sym.ELSE, yyline, yycolumn, yytext()); }
<YYINITIAL> "while"   { return new Symbol(sym.WHILE, yyline, yycolumn, yytext()); }
<YYINITIAL> "for"     { return new Symbol(sym.FOR, yyline, yycolumn, yytext()); }
<YYINITIAL> "do"      { return new Symbol(sym.DO, yyline, yycolumn, yytext()); }
<YYINITIAL> "break"   { return new Symbol(sym.BREAK, yyline, yycolumn, yytext()); }
<YYINITIAL> "continue"{ return new Symbol(sym.CONTINUE, yyline, yycolumn, yytext()); }
<YYINITIAL> "print"   { return new Symbol(sym.PRINT, yyline, yycolumn, yytext()); }

// Tipos de dato
<YYINITIAL> "int"     { return new Symbol(sym.INT, yyline, yycolumn, yytext()); }
<YYINITIAL> "double"  { return new Symbol(sym.DOUBLE, yyline, yycolumn, yytext()); }
<YYINITIAL> "bool"    { return new Symbol(sym.BOOL, yyline, yycolumn, yytext()); }
<YYINITIAL> "char"    { return new Symbol(sym.CHAR, yyline, yycolumn, yytext()); }
<YYINITIAL> "string"  { return new Symbol(sym.STRING, yyline, yycolumn, yytext()); }

// Operadores aritméticos
<YYINITIAL> "+" { return new Symbol(sym.MAS, yyline, yycolumn, yytext()); }
<YYINITIAL> "-" { return new Symbol(sym.MENOS, yyline, yycolumn, yytext()); }
<YYINITIAL> "*" { return new Symbol(sym.MULT, yyline, yycolumn, yytext()); }
<YYINITIAL> "/" { return new Symbol(sym.DIV, yyline, yycolumn, yytext()); }
<YYINITIAL> "**" { return new Symbol(sym.POTENCIA, yyline, yycolumn, yytext()); }
<YYINITIAL> "%" { return new Symbol(sym.MODULO, yyline, yycolumn, yytext()); }

// Operadores relacionales
<YYINITIAL> "==" { return new Symbol(sym.IGUAL, yyline, yycolumn, yytext()); }
<YYINITIAL> "!=" { return new Symbol(sym.DIFERENTE, yyline, yycolumn, yytext()); }
<YYINITIAL> "<"  { return new Symbol(sym.MENOR, yyline, yycolumn, yytext()); }
<YYINITIAL> "<=" { return new Symbol(sym.MENOR_IGUAL, yyline, yycolumn, yytext()); }
<YYINITIAL> ">"  { return new Symbol(sym.MAYOR, yyline, yycolumn, yytext()); }
<YYINITIAL> ">=" { return new Symbol(sym.MAYOR_IGUAL, yyline, yycolumn, yytext()); }

// Operadores lógicos
<YYINITIAL> "&&" { return new Symbol(sym.AND, yyline, yycolumn, yytext()); }
<YYINITIAL> "||" { return new Symbol(sym.OR, yyline, yycolumn, yytext()); }
<YYINITIAL> "!"  { return new Symbol(sym.NOT, yyline, yycolumn, yytext()); }

// Signos
<YYINITIAL> "(" { return new Symbol(sym.PAR_ABRIR, yyline, yycolumn, yytext()); }
<YYINITIAL> ")" { return new Symbol(sym.PAR_CERRAR, yyline, yycolumn, yytext()); }
<YYINITIAL> "{" { return new Symbol(sym.LLAVE_ABRIR, yyline, yycolumn, yytext()); }
<YYINITIAL> "}" { return new Symbol(sym.LLAVE_CERRAR, yyline, yycolumn, yytext()); }
<YYINITIAL> ";" { return new Symbol(sym.PUNTO_COMA, yyline, yycolumn, yytext()); }
<YYINITIAL> ":" { return new Symbol(sym.DOS_PUNTOS, yyline, yycolumn, yytext()); }

// Literales
<YYINITIAL> {ENTERO}   { return new Symbol(sym.ENTERO, yyline, yycolumn, yytext()); }
<YYINITIAL> {DECIMAL}  { return new Symbol(sym.DECIMAL, yyline, yycolumn, yytext()); }
<YYINITIAL> {BOOLEANO}{ return new Symbol(sym.BOOLEANO, yyline, yycolumn, yytext().equalsIgnoreCase("true")); }

// Cadenas
<YYINITIAL> \" { cadena = ""; yybegin(CADENA); }

<CADENA> {
    \"        { String tmp = cadena; cadena = ""; yybegin(YYINITIAL); return new Symbol(sym.CADENA, yyline, yycolumn, tmp); }
    \\n       { cadena += "\n"; }
    \\t       { cadena += "\t"; }
    \\r       { cadena += "\r"; }
    \\\"      { cadena += "\""; }
    \\\'      { cadena += "'"; }
    \\\\      { cadena += "\\"; }
    [^\n\"\\] { cadena += yytext(); }
    \n        { throw new Error("Error léxico: cadena no cerrada en línea " + yyline); }
}

// Caracteres
<YYINITIAL> \' { cadena = ""; yybegin(CARACTER); }

<CARACTER> {
    \'        { 
        char c = cadena.isEmpty() ? '\0' : cadena.charAt(0);
        cadena = "";
        yybegin(YYINITIAL);
        return new Symbol(sym.CARACTER, yyline, yycolumn, c);
    }
    \\n       { cadena += "\n"; }
    \\t       { cadena += "\t"; }
    \\r       { cadena += "\r"; }
    \\\"      { cadena += "\""; }
    \\\'      { cadena += "'"; }
    \\\\      { cadena += "\\"; }
    [^\n\'\\] { if (cadena.isEmpty()) cadena += yytext().charAt(0); }
    \n        { throw new Error("Error léxico: carácter no cerrado en línea " + yyline); }
}

// Identificadores
<YYINITIAL> {IDENTIFICADOR} { return new Symbol(sym.IDENTIFICADOR, yyline, yycolumn, yytext()); }

// Error léxico
. { throw new Error("Error léxico en línea " + yyline + ", columna " + yycolumn + ": carácter no válido '" + yytext() + "'"); }