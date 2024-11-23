package com.example.diceroller

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dice(
    val range: IntRange = DEFAULT_RANGE,
    val value: Int = range.first
) : Parcelable {
    init {
        require(value in range) {"Dice value must be in range $range"}
    }

    companion object : Parceler<Dice> {
        override fun create(parcel: Parcel): Dice {
            val range = parcel.readInt() .. parcel.readInt()
            val value = parcel.readInt()
            return Dice(range, value)
        }
        override fun Dice.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(range.first)
            parcel.writeInt(range.last)
            parcel.writeInt(value)
        }
    }
}

/**
 * Default range possibility of a dice
 */
val DEFAULT_RANGE = 1 .. 6

/**
 * Rolls the dice with the same range of the current dice
 */
fun Dice.reRoll() = Dice(range = range, value = range.random())
