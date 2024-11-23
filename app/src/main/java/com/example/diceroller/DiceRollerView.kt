package com.example.diceroller

import android.util.Log
import android.widget.Spinner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SwipeToDismissBoxState.Companion.Saver
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed interface DiceRollerScreenState {
    data class Idle(val dice: Dice): DiceRollerScreenState
    data object Rolling : DiceRollerScreenState

    companion object {
        val Saver =
            androidx.compose.runtime.saveable.Saver<MutableState<DiceRollerScreenState>, List<Int>>(
                save = { toSave ->
                    toSave.value.let { state ->
                        if (state is Idle) listOf(
                            state.dice.range.first,
                            state.dice.range.last,
                            state.dice.value
                        )
                        else emptyList()
                    }
                },
                restore = { saved ->
                    if (saved.isNotEmpty())
                        mutableStateOf(Idle(Dice(range = saved[0]..saved[1], value = saved[2])))
                    else
                        mutableStateOf(Rolling)
                }
            )
    }
}

@Composable
fun DiceRollerIdleView(
    state: DiceRollerScreenState.Idle,
    modifier: Modifier = Modifier,
    onDiceRoll: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Image(
            painter = painterResource(state.dice.toDiceResource()),
            contentDescription = state.dice.toString(),
            modifier = Modifier.size(200.dp)
        )

        Button(onClick = { onDiceRoll() })
        {
            Text(text = stringResource(R.string.roll_button_text))
        }
    }
}

@Composable
fun DiceRollerRollingView(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        CircularProgressIndicator(modifier = Modifier.size(200.dp))
        Button(onClick = {  }, enabled = false) {
            Text(text = stringResource(R.string.roll_button_text))
        }
    }
}

fun Dice.toDiceResource() = diceImages[value - 1]


val diceImages = listOf(
    R.drawable.dice1,
    R.drawable.dice2,
    R.drawable.dice3,
    R.drawable.dice4,
    R.drawable.dice5,
    R.drawable.dice6,
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DiceRollerIdleViewPreview() {
    DiceRollerIdleView(state = DiceRollerScreenState.Idle(Dice()), onDiceRoll = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DiceRollerRollingPreview(){
    DiceRollerRollingView()
}