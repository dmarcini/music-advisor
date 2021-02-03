# Music Advisor

A console application that implements a music advisor based on Spotify API
with follow options:
* auth - authorization Spotify's user, in goal to use this 
  application you need provide your Client ID and 
  Client Secret both available in a dashboard of Spotify development mode 
* featured - a list of Spotify-featured playlists with their links fetched from API
* new -  a list of new albums with artists and links on Spotify
* categories - a list of all available categories on Spotify
* playlists C_NAME - a list of playlists of C_NAME category and their 
  links on Spotify
* next - get the next page (by default only 5 entries are displayed at a time)
* prev - get the previous page
* exit - exit from application

---

## Technology
* Java SE
* Apache Maven

---

## Requirements
* Java SE 14 JRE installed
* Apache Maven 3.6.x installed (at least)

---

## Building & Running
Example for Linux system
```
git clone https://github.com/dmarcini/music-advisor
cd music-advisor
mvn package

java -jar target/music-advisor-1.0-SNAPSHOT.jar
```

---

## Sources
Project was based on Jetbrains Academy tutorial - Music Advisor: </br>
https://hyperskill.org/projects/62