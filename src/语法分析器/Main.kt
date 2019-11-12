package 语法分析器

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    val input = Scanner(System.`in`)
    val map = input(input)
    val vn = map.map { it.key }
    val vt = findVT(map,vn)
    println(vn.toString())
    println(vt.toString())
}

fun findVT(map: HashMap<Char, String>,vn:List<Char>) :List<Char>{
    val temp = map.map{it.value}
    val list = ArrayList<Char>()
    temp.forEach {
        it.forEach { item ->
            if(item !in vn && item != '|' && item !in list && !item.isWhitespace()){
                list.add(item)
            }
        }
    }
    return list
}

val a = arrayOf('ε', '→')

val 测试文法= """

S → ABBA
A → a | ε
B → b | ε

S → aSe | B
B → bBe | C
C → cCe | d

E → E+T | T
T → T*F | F
F → (E) | i

S → Qc | c
Q → Rb | b
R → Sa | a
    
""".trimIndent()

