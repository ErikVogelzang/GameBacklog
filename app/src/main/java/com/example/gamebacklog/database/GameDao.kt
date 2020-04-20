package com.example.gamebacklog.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gamebacklog.model.Game

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM gameTable ORDER BY sortDate")
    fun getAllGames(): LiveData<List<Game>>

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    @Delete
    suspend fun deleteGame(game: Game)
}