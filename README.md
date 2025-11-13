# CampusKonnekt

CampusKonnekt is an Android application designed to enhance the campus experience by connecting students. It serves as a central hub for sharing information, finding services, organizing events, and forming study groups. Built with modern Android development practices, it features a clean, intuitive user interface powered by Jetpack Compose.

## 🚀 Features

*   **User Authentication**: Secure user registration and login using Firebase Authentication.
*   **Campus Feed**: A dynamic feed where students can post updates, questions, and announcements.
*   **Events**: Discover, create, and RSVP to campus events, from workshops to social gatherings.
*   **Services Marketplace**: A place for students to offer or find services such as tutoring, ride-sharing, or equipment rentals.
*   **Study Groups**: Easily create and join study groups for different courses to collaborate with peers.
*   **User Profiles**: View and manage your profile, including your posts, events, and study groups.

## 🛠️ Technologies Used

*   **UI**: 100% Kotlin with [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative and modern UI.
*   **Backend**: [Firebase](https://firebase.google.com/)
    *   **Authentication**: For managing user accounts.
    *   **Firestore**: As the primary database for storing posts, events, services, and user data.
*   **Architecture**: MVVM (Model-View-ViewModel) to ensure a scalable and maintainable codebase.
*   **Asynchronous Programming**: Kotlin [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html) for managing background threads and handling data streams.
*   **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) to handle in-app navigation.

## ⚙️ Setup and Installation

To get a local copy up and running, follow these simple steps.

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/Elisha416/CampusKonnekt.git
    ```
2.  **Open in Android Studio:**
    Open the cloned project directory in Android Studio.
3.  **Firebase Setup:**
    *   Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
    *   Add an Android app to your Firebase project with the package name `com.example.campuskonnekt`.
    *   Download the `google-services.json` file and place it in the `app/` directory of the project.
    *   In the Firebase Console, enable **Authentication** (Email/Password provider) and **Firestore**.
4.  **Build and Run:**
    Sync the Gradle files and run the application on an emulator or a physical device.

## 📸 Screenshots

*(You can add screenshots here to showcase the app's features and UI)*

| Feed Screen | Profile Screen | Events Screen |
| :---: | :---: | :---: |
| *(image/gif)* | *(image/gif)* | *(image/gif)* |

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/Elisha416/CampusKonnekt/issues).

## 📄 License

Distributed under the MIT License. You can create a `LICENSE` file with the details if you wish.

---
*This README was generated with assistance from Google's AI.*