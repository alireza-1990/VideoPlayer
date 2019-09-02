## Video Player

### About Project
This is a sample project to read video files from user's device. It also allows user to create and manage playlists.

### Libraries
- The project uses Model View ViewModel (MVVM) architecture with help of google official libraries.
- The project uses rxJava and rxAndroid for reactive programming and also the execution of background tasks.
- LiveData is used for storing all ViewModel data, as a result, all data in ViewModel are lifecycle aware and updating view happens only when the view is in an active state.
- Dagger2 is used for Dependency Injection.
- Room is used for database access. Room is combined with rxJava to allow reactive access to DB.
- Mockito and Roboletric used for testing.

