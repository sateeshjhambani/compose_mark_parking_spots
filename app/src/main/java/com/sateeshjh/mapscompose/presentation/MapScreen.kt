package com.sateeshjh.mapscompose.presentation

import android.Manifest
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

@ExperimentalPermissionsApi
@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val lifecyclerOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecyclerOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecyclerOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecyclerOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(MapEvent.ToggleFalloutMap)
            }) {
                Icon(
                    imageVector = if (viewModel.state.isFalloutMap) {
                        Icons.Default.ToggleOff
                    } else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Fallout Map"
                )
            }
        }
    ) {
        if (permissionsState.allPermissionsGranted) {
            viewModel.state = viewModel.state.copy(
                properties = viewModel.state.properties.copy(
                    isMyLocationEnabled = true
                )
            )
        }

        GoogleMap(
            modifier  = Modifier.fillMaxSize(),
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            onMapLongClick = {
                viewModel.onEvent(MapEvent.OnMapLongClick(it))
            }
        ) {
            viewModel.state.parkingSpots.forEach { spot ->
                Marker(
                    position = LatLng(spot.lat, spot.lng),
                    title = "parking Spot (${spot.lat}, ${spot.lng})",
                    snippet = "Long click to delete",
                    onInfoWindowLongClick = {
                        viewModel.onEvent(MapEvent.OnInfoWindowLongClick(spot = spot))
                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            }
        }
    }
}