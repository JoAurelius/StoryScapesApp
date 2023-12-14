package com.kampus.storyscapes.activity

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.kampus.storyscapes.R
import com.kampus.storyscapes.databinding.ActivityMapBinding
import com.kampus.storyscapes.model.TokenPreferences
import com.kampus.storyscapes.viewmodels.MapsViewModel
import com.kampus.storyscapes.viewmodels.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMapBinding

    private lateinit var mapsViewModel : MapsViewModel
    private lateinit var factory : ViewModelProvider.Factory

    private lateinit var googleMap : GoogleMap

    private val boundsBuilder: LatLngBounds.Builder = LatLngBounds.Builder()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var tokenPreferences : TokenPreferences

    private val mapCallbacks  = OnMapReadyCallback {googleMap ->
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        setupViewmodel()
        mapsViewModel.getToken().observe(this) {
            addMarkers(token = it)
        }
    }

    private fun addMarkers(token : String)
    {
        mapsViewModel.getStoriesWithLocation(token).observe(this) {listStory ->
            listStory?.forEach { story ->
                val latLng = LatLng(story.lat as Double, story.lon as Double)
                googleMap.addMarker(MarkerOptions().position(latLng))
                boundsBuilder.include(latLng)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(
                bounds,
                width,
                height,
                (width * 0.10).toInt()
            )
            googleMap.animateCamera(cameraUpdateFactory)
        }
    }

    private fun permissionHandler() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it) {
                getClientLocation()
            }
        }
    }
                private fun setupViewmodel()
    {
        factory = ViewModelFactory(token = tokenPreferences)
        mapsViewModel = ViewModelProvider(this, factory).get(MapsViewModel::class.java)
    }
    private fun setMapStyle()
    {
        try {
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style))
            if(!success) Toast.makeText(this, "Error while parsing style", Toast.LENGTH_SHORT).show()
        } catch (resourceException : Resources.NotFoundException)
        {
            Toast.makeText(this, "Can't Find Style", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getClientLocation()
    {
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            if(this::googleMap.isInitialized) {
                googleMap.isMyLocationEnabled = true
            }
        }
        else {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionHandler()
        tokenPreferences =  TokenPreferences(this)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(mapCallbacks)
    }
}