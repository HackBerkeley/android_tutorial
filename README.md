Acknowledgements 
================
First of all, I'd like to make a thanks to **Daylen Yang** for letting me use his [Bear Transit API](https://beartransit.daylen.com) for this tutorial. He's an awesome developer. If you want to check some other things he's made, go to [his website]("http://www.daylen.com") and check out some of his projects.

Step 0: Downloading Android Studio
==================================
The first thing you must ensure is that you have Android Studio installed on your computer. To install this piece of software, please go to [this website]("https://developer.android.com/sdk/index.html") and click the "DOWNLOAD ANDROID STUDIO" button. Once you've gone through the setup process, double-click the app to open it, and you should be good to go. 

Step 1: Setting up Google Maps
==================================
So the first thing you want to do is go to values/google_maps_api.xml. What you will notice is that there is an empty line that says: 
```
<!-- Insert your key here -->
```
What you want to do is go to [this link](https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend). Click "Continue." Next, click "Go to Credentials" and click "Create." Copy and paste the API Key they give you into values/google_maps_api.xml.

Step 2: Create a Layout
========================
This step is designed to give you a little bit of experience with XML and Android's Layout interface. First off, go to bus_list.xml. In between the <RelativeLayout> and <ImageView> tags, type the following code: 

```
	<LinearLayout android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <TextView
            android:id="@+id/item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Medium Text"
            android:textAppearance="?android:attr/textAppearanceMedium"
            android:layout_marginLeft="10dp"
            android:layout_marginTop="5dp"
            android:padding="2dp"/>
        <TextView
            android:id="@+id/textView1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="TextView"
            android:layout_marginLeft="10dp"/>
    </LinearLayout>
'''

Here's a little explanation of what you just did. First, you created a **Linear Layout**, which essentially means that every element you add is stacked vertically. You then added two **TextViews** with padding and margins that make it so the TextViews don't directly mash into each other or the wall. The entire thing is called a **ListView**, which basically allows you to take an array and turn it into a list of things using a **ListViewAdapter**. You can see what I mean by this in values/activity_bus_list.xml and java/me.beartransit.apollojain.myapplication/BusListAdapter respectively. 

Step 3: Intents
================
In Android, we have what are called **Intents**, which is essentially a short description of an action that is about to be done. They can be used to trigger an **Activity**, which is essentially what allows an app to do different things (for instance, in Bear Transit, the real-time tracking and the location listings are in different activities). They can also be used to pass information between said Activities. Let's take a look. 

Let's first go to BusListActivity, which lists all of the Bear Transit Stops on campus. If  you scroll to the bottom, in the onPostExecute function of GetJsonTask, you can see a comment that says "PLACE CODE HERE." In that line, type the following code: 
```
Intent intent = new Intent(thisActivity , LocationDetailsActivity.class);
intent.putExtra("url", url);
intent.putExtra("name", name);
thisActivity.startActivity(intent);
```
What did we just do? First of all, we created our Intent, which will take us from this BusListActivity class to a new activity called LocationDetailsActivity. Next, we want to send two strings, one of which is the url associated with the location we're looking at and the other of which is the actual name of the location we're looking at. Next, we will have thisActivity (which is our current BusListActivity) actually start the intent and go to LocationDetailsActivity. 

Now, let's go to LocationDetailsActivity. In the method onCreate, you will see the same line "INSERT CODE HERE." Type the following lines: 

```
Intent intent = getIntent();
url = intent.getExtras().getString("url");
location = intent.getExtras().getString("name");
setTitle(location);
```
What this does is gets the intent that was sent to this particular activity. You then retrieve the values associated with the keys "url" and "name," and set the Title (which is at the top of the app) to the location.

Step 4: Making requests
========================
So in order to create Bear Transit, we're going to need to make some network requests. What does a network request yield? You can go to [this link](http://beartransit.daylen.com/api/v1/lines) to see what JSON data looks like. Now, go to java/me.beartransit.apollojain.myapplication/Communicator

```
HttpGet request = new HttpGet();
request.setHeader("Content-Type", "text/plain; charset=utf-8");
request.setURI(new URI(URL));
HttpResponse response = client.execute(request);
in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
```

Essentially, what we're doing here is making an HTTP GET Request and then reading it using what's called an InputStream Reader. This will allow us to actually read the JSON we're getting from the link we saw earlier. 

The code we are modifying is in a function called HttpGet, which is a helper function of getJsonFromUrl. We can see the effects of this function in LocationDetailsActivity. Let's check it out!

So what we want to do is scroll to GetLocationTask, which is what is called an AsyncTask. Why can't we just call the Communicator's function directly, you ask? Well, Android actually prohibits network requests from Android's main thread, since that can crash the entire app. If we were just getting back end data, we would use a regular Thread, but since we're updating the user interface, we have to use an AsyncTask, which both creates a new thread and then does an action after the execution of that thread. We will be modifying the "doInBackground" function. above the line 

```
JSONObject page = null;
```

we want to modify the line to be

```
JSONObject page = new Communicator().getJsonFromUrl((String) urls[0]);
```

Step 5: Activity Lifecycle
================================
So in Android, we have what is called the Activity Lifecycle. Here's a flowchart: 
![alt text](http://i.stack.imgur.com/2CP6n.png "Activity Lifecycle")
Everything above is pretty self explanatory. You want to trigger certain events when the app starts, pauses, restarts, etc. For instance, when the app is created, you want to display the main view. When the app pauses, you might want to stop the Asynctask in the background to save RAM. In this step, we'll do some basic Activity Lifecycle stuff. 

First, go to CurrentMapsActivity. As you can see, there are two methods that handle continuously calling and updating the locations of the shuttles: startRepeatingTask and stopRepeatingTask. What startRepeatingTask does is create a thread to update the location of the bus from a JSON GET Request every eight seconds using something called a handler. As you might have guessed, stopRepeatingTask kills this Handler. If we pause, destroy, or stop this app, we want to stop the repeating task. TO showcase this, let's go to onStop and place the following line into "INSERT CODE HERE": 

```
stopRepeatingTask();
```
Similarly, when we restart this activity, we want to resume the repeated bus location updates. So, let's go to onResume, and place the following line into "INSERT CODE HERE":

```
setUpMapIfNeeded();
```

Step 6: 
