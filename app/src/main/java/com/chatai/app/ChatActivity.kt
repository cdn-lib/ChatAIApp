package com.chatai.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatai.app.adapter.ChatAdapter
import com.chatai.app.databinding.ActivityChatBinding
import com.chatai.app.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var apiKey: String
    private lateinit var modelId: String
    private lateinit var modelName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get extras
        apiKey = intent.getStringExtra("api_key") ?: ""
        modelId = intent.getStringExtra("model_id") ?: "openai/gpt-4o-mini"
        modelName = intent.getStringExtra("model_name") ?: "GPT-4o Mini"

        setupToolbar()
        setupRecyclerView()
        setupInputField()
        observeViewModel()
        showWelcomeMessage()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = modelName
            subtitle = "OpenRouter AI"
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupInputField() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else false
        }

        binding.btnClearChat.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Percakapan")
                .setMessage("Apakah Anda yakin ingin menghapus semua pesan?")
                .setPositiveButton("Hapus") { _, _ -> viewModel.clearChat() }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun sendMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isBlank() || viewModel.isLoading.value == true) return

        binding.etMessage.text?.clear()
        viewModel.sendMessage(text, apiKey, modelId)
    }

    private fun observeViewModel() {
        viewModel.messages.observe(this) { messages ->
            chatAdapter.submitList(messages.toList()) {
                if (messages.isNotEmpty()) {
                    binding.rvChat.smoothScrollToPosition(messages.size - 1)
                }
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.btnSend.isEnabled = !loading
            binding.progressSend.visibility = if (loading) View.VISIBLE else View.GONE
            binding.ivSendIcon.visibility = if (loading) View.GONE else View.VISIBLE
        }
    }

    private fun showWelcomeMessage() {
        // Adding initial AI greeting
        if (viewModel.messages.value.isNullOrEmpty()) {
            viewModel.sendMessage(
                "Halo! Perkenalkan dirimu secara singkat dan tanyakan apa yang ingin saya bantu hari ini. Gunakan bahasa Indonesia yang ramah.",
                apiKey,
                modelId
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }
}
