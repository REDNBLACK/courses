package day22

import java.util.*


/**
--- Day 22: Wizard Simulator 20XX ---

Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.

In this version, combat still proceeds with the player and the boss taking alternating turns. The player still goes first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.

Since you're a wizard, you don't get to wear armor, and you can't attack normally. However, since you do magic damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well. As before, if armor (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.

On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose. Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.

Magic Missile costs 53 mana. It instantly does 4 damage.
Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns. Effects are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect they have, their timer is decreased by one. If this decreases the timer to zero, the effect ends. You cannot cast a spell that would start an effect which is already active. However, effects can be started on the same turn they end.

For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:

-- Player turn --
- Player has 10 hit points, 0 armor, 250 mana
- Boss has 13 hit points
Player casts Poison.

-- Boss turn --
- Player has 10 hit points, 0 armor, 77 mana
- Boss has 13 hit points
Poison deals 3 damage; its timer is now 5.
Boss attacks for 8 damage.

-- Player turn --
- Player has 2 hit points, 0 armor, 77 mana
- Boss has 10 hit points
Poison deals 3 damage; its timer is now 4.
Player casts Magic Missile, dealing 4 damage.

-- Boss turn --
- Player has 2 hit points, 0 armor, 24 mana
- Boss has 3 hit points
Poison deals 3 damage. This kills the boss, and the player wins.
Now, suppose the same initial conditions, except that the boss has 14 hit points instead:

-- Player turn --
- Player has 10 hit points, 0 armor, 250 mana
- Boss has 14 hit points
Player casts Recharge.

-- Boss turn --
- Player has 10 hit points, 0 armor, 21 mana
- Boss has 14 hit points
Recharge provides 101 mana; its timer is now 4.
Boss attacks for 8 damage!

-- Player turn --
- Player has 2 hit points, 0 armor, 122 mana
- Boss has 14 hit points
Recharge provides 101 mana; its timer is now 3.
Player casts Shield, increasing armor by 7.

-- Boss turn --
- Player has 2 hit points, 7 armor, 110 mana
- Boss has 14 hit points
Shield's timer is now 5.
Recharge provides 101 mana; its timer is now 2.
Boss attacks for 8 - 7 = 1 damage!

-- Player turn --
- Player has 1 hit point, 7 armor, 211 mana
- Boss has 14 hit points
Shield's timer is now 4.
Recharge provides 101 mana; its timer is now 1.
Player casts Drain, dealing 2 damage, and healing 2 hit points.

-- Boss turn --
- Player has 3 hit points, 7 armor, 239 mana
- Boss has 12 hit points
Shield's timer is now 3.
Recharge provides 101 mana; its timer is now 0.
Recharge wears off.
Boss attacks for 8 - 7 = 1 damage!

-- Player turn --
- Player has 2 hit points, 7 armor, 340 mana
- Boss has 12 hit points
Shield's timer is now 2.
Player casts Poison.

-- Boss turn --
- Player has 2 hit points, 7 armor, 167 mana
- Boss has 12 hit points
Shield's timer is now 1.
Poison deals 3 damage; its timer is now 5.
Boss attacks for 8 - 7 = 1 damage!

-- Player turn --
- Player has 1 hit point, 7 armor, 167 mana
- Boss has 9 hit points
Shield's timer is now 0.
Shield wears off, decreasing armor by 7.
Poison deals 3 damage; its timer is now 4.
Player casts Magic Missile, dealing 4 damage.

-- Boss turn --
- Player has 1 hit point, 0 armor, 114 mana
- Boss has 2 hit points
Poison deals 3 damage. This kills the boss, and the player wins.
You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input. What is the least amount of mana you can spend and still win the fight? (Do not include mana recharge effects as "spending" negative mana.)

 */
fun main(args: Array<String>) {
    play(Int.MAX_VALUE, State(playerMana = 500, player = Player(50, 0), boss = Player(51, 9))).let(::println) //900
}

data class State(
        val playerMana: Int,
        val player: Player,
        val boss: Player,
        val penalty: Int = 0,
        val spent: Int = 0,
        val shield: Int = 0,
        val poison: Int = 0,
        val recharge: Int = 0
) {
    fun next(best: Int) = when {
        spent >= best -> emptyList()
        else -> {
            val states = listOf(
                    spend(53, copy(boss = boss.damage(4))),
                    spend(73, copy(boss = boss.damage(2), player = player.heal(2))),
                    spend(113, copy(shield = 6), shield == 0),
                    spend(173, copy(poison = 6), poison == 0),
                    spend(229, copy(recharge = 5), recharge == 0)
            )

            states.filterNotNull().map { it.applyRules().copy(player = player.damage(penalty)) }
        }
    }

    fun spend(mana: Int, state: State, cond: Boolean = true) =
        if (mana <= playerMana && cond) state.copy(playerMana = playerMana - mana, spent = spent + mana) else null

    fun playBoss() = copy(player = player.damage(Math.max(1, boss.dmg - (if (shield > 0) 7 else 0)))).applyRules()
    fun bossLost() = boss.hp <= 0
    fun alive() = player.hp > 0

    fun applyShield() = if (shield > 0) copy(shield = shield - 1) else this
    fun applyPoison() = if (poison > 0) copy(boss = boss.damage(3), poison = poison - 1) else this
    fun applyRecharge() = if (recharge > 0) copy(playerMana = playerMana + 101, recharge = recharge - 1) else this

    fun applyRules() = applyShield().applyPoison().applyRecharge()
}

data class Player(val hp: Int, val dmg: Int) {
    fun damage(value: Int) = copy(hp = hp - value)
    fun heal(value: Int) = copy(hp = hp + value)
}

fun play(best: Int, state: State): Int {
    val (won, afterMe) = state.next(best).partition(State::bossLost)
    val (won2, afterBoss) = afterMe.map(State::playBoss).partition(State::bossLost)
    val newBest = (won + won2).map(State::spent).fold(best, { l, r -> Math.min(l, r) })

    return afterBoss.filter(State::alive).fold(newBest, ::play)
}
