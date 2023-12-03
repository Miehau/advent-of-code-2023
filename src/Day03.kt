val specialCharacter = "[!@#$%^&*+\\-/=]".toRegex()
fun main() {
    fun touchesSpecialCharacterAbove(
        index: Int,
        splitInput: List<List<String>>,
        number: Pair<String, IntRange>
    ): Boolean {
        return if (index > 0) {
            splitInput[index - 1].subList(
                valueOrZero(number.second.first - 1),
                valueOrLast(number.second.last + 2, splitInput[index])
            )
                .any { specialCharacter.matches(it) }
        } else {
            false
        }
    }

    fun touchesSpecialCharacterInlineBefore(
        splitInput: List<List<String>>,
        index: Int,
        number: Pair<String, IntRange>
    ) = specialCharacter.matches(splitInput[index][valueOrZero(number.second.first - 1)])

    fun touchesSpecialCharacterInlineAfter(
        splitInput: List<List<String>>,
        index: Int,
        number: Pair<String, IntRange>
    ) = specialCharacter.matches(splitInput[index][valueOrLast(number.second.last + 1, splitInput[index])])

    fun touchesSpecialCharacterBelow(
        index: Int,
        splitInput: List<List<String>>,
        number: Pair<String, IntRange>
    ): Boolean {
        return if (index < splitInput.size - 1) {
            splitInput[index + 1].subList(
                valueOrZero(number.second.first - 1),
                valueOrLast(number.second.last + 2, splitInput[index])
            )
                .any { specialCharacter.matches(it) }
        } else {
            false
        }
    }

    fun part1(input: List<String>): Int {
        val splitInput = input
            .map { line -> line.split("").drop(1).dropLast(1) }
            .toList()
        val numbersInLines = input
            .mapIndexed { index, line ->
                val numbersInLines = numberRegex
                    .findAll(line)
                    .toList()
                    .map { Pair(it.value, it.range) }
                Pair(index, numbersInLines)
            }
        val sum = numbersInLines.mapIndexed { index, numbersInLine ->
            numbersInLine.second.mapNotNull { number ->
                val touchesAbove = touchesSpecialCharacterAbove(index, splitInput, number)
                val touchesBefore = touchesSpecialCharacterInlineBefore(splitInput, index, number)
                val touchesAfter = touchesSpecialCharacterInlineAfter(splitInput, index, number)
                val touchesBelow = touchesSpecialCharacterBelow(index, splitInput, number)
                println(number.first)
                println("$touchesAbove || $touchesBefore || $touchesAfter || $touchesBelow")
                if (touchesAbove || touchesBefore || touchesAfter || touchesBelow) {
                    number.first.toInt()
                } else {
                    null
                }
            }.sumOf { it }
        }.sumOf { it }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day3_test")
    check(part1(testInput) == 4361)

    val input = readInput("day3")
    part1(input).println()
    part2(input).println()
}

fun valueOrZero(value: Int): Int {
    return if (value < 0) {
        0
    } else {
        value
    }
}

fun valueOrLast(value: Int, collection: Collection<Any>): Int {
    return if (value >= collection.size) {
        collection.size - 1
    } else {
        value
    }
}