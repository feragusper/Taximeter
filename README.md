# Taximeter

Android application that simulates a taximeter, calculating fares dynamically based on distance, time, and additional charges like luggage.

💻 Requirements and Getting Started
-----------------------------------

Java 17 and Android Studio Flamingo or later are required.

To build it locally:

Use ./gradlew assembleDebug to build it, or run it directly in Android Studio.

Use ./gradlew test to execute unit tests locally.

🏗️ Architecture
---------------

The project follows Clean Architecture principles with a modularized approach, incorporating MVI, Use Cases, and Repository patterns.

### Project Structure

 - **features**: Contains core functionalities of the taximeter.
 - **domain**: Business logic, independent from the platform.
 - **data**: Implements repositories and data sources.
 - **libraries**: Shared modules that provide reusable components.
 - **build-logic**: Custom Gradle setup to modularize dependency management.

📚 Libraries Included
---------------------

 - Kotlin - Modern programming language for Android development.
 - Coroutines / Flow - Asynchronous programming.
 - Jetpack Compose - Declarative UI framework.
 - Hilt - Dependency injection framework.
 - JUnit / Mockk - Unit testing and mocking framework.

🚕 Key Features
---------------

 - Real-time Fare Calculation: Computes the fare dynamically based on distance, time, and extra luggage charges.
 - Flow-Based Location Simulation: Uses a custom LocationProvider to generate real-time GPS movement.
 - Composable UI: Built entirely with Jetpack Compose for a modern and reactive UI experience.
 - Modular and Scalable: Easily extendable pricing strategy for future enhancements.
 - Production-Ready: Designed with best practices, maintainability, and scalability in mind.

🤝 Support & Contribute
-----------------------

If you've found an error in this project, please file an issue: https://github.com/feragusper/SmokeAnalytics/issues

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub.

Pull requests are welcome.

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## License
```
MIT License

© 2024 feragusper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.
```