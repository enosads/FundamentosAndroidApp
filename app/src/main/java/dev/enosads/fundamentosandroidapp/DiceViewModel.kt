package dev.enosads.fundamentosandroidapp

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class DiceUiState(
    @DrawableRes val rolledDice1ImageRes: Int? = null,
    @DrawableRes val rolledDice2ImageRes: Int? = null,
    @DrawableRes val rolledDice3ImageRes: Int? = null,
    val numberOfRolls: Int = 0,
    val rolledDicesList: List<RolledDices> = emptyList()
)

class DiceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    private val _uiStateLiveData = MutableLiveData(DiceUiState())
    val uiStateLiveData: LiveData<DiceUiState> = _uiStateLiveData


    fun rollDice() {
        val valueDice1 = Random.nextInt(1, 7)
        val valueDice2 = Random.nextInt(1, 7)
        val valueDice3 = Random.nextInt(1, 7)

        val rolledDices = RolledDices(valueDice1, valueDice2, valueDice3)

        _uiState.update { currentState ->
            val currentRolledDicesList = currentState.rolledDicesList.toMutableList()
            currentRolledDicesList.add(rolledDices)
            val updatedRolledDicesList = currentRolledDicesList.toList()

            currentState.copy(
                rolledDice1ImageRes = getDiceImageResource(valueDice1),
                rolledDice2ImageRes = getDiceImageResource(valueDice2),
                rolledDice3ImageRes = getDiceImageResource(valueDice3),
                rolledDicesList = updatedRolledDicesList,
                numberOfRolls = currentState.numberOfRolls + 1
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            val currentRolledDicesList =
                _uiStateLiveData.value?.rolledDicesList?.toMutableList().orEmpty().toMutableList()
            currentRolledDicesList.add(rolledDices)
            val updatedRolledDicesList = currentRolledDicesList.toList()

            _uiStateLiveData.postValue(
                DiceUiState(
                    rolledDice1ImageRes = getDiceImageResource(valueDice1),
                    rolledDice2ImageRes = getDiceImageResource(valueDice2),
                    rolledDice3ImageRes = getDiceImageResource(valueDice3),
                    rolledDicesList = updatedRolledDicesList,
                    numberOfRolls = (_uiStateLiveData.value?.numberOfRolls ?: 0) + 1
                )
            )
        }
    }
}