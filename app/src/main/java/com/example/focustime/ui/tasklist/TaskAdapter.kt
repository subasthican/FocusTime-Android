package com.example.focustime.ui.tasklist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.focustime.R
import com.example.focustime.data.Task
import com.example.focustime.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskToggle: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var isAnimating = false

    fun notifyTaskChanged(position: Int) {
        notifyItemChanged(position)
    }

    fun notifyTaskInserted(position: Int) {
        notifyItemInserted(position)
    }

    fun notifyTaskRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvTaskDescription.text = task.description
                
                // Update checkbox state
                updateCheckboxState(task.isDone)
                
                // Update text appearance based on completion
                updateTextAppearance(task.isDone)
                
                // Set click listeners
                root.setOnClickListener {
                    if (!isAnimating) {
                        onTaskClick(task)
                    }
                }
                
                root.setOnLongClickListener {
                    if (!isAnimating) {
                        onTaskLongClick(task)
                        true
                    } else {
                        false
                    }
                }
                
                ivCheckbox.setOnClickListener {
                    if (!isAnimating) {
                        animateToggle(task)
                    }
                }
            }
        }

        private fun updateCheckboxState(isDone: Boolean) {
            binding.ivCheckbox.apply {
                if (isDone) {
                    background = binding.root.context.getDrawable(R.drawable.checkbox_checked)
                    visibility = View.VISIBLE
                } else {
                    background = binding.root.context.getDrawable(R.drawable.checkbox_unchecked)
                    visibility = View.VISIBLE
                }
            }
        }

                private fun updateTextAppearance(isDone: Boolean) {
                    binding.apply {
                        if (isDone) {
                            // Apple-style: 70% opacity + strikethrough for completed tasks
                            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            tvTaskTitle.alpha = 0.7f
                            tvTaskDescription.alpha = 0.7f
                        } else {
                            // Reset to normal appearance
                            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                            tvTaskTitle.alpha = 1.0f
                            tvTaskDescription.alpha = 1.0f
                        }
                    }
                }

        private fun animateToggle(task: Task) {
            isAnimating = true
            
            val newState = !task.isDone
            
            // Apple-style checkbox animation: scale down, then bounce back
            val scaleDown = ValueAnimator.ofFloat(1.0f, 0.85f)
            val scaleUp = ValueAnimator.ofFloat(0.85f, 1.1f, 1.0f)
            
            scaleDown.duration = 150
            scaleUp.duration = 200
            scaleUp.interpolator = OvershootInterpolator()
            
            scaleDown.addUpdateListener { animator ->
                val scale = animator.animatedValue as Float
                binding.ivCheckbox.scaleX = scale
                binding.ivCheckbox.scaleY = scale
            }
            
            scaleUp.addUpdateListener { animator ->
                val scale = animator.animatedValue as Float
                binding.ivCheckbox.scaleX = scale
                binding.ivCheckbox.scaleY = scale
            }
            
            scaleDown.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // Update state at the bottom of the scale
                    updateCheckboxState(newState)
                    updateTextAppearance(newState)
                    onTaskToggle(task.copy(isDone = newState))
                    scaleUp.start()
                }
            })
            
            scaleUp.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                }
            })
            
            scaleDown.start()
        }
    }
}
