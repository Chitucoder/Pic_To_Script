# Pic to Script

## 📌 Overview
**Pic to Script** is an Android application that allows users to capture an image and convert the extracted text into multiple formats, such as Word, Excel, and Text. The app leverages Google's ML Kit Text Recognition Version 2 for text extraction and integrates Firebase for backend support.

## ✨ Features
- 📸 **Image to Text Conversion**: Capture an image and extract text using Google's ML Kit.
- 📝 **Multiple Output Formats**: Export text as Word (.docx), Excel (.xlsx), or plain text (.txt).
- ☁️ **Firebase Integration**: Securely store extracted text and retrieve it anytime.
- 🔄 **Improved Image Processing**: Saves and accesses images before conversion for better quality.
- 🛠 **User-Friendly UI**: Simple and intuitive interface for seamless text extraction.

## 🚀 Tech Stack
- **Android Development**: Java, Android Studio
- **ML Kit**: Google's Text Recognition API (Version 2)
- **Cloud Backend**: Firebase
- **File Handling**: Apache POI (for Word and Excel file creation)

## 🛠 Installation & Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Chitucoder/Pic_To_Script
   ```
2. Open the project in **Android Studio**.
3. Install dependencies via **Gradle Sync**.
4. Configure **Firebase**:
   - Add `google-services.json` to the `app/` directory.
   - Enable **ML Kit Text Recognition** in Firebase Console.
5. Run the project on an Android device/emulator.

## ⚙️ Challenges Faced & Solutions
- **Image Quality Issue**: Initially, direct bitmap conversion resulted in poor text recognition. Fixed by first storing the image and then processing it.
- **Android Studio Dependency Errors**: Faced frequent version conflicts while collaborating via GitHub. Solved by maintaining a consistent dependency versioning strategy.

## 🤝 Contributing
Feel free to fork the repo, create an issue, or submit a pull request!

## 📬 Contact
For queries or collaboration, reach out at chaitukulkarni27@gmail.com

