package com.sateeshjh.mapscompose.presentation

import com.google.android.gms.maps.model.LatLng
import com.sateeshjh.mapscompose.domain.model.ParkingSpot

sealed class MapEvent {
    object ToggleFalloutMap: MapEvent()
    data class OnMapLongClick(val latlng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val spot: ParkingSpot): MapEvent()
}
