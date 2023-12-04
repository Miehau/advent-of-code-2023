val specialCharacter = "[!@#$%^&*+\\-/=]".toRegex()
fun main() {
    fun addInteractionToGear(
        coordinates: Pair<Int, Int>,
        number: Pair<String, IntRange>
    ) {
        if (gears.containsKey(coordinates)) {
            gears[coordinates]!!.interact(number.first.toInt())
        } else {
            val gear = Gear(coordinates)
            gear.interact(number.first.toInt())
            gears[coordinates] = gear
        }
    }

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
                .onEachIndexed { offset, v ->
                    if (v == "*") {
                        val coordinates = Pair(valueOrZero(number.second.first - 1) + offset, index - 1)
                        addInteractionToGear(coordinates, number)
                    }
                }
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
        .also {
            if ("*" == splitInput[index][valueOrZero(number.second.first - 1)]) {
                val coordinates = Pair(valueOrZero(number.second.first - 1), index)
                addInteractionToGear(coordinates, number)
            }
        }

    fun touchesSpecialCharacterInlineAfter(
        splitInput: List<List<String>>,
        index: Int,
        number: Pair<String, IntRange>
    ) = specialCharacter.matches(splitInput[index][valueOrLast(number.second.last + 1, splitInput[index])])
        .also {
            if ("*" == splitInput[index][valueOrLast(number.second.last + 1, splitInput[index])]) {
                val coordinates = Pair(valueOrLast(number.second.last + 1, splitInput[index]), index)
                addInteractionToGear(coordinates, number)
            }
        }

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
                .onEachIndexed { offset, v ->
                    if (v == "*") {
                        val coordinates = Pair(valueOrZero(number.second.first - 1) + offset, index + 1)
                        addInteractionToGear(coordinates, number)
                    }
                }
                .any { specialCharacter.matches(it) }
        } else {
            false
        }
    }

    fun part1(input: List<String>): Int {
        gears.clear()
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
                if (touchesAbove || touchesBefore || touchesAfter || touchesBelow) {
                    number.first.toInt()
                } else {
                    null
                }
            }.sumOf { it }
        }.sumOf { it }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day3_test")
    check(part1(testInput) == 4361)

    val input = readInput("day3")
    check(part1(input) == 539713)
    val gearRatios = gears
        .filter { it.value.getInteractionCount() == 2 }
        .map { it.value.getGearRatio() }
        .sum()
    val asd = gears.entries.sortedBy { it.key.second }
        .map { Pair(it.key, it.value.interactions) }
    println("gear ratios: $gearRatios")
}

val gears = mutableMapOf<Pair<Int, Int>, Gear>()

data class Gear(val coordinates: Pair<Int, Int>) {
    val interactions = mutableListOf<Int>()

    fun interact(number: Int) {
        this.interactions.add(number)
    }

    fun getInteractionCount(): Int {
        return this.interactions.count()
    }

    fun getGearRatio(): Int {
        return this.interactions
            .fold(1) { acc, next -> acc * next }
    }
}