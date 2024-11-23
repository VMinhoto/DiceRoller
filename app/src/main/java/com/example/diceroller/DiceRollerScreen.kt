package com.example.diceroller

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

const val ROLLING_TIME_MS = 2000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerScreen(initialDice: Dice = Dice()) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.app_name)) }) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        var state: DiceRollerScreenState by rememberSaveable(saver = DiceRollerScreenState.Saver) { mutableStateOf(DiceRollerScreenState.Idle(initialDice.reRoll())) }

        val currentState = state
        LaunchedEffect(currentState) {
            if (currentState is DiceRollerScreenState.Rolling) {
                delay(timeMillis = ROLLING_TIME_MS)
                state = DiceRollerScreenState.Idle(initialDice.reRoll())
            }
        }


        when (currentState) {
            is DiceRollerScreenState.Idle -> DiceRollerIdleView(
                state = currentState,
                modifier = Modifier.padding(innerPadding),
                onDiceRoll = {
                    state = DiceRollerScreenState.Rolling
                }
            )
            is DiceRollerScreenState.Rolling -> DiceRollerRollingView()
        }
    }
}

