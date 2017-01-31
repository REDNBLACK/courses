package day15

import parseInput
import splitToLines

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

--- Part Two ---

Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly 500 calories per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning process the same (100 teaspoons, same ingredients, same scoring system).

For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total score would go down, though: only 57600000, the best you can do in such trying circumstances.

Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make with a calorie total of 500?

 */

fun main(args: Array<String>) {
    val test = """
               |Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
               |Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
               """.trimMargin()
    val input = parseInput("day15-input.txt")

    println(findBestCombination(test) == 62842880)
    println(findBestCombination(test, maxCalories = 500) == 57600000)

    println(findBestCombination(input))
    println(findBestCombination(input, maxCalories = 500))
}

fun findBestCombination(input: String, maxTeaspoons: Int = 100, maxCalories: Int = 0): Int? {
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

    fun score(recipe: List<Int>, maxCalories: Int): Int {
        val proportions = ingredients.map { it.toList() }
                .zip(recipe)
                .map { p -> p.first.map { it * p.second } }
        val dough = proportions.reduce { a, b -> a.zip(b).map { it.toList().sum() } }
        val calories = dough.last()
        val result = dough.dropLast(1).map { Math.max(it, 0) }.reduce { a, b -> a * b }

        return if (maxCalories != 0) (if (calories == maxCalories) result else 0) else result
    }

    return mixtures(ingredients.size, maxTeaspoons).map { score(it, maxCalories) }.max()
}

data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
) {
    fun toList() = listOf(capacity, durability, flavor, texture, calories)
}

private fun parseIngredients(input: String) = input.splitToLines()
        .map {
            val name = it.split(":", limit = 2).first()
            val (capacity, durability, flavor, texture, calories) = Regex("""(-?\d+)""")
                    .findAll(it)
                    .map { it.groupValues[1] }
                    .map(String::toInt)
                    .toList()

            Ingredient(
                    name = name,
                    capacity = capacity,
                    durability = durability,
                    flavor = flavor,
                    texture = texture,
                    calories = calories
            )
        }
