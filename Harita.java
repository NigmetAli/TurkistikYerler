package nmt.turkistikyerler;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by NigmetAli on 14.05.2016.
 */
public class Harita extends FragmentActivity implements OnMapReadyCallback{


    konumCek konumcek;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harita_layout);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        konumcek = (konumCek) getApplicationContext();


        mMap = googleMap;
        LatLng konum = new LatLng(konumcek.getKonumX(), konumcek.getKonumY());
        mMap.addMarker(new MarkerOptions().position(konum).title(konumcek.getYerAdi()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(konum, (float) 10.5));
        mMap.setMyLocationEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);





    }
}
