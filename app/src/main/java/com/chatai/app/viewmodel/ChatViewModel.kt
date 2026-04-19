package com.chatai.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatai.app.api.RetrofitClient
import com.chatai.app.model.ChatMessage
import com.chatai.app.model.MessagePayload
import com.chatai.app.model.OpenRouterRequest
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val conversationHistory = mutableListOf<MessagePayload>()

    fun sendMessage(userText: String, apiKey: String, model: String) {
        if (userText.isBlank()) return
        if (apiKey.isBlank()) {
            _error.value = "API Key tidak boleh kosong!"
            return
        }

        // Add user message
        val userMsg = ChatMessage(role = "user", content = userText)
        conversationHistory.add(MessagePayload("user", userText))
        addMessage(userMsg)

        // Add loading placeholder
        val loadingMsg = ChatMessage(role = "assistant", content = "...", isLoading = true)
        addMessage(loadingMsg)

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(apiKey)
                val request = OpenRouterRequest(
                    model = model,
                    messages = conversationHistory.toList()
                )

                val response = api.sendMessage(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    val apiError = body?.error
                    val aiText = body?.choices?.firstOrNull()?.message?.content

                    if (apiError != null) {
                        removeLoadingMessage()
                        val errMsg = ChatMessage(
                            role = "assistant",
                            content = "❌ Error: ${apiError.message ?: "Terjadi kesalahan"}"
                        )
                        addMessage(errMsg)
                        _error.value = apiError.message
                    } else if (!aiText.isNullOrBlank()) {
                        conversationHistory.add(MessagePayload("assistant", aiText))
                        removeLoadingMessage()
                        val aiMsg = ChatMessage(role = "assistant", content = aiText)
                        addMessage(aiMsg)
                    } else {
                        removeLoadingMessage()
                        _error.value = "Respon kosong dari AI"
                    }
                } else {
                    removeLoadingMessage()
                    val code = response.code()
                    val errMessage = when (code) {
                        401 -> "❌ API Key tidak valid atau tidak dikenali"
                        402 -> "❌ Saldo OpenRouter habis"
                        429 -> "❌ Terlalu banyak request, coba lagi nanti"
                        500 -> "❌ Server error, coba lagi"
                        else -> "❌ Error HTTP $code"
                    }
                    val errMsg = ChatMessage(role = "assistant", content = errMessage)
                    addMessage(errMsg)
                    _error.value = errMessage
                }
            } catch (e: Exception) {
                removeLoadingMessage()
                val errText = "❌ Koneksi gagal: ${e.localizedMessage ?: "Cek koneksi internet Anda"}"
                addMessage(ChatMessage(role = "assistant", content = errText))
                _error.value = errText
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearChat() {
        conversationHistory.clear()
        _messages.value = emptyList()
        _error.value = null
    }

    private fun addMessage(message: ChatMessage) {
        val current = _messages.value?.toMutableList() ?: mutableListOf()
        current.add(message)
        _messages.value = current
    }

    private fun removeLoadingMessage() {
        val current = _messages.value?.toMutableList() ?: return
        val idx = current.indexOfLast { it.isLoading }
        if (idx >= 0) {
            current.removeAt(idx)
            _messages.value = current
        }
    }
}
