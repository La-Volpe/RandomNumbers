# NavVis Android Code Challenge

# Table of Contents

- [NavVis Android Code Challenge](#navvis-android-code-challenge)
  - [Overview](#overview)
  - [Features](#features)
  - [Installation & Setup](#installation--setup)
  - [Usage](#usage)
  - [Testing](#testing)
- [Implementation Details](#implementation-details)
  - [Choice of Libraries and Tools](#1-choice-of-libraries-and-tools)
    - [ktor-networking](#ktor-networking)
    - [koin-dependency-injection](#koin-dependency-injection)
    - [mockk-unit-testing](#mockk-unit-testing)
    - [coroutines-async-processing](#coroutines-async-processing)
  - [Why Modularizing NumberDataSource](#2-why-modularizing-numberdatasource)
  - [Logic Behind NumberParser](#3-logic-behind-numberparser)
    - [Steps](#steps)
  - [Logic of NetworkSimulator](#4-logic-of-networksimulator)
    - [Network Modes](#network-modes)
    - [How It Works](#how-it-works)
  - [Overall Architecture](#5-Overall-architecture)
    - [Layered Structure](#layered-structure)
    - [Data Flow](#data-flow)
  - [Trade-offs and Considerations](#6-trade-offs-and-considerations)
    - [Key Trade-offs](#key-trade-offs)
  - [Summary](#summary)
  - [Author](#author-javad-arjmandi)

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

## 1. Choice of Libraries and Tools
This project is built using **Kotlin-first libraries** that are lightweight and easy to use, ensuring modern and efficient Android development:

### **Ktor (Networking)**
- Retrofit is great, but it's not 2014 anymore. (Sorry Jake Wharton!)
- Unlike Retrofit, **Ktor is a Kotlin-native HTTP client** designed with coroutines in mind.
- Lightweight and flexible, allowing smooth error handling and response parsing.
- **Built-in coroutine support** ensures non-blocking network calls.
- Less boilerplate compared to Retrofit.

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

- **Reusability**: This module can be used in other projects, such as a different module in the same app, a backend service or another Android app.
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
Since the real API was unavailable, we created a **mock network simulator** to test different conditions:

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
- **Scalability** for adding features (e.g., offline caching with Room).

## 6. Trade-offs and Considerations
This project was developed in **10-12 hours**, and naturally, some trade-offs had to be made. My guiding principles are:

1. **"Perfect" is the enemy of "good"** – Delivering something functional within constraints is better than endlessly optimizing.
2. **Make it work, then make it better** – Prioritize a working solution before refining it.
3. **Time is linear and continuous** – A solution must fit within the available time.

### **Key Trade-offs:**
- **Focused testing on core logic**: I wrote tests only for `NumberParser` and `NetworkSimulator`, as they are the backbone of the app. Ideally, the UI, repository, and other classes should have tests too.
- **No NDK implementation (yet)**: I considered implementing the `NumberDataSource` in **C++ with NDK**, but refreshing my knowledge on JNI would take time. However, the current design allows easy reuse in a **JVM backend** environment.
- **Long `NumberListScreen.kt` file**: The composables inside it could be moved into separate files for better modularity. It's not terrible, but it could be improved.
- **More abstraction would improve maintainability**: Given more time, I would add even more abstraction layers to keep the codebase even cleaner.
- **ViewModel dependency concerns**: In my own projects, I never make a **direct call** to another class from a ViewModel. This anti-pattern makes refactoring and testing harder. Instead, I'd introduce a `NumberContext` or `NumberMiddleware` class that acts as a **delegate** between the ViewModel and data sources. A similar approach would also apply to `NumberDataSource` and `NetworkSimulator`, keeping dependencies more manageable.

## Summary
This implementation prioritizes **modularity, efficiency, and testability**. By leveraging **Kotlin-first libraries**, **bitwise operations for parsing**, and **network simulation**, we ensure a robust and maintainable Android solution. Trade-offs were made to deliver the best possible result within the given time constraints while keeping future improvements in mind.

---

### Author: **Javad Arjmandi**
📧 Contact: [javad@arjmandi.de](mailto:javad@arjmandi.me)

