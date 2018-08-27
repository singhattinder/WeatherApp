package com.attinder.weatherdemoapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.attinder.weatherdemoapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;


import com.attinder.weatherdemoapp.models.Weather;
import services.FetchAddressIntentService;
import services.FireBaseDatabaseService;
import services.JSONParser;
import services.OpenWeatherService;
import com.attinder.weatherdemoapp.utils.dataParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView sunset;
    private TextView sunrise;
    private TextView tempMax;
    private TextView tempMin;
    private TextView sunsetTime;
    private TextView sunriseTime;
    private DrawerLayout mDrawerLayout;
    private TextView userName;
    private TextView emaiId;
    private ImageView userImage;
    private MenuItem signInMenu;
    private MenuItem signOutMenu;
    private MenuItem favouriteCity;
    private ImageView locationButton;
    private ImageButton search;
    private FrameLayout frameLayout;
    private FrameLayout backGroundFrame;
    private LinearLayout navigationHeaderContainer;
    private FirebaseAuth mAuth;
    private ImageView likeImageView;

    private TextView hum;
    private FusedLocationProviderClient mFusedLocationClient;


    private int REQUEST_LOCATION = 1;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private  String city;




    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.google.android.gms.location.sample.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";

    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser account = null;
        try{
             account = mAuth.getCurrentUser();
        }
        catch (NullPointerException ex) {
            Log.d("google", ex.toString());
        }


        Log.d("google","onStart is invoked");


        if(account != null) {
           userName.setText(account.getDisplayName());
           emaiId.setText(account.getEmail());
           if(account.getPhotoUrl() != null){
            Glide.with(this).load(account.getPhotoUrl()).into(userImage);}
            else {
               userImage.setImageResource(R.mipmap.ic_launcher_round);
           }
            signInMenu.setVisible(false);
        }
        else {
            signOutMenu.setVisible(false);
            favouriteCity.setVisible(false);
            likeImageView.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

          city = getIntent().getStringExtra("search");
        try
        {
            Log.d("city", city);
        }
        catch (NullPointerException ex) {
            Log.d("city null", ex.toString());
        }

        if (city == null) {

            city = "Manhattan";
        }




        mResultReceiver = new AddressResultReceiver(new Handler());

        cityText =  findViewById(R.id.cityText);
        condDescr =  findViewById(R.id.condDescr);
        temp =  findViewById(R.id.temp);
        hum =  findViewById(R.id.hum);
        press =  findViewById(R.id.press);
        windSpeed = findViewById(R.id.windSpeed);
        windDeg =  findViewById(R.id.windDeg);
        sunset = findViewById(R.id.sunset);
        sunrise = findViewById(R.id.sunrise);
        tempMax = findViewById(R.id.tempMax);
        tempMin = findViewById(R.id.tempMin);
        search = findViewById(R.id.search);
        backGroundFrame = findViewById(R.id.content_frame);
        likeImageView = findViewById(R.id.like);



        sunsetTime = findViewById(R.id.sunsetTime);
        sunriseTime = findViewById(R.id.sunriseTime);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        locationButton = findViewById(R.id.fab);


        mAuth = FirebaseAuth.getInstance();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);








        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_dehaze);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        emaiId =  headerView.findViewById(R.id.user_email_id);
        userImage = headerView.findViewById(R.id.user_image);
        navigationHeaderContainer =  headerView.findViewById(R.id.navigation_header_container);

        Menu menu = navigationView.getMenu();
         signInMenu = menu.findItem(R.id.sign_in_menu);
         signOutMenu = menu.findItem(R.id.sign_out_menu);
        favouriteCity = menu.findItem(R.id.favourite_cities);



        Glide.with(MainActivity.this).load(R.drawable.navbar_logo).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                navigationHeaderContainer.setBackground(resource);
            }


        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        switch (menuItem.getItemId()) {
                            case R.id.sign_in_menu:
                                Intent intent =  new Intent(MainActivity.this, SignInActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.sign_out_menu:
                                FirebaseAuth.getInstance().signOut();
                                signInMenu.setVisible(true);
                                signOutMenu.setVisible(false);
                                userName.setText(R.string.nav_header_title);
                                emaiId.setText(R.string.nav_header_subtitle);
                                favouriteCity.setVisible(false);
                                userImage.setImageResource(R.mipmap.ic_launcher_round);
                                likeImageView.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(mDrawerLayout, "Signout Successful", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                View snackbarView = snackbar.getView();

                                int snackbarTextId = android.support.design.R.id.snackbar_text;
                                TextView textView = snackbarView.findViewById(snackbarTextId);
                                textView.setBackgroundColor(getResources().getColor(R.color.snackbar_backgroud));
                                textView.setTextColor(getResources().getColor(R.color.snackbar_text));

                                snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_backgroud));
                                snackbar.show();

                                break;

                            case R.id.favourite_cities:
                                Intent newIntent =  new Intent(MainActivity.this, FavourtieLocationsActivity.class);
                                startActivity(newIntent);
                                break;

                            default:
                               break;
                        }
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        Typeface typefaceTemp = Typeface.createFromAsset(getAssets(), "fonts/SFLight.ttf");
        temp.setTypeface(typefaceTemp);

        Typeface typefaceWindSpeed = Typeface.createFromAsset(getAssets(), "fonts/SFBold.ttf");
        windSpeed.setTypeface(typefaceWindSpeed);

        Typeface typefacePress = Typeface.createFromAsset(getAssets(), "fonts/SFBold.ttf");
        press.setTypeface(typefacePress);

        Typeface typefaceHum = Typeface.createFromAsset(getAssets(), "fonts/SFBold.ttf");
        hum.setTypeface(typefaceHum);

        Typeface typefaceWindDeg = Typeface.createFromAsset(getAssets(), "fonts/SFBold.ttf");
        windDeg.setTypeface(typefaceWindDeg);

        Typeface typefaceCityText = Typeface.createFromAsset(getAssets(), "fonts/SFLight.ttf");
        cityText.setTypeface(typefaceCityText);

        Typeface typefaceCondDesc = Typeface.createFromAsset(getAssets(), "fonts/SFLight.ttf");
        condDescr.setTypeface(typefaceCondDesc);




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogFragment dialogFragment = SearchDialogFragment.newInstance("a","a");
                dialogFragment.show(ft, "dialog_fragment");


            }
        });

        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(MainActivity.this, FireBaseDatabaseService.class);
                intent.putExtra("cityname", city);
                startService(intent);

            }
        });


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                } else {
                    // permission has been granted, continue as usual
                    mFusedLocationClient.getLastLocation()
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (task.isSuccessful()) {
                                        Location location = task.getResult();
                                        if(location != null){
                                            mLastLocation = location;
                                            startIntentService();
                                        }

                                    }
                                    else {
                                        Exception exception = task.getException();
                                        Log.d("google", "location is null");
                                    }
                                }
                            });
                }

            }
        });




        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new OpenWeatherService()).getWeatherData(params[0]));

            if (data != null) {
                try {
                    weather = JSONParser.getWeather(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if(weather.location != null) {

                cityText.setText(weather.location.getCity() + ", " + weather.location.getCountry());
                condDescr.setText(weather.currentCondition.getCondition());
                temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
                hum.setText("" + weather.currentCondition.getHumidity() + " %");
                press.setText("" + weather.currentCondition.getPressure() + " hPa");
                windSpeed.setText("" + weather.wind.getSpeed() + " mps");
                windDeg.setText("" + weather.wind.getDeg() + "°");
                sunsetTime.setText(dataParser.unixTimeParse(weather.location.getSunset()));
                sunriseTime.setText(dataParser.unixTimeParse(weather.location.getSunrise()));
                tempMax.setText(String.valueOf(dataParser.tempParse(weather.temperature.getMaxTemp())) + "°C");
                tempMin.setText(String.valueOf(dataParser.tempParse(weather.temperature.getMinTemp())) + "°C");

                String condition = weather.currentCondition.getCondition();
                switch (condition) {

                    case "Clear":
                        Glide.with(MainActivity.this).load(R.drawable.clear1).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;
                    case "Rain":
                        Glide.with(MainActivity.this).load(R.drawable.rain).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;

                    case "Clouds":
                        Glide.with(MainActivity.this).load(R.drawable.cloudy).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;

                    case "Mist":
                        Glide.with(MainActivity.this).load(R.drawable.mist).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;

                    case "Fog":
                        Glide.with(MainActivity.this).load(R.drawable.fog).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;
                    case "Haze":
                        Glide.with(MainActivity.this).load(R.drawable.haze1).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;

                    case "Sunny":
                        Glide.with(MainActivity.this).load(R.drawable.sunny).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;
                    case "Thunderstorm":
                        Glide.with(MainActivity.this).load(R.drawable.thunderstorm).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                backGroundFrame.setBackground(resource);
                            }


                        });
                        break;

                        default:
                            Glide.with(MainActivity.this).load(R.drawable.clear1).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                    backGroundFrame.setBackground(resource);
                                }


                            });

                }

            }

            else {
                AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);

                builder.setTitle("City not found")
                        .setMessage("Please try again beacuse there was some error in finding this city")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }



        }
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }


    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
        Toast.makeText(this, "gdg", Toast.LENGTH_SHORT).show();
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }


            String adminArea = resultData.getString(Constants.RESULT_DATA_KEY);
            if (adminArea != null) {

                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{adminArea});
            }
            else  {
                String city = "boston";
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{String.valueOf(city)});
            }

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {

            }


        }
    }


    }
