# ASH Table Tennis Ball Fetcher Mobile Application

## Overview

The ASH Table Tennis Ball Fetcher project involves developing a RaspberryPi 5 controlled robot to autonomously collect table tennis balls in a training ITTF environment and a mobile application to control the robot and monitor its status. 

![Animation for app execution](https://imgur.com/a/ash-animation-todJIn3)

## Features

- **Autonomous Navigation**: The robot can navigate and avoid obstacles within a predefined area.
- **Ball Collection**: Utilizes a vacuum system to collect and store table tennis balls.
- **User Interface**: The mobile app allows users to control the robot, schedule operations, and monitor status.
- **Environmental Compliance**: Designed to operate in standard training environments with specific lighting, flooring, and space restrictions.

## ASH characteristics

1. **Ball Collection System**:
    - Guide flaps for ball guidance.
    - Storage for 10 to 15 balls.
    - Sealed system for optimal suction efficiency.

2. **Navigation and Mobility**:
    - Two motorized wheels and one swivel wheel.
    - Object recognition and distance measurement in four directions.

3. **Power Supply**:
    - Powered by two 12V rechargeable battery packs.
    - Supplies 5V to the main controller and peripherals, and 6V to the motors and servo motor.

4. **Return Base**:
    - Includes magnets and a 25cm tall tower with a LED bulb for recognition.

5. **User Interface**:
    - Immediate start and end of the robot cycle.
    - Field Of View (FOV) estimation map.

## Mobile Application

The mobile application is designed to interface with the ASH robot, providing an intuitive and user-friendly way to control and monitor the robot's operations.

### Features

1. **Bluetooth Connectivity**: Connect to the ASH robot using Bluetooth for seamless communication.
2. **Command and Control**: Send commands to start, pause, resume, or stop the ball fetching process.
3. **Scheduling**: Schedule the robot to start the ball fetching process at specified times.
4. **Real-Time Monitoring**: Monitor the robot's status, sensor readings, battery levels, and ball collection count.
5. **Field of View (FOV) Map**: View a visual representation of the robot’s surroundings and the positions of detected balls.

### Activities

1. **Connection Activity**:
    - **Purpose**: To establish a Bluetooth connection with the ASH robot.
    - **Features**: Displays ASH logo and Bluetooth status icon.

2. **Home Screen Activity**:
    - **Purpose**: To provide the main interface for controlling the robot.
    - **Features**: 
        - "Catch!" button to start the ball fetching process.
        - "Schedule" button to set a schedule for the fetching process.
        - Displays battery status.

3. **Schedule Activity**:
    - **Purpose**: To set a start and end time for the ball fetching process.
    - **Features**:
        - Time pickers for selecting start and end times.
        - Buttons to confirm and send the schedule to the robot.

4. **Catching Process Screen**:
    - **Purpose**: To monitor the ball fetching process in real-time.
    - **Features**:
        - Displays sensor data from all four directions.
        - Shows the current status of the robot.
        - Buttons to pause, resume, or return the robot to the base.
        - Button to view the Field of View (FOV) map.

5. **Field of View (FOV) Screen**:
    - **Purpose**: To provide a visual representation of the robot’s surroundings.
    - **Features**:
        - Displays a map with positions of detected balls.

6. **Error Screen**:
    - **Purpose**: To display errors encountered by the robot.
    - **Features**:
        - Shows different error messages (e.g., 'base not found,' 'robot stuck,' 'ball stuck in the tube').
        - Reconnect button for resolving issues.

### Data Exchange

- **Data Sent to the Robot**:
    - Commands such as `start`, `return_to_base`, `pause`, `resume`, and `schedule`.

- **Data Received from the Robot**:
    - Status updates including sensor distances, battery level, balls collected, balls coordinates, robot status, and errors.

## Getting Started

### Prerequisites

- Android device with Bluetooth and/or Wi-Fi capabilities
- ASH Robot

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/ASH-Table-Tennis-Ball-Fetcher-App.git
    ```
2. Open the project in Android Studio.
3. Build and run the application on your Android device.

### Usage

1. **Connecting to the Robot**:
    - Launch the app and connect to the robot via Bluetooth.
2. **Starting the Operation**:
    - Use the home page to start or schedule the ball fetching process.
3. **Monitoring Status**:
    - View real-time updates of the robot’s status, battery levels, and sensor readings.
4. **Field Of View (FOV) Map**:
    - Check the FOV screen to see the positions of detected balls.

## Contact

For any inquiries or issues, please contact Alfons Andrade at alfons@alunos.utfpr.edu.br .

---
