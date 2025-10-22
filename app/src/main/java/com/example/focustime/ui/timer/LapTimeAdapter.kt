package com.example.focustime.ui.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.focustime.R
import com.example.focustime.data.LapTimeEntry
import com.example.focustime.databinding.ItemLapTimeBinding

class LapTimeAdapter : RecyclerView.Adapter<LapTimeAdapter.LapTimeViewHolder>() {

    private var lapTimes = mutableListOf<LapTimeEntry>()

    fun addLapTime(lapNumber: Int, timeInMillis: Long, taskName: String) {
        val lapEntry = LapTimeEntry(
            id = System.currentTimeMillis().toString(),
            lapNumber = lapNumber,
            timeInMillis = timeInMillis,
            taskName = taskName
        )
        lapTimes.add(0, lapEntry) // Add to top
        notifyItemInserted(0)
    }
    
    fun addLapTimeEntry(lapEntry: LapTimeEntry) {
        lapTimes.add(0, lapEntry) // Add to top
        notifyItemInserted(0)
    }
    
    fun setLapTimes(lapTimesList: List<LapTimeEntry>) {
        lapTimes.clear()
        lapTimes.addAll(lapTimesList.reversed()) // Reverse to show newest first
        notifyDataSetChanged()
    }

    fun clearLapTimes() {
        lapTimes.clear()
        notifyDataSetChanged()
    }
    
    fun getLapTimesList(): List<LapTimeEntry> {
        return lapTimes.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val binding = ItemLapTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LapTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapTimeViewHolder, position: Int) {
        holder.bind(lapTimes[position])
        
        // Add fade-in animation for new items
        if (position == 0) {
            holder.itemView.alpha = 0f
            holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    override fun getItemCount(): Int = lapTimes.size

    inner class LapTimeViewHolder(private val binding: ItemLapTimeBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lapEntry: LapTimeEntry) {
            binding.apply {
                tvLapNumber.text = "Lap ${lapEntry.lapNumber}"
                tvLapTime.text = lapEntry.getFormattedTime()
                tvLapTask.text = lapEntry.taskName
            }
        }
    }
}

