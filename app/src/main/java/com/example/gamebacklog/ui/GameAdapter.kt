package com.example.gamebacklog.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game
import kotlinx.android.synthetic.main.layout_game.view.*

class GameAdapter(val gameBacklog: List<Game>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.layout_game,
            parent,
            false
        )
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int = gameBacklog.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GameViewHolder).bind(gameBacklog[position])
    }

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            itemView.tvName.text = game.title
            itemView.tvPlatform.text = game.platform
            itemView.tvRelease.text = game.releaseDate
        }
    }
}