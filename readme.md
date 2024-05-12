# Air Quality Monitoring Application

![mockup](assets/mockup.png)

## Project Description
The application aims to provide users with instant access to details regarding air pollution and weather information in their vicinity, without the need to search various websites for fragmented information. It visualizes air quality in a simple and intuitive way through appropriate animations and color codes indicating optimal or hazardous atmospheric conditions. Additionally, users can check the history of pollution measurements and export them to a PDF file. Data is sourced from aqicn.org/city/wroclaw/. The application's logic resides on the backend, responsible for fetching and processing data correctly. Users can change the city through a resilient search feature and login is preceded by a request for location, ensuring the display of meteorological conditions relevant to the user's area.

## Technologies Used
- Kotlin 221-1.8.0-release-for-android-studio-AS5591.52
- Android 13.0 (Tiramisu)
- Gradle version 7.5
- APIs:
  - [aqicn.org](https://aqicn.org/)
  - [open-meteo.com](https://open-meteo.com/)
  - [geoapify.com](https://www.geoapify.com/)
  - Maps SDK for Android

## Application Features
The application allows:
- Checking details regarding air pollution and weather information in real-time.
- Visualizing the air quality status through appropriate animations and color utilization.
- Checking the history of saved pollution measurements and exporting them to a PDF file.
- Changing the city through a search feature resilient to incorrect data.
- Logging in with a request for the user's location.


## Project Implementation:

During the project, all objectives were successfully accomplished. The main elements realized include:

-User Interface: Intuitive UI allowing users to instantly check air pollution and weather details in their area, visualized through appropriate indicators and weather icons.
- Data Retrieval: Backend implemented to fetch air pollution and weather data from external sources, process it, and generate statistics and visualizations.
- City Search: Resilient city search feature providing accurate results and handling errors effectively.
- Documentation: Comprehensive documentation generated based on Dokka dependency, including docstrings and comments in code.
- Tests: Functional and non-functional tests conducted to identify defects and optimize code performance.

## Application Optimization and Network Communication:

- Application startup time on an emulator (Pixel 6 Pro API 33) averaged 5.425 seconds.
- Average API response times were as follows:
  - aqicn.org/city/wroclaw/: 238ms
  - open-meteo.com/: 219ms
  - geoapify.com/: 260ms
  - Maps SDK for Android: 2.324s

## Future Development Opportunities:

- UI Functionality Expansion: Potential to add features like weather forecasts, air pollution alerts, and outdoor activity recommendations.
- Integration with Additional Data Sources: Consider integrating with more weather or air quality data providers for comprehensive information.
- Data Retrieval Efficiency Enhancement: Optimize data retrieval and processing for improved performance.
- Data Export Module Development: Expand data export formats to include CSV or JSON for broader usability.
- City Search Improvement: Further optimize and enhance the city search process, including suggestion mechanisms and error handling.


## Application Deficiencies:
Several deficiencies were identified during the application analysis, including reliance on external data sources, computational complexity of the map interface, data unavailability for certain areas, internet connection dependency, and potential performance issues.