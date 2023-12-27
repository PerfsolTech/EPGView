# EPG View Android Library - TV Schedule Display Library

![EPG View](https://github.com/PerfsolTech/EPGView/blob/main/epg_image.png?raw=true)

EPG View Android Library is a powerful tool for creating TV schedule (EPG) viewing interfaces on Android TV and mobile devices based on RecyclerView and makes it easy to efficiently display large sets of data. It provides a simple and flexible way to display and manage TV channel schedules, allowing users to browse available programs and set reminders easily.

## Features

- Display the TV schedule as a grid.
- Support for various EPG data sources (Mapping XML, JSON, API to library model).
- Customizable design and styling.
- Program search and filtering capabilities.
- Easy integration into your Android TV or mobile application.
- Supporting D-Pad and touch (or mouse) scrolling

## Installation

To start using the EPGGrid Android Library in your project, add the following dependencies to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.PerfsolTech:EPGView:$epg_latest_version' // Replace with the latest version
}


## Usage
val epgGrid = findViewById(R.id.epg_grid);
epgGrid.initView(
    listOf(
        ChannelModel(
            id = channel.id, // string
            logo = channel.logo, // channel logo url, string
            shows = listOf(
                ShowModel(
                    id = show.id, // string
                    channelId = channel.id, // string
                    name = show.name, // string
                    showPreviewImage = show.previewImage, // string (optional)
                    startDate = show.startDate, // DateTime
                    endTime = show.endTime, // DateTime
                )
            )
        )
    )
)

epgGrid.listener = object : EPGRecyclerView.OnEventListener {
    override fun onShowClick(channelId: String, showId: String) {
        // triggered by enter click with D-Pad (OK button) or mouse click
    }

    override fun onShowExit() {
        // triggered on losing focus from EPG view
    }

    override fun onShowSelected(channelId: String, showId: String) {
        // triggered by navigation through grid with D-Pad or mouse click
    }
}

with(EPGConfig) {
    showBackgroundDrawable = R.drawable.show_background
    channelLogoBackgroundDrawable = R.drawable.epg_channel_logo_background
    rowHeight = 50
    rowLogoHeight = 40
    marginVerticalChannelLogo = 5
}

epgGrid.setStartHour(START_HOUR) // int (from 0 to 23) time from EPG grid started
epgGrid.setEndHour(End_HOUR) // int (from 0 to 23) time to EPG grid end

