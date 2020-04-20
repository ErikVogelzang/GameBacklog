package com.example.gamebacklog.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.gamebacklog.database.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameRepository = GameRepository(application.applicationContext)
    private var deletedGames = arrayListOf<Game>()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val gameBacklog = gameRepository.getAllGames()

    fun insertGame(game: Game) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game!!)
            }
        }
    }

    fun deleteGame(game: Game) {
        deletedGames.clear()
        deletedGames.add(game)
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteGame(game!!)
            }
        }
    }

    fun deleteAllGames() {
        deletedGames.clear()
        for (game in gameBacklog.value!!.iterator()) {
            deletedGames.add(game)
        }
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
        }
    }

    fun getAllGames(): LiveData<List<Game>> {
        return gameBacklog
    }

    fun getLastDeletedGames() : List<Game> {
        return deletedGames
    }
}