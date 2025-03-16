# NavVis Android Code Challenge

## Overview
This is an Android application built using **Kotlin**, **Jetpack Compose**, and **Ktor** for networking. The app retrieves a JSON response containing numbers, processes them into structured items with checkboxes, and displays them in a categorized list view.

## Features
- Fetches numbers from an API (`https://navvis.com/numbers.json`).
- Handles **network failures and malformed responses** gracefully.
- Parses each number to determine:
  - **Section Index** (0-3 → SECTION1-SECTION4)
  - **Item Value** (e.g., Item1, Item2, etc.)
  - **Checkmark Status** (Checked/Unchecked)
- Displays parsed numbers in a **grouped, sorted list** using Jetpack Compose.
- **Network Mode Selector**: Simulates different network conditions (Stable, Flaky, No Connection, Malformed Response).

## Installation & Setup
### Prerequisites
- **Android Studio Flamingo or newer**
- **JDK 11+**
- **Gradle 8+**

### Steps
1. **Open the project in Android Studio**
2. **Sync Gradle & Run the App**
   - Select an emulator or a physical device.
   - Click ▶️ to run the application.

## Usage
### Selecting a Network Mode
- The app provides a **dropdown menu** to select network conditions:
  - **Stable**: Fetches numbers normally.
  - **Flaky**: Simulates timeouts & incomplete responses.
  - **No Connection**: Simulates no network availability.
  - **Malformed Response**: Simulates a broken JSON response.

### Interacting with the List
- **Items are categorized under SECTION1-SECTION4** based on the parsed data.
- **Checkboxes** indicate whether an item is checked or not.
- Click **"Get New Numbers"** to refresh data.

## Testing
### Running Unit Tests
- Open **`ParserTest.kt`** or **`NetworkSimulatorTest.kt`** and run the tests using:
   ```bash
   ./gradlew test
   ```
- The project contains tests for:
  - JSON Parsing (`NumberParserImpl.kt`)
  - Network Simulation (`NetworkSimulator.kt`)

# Implementation Details
## Summary
This implementation prioritizes **modularity, efficiency, and testability**. By leveraging **Kotlin-first libraries**, **bitwise operations for parsing**, and **network simulation**, we ensure a robust and maintainable Android solution.
### Project Structure
```
NavvisTask/
│── app/                      # Main Application Module
│   ├── src/main/java/de/arjmandi/navvistask/
│   │   ├── ui/               # UI Screens & Components
│   │   ├── di/               # Dependency Injection Modules
│   │   ├── App.kt            # Application Entry Point
│   │   ├── MainActivity.kt   # Main Activity
│   │   ├── UiState.kt        # UI State Management
│
│── numberdatasource/         # Data Handling Module
│   ├── src/main/java/de/arjmandi/navvistask/numberdatasource/
│   │   ├── data/
│   │   │   ├── repository/   # Repository Implementation
│   │   │   ├── remote/       # API Client with Ktor
│   │   │   ├── mock/         # Network Simulator
│   │   ├── domain/
│   │   │   ├── model/        # Data Models
│   │   │   ├── repository/   # Repository Interface
│   │   │   ├── parser/       # Parsing Logic
│   │   ├── di/               # Dependency Injection (Koin)
```

## 1. Tech Stack
This project is built using **Kotlin-first libraries** that are lightweight and easy to use, ensuring modern and efficient Android development:

### **Ktor (Networking)**
- **Ktor is a Kotlin-native HTTP client** designed with coroutines in mind.
- Lightweight and flexible, allowing smooth error handling and response parsing.
- **Built-in coroutine support** ensures non-blocking network calls.
- Retrofit is great! But it's not 2014 anymore. (sorry, Jake Wharton)

### **Koin (Dependency Injection)**
- A simple, Kotlin-first DI framework without reflection.
- Easier to set up and use compared to Dagger/Hilt.
- Supports modularization without excessive boilerplate code.

### **MockK (Unit Testing)**
- Designed specifically for Kotlin, making mocking easier.
- More intuitive compared to Mockito when dealing with coroutines and extension functions.

### **Coroutines (Async Processing)**
- Provides **structured concurrency** and avoids callback hell.
- Used for **network calls, parsing, and UI updates**, making the code efficient and readable.
- Works seamlessly with Jetpack Compose and Ktor.

## 2. Why Modularizing `NumberDataSource`?
The `NumberDataSource` module is kept separate for:

- **Reusability**: This module can be used in other projects, such as a backend service or another Android app.
- **Scalability**: If the parsing logic needs to be changed or extended (e.g., support different APIs), we can do so without affecting the main app.
- **Testing**: The parsing and network logic can be **independently tested** without UI dependencies.

The module provides:

1. A **data repository** to manage API and mock data.
2. A **parser** that converts raw JSON numbers into structured data.
3. A **network simulator** to handle different network conditions.


## 3. Logic Behind `NumberParser`
The task required extracting meaningful values from numbers in a JSON array. We implemented the parsing logic using **bitwise operations**:

### **Steps:**
Each number (0-255) is processed as follows:

1. **Extract Section Index** → Last 2 bits (`number & 0b11`) → Determines `SECTION1` to `SECTION4`.
2. **Extract Item Value** → Bits 2-6 (`(number shr 2) & 0b11111`) → Maps to `Item1` to `Item32`.
3. **Extract Checkmark Status** → Most significant bit (`(number & 0b10000000) != 0`) → Determines if the item is checked.

This approach ensures:

- **Fast, efficient parsing** using bitwise operations.
- **Error handling** for invalid numbers (out of range or malformed values).
- **Scalability** (can extend mapping rules if needed).

## 4. Logic of `NetworkSimulator`
Since the real API was unavailable, I created a **mock network simulator** to test different conditions:

### **Network Modes:**
1. **Stable** → Returns a normal response with valid numbers.
2. **Stable with Malformed Response** → Randomly includes **invalid data** (e.g., empty response, string values in JSON).
3. **Flaky** → Simulates timeouts and incomplete responses.
4. **No Connection** → Simulates an offline state by throwing an `IOException`.

### **How It Works:**
- Uses a **random generator** to create **realistic failures**.
- Can **simulate an empty response or corrupted data**.
- Injects **network delay** (`delay(3000)`) for timeout scenarios.
- Helps test how the app handles errors **without needing an actual API**.

## 5. Overall Architecture
The project follows a **modular, clean architecture**, ensuring scalability and maintainability:

### **Layered Structure**
```
App Module (UI + ViewModel)
│
├── numberdatasource (Data Layer)
│   ├── repository (Handles API & mock data)
│   ├── parser (Processes numbers)
│   ├── mock (Simulates network responses)
│   ├── di (Dependency injection using Koin)
```

### **Data Flow**
1. **ViewModel** requests numbers from `NumberDataSource`.
2. `NumberDataSource` fetches from `ApiClient` or `NetworkSimulator`.
3. The **response is parsed** into structured `ParsedNumber` objects.
4. ViewModel updates the **UI state**, triggering Jetpack Compose.

This architecture ensures:

- **Loose coupling** between UI, data, and networking layers.
- **Easy unit testing** for each component.
- **Scalability** for adding features (e.g., offline caching with Room, reading data from a new source, etc.).

---


### Author: **Javad Arjmandi**
📧 Contact: javad@arjmandi.de

