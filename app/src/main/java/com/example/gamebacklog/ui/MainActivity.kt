package com.example.gamebacklog.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.model.GameViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val gameBacklog = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(gameBacklog)
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        rvGames.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvGames.adapter = gameAdapter
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getAllGames().observe(this, Observer { gameBacklog ->
            this.gameBacklog.clear()
            this.gameBacklog.addAll(gameBacklog)
            gameAdapter.notifyDataSetChanged()
        })
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                gameViewModel.deleteGame(gameBacklog[viewHolder.adapterPosition])
                showUndoSnackbar(getString(R.string.delete_game))
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvGames)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_game_backlog -> {
                gameViewModel.deleteAllGames()
                showUndoSnackbar(getString(R.string.delete_backlog))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUndoSnackbar(text: String) {
        Snackbar.make(rvGames, text, Snackbar.LENGTH_LONG)
            .setActionTextColor(ResourcesCompat.getColor(resources, R.color.colorUndoText, null))
            .setAction(R.string.undo_text, View.OnClickListener {
                for (game in gameViewModel.getLastDeletedGames()) {
                    gameViewModel.insertGame(game)
                }
            }).show()
    }
}
