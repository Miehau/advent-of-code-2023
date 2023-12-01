private val wordToDigit = buildMap {
    put("one", "1")
    put("two", "2")
    put("three", "3")
    put("four", "4")
    put("five", "5")
    put("six", "6")
    put("seven", "7")
    put("eight", "8")
    put("nine", "9")
}

fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { line -> extractNumber(line) }
            .sumOf { it.toInt() }

    }

    fun part2(input: List<String>): Int {
        return part1(
            input.map { line -> extractNumbersFrom(line) }
        )
    }

    val input = readInput("day1")
    part1(input).println()
    part2(input).println()
}

fun extractNumber(line: String): String {
    val first = line.find { it.isDigit() }
    val last = line.findLast { it.isDigit() }
    return first.toStringOrEmpty() + last.toStringOrEmpty()
}

private fun Char?.toStringOrEmpty(): String {
    return this?.toString() ?: ""
}

fun extractNumbersFrom(input: String): String {
    return input.mapIndexed() { index, char ->
        if (char.isDigit()) {
            char.toString()
        } else {
            val substringFromIndex = input.substring(index)
            getDigitFromBeginningOfWord(substringFromIndex)
        }
    }.joinToString("")
}

private fun getDigitFromBeginningOfWord(input: String): String {
    wordToDigit.forEach { entry ->
        if (input.startsWith(entry.key)) {
            return entry.value
        }
    }
    return ""
}
