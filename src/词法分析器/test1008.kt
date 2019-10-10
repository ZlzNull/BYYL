package 词法分析器

import kotlin.system.exitProcess

//val type = arrayOf("标识符","保留字","常数","运算符","界符")

//var s = """#include<stdio.h>
//        |
//        |int main(){
//        |   int a[10];//定义数组
//        |   //123
//        |   double b;
//        |   //132
//        |   /*123*/
//        |   /*321
//        |   123
//        |   */
//        |   b = 2.0;
//        |   printf("%lf",b);
//        |}""".trimMargin()

var s = """#include<stdio.h>
|#include"conio.h"
|
|int main()
|{
|
|   long int i;
|   string a = "张";
|   int bonus1,bonus2,bonus4,bonus6,bonus10,bonus;
|   scanf("%ld",&i);
|   bonus1=100000*0.1;
|   bonus2=bonus1+100000*0.75;
|   bonus4=bonus2+200000*0.5;
|   bonus6=bonus4+200000*0.3;
|   bonus10=bonus6+400000*0.15;
|   if(i<=100000)
|       bonus=i*0.1;
|   else if(i<=200000)
|       bonus=bonus1+(i-100000)*0.075;
|   else if(i<=400000)
|       bonus=bonus2+(i-200000)*0.05;
|   else if(i<=600000)
|       bonus=bonus4+(i-400000)*0.03;
|   else if(i<=1000000)
|       bonus=bonus6+(i-600000)*0.015;
|   else
|       bonus=bonus10+(i-1000000)*0.01;
|   printf("bonus=%d",bonus);
|   getch();
|}
""".trimMargin()

val reserveWord = arrayOf(
    "int"/*1*/, "long"/*2*/, "short"/*3*/, "float"/*4*/, "double"/*5*/, "char"/*6*/,
    "unsigned"/*7*/, "signed"/*8*/, "const"/*9*/, "void"/*10*/, "volatile"/*11*/,
    "enum"/*12*/, "struct"/*13*/, "union"/*14*/, "if"/*15*/, "else"/*16*/, "goto"/*17*/,
    "switch"/*18*/, "case"/*19*/, "do"/*20*/, "while"/*21*/, "for"/*22*/, "continue"/*23*/,
    "break"/*24*/, "return"/*25*/, "default"/*26*/, "typedef"/*27*/, "auto"/*28*/,
    "register"/*29*/, "extern"/*30*/, "static"/*31*/, "sizeof"/*32*/
)
val operatorOrDelimiter = arrayOf(
    ';'/*33*/, '('/*34*/, ')'/*35*/, '^'/*36*/, ','/*37*/, '\"'/*38*/, '\''/*39*/, '#'/*40*/,
    '['/*41*/, ']'/*42*/, '{'/*43*/, '}'/*44*/, '\\'/*45*/, '.'/*46*/, '?'/*47*/, ':'/*48*/
)


val operationalCharacter = arrayOf(
    "+"/*49*/, "-"/*50*/, "*"/*51*/, "/"/*52*/, "%"/*53*/, "++"/*54*/, "--"/*55*/, "=="/*56*/, "!="/*57*/,
    ">"/*58*/, "<"/*59*/, ">="/*60*/, "<="/*61*/, "&&"/*62*/, "||"/*63*/, "!"/*64*/, "&"/*65*/, "|"/*66*/,
    "^"/*67*/, "~"/*68*/, "<<"/*69*/, ">>"/*70*/, "="/*71*/, "+="/*72*/, "-="/*73*/, "*="/*74*/, "/="/*75*/,
    "%="/*76*/, "<<="/*77*/, ">>="/*78*/, "&="/*79*/, "^="/*80*/, "|="/*81*/, "->"/*82*/
)

data class Bean(
    val VALUE: String,
    val CLASS: Int
)

var index: Int = 0
val wordList = ArrayList<Bean>()


fun main() {
    while (index < s.length - 1) {
        removeComment()
    }

    removeEnter()
    println(s)
    index = 0
    while (index < s.length - 1) {
        val bean: Bean? = wordAnalysis()
        if (bean != null) {
            wordList.add(bean)
        }
    }

    wordList.println()

}

fun ArrayList<Bean>.println(){
    this.forEach {
        println("( ${it.VALUE} , ${it.CLASS} )")
    }
}

fun removeComment() {
    if (s[index] == '/') {
        when {
            s[index + 1] == '/' -> {
                var tempIndex = index + 2
                while (s[tempIndex] != '\n') {
                    tempIndex++
                }
                s = s.replaceRange(index, tempIndex, "")
                index++
            }
            s[index + 1] == '*' -> {
                var tempIndex = index + 2
                while (s[tempIndex] != '*' || s[tempIndex + 1] != '/') {
                    tempIndex++
                    if (tempIndex == s.length) {
                        //已到程序结尾，注释出错
                        println("注释出错")
                        //直接结束程序
                        exitProcess(-1)
                    }
                }
                s = s.replaceRange(index, tempIndex + 2, "")
                index++
            }
            else -> index++
        }
    } else {
        index++
    }
}

fun wordAnalysis(): Bean? =
    when {
        s[index].isLetter() -> {
            var word = ""
            word += s[index++]
            while (s[index].isLetter() || s[index].isDigit()) {
                word += s[index++]
            }
            if (word in reserveWord) {
                Bean(word, reserveWord.indexOf(word) + 1)
            } else {
                Bean(word, wordList.size + 83)
            }
        }
        s[index].isDigit() -> {
            var num = ""
            num += s[index++]
            while (s[index].isDigit() || s[index] == '.') {
                num += s[index++]
            }
            Bean(num, wordList.size + 83)
        }
        s[index] == '+' -> {
            index++
            when {
                s[index] == '+' -> {
                    index++
                    createBean("++")
                }
                s[index] == '=' -> {
                    index++
                    createBean("+=")
                }
                else -> {
                    createBean("+")
                }
            }
        }
        s[index] == '-' -> {
            index++
            when {
                s[index] == '-' -> {
                    index++
                    createBean("--")
                }
                s[index] == '=' -> {
                    index++
                    createBean("-=")
                }
                s[index] == '>' -> {
                    index++
                    createBean("->")
                }
                else -> {
                    createBean("-")
                }
            }
        }
        s[index] == '%' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("%=")
            } else {
                createBean("%")
            }
        }

        s[index] == '/' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("/=")
            } else {
                createBean("/")
            }
        }
        s[index] == '*' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("*=")
            } else {
                createBean("*")
            }
        }
        s[index] == '=' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("==")
            } else {
                createBean("=")
            }
        }
        s[index] == '!' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("!=")
            } else {
                createBean("!")
            }
        }
        s[index] == '<' -> {
            index++
            when {
                s[index] == '=' -> {
                    index++
                    createBean("<=")
                }
                s[index] == '<' -> {
                    index++
                    if (s[index] == '=') {
                        index++
                        createBean("<<=")
                    } else {
                        createBean("<<")
                    }
                }
                else -> {
                    createBean("<")
                }
            }
        }
        s[index] == '>' -> {
            index++
            when {
                s[index] == '=' -> {
                    index++
                    createBean(">=")
                }
                s[index] == '>' -> {
                    index++
                    if (s[index] == '=') {
                        index++
                        createBean(">>=")
                    } else {
                        createBean(">>")
                    }
                }
                else -> {
                    createBean(">")
                }
            }
        }
        s[index] == '&' -> {
            index++
            when {
                s[index] == '&' -> {
                    index++
                    createBean("&&")
                }
                s[index] == '=' -> {
                    index++
                    createBean("&=")
                }
                else -> {
                    createBean("&")
                }
            }
        }
        s[index] == '|' -> {
            index++
            when {
                s[index] == '|' -> {
                    index++
                    createBean("||")
                }
                s[index] == '=' -> {
                    index++
                    createBean("|=")
                }
                else -> {
                    createBean("|")
                }
            }
        }
        s[index] == '^' -> {
            index++
            if (s[index] == '=') {
                index++
                createBean("^=")
            } else {
                createBean("^")
            }
        }
        s[index] == '~' -> {
            index++
            createBean("~")
        }
        s[index] in operatorOrDelimiter -> {
            Bean("" + s[index], operatorOrDelimiter.indexOf(s[index++]) + 33)
        }
        s[index] == ' ' -> {
            index++
            null
        }
        s[index] == '\n' -> {
            index++
            null
        }
        s[index] == '#' -> {
            index++
            Bean("#",0)
        }
        else -> {
            println(s[index] + "--不能被识别")
            exitProcess(-1)
        }
    }


fun removeEnter() {
    s = s.replace("\n", "")
    s = s.replace(";", ";\n")
    s = s.replace("}", "}\n")
    s = s.replace("{", "{\n")
    s = s.replace(".h>", ".h>\n")
    s = s.replace(".h\"", ".h\"\n")
}

fun createBean(str: String) = Bean(str, operationalCharacter.indexOf(str) + 49)