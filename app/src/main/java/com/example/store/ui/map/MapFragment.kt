package com.example.store.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentMapBinding
import com.example.store.model.Address
import com.example.store.ui.customer.CustomerViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    var navigatedFromCInfo=false
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentMarker: Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if (currentMarker != null) {
                    currentMarker?.remove()
                }
                val newLatLong = LatLng(p0.position.latitude, p0.position.longitude)
                showLocationOnMap(newLatLong)
            }

            override fun onMarkerDragStart(p0: Marker) {
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           navigatedFromCInfo=it.getBoolean("navigatedFromCustomerInfo",false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.btnAddAddressMap.setOnClickListener {
            val addressLocationStr = mapViewModel.getAddressFromLatLang(
                currentMarker?.position?.latitude.toString(),
                currentMarker?.position?.longitude.toString(), requireContext()
            )
            Toast.makeText(requireContext(), addressLocationStr, Toast.LENGTH_SHORT).show()
            /*val address = Address(
                "",
                addressLocationStr,
                currentMarker?.position?.latitude.toString(),
                currentMarker?.position?.longitude.toString()
            )*/
            val action=MapFragmentDirections.actionMapFragmentToAddAddressFragment(addressLocationStr,navigatedFromCInfo)
            findNavController().navigate(action)
        }

        binding.btnCancelMap.setOnClickListener {
            val action=MapFragmentDirections.actionMapFragmentToAddAddressFragment("",navigatedFromCInfo)
            findNavController().navigate(action)
        }
    }

    private fun getPermission() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا دسترسی به لوکیشن را فعال کنید",
                    Toast.LENGTH_SHORT
                ).show()

                val uri: Uri = Uri.fromParts(
                    "package",
                    activity?.applicationContext?.packageName,
                    null
                )
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = uri
                //"Navigate to address fragment")
            }

        }
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    showLocationOnMap(LatLng(it.latitude, it.longitude))
                }
            }
    }

    fun showLocationOnMap(latLng: LatLng) {
        map.setMinZoomPreference(2.0f)
        map.setMaxZoomPreference(18.0f)
        map.cameraPosition.zoom
        val markerOption =
            MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("شما اینجا هستید")
                .zIndex(2.0f)
                .draggable(true)

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.0f))
        currentMarker = map.addMarker(markerOption)
        currentMarker?.showInfoWindow()

    }
}
