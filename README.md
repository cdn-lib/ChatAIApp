# ChatAI Android - OpenRouter API Chat App
# Kotlin + MVVM + Material Design 3

## Cara Install & Build

### Prasyarat
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 8 atau lebih baru (biasanya sudah include di Android Studio)
- Android SDK API 26+
- Koneksi internet (untuk download dependencies)

---

## Langkah Build APK

### 1. Buka di Android Studio
```
File → Open → Pilih folder ChatAIApp
```

### 2. Sync Gradle
Android Studio otomatis sync. Tunggu hingga selesai (butuh internet pertama kali).

### 3. Build Debug APK
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```
APK akan ada di: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Build Release APK (opsional)
```
Build → Generate Signed Bundle / APK
```

---

## Mendapatkan OpenRouter API Key

1. Buka https://openrouter.ai/keys
2. Daftar/Login dengan Google
3. Klik "Create Key"
4. Copy API key (format: `sk-or-v1-...`)
5. Masukkan di app saat pertama buka

---

## Model AI Gratis (Tanpa Biaya)

| Model | ID |
|-------|-----|
| GPT-4o Mini 🟢 | openai/gpt-4o-mini |
| Claude 3 Haiku 🟢 | anthropic/claude-3-haiku |
| Gemini Flash 1.5 🟢 | google/gemini-flash-1.5 |
| Llama 3 8B 🟢 | meta-llama/llama-3-8b-instruct |
| Mistral 7B 🟢 | mistralai/mistral-7b-instruct |
| DeepSeek Chat 🟢 | deepseek/deepseek-chat |

---

## Fitur Aplikasi

- ✅ Chat UI dengan bubble gradient
- ✅ Multi-model AI (10 model tersedia)
- ✅ API Key disimpan otomatis (SharedPreferences)
- ✅ Markdown rendering (bold, kode, list, dll)
- ✅ Context memory (chat history dikirim ke API)
- ✅ Copy pesan dengan long press
- ✅ Hapus percakapan
- ✅ Error handling lengkap
- ✅ Dark theme premium

---

## Struktur File

```
ChatAIApp/
├── app/src/main/
│   ├── java/com/chatai/app/
│   │   ├── MainActivity.kt         ← Setup screen (API key, model)
│   │   ├── ChatActivity.kt         ← Chat screen utama
│   │   ├── api/
│   │   │   ├── OpenRouterApi.kt    ← Retrofit interface
│   │   │   └── RetrofitClient.kt   ← HTTP client factory
│   │   ├── model/
│   │   │   ├── ChatMessage.kt      ← Data class pesan
│   │   │   └── OpenRouterModels.kt ← Request/response + model list
│   │   ├── adapter/
│   │   │   └── ChatAdapter.kt      ← RecyclerView adapter
│   │   └── viewmodel/
│   │       └── ChatViewModel.kt    ← Business logic + API calls
│   └── res/
│       ├── layout/                 ← XML layouts
│       ├── values/                 ← Colors, strings, themes
│       └── drawable/               ← Shape drawables (bubbles)
```
