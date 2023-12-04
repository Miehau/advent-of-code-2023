fun main() {
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    check(part1(input) == 24848)
    println("Expecting 30 cards and got ${part2(testInput)}")
    part2(input).println()
}

fun part1(input: List<String>): Int {
    return input
        .map { line -> removeGameNumber(line) }
        .mapIndexed { index, line -> mapIntoScratchcards(index, line) }
        .sumOf { card -> card.calculatePoints() }
}

fun part2(input: List<String>): Int {
    val cards = input
        .map { line -> removeGameNumber(line) }
        .mapIndexed { index, line -> mapIntoScratchcards(index, line) }

    return cards
        .flatMap { card -> multiplyCardsFor(card, cards, listOf(card)) }
        .count()
}

fun multiplyCardsFor(
    currentCard: Scratchcard,
    cards: List<Scratchcard>,
    allCards: List<Scratchcard>
): List<Scratchcard> {
    val numberOfWinningCards = currentCard.numberOfWinningNumbers()
    if (isPenultimateCard(numberOfWinningCards, currentCard, cards)) {
        return listOf(cards.last())
            .fold(allCards) { acc, next ->
                acc.plus(multiplyCardsFor(next, cards, listOf(next)))
            }
    }
    return getCardsToMultiply(cards, currentCard, numberOfWinningCards)
        .fold(allCards) { acc, next ->
            acc.plus(multiplyCardsFor(next, cards, listOf(next)))
        }

}

private fun getCardsToMultiply(
    cards: List<Scratchcard>,
    currentCard: Scratchcard,
    numberOfWinningCards: Int
) = cards.subList(
    valueOrLast(currentCard.id, cards),
    valueOrLast(currentCard.id + numberOfWinningCards, cards)
)

private fun isPenultimateCard(
    numberOfWinningCards: Int,
    currentCard: Scratchcard,
    cards: List<Scratchcard>
) = numberOfWinningCards > 0 && currentCard.id == cards.last().id - 1

private fun mapIntoScratchcards(index: Int, line: String): Scratchcard {
    val (winningNumbers, yourNumbers) = line.split("|")
    return Scratchcard(
        index + 1,
        winningNumbers.trim().split("\\W+".toRegex()),
        yourNumbers.trim().split("\\W+".toRegex())
    )
}

private fun removeGameNumber(line: String) = line.split(":")[1]

data class Scratchcard(val id: Int, val winningNumbers: List<String>, val yourNumbers: List<String>) {

    fun calculatePoints(): Int {
        return yourNumbers
            .filter { number -> winningNumbers.contains(number) }
            .map { it.toDouble() }
            .fold(0.5) { acc, _ -> acc * 2 }
            .toInt()
    }

    fun numberOfWinningNumbers(): Int {
        return yourNumbers
            .count { number -> winningNumbers.contains(number) }
    }
}