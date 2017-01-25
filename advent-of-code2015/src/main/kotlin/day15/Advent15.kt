package day15

import parseInput

/**
--- Day 15: Science for Hungry People ---

Today, you set out on the task of perfecting your milk-dunking cookie recipe. All you have to do is find the right balance of ingredients.

Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a list of the remaining ingredients you could use to finish the recipe (your puzzle input) and their properties per teaspoon:

capacity (how well it helps the cookie absorb milk)
durability (how well it keeps the cookie intact when full of milk)
flavor (how tasty it makes the cookie)
texture (how it improves the feel of the cookie)
calories (how many calories it adds to the cookie)
You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can reproduce your results in the future. The total score of a cookie can be found by adding up each of the properties (negative totals become 0) and then multiplying together everything except calories.

For instance, suppose you have these two ingredients:

Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:

A capacity of 44*-1 + 56*2 = 68
A durability of 44*-2 + 56*3 = 80
A flavor of 44*6 + 56*-2 = 152
A texture of 44*3 + 56*-1 = 76
Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880, which happens to be the best score possible given these ingredients. If any properties had produced a negative total, it would have instead become zero, causing the whole score to multiply to zero.

Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make?

 */

fun main(args: Array<String>) {
    val test = """
               |Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
               |Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
               """.trimMargin()
    val input = parseInput("day15-input.txt")

    println(findBestCombination(test))
}

fun findBestCombination(input: String): Int? {
    val ingredients = parseIngredients(input)

    fun mixtures(size: Int, max: Int, acc: MutableList<List<Int>> = mutableListOf()): List<List<Int>> {
        val start = if (size == 1) max else 0

        for (i in start..max) {
            val left = max - i
            if (size - 1 > 0) {
                mixtures(size - 1, left).mapTo(acc) { it + i }
            } else {
                acc.add(listOf(i))
            }
        }

        return acc
    }

    fun score(recipe: List<Int>): Int {
        val proportions = ingredients.map { it.toList() }.zip(recipe).map { p -> p.first.map { it * p.second } }
        val dough = proportions.reduce { a, b -> a.zip(b).map { it.toList().sum() } }.dropLast(1)
        val result = dough.map { Math.max(it, 0) }.reduce { a, b -> a * b }

        // 62_842_880
        // 15_020_544
        return result
    }

    return mixtures(ingredients.size, 100).map(::score).max()
}

data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
) {
    fun toList() = listOf(capacity, durability, flavor, texture)
}

private fun parseIngredients(input: String) = input
        .split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            val name = it.split(":", limit = 2).first()
            val (capacity, durability, flavor, texture, calories) = Regex("""(\d+)""")
                    .findAll(it)
                    .map { it.groupValues[1] }
                    .toList()

            Ingredient(
                    name = name,
                    capacity = capacity.toInt(),
                    durability = durability.toInt(),
                    flavor = flavor.toInt(),
                    texture = texture.toInt(),
                    calories = calories.toInt()
            )
        }
