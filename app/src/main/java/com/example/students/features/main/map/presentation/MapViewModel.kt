package com.example.students.features.main.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.domain.LocationUseCase
import com.example.students.features.main.map.presentation.model.CreatePointState
import com.example.students.features.main.map.presentation.model.LocationsNearby
import com.example.students.features.main.map.presentation.model.MapLocations
import com.example.students.utils.Resource
import com.example.students.utils.SingleLiveEvent
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val locationUseCase: LocationUseCase,
) : ViewModel() {

    private val bankMapObjectsLiveData =
        SingleLiveEvent<Resource<MutableList<MapLocations.MapObject>>>()

    private val _createState: MutableSharedFlow<CreatePointState> =
        MutableSharedFlow()
    val createState: SharedFlow<CreatePointState> = _createState

    var createLatitude: Double = 0.0
    var createLongitude: Double = 0.0

    fun createPoint(lat: Double, long: Double) {
        createLatitude = lat
        createLongitude = long
    }

    /**
     * поискк в коллекции ключ/значение  внутри bankMapObjectsLiveData  обьекта MapObject по его id
     * осуществляется по клику на обьект карты или списка
     */
    fun getBankMapObjectById(idBankMapObject: Long): MapLocations.MapObject? {
        bankMapObjectsLiveData.value?.data?.map {
            if (it.id == idBankMapObject) return it
        }
        return null
    }

    var type: String = MapLocations.Type.ALL.value

    fun getBankMapObjectsLiveData(): SingleLiveEvent<Resource<MutableList<MapLocations.MapObject>>> =
        bankMapObjectsLiveData

    /**
     * загрузка обьктов вокруг точки
     */
    fun loadBankObjectsLocations(
        point: Point,
        userPoint: Point,
        range: Double,
        isUserLocation: Boolean,
        hasLocationPermissions: Boolean,
    ) {
        viewModelScope.launch {
            bankMapObjectsLiveData.value = Resource.loading()

            val result = locationUseCase.getNearbyLocations(
                type = type,
                range = range,
                latitude = point.latitude,
                longitude = point.longitude
            )
            when (result.state) {
                Resource.State.SUCCESS -> bankMapObjectsLiveData.value = Resource.success(
                    prepareMapPointers(
                        result.data!!, isUserLocation, userPoint, hasLocationPermissions
                    )
                )
                Resource.State.ERROR -> bankMapObjectsLiveData.value = Resource.error(
                    result.error, Resource.Source.SERVER,
//                    prepareMapPointers(
//                        result.data!!, isUserLocation, userPoint, hasLocationPermissions
//                    )
                )
                else -> {}
            }

        }
    }

    fun createMapPoint(request: CreatePointRequest) {
        viewModelScope.launch {
            try {
                _createState.emit(CreatePointState.Loading)
                withContext(Dispatchers.IO) {
                    val result = locationUseCase.createPost(
                        request
                    )
                    if (result.data != null) {
                        _createState.emit(CreatePointState.PointCreated)
                    } else {
                        _createState.emit(CreatePointState.Error)
                    }
                }
            } catch (e: Exception) {
                _createState.emit(CreatePointState.Error)
            }
        }
    }

    //    PRIVATE
    private fun prepareMapPointers(
        result: LocationsNearby.Ui.NearbyLocation,
        isUserLocation: Boolean,
        userPoint: Point,
        hasLocationPermissions: Boolean,
    ): MutableList<MapLocations.MapObject> {
        val listLocation: MutableList<MapLocations.MapObject> = mutableListOf()

        val friendsList: MutableList<MapLocations.MapObject> = mutableListOf()
        val notesList: MutableList<MapLocations.MapObject> = mutableListOf()


        result.friends.map {
            convertNearby2FriendObject(
                it
            )
        }.forEach { item -> friendsList.add(item) }

        result.notes.map {
            convertNearby2NoteObject(
                it
            )
        }.forEach { item -> notesList.add(item) }

        return if (hasLocationPermissions) {
            listLocation.apply {
                addAll(friendsList)
                addAll(notesList)
            }

        } else {
            val sortedListFriends = friendsList.sortedWith(compareBy(MapLocations.MapObject::name))
            val sortedListNotes = notesList.sortedWith(compareBy(MapLocations.MapObject::name))
            listLocation.apply {
                addAll(sortedListFriends)
                addAll(sortedListNotes)
            }
            listLocation
        }
    }

    private fun convertNearby2FriendObject(
        locationsNearby: LocationsNearby.Ui.Friend?,
    ): MapLocations.MapObject {
        return MapLocations.MapObject(
            latitude = locationsNearby?.latitude ?: 43.25654,
            longitude = locationsNearby?.longitude ?: 76.92848,
            type = MapLocations.Type.Friend,
            id = locationsNearby?.id ?: 0,
            name = locationsNearby?.name ?: "",
            address = locationsNearby?.address ?: "",
            userId = locationsNearby?.userId ?: 0,
            distance = locationsNearby?.distance ?: 0.0
        )
    }

    private fun convertNearby2NoteObject(
        locationsNearby: LocationsNearby.Ui.Note?,
    ): MapLocations.MapObject {
        return MapLocations.MapObject(
            latitude = locationsNearby?.latitude ?: 43.25654,
            longitude = locationsNearby?.longitude ?: 76.92848,
            type = MapLocations.Type.Note,
            id = locationsNearby?.id ?: 0,
            name = locationsNearby?.name ?: "",
            address = locationsNearby?.address ?: "",
            userId = locationsNearby?.userId ?: 0,
            description = locationsNearby?.placeDescription ?: "",
            distance = locationsNearby?.distance ?: 0.0
        )
    }
}