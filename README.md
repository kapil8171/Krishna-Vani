# 📖 Krishna Vani – A Bhagavad Gita Reading App

![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-7f52ff.svg)
![Material Design](https://img.shields.io/badge/UI-Material%20Design-blue)
![Architecture-MVVM](https://img.shields.io/badge/Architecture-MVVM-green)
![Status](https://img.shields.io/badge/Status-Active-brightgreen)

**Krishna Vani** is a modern, responsive spiritual reading app for **Bhagavad Gita** lovers and religious readers. It provides access to all chapters and verses, detailed explanations, commentaries from multiple authors, and offline saving options for seamless reading.

---

## ✨ Features

- 📚 Read all **18 chapters** and **700 verses** of the Bhagavad Gita
- 📖 Get **detailed explanations**, **summaries**, and **multi-author commentaries**
- ❤️ **Save verses or chapters** locally to read offline
- 🌙 **Light/Dark theme** support
- ⚡ Modern **Material Design UI** with **Shimmer Loading Effect**
- 🔍 Clean **single-activity architecture** using Fragments + Navigation Components
- 📶 Network manager to gracefully handle **connectivity issues**
- 💾 Uses **Room DB** to store offline data and **Shared Preferences** for user settings
- 🧭 Smooth navigation flow with **Jetpack Navigation**
- 💡 Optimized RecyclerView with **DiffUtil** for efficient rendering
- 📱 Fully **responsive layout**

---

## 🛠️ Tech Stack

| Layer            | Technology/Library                          |
|------------------|---------------------------------------------|
| Language         | Kotlin                                       |
| UI Design        | XML + Material Components                   |
| Architecture     | MVVM + Repository Pattern                   |
| Asynchronous     | Kotlin Coroutines + Flow                    |
| API Handling     | Retrofit + NetworkManager                   |
| Local Storage    | Room Database + SharedPreferences           |
| UI Components    | RecyclerView + DiffUtil + Shimmer Effect    |
| Navigation       | Jetpack Navigation Component (Single Activity) |
| State Management | LiveData + ViewModel                        |

---

## 📲 App Flow

```text
SplashFragment
    ↓
HomeFragment (Chapters List)
    ├──→ VersesFragment (on Chapter Click)
    │         └──→ VerseDetailFragment (on Verse Click)
    └──→ SettingsFragment (from corner settings icon)
              ├──→ SavedChaptersFragment
              └──→ SavedVersesFragment



