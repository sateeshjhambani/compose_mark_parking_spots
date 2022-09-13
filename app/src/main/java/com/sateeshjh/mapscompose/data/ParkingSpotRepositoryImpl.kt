package com.sateeshjh.mapscompose.data

import com.sateeshjh.mapscompose.domain.model.ParkingSpot
import com.sateeshjh.mapscompose.domain.repository.ParkingSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParkingSpotRepositoryImpl(
    private val dao: ParkingSpotDao
): ParkingSpotRepository {
    override suspend fun insertParkingSpot(spot: ParkingSpot) {
        dao.insertParkingSpot(spot.toParkingSpotEntity())
    }

    override suspend fun deleteParkingSpot(spot: ParkingSpot) {
        dao.deleteParkingSpot(spot.toParkingSpotEntity())
    }

    override fun getParkingSpots(): Flow<List<ParkingSpot>> {
        return dao.getParkingSpots().map {
            it.map { it.toParkingSpot() }
        }
    }
}