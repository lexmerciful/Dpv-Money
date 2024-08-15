package com.lexmerciful.dpvmoney.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.databinding.ActivityBaseBinding

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showSnackBar(binding: ViewBinding, message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    fun showLoading(message: String?) {
        // hideBaseToolbar()
        binding.progressInclude.progress.visibility = View.VISIBLE
        if (message !== null) {
            binding.progressInclude.progressTextView.text = message
        } else {
            binding.progressInclude.progressTextView.visibility = View.GONE
        }
    }

    fun hideLoading() {
        binding.progressInclude.progress.visibility = View.GONE
    }
}