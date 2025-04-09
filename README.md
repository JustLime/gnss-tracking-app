# GNSS Tracking App

## Description

This project presents a working GNSS tracking app for my Bachelor Thesis at
Heilbronn University of Applied Sciences (HHN). Designed for Android 10+ and leveraging
OpenStreetMap tools for Android, the app offers an intuitive interface for tracking GNSS data and
visualizing locations on a map in real-time. It also provides real-time location notifications and
detailed statistical insights.

The app is built with the latest version of Kotlin and utilizes the K2 compiler, which enhances
compile speed.

## Used Libraries

- [osmdroid - OpenStreetMap-Tools for Android](https://github.com/osmdroid/osmdroid)
- [Jetpack Compose with Material 3](https://developer.android.com/compose)
- [Ktor for creating HTTP requests and websocket connections](https://ktor.io/)
- [Koin - Kotlin & Kotlin Multiplatform Dependency Injection framework](https://insert-koin.io/)
- [Gson - Converts Java Objects into JSON and back](https://github.com/google/gson)

## Screenshots

<div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap;">
<img src="./doc/screenshots/App/screenshot_real_device_map_screen_dark.jpg" alt="MapScreen Dark Mode" width="30%">
<img src="./doc/screenshots/App/screenshot_real_device_map_screen_light.jpg" alt="MapScreen Light Mode" width="30%">
<img src="./doc/screenshots/App/screenshot_real_device_location_notification.jpg" alt="Notification Real Time Location" width="30%">
<img src="./doc/screenshots/App/screenshot_real_device_stat_screen_light.jpg" alt="Statistics Screen" width="30%">
<img src="./doc/screenshots/App/screenshot_real_device_settings_screen_light.jpg" alt="Settings Screen" width="30%">
</div>

## Features

### Implemented

- Map Screen for Location Tracking
  - Displays your location as a circle on the map, with accuracy support
  - Scale overlay
  - Pinch-to-zoom functionality for map navigation
  - Use the Geocoder API as a fallback to find your location by tapping the
    button at the bottom right
- Real-Time location notifications
- WebSocket connection support to interface with a GNSS module
- Statistics Screen to visualize GNSS data
- Settings Screen for customization options

## Installation

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- [Visual Studio Code](https://code.visualstudio.com/)
- [MicroPico](https://marketplace.visualstudio.com/items?itemName=paulober.pico-w-go)

### Configuration

#### Rover Firmware

The source code for the firmware can be found in [de.hhn.gnss_rtk_rover](./lib/de.hhn.gnss_rtk_rover/).

1. Change the global settings like WiFi credentials of your mobile hotspot and NTRIP credentials in [globals.py](./lib/de.hhn.gnss_rtk_rover/utils/globals.py)
2. Build and flash the firmware to the microcontroller
3. Run the microcontroller

#### Websocket Connection

1. Create a mobile hotspot on your smartphone
2. Connect the microcontroller with the GNSS module via mobile hotspot of your
smartphone
3. Retrieve the microcontroller's IP address
4. Set the IP address in the `WEB_SOCKET_IP` variable
   in [BaseApplication.kt](app/src/main/java/de/hhn/gnsstrackingapp/BaseApplication.kt)
5. Build and run the app on your device

## Known Issues

- [ ] No unit testing story
- [ ] Allowed insecure WebSocket connection in in [network_security_config.xml](app/src/main/res/xml/network_security_config.xml), as required by Android's security policy
