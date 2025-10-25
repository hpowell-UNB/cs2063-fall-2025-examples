package mobiledev.unb.ca.locationproviderdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {
    private lateinit var locationTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        locationTextView = findViewById(R.id.locationTextView)
        val locationButton: Button = findViewById(R.id.locationButton)
        locationButton.setOnClickListener { lastLocation }

        // Initialize an instance of the location client
        initLocationClient()
    }

    // Permissions Checking
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle the user's response to the permission request
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                Log.i(TAG, "Permission granted")
                lastLocation
            } else {
                Log.w(TAG, "Permission denied")
                showToast(getString(R.string.permission_denied))
            }
        }

    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        lastLocation
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestLocationPermissions()
            }
            else -> {
                // Request location permissions
                requestLocationPermissions()
            }
        }
    }

    /**
     * Checks to see if the user has turned on location from Settings
     * @return The location manager object
     */
    private val isLocationEnabled: Boolean
        get() {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    // Get last known location. In some rare situations this can be null.
    private val lastLocation: Unit
        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        get() {
            checkLocationPermissions()
            if (isLocationEnabled) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(this) { lastLocation: Location? ->
                        lastLocation?.let {
                            setTextViewDisplay(it)
                        } ?: run {
                            locationTextView.text = getString(R.string.fetch_location_error)
                            requestNewLocationData()
                        }
                    }
            } else {
                showToast("Please turn location services on")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }

    private fun setTextViewDisplay(location: Location) {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()
        val accuracy = location.accuracy.toString()
        val text = getString(R.string.location_details, latitude, longitude, accuracy)
        locationTextView.text = text
    }

    private fun showToast(text: String) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST) { // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "onRequestPermissionsResult: Granted")
                lastLocation
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Denied")
                showToast("Permission request denied")
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(UPDATE_INTERVAL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(100)
            .setMaxUpdateDelayMillis(100)
            .build()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { setTextViewDisplay(it) }
        }
    }

    companion object {
        private const val TAG = "TAG"
        private const val UPDATE_INTERVAL: Long = 60 * 60 * 1000
        private const val LOCATION_REQUEST = 101
    }
}