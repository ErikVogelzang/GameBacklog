package com.example.gamebacklog.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gamebacklog.model.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao
    private val gameBacklog: LiveData<List<Game>>

    init {
        val database = GameBacklogRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
        gameBacklog = gameDao.getAllGames()
    }

    fun getAllGames(): LiveData<List<Game>> = gameBacklog

    suspend fun insertGame(game: Game) = gameDao.insertGame(game)

    suspend fun deleteAllGames() = gameDao.deleteAllGames()

    suspend fun deleteGame(game: Game) = gameDao.deleteGame(game)
}