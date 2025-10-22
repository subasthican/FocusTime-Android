<div align="center">

# ⏱️ FocusTime

### *Where Productivity Meets Elegance*

<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform">
<img src="https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
<img src="https://img.shields.io/badge/Material_Design-3-0081CB?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design">
<img src="https://img.shields.io/badge/Min_SDK-26-00C853?style=for-the-badge" alt="Min SDK">

---

**A minimalist productivity companion that transforms how you manage tasks and track focus time.**  
*Inspired by Apple's design philosophy, built for Android excellence.*

[Features](#-features) • [Screenshots](#-screenshots) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [Architecture](#-architecture) • [Contributing](#-contributing)

---

</div>

## 🌟 The Story

In a world full of distractions, **FocusTime** is your sanctuary. We believe productivity shouldn't be complicated—it should be *beautiful*, *intuitive*, and *effortless*. That's why we crafted an app that not only helps you focus but makes you *want* to focus.

> *"Design is not just what it looks like and feels like. Design is how it works."* — Steve Jobs

FocusTime embodies this philosophy with an Apple-inspired interface that feels native to your workflow while leveraging the power of Material Design 3.

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 📋 **Intelligent Task Management**
- Create, edit, and organize tasks with ease
- Mark tasks as complete with satisfying animations
- Clean, card-based layout inspired by Apple Pages
- Long-press to delete with confirmation
- Real-time task filtering (show incomplete tasks only)

</td>
<td width="50%">

### ⏱️ **Precision Focus Timer**
- Start, pause, and stop with single-function buttons
- Select any task to track focus time
- Beautiful circular timer buttons with animations
- Real-time elapsed time display (MM:SS format)
- Smart resume functionality that preserves elapsed time

</td>
</tr>
<tr>
<td width="50%">

### 🏁 **Lap Time Tracking**
- Record multiple laps during focus sessions
- Each lap shows the task it was recorded for
- Persistent lap history across app sessions
- Long-press stop button to clear all laps
- Smooth animations for new lap entries

</td>
<td width="50%">

### 🌙 **Adaptive Dark Mode**
- Full dark mode support system-wide
- Carefully crafted color palettes for both themes
- Smooth theme transitions
- Respects system dark mode preference
- Apple-style dark mode aesthetics

</td>
</tr>
<tr>
<td width="50%">

### 💾 **Persistent Data Storage**
- All tasks automatically saved
- Lap times preserved across sessions
- SharedPreferences with Gson serialization
- No data loss on app restart
- Fast and reliable storage

</td>
<td width="50%">

### 🎨 **Polished UI/UX**
- Apple Pages-inspired navigation bar
- Material Design 3 components
- Smooth animations and transitions
- Haptic feedback on interactions
- Consistent design language throughout

</td>
</tr>
</table>

---

## 📸 Screenshots

<div align="center">

### Light Mode
*Coming Soon - Add your screenshots here*

### Dark Mode
*Coming Soon - Add your screenshots here*

</div>

---

## 🛠️ Tech Stack

<div align="center">

| Category | Technology |
|----------|------------|
| **Language** | ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white) 100% Pure Kotlin |
| **UI Framework** | ![Material Design](https://img.shields.io/badge/Material_Design_3-0081CB?style=flat-square&logo=material-design&logoColor=white) Latest MD3 Components |
| **Architecture** | ![MVVM](https://img.shields.io/badge/MVVM-Architecture-orange?style=flat-square) Fragment-based Navigation |
| **Data Layer** | ![SharedPreferences](https://img.shields.io/badge/SharedPreferences-Gson-green?style=flat-square) Type-safe serialization |
| **Async** | ![Coroutines](https://img.shields.io/badge/Coroutines-Handler-purple?style=flat-square) Timer management |
| **Build System** | ![Gradle](https://img.shields.io/badge/Gradle-KTS-02303A?style=flat-square&logo=gradle&logoColor=white) Kotlin DSL |
| **Min SDK** | ![Android 8.0](https://img.shields.io/badge/Android-8.0%20(API%2026)-3DDC84?style=flat-square&logo=android&logoColor=white) |

</div>

### 📦 Key Dependencies

```kotlin
// UI Components
implementation "androidx.fragment:fragment-ktx:1.8.5"
implementation "com.google.android.material:material:1.12.0"
implementation "androidx.constraintlayout:constraintlayout:2.2.0"

// Data Persistence
implementation "com.google.code.gson:gson:2.10.1"

// View Binding
implementation "androidx.databinding:databinding-runtime:8.8.0"
```

---

## 🚀 Installation

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17 or higher
- Android SDK with API 26+
- Gradle 8.13

### Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/FocusTime-Android.git

# Navigate to project directory
cd FocusTime-Android

# Open in Android Studio
# File > Open > Select FocusTime-Android folder

# Build and Run
# Click the green 'Run' button or press Shift+F10
```

### Manual Build

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Build release APK
./gradlew assembleRelease
```

---

## 🏗️ Architecture

FocusTime follows modern Android development best practices with a clean, maintainable architecture:

```
app/
├── src/main/
│   ├── java/com/example/focustime/
│   │   ├── ui/
│   │   │   ├── main/
│   │   │   │   └── MainActivity.kt          # Main activity with navigation
│   │   │   ├── tasklist/
│   │   │   │   ├── TaskListFragment.kt      # Task management UI
│   │   │   │   └── TaskAdapter.kt           # RecyclerView adapter
│   │   │   ├── timer/
│   │   │   │   ├── TimerFragment.kt         # Timer UI and logic
│   │   │   │   ├── LapTimeAdapter.kt        # Lap times display
│   │   │   │   └── TaskDropdownAdapter.kt   # Task selection dropdown
│   │   │   └── settings/
│   │   │       └── SettingsFragment.kt      # App settings
│   │   └── data/
│   │       ├── Task.kt                       # Task data model
│   │       ├── TaskTimer.kt                  # Timer data model
│   │       └── LapTimeEntry.kt               # Lap time data model
│   └── res/
│       ├── layout/                           # XML layouts
│       ├── drawable/                         # Vector drawables
│       ├── values/                           # Light theme colors & styles
│       ├── values-night/                     # Dark theme colors & styles
│       └── menu/                             # Navigation menu
```

### Design Patterns

- **🎯 MVVM Architecture**: Separation of concerns with ViewBinding
- **♻️ RecyclerView Pattern**: Efficient list rendering with custom adapters
- **💾 Repository Pattern**: Data persistence with SharedPreferences
- **🎨 Material Theming**: Dynamic color system with theme overlays
- **🔄 State Management**: Fragment lifecycle-aware state handling

---

## 🎨 Design Philosophy

### Color Palette

#### Light Mode
```kotlin
Primary (Blue):     #007AFF  // Apple-style blue for actions
Background:         #FFFFFF  // Pure white for clarity
Surface:            #F7F7F7  // Subtle gray for cards
Text Primary:       #1C1C1C  // Near-black for readability
Text Secondary:     #7D7D7D  // Gray for supporting text
```

#### Dark Mode
```kotlin
Primary (Blue):     #0A84FF  // Adjusted blue for dark backgrounds
Background:         #1C1C1E  // True dark for OLED
Surface:            #2C2C2E  // Elevated dark surface
Text Primary:       #F2F2F7  // Off-white for comfort
Text Secondary:     #AEAEB2  // Muted gray for hierarchy
```

### Typography
- **Headers**: SF Pro Display / Sans-serif Medium (32sp, Bold)
- **Body**: SF Pro Text / Sans-serif (16sp, Regular)
- **Captions**: SF Pro Text / Sans-serif (14sp, Regular)
- **Navigation**: SF Pro Text / Sans-serif Medium (14sp)

---

## 🔥 Unique Features

### What Makes FocusTime Different?

1. **🎯 Apple-Inspired on Android**
   - We brought the elegance of iOS design to Android without compromising native Android UX patterns.

2. **🏁 Task-Linked Lap Times**
   - Unlike other timer apps, FocusTime associates each lap with the task you're working on—no more guessing!

3. **🎨 True Dark Mode**
   - Not just inverted colors—every shade was carefully chosen for optimal readability and battery efficiency on OLED screens.

4. **⚡ Zero Lag Animations**
   - Buttery smooth 60fps animations that make every interaction feel responsive and premium.

5. **💾 Smart Data Persistence**
   - Your data is always safe. Tasks and lap times persist automatically—no manual save buttons needed.

6. **🔒 Privacy First**
   - All data stored locally on your device. No accounts, no cloud sync, no tracking—just pure productivity.

---

## 📱 User Guide

### Getting Started

#### 1️⃣ **Create Your First Task**
- Tap the blue `+` button on the Tasks screen
- Enter a task title (required) and optional description
- Tap "Save" to add it to your list

#### 2️⃣ **Start a Focus Session**
- Navigate to the Timer screen
- Tap the task selector dropdown
- Choose a task from your list
- Press the **▶ Play** button to start

#### 3️⃣ **Record Lap Times**
- While the timer is running, tap the **⏱ Lap** button
- Each lap is automatically tagged with your current task
- Long-press the **⏹ Stop** button to clear all laps

#### 4️⃣ **Manage Your Tasks**
- Tap the checkbox to mark tasks as complete
- Tap anywhere on a task card to edit it
- Long-press a task to delete it

#### 5️⃣ **Toggle Dark Mode**
- Go to Settings
- Tap the "Dark Mode" toggle
- The theme changes instantly across all screens

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

---

## 📊 Project Stats

<div align="center">

![Lines of Code](https://img.shields.io/badge/Lines_of_Code-2000%2B-blue?style=for-the-badge)
![Files](https://img.shields.io/badge/Files-50%2B-green?style=for-the-badge)
![Layouts](https://img.shields.io/badge/Layouts-15%2B-orange?style=for-the-badge)
![Drawables](https://img.shields.io/badge/Drawables-30%2B-purple?style=for-the-badge)

</div>

---

## 🤝 Contributing

We welcome contributions! Here's how you can help make FocusTime even better:

### Ways to Contribute

- 🐛 **Report Bugs**: Open an issue with detailed reproduction steps
- 💡 **Suggest Features**: Share your ideas for new functionality
- 📝 **Improve Documentation**: Help us make the docs clearer
- 🎨 **Design Enhancements**: Propose UI/UX improvements
- 🔧 **Code Contributions**: Submit pull requests with fixes or features

### Contribution Guidelines

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Ensure all existing tests pass before submitting PR

---

## 🐛 Known Issues

- [ ] Timer continues in background when app is minimized (planned fix in v2.0)
- [ ] No task search functionality yet (coming soon)
- [ ] No task categories or tags (planned for future release)

---

## 🗺️ Roadmap

### Version 2.0 (Planned)
- [ ] 🔔 **Notifications**: Focus session reminders and alerts
- [ ] 📊 **Statistics**: Daily, weekly, and monthly productivity charts
- [ ] 🏷️ **Task Categories**: Organize tasks with custom tags
- [ ] 🔍 **Search**: Quickly find tasks by title or description
- [ ] 🎵 **Focus Sounds**: Optional background white noise
- [ ] ☁️ **Cloud Sync**: Optional Google Drive backup

### Version 2.5 (Future)
- [ ] 🤝 **Team Features**: Share tasks with collaborators
- [ ] 📈 **Advanced Analytics**: Detailed productivity insights
- [ ] 🎯 **Goals**: Set and track weekly focus goals
- [ ] 🏆 **Achievements**: Gamification elements
- [ ] 🌐 **Export**: CSV/JSON export of all data

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 FocusTime

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 👨‍💻 Author

**Subasthican**  
*Android Developer | Kotlin Enthusiast | UI/UX Passionate*

- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- **Apple Inc.** - For design inspiration from iOS and Apple Pages
- **Material Design Team** - For the excellent MD3 component library
- **Android Community** - For continuous support and resources
- **Kotlin Team** - For the amazing Kotlin language
- **All Contributors** - For making this project better

---

## 💬 Community

<div align="center">

[![GitHub Issues](https://img.shields.io/github/issues/yourusername/FocusTime-Android?style=for-the-badge)](https://github.com/yourusername/FocusTime-Android/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/yourusername/FocusTime-Android?style=for-the-badge)](https://github.com/yourusername/FocusTime-Android/pulls)
[![GitHub Stars](https://img.shields.io/github/stars/yourusername/FocusTime-Android?style=for-the-badge)](https://github.com/yourusername/FocusTime-Android/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/yourusername/FocusTime-Android?style=for-the-badge)](https://github.com/yourusername/FocusTime-Android/network/members)

**If you found this project helpful, please consider giving it a ⭐ on GitHub!**

</div>

---

## 📞 Support

Need help? Have questions?

- 📧 **Email**: support@focustime.app
- 💬 **Discussions**: [GitHub Discussions](https://github.com/yourusername/FocusTime-Android/discussions)
- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/yourusername/FocusTime-Android/issues)
- 📖 **Documentation**: [Wiki](https://github.com/yourusername/FocusTime-Android/wiki)

---

<div align="center">

### 🎯 *Focus. Track. Achieve.*

**Built with ❤️ using Kotlin and Material Design 3**

---

<sub>© 2025 FocusTime. Made with passion for productivity.</sub>

</div>

