package 词法分析器
//
//fun main(){
//
//    var i = "2.0"
//    println(i.toBinaryString())
//
//}

//fun String.toBinaryString(): String{
//    var a = ""
//    var b = ""
//    return if('.' in this){
//        a = this.substring(0,this.indexOfFirst { it == '.' })
//        a = a.toInt().toBinaryString()
//        b = "0" + this.substring(this.indexOfFirst { it == '.' } , this.length)
//        b = b.toDouble().toBinaryString()
//        "$a.$b"
//    }else{
//        a = this
//        a = a.toInt().toBinaryString()
//        a
//    }
//}
//
//fun Double.toBinaryString(): String{
//    var a = this
//    if(a == 0.0){
//        return "0"
//    }
//    println("a = $a")
//    var b = ""
//    for(i in 1..20){
//        a *= 2
//        b +=  a.toInt().toString()
//        a %= 1
//    }
//    return b
//}