package com.chatai.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.chatai.app.databinding.ActivityMainBinding
import com.chatai.app.model.AvailableModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupModelSpinner()
        loadSavedApiKey()
        setupClickListeners()
    }

    private fun setupModelSpinner() {
        val modelNames = AvailableModels.models.map { it.displayName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modelNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerModel.adapter = adapter
    }

    private fun loadSavedApiKey() {
        val prefs = getSharedPreferences("chatai_prefs", Context.MODE_PRIVATE)
        val savedKey = prefs.getString("api_key", "")
        if (!savedKey.isNullOrBlank()) {
            binding.etApiKey.setText(savedKey)
        }
    }

    private fun setupClickListeners() {
        binding.btnStartChat.setOnClickListener {
            val apiKey = binding.etApiKey.text.toString().trim()
            val selectedIndex = binding.spinnerModel.selectedItemPosition
            val selectedModel = AvailableModels.models[selectedIndex]

            if (apiKey.isBlank()) {
                binding.tilApiKey.error = "Masukkan API Key terlebih dahulu"
                return@setOnClickListener
            }

            binding.tilApiKey.error = null

            // Save API key
            getSharedPreferences("chatai_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("api_key", apiKey)
                .apply()

            // Navigate to chat
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("api_key", apiKey)
                putExtra("model_id", selectedModel.id)
                putExtra("model_name", selectedModel.displayName)
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.tvGetApiKey.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse("https://openrouter.ai/keys")
            }
            startActivity(intent)
        }
    }
}
