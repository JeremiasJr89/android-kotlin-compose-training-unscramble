/*
 * Copyright (c)2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE
import com.example.android.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel containing the app data and methods to process the data
 */
class GameViewModel : ViewModel() {
    private lateinit var currentWord: String
    var userGuess by mutableStateOf((""))
        private set

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        var usedWords: MutableSet<String>
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.scor.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _uiState.update{currentWord ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
            updateUserGuess("")
        }
        private fun shuffleCurrentWord(word: String): String {
            val tempWord = word.toCharArray()
            tempWord.shuffle()
            while (String(tempWord)).equals(word) {
                tempWord.shuffle()
            }
            return String(tempWord)
        }

        init {
            resetGame()
        }
    private fun updateGameState( updateScore: Int){
        _uiState.update {
            currentState->
            currentState.copy(
                isGuessWordWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updateScore,
                currentWordCount = currentState.currentWordCount.inc(),
            )
        }
    }

        fun resetGame() {
            usedWords.clear()
            _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
        }

        fun updateUserGuess(guessedWord: String) {
            userGuess = guessedWord
        }

    }
