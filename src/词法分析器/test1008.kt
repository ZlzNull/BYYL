package 词法分析器

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

var s = """
|int main()
|{
|   long int i;
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
    "auto",/*1*/ "break",/*2*/ "case",/*3*/ "char",/*4*/ "const",/*5*/ "continue",/*6*/
    "default",/*7*/ "do",/*8*/ "double",/*9*/ "else",/*10*/ "enum",/*11*/ "extern",/*12*/ "float",/*13*/
    "for",/*14*/ "goto",/*15*/ "if",/*16*/ "int",/*17*/ "Int",/*18*/ "long",/*19*/ "Long",/*20*/ "register",/*21*/
    "return",/*21*/ "short",/*22*/ "signed",/*23*/ "sizeof",/*24*/ "static",/*25*/ "struct",/*26*/
    "switch",/*27*/ "typedef",/*28*/ "union",/*29*/ "unsigned",/*30*/ "void",/*31*/ "volatile",/*32*/
    "while",/*33*/ "main",/*34*/ "include",/*35*/ "printf",/*36*/ "scanf",/*37*/ "getch"/*38*/
)
val operatorOrDelimiter = arrayOf(
    '+', '-','*', '/', ';', '(', ')', '^',
    ',', '\"', '\'', '~', '#', '%', '[',
    ']', '{', '}', '\\', '.', '?', ':'
)

val mark = ArrayList<String>()
var index = 0;

fun main() {
    removeEnter()
    println(s)

    while (index < s.length - 1) {
        wordAnalysis()
    }

    println(mark.toString())
}

fun wordAnalysis(): Int? {
//    println(s[index])
    when {
        s[index].isLetter() -> {
            var word = ""
            word += s[index++]
            while (s[index].isLetter() || s[index].isDigit() || s[index] == '.') {
                word += s[index++]
            }
            index++
            when {
                word in reserveWord -> {
                    println("这是一个保留字 = $word")
                    return 3
                }
                "." in word -> {
                    println("这是一个头文件引用 = $word")
                    return 3
                }
                word !in mark -> {
                    mark.add(word)
                    println("这是一个关键字 = $word")
                    return 3
                }
            }
        }
        s[index].isDigit() -> {
            var num = ""
            num += s[index++]
            while(s[index].isDigit() || s[index] == '.'){
                num+=s[index++]
            }
            if('.' in num){
                println("这是一个数值 = ${num.toDouble()}")
            }else{
                println("这是一个数值 = ${num.toInt()}")
            }
        }
        s[index] == '%' -> {
            var str = ""
            str += s[index++]
            while (s[index].isLetter()) {
                str += s[index++]
            }
            println("这是一个输出格式控制 = $str")
        }
        s[index] == '/' -> {
            if(s[index+1] == '/'){
                var tempIndex = index + 2
                while(s[tempIndex] != '\n'){
                    tempIndex++
                }
                s = s.replaceRange(index,tempIndex,"")
                println("单行注释去除成功")
                println(s)
            }else if(s[index + 1] == '*'){
                var tempIndex = index + 2
                while(s[tempIndex] != '*' || s[tempIndex+1] != '/'){
                    tempIndex++
                    if(tempIndex == s.length){
                        //已到程序结尾，注释出错
                        println("注释出错")
                    }
                }
                s= s.replaceRange(index,tempIndex+2,"")
                println("多行注释去除成功")
                println(s)
            }
        }
        s[index] in operatorOrDelimiter -> {
            return operatorOrDelimiter.indexOf(s[index++] + 1)
        }
        s[index] == '<' -> {
            index++
            return when(s[index]){
                '=' -> 3
                '<' -> 3
                else -> 3
            }
        }
        s[index] == '>' -> {
            index++
            return when(s[index]){
                '=' -> 3
                '>' -> 3
                else -> 3
            }
        }
        s[index] == '=' -> {
            index++
            return when(s[index]){
                '=' -> 3
                else -> 3
            }
        }
        s[index] == '!' -> {
            index++
            return when(s[index]){
                '=' -> 3
                else -> 3
            }
        }
        s[index] == '&' -> {
            index++
            return when(s[index]){
                '&' -> 3
                else -> 3
            }
        }
        s[index] == '|' -> {
            index++
            return when(s[index]){
                '|' -> 3
                else -> 3
            }
        }
        s[index] == ' ' -> {
            index++
            return null
        }
        s[index] == '\n' -> {
            index++
            return null
        }
        else -> {
            println(s[index] + "--不能被识别")
        }
    }
    return null
}

fun removeEnter(){
    s = s.replace("\n","")
//    s = s.replace("    ","")
    s = s.replace(";",";\n")
    s = s.replace("}","}\n")
    s = s.replace("{","{\n")
    s = s.replace(".h>",".h>\n")
    s = s.replace(".h\"",".h\"\n")
}