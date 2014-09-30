package com.josh.cs_4401app;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InfoGeneralFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, View.OnClickListener {

    public EditText outputDateView, outputFOI1, outputFOI2, outputMunicipality;
    public TextView countyID, selectedDistrictView, siteCoordsView, siteIDView;
    public Spinner districtSpinner, countySpinner;
    public int selectedCounty, selectedDistrict, siteNum = 0;
    public String siteID, outputDate, fi1, fi2, districtName, countyName, municipalityName, siteCoords, ARG_outputDate,
            ARG_fi1, ARG_fi2, ARG_districtName, ARG_countyName, ARG_municipalityName, ARG_siteID, ARG_siteCoords, ARG_bundleName;
    private GoogleMap mMap;
    private Double latitude, longitude;
    private double siteLat, siteLong;
    private ScrollView mScrollView;
    private LocationManager mLocManager = null;
    private LocationListener mLocListener = null;
    LocationClient mLocationClient;
    Location mLastLocation, takenLocation;
    Marker siteMarker;
    List<Marker> markerList;
    List<String> siteIDList;
    View v;

    Button getLocationButton, newSiteButton;


    boolean mUpdatesRequested;

    //-------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_gen_info, container, false);

        setup();

        return v;
    }

    public void setup(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");


        latitude = 40.457385; // set home marker for calendar to BKSQ
        longitude = -79.916863;

        setUpMapIfNeeded(); // For setting up the MapFragment

        markerList = new ArrayList<Marker>();

        //------ setting up variables/fields
        outputDateView = (EditText) v.findViewById(R.id.outputDateView);
        outputFOI1 = (EditText) v.findViewById(R.id.outputFOI1);
        outputFOI2 = (EditText) v.findViewById(R.id.outputFOI2);
        districtSpinner = (Spinner) v.findViewById(R.id.districtSpinner);
        countySpinner = (Spinner) v.findViewById(R.id.countySpinner);
        outputMunicipality = (EditText) v.findViewById(R.id.municipalityName);
        countyID = (TextView) v.findViewById(R.id.countyID);
        mScrollView = (ScrollView) v.findViewById(R.id.genScroll);
        getLocationButton = (Button) v.findViewById(R.id.getLocationButton);
        siteCoordsView = (TextView) v.findViewById(R.id.siteCoords);
        siteIDView = (TextView) v.findViewById(R.id.siteID);
        newSiteButton = (Button) v.findViewById(R.id.newSiteButton);
        outputDateView.setText(sdf.format(new Date()));
        newSiteButton.setOnClickListener(this);

        //------ location services
        getLocationButton.setOnClickListener(this);
        mLocationClient = new LocationClient(getActivity(), this, this);
        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        String providerName = mLocManager.getBestProvider(locationCriteria, true);
        if (providerName != null)
            mLastLocation = mLocManager.getLastKnownLocation(providerName);

        markerRequest = (TextView) v.findViewById(R.id.markerRequest);
        markerIDView = (TextView) v.findViewById(R.id.markerID);
        markerRequest.setText(String.valueOf(newMarkerRequested));
        markerIDView.setText(markerID);




    }

    public void main() {
        districtSpinner();
        countySpinner(selectedDistrict);

    }

    public void setParams() {
        SimpleDateFormat nodashsdf = new SimpleDateFormat("ddMMyyyy");

        String bundleName = siteID + "Args";
        Bundle siteIDArgs = new Bundle();
        outputDate = nodashsdf.format(new Date());
        ARG_bundleName = "bundle identifier";
        ARG_outputDate = "output date";
        ARG_fi1 = "field investigator 1";
        ARG_fi2 = "field investigator 2";
        ARG_districtName = "district";
        ARG_countyName = "county";
        ARG_municipalityName = "municipality";
        ARG_siteID = "site ID";
        ARG_siteCoords = "GPS coordinates";

        siteIDArgs.putString(ARG_bundleName, bundleName);
        siteIDArgs.putString(ARG_outputDate, outputDate);
        siteIDArgs.putString(ARG_fi1, fi1);
        siteIDArgs.putString(ARG_fi2, fi2);
        siteIDArgs.putString(ARG_districtName, districtName);
        siteIDArgs.putString(ARG_countyName, countyName);
        siteIDArgs.putString(ARG_municipalityName, municipalityName);
        siteIDArgs.putString(ARG_siteID, siteID);
        siteIDArgs.putString(ARG_siteCoords, siteCoords);

        this.setArguments(siteIDArgs);

    }


    int newMarkerRequested = 0;
    String markerID = "";
    TextView markerRequest, markerIDView;

    @Override
    //------ location services/map settings
    public void onClick(View v) {

        if (v == newSiteButton) {

            siteNum = siteNum+1;
            SimpleDateFormat nodashsdf = new SimpleDateFormat("MMddyyyy");
            siteID = (nodashsdf.format(new Date())+ String.valueOf(siteNum));
            //siteIDList.add(siteID);
            siteIDView.setText(siteID);
            newMarkerRequested = 1;
            markerRequest.setText(String.valueOf(newMarkerRequested));

        }
        else if (v == getLocationButton) {
            mLocListener = new MyLocationListener();
            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
            siteCoordsView.setText(String.valueOf(siteLat) + ", " + String.valueOf(siteLong));
            mMap.getMyLocation();
            LatLng siteCoordRaw = new LatLng(siteLat, siteLong);
            siteCoords = siteCoordRaw.toString();


                siteMarker = mMap.addMarker(new MarkerOptions().position(siteCoordRaw));
                siteMarker.setTitle(siteID);
                markerID = siteMarker.getId();
                markerList.add(siteMarker);




            CameraUpdate center = CameraUpdateFactory.newLatLng(siteCoordRaw);
            mMap.moveCamera(center);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            newMarkerRequested = 0;
        }
    }

    //------ district selection
    public void districtSpinner() {

        ArrayAdapter<String> Dadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.districtNames));
        Dadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        districtSpinner.setAdapter(Dadapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                          int selectedDistrict = position + 1;

                                                          countySpinner(selectedDistrict);
                                                      }

                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }
        );
    }

    private int getDistrictResourceId(int districtnr) {
        int resId = R.array.district1;
        switch (districtnr) {
            case 1:
                resId = R.array.district1;
                districtName = "01";
                break;
            case 2:
                resId = R.array.district2;
                districtName = "02";
                break;
            case 3:
                resId = R.array.district3;
                districtName = "03";
                break;
            case 4:
                resId = R.array.district4;
                districtName = "04";
                break;
            case 5:
                resId = R.array.district5;
                districtName = "05";

                break;
            case 6:
                resId = R.array.district6;
                districtName = "06";

                break;
            case 7:
                resId = R.array.district8;
                districtName = "08";

                break;
            case 8:
                resId = R.array.district9;
                districtName = "09";

                break;
            case 9:
                resId = R.array.district10;
                districtName = "10";

                break;
            case 10:
                resId = R.array.district11;
                districtName = "11";

                break;
            case 11:
                resId = R.array.district12;
                districtName = "12";

                break;


        }

        return resId;
    }

    public void countySpinner(int districtnr) {

        int resId = getDistrictResourceId(districtnr);

        ArrayAdapter<String> Cadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(resId));
        Cadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        countySpinner.setAdapter(Cadapter);

        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    }

                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                    }
                                                }
        );
    }

    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((WorkaroundMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();

        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);

        ((WorkaroundMapFragment) getFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

    }

    private void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 12.0f));
        mMap.clear();

    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            if (loc.getLatitude() == 0.0 && loc.getLongitude() == 0.0) {
                takenLocation = mLastLocation;
            } else if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
                takenLocation = loc;
            }
            siteLat = takenLocation.getLatitude();
            siteLong = takenLocation.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getActivity(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getActivity(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (mMap != null)
            setUpMap();

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) Main.fragmentManager
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            Main.fragmentManager.beginTransaction()
                    .remove(Main.fragmentManager.findFragmentById(R.id.map)).commit();
            mMap = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
        main();
    }

    @Override
    public void onStop() {

        mLocationClient.disconnect();
        super.onStop();

    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
        }
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
        super.onResume();
    }


}



