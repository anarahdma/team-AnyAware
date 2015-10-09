![AnyAware Logo](https://raw.githubusercontent.com/BigDataForSanDiego/team-AnyAware/master/logo.PNG)

## Team Members
Ali Esparza, Bethany Plunkett, Bogdan Matuszynski, Edward Salazar, Ian Follett, Xi Huang

## Overview
Using the data found on the San Diego Regional Data Library related to crime incidents from 2007 - 2013 we were able to develop an algorithm to rate neightborhoods based on crime rate. Using an internal threshhold we determine if the neighborhood is unsafe and users should use caution at certain times of the day or night. Using the [Gimbal Framework](http://www.gimbal.com/) we then create a geofence of the area. Our supplemental Android application will notify users via [Google Cloud Messaging](https://developers.google.com/cloud-messaging/) if they enter one of these areas and provide them with tips to avoid crime.

## Sample Output
![sample output](https://raw.githubusercontent.com/BigDataForSanDiego/team-AnyAware/master/Capture.PNG)

## Datasets
- [San Diego Region Crime Incidents 2007 - 2013](https://s3.amazonaws.com/s3.sandiegodata.org/repo/clarinova.com/crime-incidents-casnd-7ba4-r3/incidents-5y.csv)

## Frameworks
- [Gimbal](http://www.gimbal.com/)
- [Google Cloud Messaging](https://developers.google.com/cloud-messaging/)

## Compiling the Android application
- to run this sample in Android Studio choose the 'Import Project' option
- fill the API KEY into the MainActivity
- please contact a team member for a Sample API key

```java
package com.gimbal.hello_gimbal_android;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;


public class MainActivity extends ActionBarActivity {

    private PlaceManager placeManager;
    private PlaceEventListener placeEventListener;
    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);

        listAdapter.add("Setting Gimbal API Key");
        listAdapter.notifyDataSetChanged();
        Gimbal.setApiKey(this.getApplication(), "YOUR_API_KEY_HERE");

        placeEventListener = new PlaceEventListener() {
            @Override
            public void onVisitStart(Visit visit) {
                listAdapter.add(String.format("Start Visit for %s", visit.getPlace().getName()));
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVisitEnd(Visit visit) {
                listAdapter.add(String.format("End Visit for %s", visit.getPlace().getName()));
                listAdapter.notifyDataSetChanged();
            }
        };

        placeManager = PlaceManager.getInstance();
        placeManager.addListener(placeEventListener);
        placeManager.startMonitoring();

        CommunicationManager.getInstance().startReceivingCommunications();
    }

}
```