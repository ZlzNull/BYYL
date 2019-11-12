package 语法分析器

import java.util.*
import kotlin.collections.HashMap


fun input(input:Scanner):HashMap<Char,String>{
    val map = HashMap<Char,String>()
    while (input.hasNextLine()) {
        val line = input.nextLine()
        if(line.isEmpty()){
            break
        }
        val temp = line.split('→')
        var key = temp[0]
        if(key[1].isWhitespace()){
            key = key.substring(0,1)
        }
        var value = temp[1]
        if(value[0].isWhitespace()){
           value = value.substring(1,value.length)
        }
        map[key[0]] = value
    }
    println(map.toString())
    return map
}