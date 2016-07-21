# Gym-Pal
An Android Application to stay fit :)

At launch user needs to provide name of the Wifi you want to track it can be your gyms
or any other where you frequent and want to keep track of 

Key Features
1. Keeps track of number of times user has visited the gym in the current year.
2. When was the last visit
3. Shows Gym visits in a widget
4. Notifies users the step counts since last reboot.
5. Shows user average step counts since last reboot.
6. Running pal is second activity.
7. It shows current user location and plots it on the map.
8. When you run from point A to point B this activity  can calculate distance using this Google Distance matrix API

Third party libraries used 

    jakewharton:butterknife:7.0.1 --- to bind data to views handle button clicks
    apt 'net.simonvt.schematic:schematic-compiler:0.6.7 ---- To create content Provider backed with sql lite database
    compile 'com.facebook.stetho:stetho:1.3.1'--- To inspect database and network connection from chrome browser
    
    Google services used

    'com.google.android.gms:play-services-ads:9.2.0 --- For Ads banner
    'com.google.android.gms:play-services:9.2.0--- For Maps
    'com.google.maps.android:android-maps-utils:0.4+ For MAps
