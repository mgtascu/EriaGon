package licenta.mgtascu.eriagon;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;


import java.util.ArrayList;


public class MapActivity extends FragmentActivity
        implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener {


    private GoogleMap mMap;
    private ArrayList<LatLng> arrayList = new ArrayList<>(100);
    private boolean isClicked = false;
    Polygon polygon;
    Button logOut, btnInfo;
    private FirebaseAuth firebaseAuth;
    private LatLng cluj = new LatLng(46.7712, 23.6236);
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(this, "Press the help button (i) fore more information", Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LogIn.class));
        }

        logOut = findViewById(R.id.btnLogout);
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Help.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });
        spinner = findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.map_tpypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (i == 1) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (i == 2) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (i == 3) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluj, 13));
        setUpMap();

    }

    private void setUpMap() {

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnPolygonClickListener(this);

    }

    public void onMapClick(final LatLng latLng) {

        if (isClicked == false) {

            mMap.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title(String.format("%.3f", latLng.latitude) + "," + String.format("%.3f", latLng.longitude)));
            arrayList.add(latLng);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.remove();
                arrayList.remove(latLng);

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }


    public void onPolygonClick(Polygon polygon) {

        double area = SphericalUtil.computeArea(arrayList);
        Toast.makeText(this, String.format("Polygon area = %.2f", area) + " square meters", Toast.LENGTH_LONG).show();
    }


    public void onMapLongClick(LatLng latLng) {

        mMap.clear();
        arrayList.clear();
        isClicked = false;
    }

    public void polygonPoints() {

        if (arrayList.size() >= 3) {
            isClicked = true;
            polygon = mMap.addPolygon(new PolygonOptions()
                    .clickable(true)
                    .addAll(arrayList)
                    .strokeColor(Color.CYAN)
                    .strokeWidth(3)
                    .fillColor(Color.argb(127, 255, 0, 0)));

        }
    }

    public boolean onMarkerClick(Marker marker) {

        if (arrayList.get(0).equals(marker.getPosition())) {
            polygonPoints();
        }
        return false;
    }
}

