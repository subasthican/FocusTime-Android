package com.example.focustime.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.focustime.R
import com.example.focustime.data.PreferencesManager
import com.example.focustime.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferencesManager = PreferencesManager(requireContext())
        setupClickListeners()
        loadSettings()
    }

    private fun setupClickListeners() {
        // Dark Mode Switch
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.isDarkMode = isChecked
            animateSwitchToggle()
            showToast("Dark mode ${if (isChecked) "enabled" else "disabled"}")
            
            // Apply theme after a short delay to allow animation to complete
            binding.root.postDelayed({
                applyTheme(isChecked)
            }, 200)
        }

        // Clear All Tasks
        binding.btnClearTasks.setOnClickListener {
            showClearTasksDialog()
        }

        // Reset Timer History
        binding.btnResetTimer.setOnClickListener {
            showResetTimerDialog()
        }

        // About
        binding.btnAbout.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun loadSettings() {
        binding.switchDarkMode.isChecked = preferencesManager.isDarkMode
        // Apply the saved theme on load
        applyTheme(preferencesManager.isDarkMode)
    }
    
    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun animateSwitchToggle() {
        if (_binding == null) return // Check if binding is null
        
        binding.switchDarkMode.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(100)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                if (_binding != null) { // Check if binding is still valid
                    binding.switchDarkMode.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }
            }
            .start()
    }

    private fun showClearTasksDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Tasks")
            .setMessage("Are you sure you want to delete all tasks? This action cannot be undone.")
            .setPositiveButton("Clear All") { _, _ ->
                clearAllTasks()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showResetTimerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset Timer History")
            .setMessage("Are you sure you want to clear all timer data? This action cannot be undone.")
            .setPositiveButton("Reset") { _, _ ->
                resetTimerHistory()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About FocusTime")
            .setMessage("FocusTime v1.0.0\n\nA productivity app to help you stay focused and track your time effectively.\n\nBuilt with ❤️ for productivity enthusiasts.")
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun clearAllTasks() {
        // This would typically clear tasks from a database or data source
        // For now, we'll just show a success message
        showToast("All tasks cleared successfully")
        animateButtonPress(binding.btnClearTasks)
    }

    private fun resetTimerHistory() {
        // This would typically reset timer data from a database or data source
        // For now, we'll just show a success message
        showToast("Timer history reset successfully")
        animateButtonPress(binding.btnResetTimer)
    }

    private fun animateButtonPress(view: View) {
        if (_binding == null) return // Check if binding is null
        
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                if (_binding != null) { // Check if binding is still valid
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
            }
            .start()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

