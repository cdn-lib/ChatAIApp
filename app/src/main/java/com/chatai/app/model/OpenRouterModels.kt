package com.chatai.app.model

// ===== REQUEST =====
data class OpenRouterRequest(
    val model: String,
    val messages: List<MessagePayload>,
    val max_tokens: Int = 2048,
    val temperature: Double = 0.7
)

data class MessagePayload(
    val role: String,
    val content: String
)

// ===== RESPONSE =====
data class OpenRouterResponse(
    val id: String?,
    val choices: List<Choice>?,
    val error: ApiError?
)

data class Choice(
    val message: MessagePayload?,
    val finish_reason: String?
)

data class ApiError(
    val message: String?,
    val code: Int?
)

// ===== MODELS LIST =====
data class AiModel(
    val id: String,
    val displayName: String,
    val isFree: Boolean = false
)

object AvailableModels {
    val models = listOf(
        AiModel("openai/gpt-4o-mini", "GPT-4o Mini 🟢", true),
        AiModel("openai/gpt-4o", "GPT-4o ⭐"),
        AiModel("anthropic/claude-3-haiku", "Claude 3 Haiku 🟢", true),
        AiModel("anthropic/claude-3-5-sonnet", "Claude 3.5 Sonnet ⭐"),
        AiModel("google/gemini-flash-1.5", "Gemini Flash 1.5 🟢", true),
        AiModel("google/gemini-pro-1.5", "Gemini Pro 1.5"),
        AiModel("meta-llama/llama-3-8b-instruct", "Llama 3 8B 🟢", true),
        AiModel("meta-llama/llama-3-70b-instruct", "Llama 3 70B"),
        AiModel("mistralai/mistral-7b-instruct", "Mistral 7B 🟢", true),
        AiModel("deepseek/deepseek-chat", "DeepSeek Chat 🟢", true)
    )
}
