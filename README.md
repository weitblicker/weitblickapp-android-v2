# weitblickapp-android-v2


code structure: 


/data -> All user-specific classes including session management, registration & change of user details

1.Login_Activity:   Activity handles all Authentication processes. Uses SessionManager and LoginData.
2.SessionManager:   Initiates and handles terminaton of the users session. Saves Session in SharedPreferences.
3.LoginData:        Handles all Sever-Requests regarding the authenticated user.
4.RegisterActivity: Activity handles all Registration processes. Uses RegisterData.
5.Register Data:    Handles all Sever-Requests regarding the creation of new users.

/ui -> MainActivity + splash-Activity for splash screen & all classes for app-functionality + utility classes  


a standarized package consists of the following classes

1. (...)ViewModel : POJO to hold the server-requested data in (Model)
2. (...)ListFragment: data requests and manipulation of model-data (Data-Controller)
3. (...)ListAdapter: View-Controller for displaying data in ListView

4. (...)DetailFragment: View-Controller for Details-Page of List-Item
5. (...)AdapterShort: View-Controller for different display of referenced data in DetailFragments (e.g. a Project is referenced to News.. "short"-display of news in Project)

/utils:

- CircleTransform: Transformation class to create circle-shaped Bitmaps
- ImageSliderAdapter: Controller to instantiate Image-Views in ViewPager of Details-Page -> Switch-Case: if no picture exists, check which default-picture to load
- MyJsonArrayRequest: Special type of Array-Request which passes Json-Object in body and gets an Array-Response
- ViewPagerAdapter: To add Fragments to ViewPager for tab-view (e.g. news & events)


/location -> all classes for cycle-functionality

1. MapOverviewFragment: Fragment to chose project to donate for and displays projects location on map
-> on click for play button checks if user is actually logged in to start cycling and if so starts "MapFragment"

2. MapFragment: 
- setUp Google Maps
- fetches Location, calculates KmTotal & sends Segments to Server every 30s which returns ammount of euros for send distance
- Updates UI, Listener for GPS (if disabled -> show popup)
- Uses LocationService to fetch Kalman-filtered Locations

3. LocationService: 
- Initiates LocationRequest using the LocationManager, Listener for onLocationChanged 
- sends Broadcast of new Location to MapFragment
- KalmanLatLong: Helper class for Kalman-calculations

4. Tour
- POJO for pending tour data





