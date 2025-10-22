package com.example.focustime.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.focustime.R
import com.example.focustime.databinding.ActivityLogoBinding
import com.example.focustime.ui.main.MainActivity

class LogoActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLogoBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide action bar for full screen experience
        supportActionBar?.hide()
        
        // Start animations
        startLogoAnimations()
        
        // Navigate to MainActivity after 2 seconds (reduced for testing)
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, 2000)
    }
    
    private fun startLogoAnimations() {
        // Logo animation: Fade in and move up
        binding.ivLogo.animate()
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                // Move logo up slightly after fade in
                binding.ivLogo.animate()
                    .translationY(-20f)
                    .setDuration(500)
                    .setInterpolator(OvershootInterpolator())
                    .start()
            }
            .start()
        
        // App name animation: Fade in with scale effect (delay 0.8s)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvAppName.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setInterpolator(OvershootInterpolator())
                .start()
        }, 800)
        
        // Set initial scale for app name
        binding.tvAppName.scaleX = 0.9f
        binding.tvAppName.scaleY = 0.9f
        
        // Tagline animation: Simple fade in (delay 1.5s)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvTagline.animate()
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }, 1500)
    }
    
    private fun navigateToMainActivity() {
        try {
            android.util.Log.d("LogoActivity", "Navigating to MainActivity")
            
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            
            // Add smooth transition
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            
            android.util.Log.d("LogoActivity", "Navigation completed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent back button during logo page
        // Do nothing
    }
}
