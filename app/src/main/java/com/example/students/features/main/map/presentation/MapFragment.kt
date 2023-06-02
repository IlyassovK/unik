package com.example.students.features.main.map.presentation

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import com.example.students.databinding.FragmentMapBinding
import com.example.students.features.main.map.BottomDialog
import com.example.students.features.main.map.CreatePointBottomDialog
import com.example.students.features.main.map.presentation.model.MapLocations
import com.example.students.features.map.RegionItem
import com.example.students.features.map.Regions
import com.example.students.utils.PermissionsUtils
import com.example.students.utils.Resource
import com.example.students.utils.setSafeOnClickListener
import com.example.students.utils.ui.ToggleOnlyProgrammaticallyWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObjectCollection
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geo
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MapFragment : Fragment(), UserLocationObjectListener, ClusterListener,
    ClusterTapListener, CameraListener, MapObjectsAdapter.ItemClickListener, InputListener {

    private lateinit var binding: FragmentMapBinding

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by sharedViewModel()

    private lateinit var locationManager: com.yandex.mapkit.location.LocationManager
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var yandexMapView: MapView

    private var locationPoint: Point = Regions.KZ_REGION_POINT
    private var userPoint: Point = Regions.KZ_REGION_POINT
    private var locationRadius: Double = 1.0

    lateinit var clusterizedCollection: ClusterizedPlacemarkCollection


    private var isList: Boolean = false
    private var shouldUserLocationLoad: Boolean = false
    private var shouldReloadButtonShow: Boolean = false
    private var isUserLocationSetup = false
    private var isUserLocation: Boolean = false
    private var hasLocationPermissions: Boolean = false

    private lateinit var recyclerViewAdapter: RecyclerView.Adapter<MapObjectsAdapter.ViewHolder>


    private lateinit var filter: Filter
    private lateinit var toggleAll: ToggleOnlyProgrammaticallyWrapper
    private lateinit var toggleFriend: ToggleOnlyProgrammaticallyWrapper
    private lateinit var toggleNotes: ToggleOnlyProgrammaticallyWrapper


    private val loadingMapObjectsObserver =
        Observer<Resource<MutableList<MapLocations.MapObject>>> {
            when (it?.state) {
                Resource.State.LOADING -> {

                }
                Resource.State.SUCCESS -> {
                    it.data?.let {
                        addMapObjects()
                    }
                }
                Resource.State.ERROR -> {
                    it.data?.let {
                        addMapObjects()
                    }
                }
                else -> {}
            }
        }

    private val mapBankObjectTapListener = MapObjectTapListener { objectMap, point ->
        point
        val mapObject = viewModel.getBankMapObjectById(objectMap.userData as Long)
        showMapObjectDetails(mapObject, false)
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val viewManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter =
            MapObjectsAdapter(
                requireContext()
            )
        (recyclerViewAdapter as MapObjectsAdapter).setClickListener(this)
        val itemDecorator =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_map_object_list
            )!!
        )
        binding.apply {

            mapview.map.addCameraListener(this@MapFragment)

            bankMapObjectListRv.apply {
                setHasFixedSize(true)
                addItemDecoration(itemDecorator)
                layoutManager = viewManager
                adapter = recyclerViewAdapter
            }

            mapViewCompassBtn.setSafeOnClickListener {
                compassMap()
            }

            mapViewCentredBtn.setSafeOnClickListener {
                if (!PermissionsUtils.isSystemLocationEnabled(requireContext())) {
                    PermissionsUtils.showLocationDeniedDialog(requireContext(),
                        null,
                        DialogInterface.OnClickListener { _, _ -> })
                } else if (!PermissionsUtils.isSystemLocationServiceEnabled(requireContext())) {
                    PermissionsUtils.checkSystemLocationServiceWithDialogInLocation(requireContext(),
                        null,
                        DialogInterface.OnClickListener { _, _ -> })
                } else if (!isUserLocationSetup) {
                    setupUserLocation()
                } else {
                    if (!isUserLocation) {
                        isUserLocation = true
                    }
                    moveCameraToUserLocation()
                    reloadObjectsLocationsOnUserPoint()
//                    reloadObjectsBtn.visibility = View.GONE
                }
            }

//            reloadObjectsBtn.setSafeOnClickListener {
//                isUserLocation = false
//                reloadObjectsLocations()
//                reloadObjectsBtn.visibility = View.GONE
//            }

            branchTitleTgl.setOnCheckedChangeListener { _, ifList ->
                showBankObjectsAs(ifList)
                isList = ifList
                verifyList()
            }
        }

        locationManager = MapKitFactory.getInstance().createLocationManager()
        userLocationLayer =
            MapKitFactory.getInstance().createUserLocationLayer(binding.mapview.mapWindow)

        clusterizedCollection =
            binding.mapview.map.mapObjects.addClusterizedPlacemarkCollection(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleAll = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeAll)
        toggleFriend = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeFriends)
        toggleNotes = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeNotes)

        filter = Filter(
            allToggle = toggleAll,
            friendsToggle = toggleFriend,
            notesToggle = toggleNotes,
        )

        viewModel.getBankMapObjectsLiveData().observe(this, loadingMapObjectsObserver)

        setElementActionsListeners()

        checkStartPermissions()

        binding.addPointBtn.visibility = View.GONE
        binding.addPointBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_mapFragment_to_createPointBottomDialog
            )
        }

        binding.mapview.map.addInputListener(this)


//        binding.mapview.map.addTapListener {
//            val point = it.geoObject.geometry[0].point
//
//            viewModel.createPoint(
//                lat = point?.latitude ?: 0.0,
//                long = point?.longitude ?: 0.0
//            )
//
////            if (point != null) {
////                val bundle = Bundle()
////                bundle.putDouble(CreatePointBottomDialog.CREATE_POINT_LAT, point.latitude)
////                bundle.putDouble(CreatePointBottomDialog.CREATE_POINT_LONG, point.longitude)
////                findNavController().navigate(
////                    R.id.action_mapFragment_to_createPointBottomDialog,
////                    bundle
////                )
////            }
//            true
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.getBankMapObjectsLiveData().removeObservers(this)
    }

    private fun checkStartPermissions() {
        if (PermissionsUtils.isSystemLocationServiceEnabled(requireContext())
            && PermissionsUtils.isSystemLocationEnabled(requireContext())
        ) {
            setupUserLocation()
        } else if (PermissionsUtils.isSystemLocationServiceEnabled(requireContext())
            && !PermissionsUtils.isSystemLocationEnabled(requireContext())
        ) {
            walkFromPermissions()
        } else {
//            prepareRegionApproveDialog()
        }
    }

    /**
     * проверка разрешения на определение локации
     * с запросом на вывод стандартного диалога
     */
    private fun walkFromPermissions() {
        if (!PermissionsUtils.allowPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            PermissionsUtils.requestPermissionsFromFragment(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_CODE_LOCATION
            )
        }
    }


    /**
     * слушатели вью элементов
     */
    private fun setElementActionsListeners() {
        setBankMapsObjectsFilterListeners()
    }

    /**
     * слушатели фитьтров
     */
    private fun setBankMapsObjectsFilterListeners() {
        toggleAll.setListener { _, _ ->
            onFilterAllClick()
        }
        toggleFriend.setListener { _, _ ->
            onFilterFriendsClick()
        }
        toggleNotes.setListener { _, _ ->
            onFilterNotesClick()
        }
    }

    /**
     * слушатели фитьтра всех типов обьектов
     */
    private fun onFilterAllClick() {
        filter.toggleAll()
        clearBankObjectsList()
        addMapObjects()
    }

    /**
     * слушатели фитьтра банкоматов
     */
    private fun onFilterFriendsClick() {
        filter.toggleFriends()
        clearBankObjectsList()
        addMapObjects()
    }

    /**
     * слушатели фитьтра отделений
     */
    private fun onFilterNotesClick() {
        filter.toggleNotes()
        clearBankObjectsList()
        addMapObjects()
    }


    /**
     * manage change visibility between map and list of bank objects
     *
     * @param list - {true} if list
     */
    private fun showBankObjectsAs(list: Boolean) {
        binding.apply {
            if (list) {
                MapKitFactory.getInstance().onStop()
                mapview.visibility = View.GONE
                mapViewCentredBtn.visibility = View.GONE
                mapViewCompassBtn.visibility = View.GONE
                bankMapObjectListRv.visibility = View.VISIBLE
//                reloadObjectsBtn.visibility = View.GONE
            } else {
                MapKitFactory.getInstance().onStart()
                mapview.visibility = View.VISIBLE
                mapViewCentredBtn.visibility = View.VISIBLE
                mapViewCompassBtn.visibility = View.VISIBLE
                bankMapObjectListRv.visibility = View.GONE
                binding.mapListEmptyLayout.visibility = View.GONE
//                if (shouldReloadButtonShow)
//                    reloadObjectsBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun setupUserLocation() {
        if (PermissionsUtils.checkComplexLocationPermissions(requireActivity())) {

            isUserLocation = true
            isUserLocationSetup = true
            hasLocationPermissions = true

            userLocationLayer.isVisible = true
            userLocationLayer.isHeadingEnabled = true
            userLocationLayer.setObjectListener(this@MapFragment)

            locationManager.requestSingleUpdate(object : LocationListener {
                override fun onLocationUpdated(@NonNull location: Location) {
                    userPoint = location.position
                    moveCameraToUserLocation()
                    reloadObjectsLocationsOnUserPoint()
                }

                override fun onLocationStatusUpdated(locationStatus: LocationStatus) {

                }
            })

        }
    }

    private fun moveCameraToUserLocation() {
        userLocationLayer.cameraPosition()?.let {
            userPoint = it.target
            locationPoint = it.target
        }
        shouldUserLocationLoad = true
        binding.mapview.map.move(
            CameraPosition(userPoint, USER_LOCATION_ZOOM, 0f, 0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun reloadObjectsLocations() {
        val range = locationRadius * 1.4 / 1000
        viewModel.loadBankObjectsLocations(
            locationPoint,
            userPoint,
            range,
            isUserLocation,
            hasLocationPermissions
        )
        shouldReloadButtonShow = false
        shouldUserLocationLoad = false
    }

    private fun reloadObjectsLocationsOnUserPoint() {
        viewModel.loadBankObjectsLocations(
            userPoint,
            userPoint,
            DEFAULT_USER_LOCATION_RADIUS,
            isUserLocation,
            hasLocationPermissions
        )
        shouldReloadButtonShow = false
        shouldUserLocationLoad = false
    }

    /**
     * добавление маркеров по типу
     *
     * @param type тип обьекта
     */
    private fun addMapPointers(type: MapLocations.Type) {

        val listItems: MutableList<MapLocations.MapObject> = mutableListOf()

        // Note that application must retain strong references to both
        // cluster listener and cluster tap listener
        clusterizedCollection.clear()
        if (type == MapLocations.Type.ALL) {

            val tempList: MutableList<MapLocations.MapObject>? =
                viewModel.getBankMapObjectsLiveData().value?.data

            tempList?.let { listItems.addAll(it) }
            tempList?.map {
                fillObjectList(it)
            }

        } else {
            val tempList: MutableList<MapLocations.MapObject>? =
                viewModel.getBankMapObjectsLiveData().value?.data
            val filterList = tempList?.filter { mapObj ->
                mapObj.type == type
            }
            filterList?.let { listItems.addAll(it) }
            filterList?.map { entryItem ->
                fillObjectList(entryItem)
            }
        }
        addRecyclerElements(listItems)
        // Placemarks won't be displayed until this method is called. It must be also called
        // to force clusters update after collection change
        clusterizedCollection.clusterPlacemarks(35.0, 15)
    }

    private fun fillObjectList(
        bankObject: MapLocations.MapObject,
    ) {
        val place = clusterizedCollection.addPlacemark(
            Point(bankObject.latitude, bankObject.longitude),
            prepareImageProvider(bankObject.type),
            IconStyle()
        )
        place.userData = bankObject.id
        place.addTapListener(mapBankObjectTapListener)

    }

    private fun addMapObjects() {
        when {
            filter.isAllChecked() -> {
                addMapPointers(MapLocations.Type.ALL)
            }
            filter.isFriendsChecked() -> {
                addMapPointers(MapLocations.Type.Friend)
            }
            filter.isNotesChecked() -> {
                addMapPointers(MapLocations.Type.Note)
            }
        }
    }

    /**
     * добавление элементов в список recycler view
     */
    private fun addRecyclerElements(list: List<MapLocations.MapObject>) {
        (recyclerViewAdapter as MapObjectsAdapter).addListElements(list)
        verifyList()
    }

    /**
     * проверка наличия элементов в списке для отображения изображения пустого списка
     */
    private fun verifyList() {
        if ((recyclerViewAdapter as MapObjectsAdapter).getItems().isEmpty() && isList) {
            binding.mapListEmptyLayout.visibility = View.VISIBLE
        } else {
            binding.mapListEmptyLayout.visibility = View.GONE
        }
    }

    private fun moveCameraToLocationPoint() {
        moveCameraToLocationPoint(locationPoint, CHOOSED_LOCATION_ZOOM)
    }

    private fun moveCameraToLocationPoint(zoom: Float) {
        moveCameraToLocationPoint(locationPoint, zoom)
    }

    private fun moveCameraToLocationPoint(point: Point, zoom: Float) {
        binding.mapview.map.move(CameraPosition(point, zoom, 0.0F, 0.0F))
    }


    /**
     * чистка списка отображения
     */
    private fun clearBankObjectsList() {
        clusterizedCollection?.clear()
        (recyclerViewAdapter as MapObjectsAdapter)?.clearAllListElements()
    }


    /**
     * set map to north oriented
     */
    private fun compassMap() {
        val current = binding.mapview.map.cameraPosition
        val north = CameraPosition(current.target, current.zoom, 0F, 0F)
        binding.mapview.map.move(north, Animation(Animation.Type.SMOOTH, 0.3F), null)

    }

    /**
     * селектор выбора иконки соответствующий типу
     * @param type тип обьекта
     */
    private fun prepareImageProvider(type: MapLocations.Type): ImageProvider {
        return ImageProvider.fromBitmap(
            getBitmapFromVectorDrawable(
                context = requireActivity(),
                drawableId = when (type) {
                    MapLocations.Type.Friend -> R.drawable.icv_person
                    MapLocations.Type.Note -> R.drawable.ic_bank_object_note
                    else -> R.drawable.ic_point_green_white_border_10dp
                }

            )
        )
    }

    /**
     * преобразование svg to bitmap
     * требуется для вывода иконок маркеров на карту
     */
    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun onStop() {
        super.onStop()
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }


    // устанавливаем иконки для отображения юзера
    override fun onObjectAdded(userLocationView: UserLocationView) {

        val style = IconStyle().setScale(0.8F).setAnchor(PointF(0.65F, 0.25F))

        userLocationView.pin.setIcon(
            ImageProvider.fromResource(
                requireContext(),
                R.drawable.own_location_pointer
            ), style
        )
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                requireContext(),
                R.drawable.own_location_pointer
            ), style
        )
        userLocationView.accuracyCircle.fillColor = Color.TRANSPARENT
    }

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
    override fun onClusterAdded(cluster: Cluster) {
        // We setup cluster appearance and tap handler in this method
        cluster.appearance.setIcon(
            ClusterImageProvider(
                cluster.size,
                requireActivity()
            )
        )
        cluster.addClusterTapListener(this)
    }

    override fun onClusterTap(cluster: Cluster): Boolean {
        // We return true to notify map that the tap was handled and shouldn't be
        // propagated further.
//        todo logic according user cases
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    for (i in 0 until grantResults.size) {
                        val permission = permissions[i]
                        if (Manifest.permission.ACCESS_COARSE_LOCATION == permission) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // you now have such permission
                            }
                        }
                        if (Manifest.permission.ACCESS_FINE_LOCATION == permission) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                setupUserLocation()
                            } else {
//                                prepareRegionApproveDialog()
                            }
                        }
                    }
                } else {
//                    prepareRegionApproveDialog()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CODE_REGION_ID) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    setLocationPointByRegion(
//                        data?.getParcelableExtra(RegionConfirmDialogFragment.KEY_REGION_ID_RESULT)
//                    )
//                    shouldUserLocationLoad = true
//                    moveCameraToLocationPoint()
//                }
//                Activity.RESULT_CANCELED -> {
//                    navigate2ChooseRegion()
//                }
//            }
//        }
//        if (requestCode == ChooseRegionFragment.REQUEST_CODE) {
//            if (data != null) {
//                data.getParcelableExtra<RegionItem?>(ChooseRegionFragment.RESULT_REGION)?.let {
//                    setLocationPointByRegion(it)
//                }
//                shouldUserLocationLoad = true
//                moveCameraToLocationPoint()
//            } else {
//                shouldUserLocationLoad = true
//                moveCameraToLocationPoint(DEFAULT_LOCATION_ZOOM)
//            }
//        }
    }

    private fun setLocationPointByRegion(regionItem: RegionItem?) {
        // TODO: 10.10.2020 change after add parameters
        locationPoint = if (regionItem == null) {
            Regions.KZ_REGION_POINT
        } else {
            Point(regionItem.latitude!!, regionItem.longitude!!)
        }
        isUserLocation = false
    }

    override fun onCameraPositionChanged(
        map: com.yandex.mapkit.map.Map,
        cameraPosition: CameraPosition,
        cameraUpdateSource: CameraUpdateReason,
        finish: Boolean,
    ) {

        binding.mapViewCompassBtn.rotateNeedle(cameraPosition.azimuth)

        if (finish) {
            when (cameraUpdateSource) {
                CameraUpdateReason.GESTURES -> {
                    if (!isUserLocation) {

                        val visibleRadius =
                            Geo.distance(map.visibleRegion.bottomLeft, cameraPosition.target)
                        val shiftedDistance = Geo.distance(locationPoint, cameraPosition.target)

                        if (visibleRadius / locationRadius > 2 || shiftedDistance / locationRadius > 1) {

//                            binding.reloadObjectsBtn.visibility = View.VISIBLE
                            shouldReloadButtonShow = true

                        }

                        if (shouldReloadButtonShow) {
                            locationPoint = cameraPosition.target
                            locationRadius = visibleRadius
                        }

                    } else {

                        val visibleRadius =
                            Geo.distance(map.visibleRegion.bottomLeft, cameraPosition.target)
                        val shiftedDistance = Geo.distance(userPoint, cameraPosition.target)

                        if (shiftedDistance > DEFAULT_USER_LOCATION_RADIUS * 500) {
//                            binding.reloadObjectsBtn.visibility = View.VISIBLE
//                            shouldReloadButtonShow = true
                        }

                        if (shouldReloadButtonShow) {
                            locationPoint = cameraPosition.target
                            locationRadius = visibleRadius
                        }

                    }
                }
                CameraUpdateReason.APPLICATION -> {
                    if (!isUserLocation) {

                        val visibleRadius =
                            Geo.distance(map.visibleRegion.bottomLeft, cameraPosition.target)

                        locationPoint = cameraPosition.target
                        locationRadius = visibleRadius

                        if (shouldUserLocationLoad) {
                            reloadObjectsLocations()
                        }

                    }
                }
            }
        }
    }

    /**
     * open bottomsheet dialog
     * @param mapObject обьект содержащий основные данные
     * @param callFromList флаг места вызова (true - если из списка, false - если с карты)
     */
    private fun showMapObjectDetails(mapObject: MapLocations.MapObject?, callFromList: Boolean) {
        mapObject?.let {
            val bottomDialog =
                BottomDialog()
            val bundle = Bundle()
            bundle.putBoolean(
                BottomDialog.KEY_BUNDLE_MAP_OBJECT_INITIATOR_BOTTOM_DIALOG,
                callFromList
            )
            bundle.putParcelable(BottomDialog.KEY_BUNDLE_MAP_OBJECT_BOTTOM_DIALOG, it)
            bottomDialog.arguments = bundle
            bottomDialog.listener = object : BottomDialog.BottomDialogListener {
                override fun onPrepareTheShorterWayClick(bankMapObject: MapLocations.MapObject) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=${userPoint.latitude},${userPoint.longitude}&daddr=${bankMapObject.latitude},${bankMapObject.longitude}")
                    )
                    startActivity(intent)
                }

                override fun onShowOnMapClick(bankMapObject: MapLocations.MapObject) {
                    binding.branchTitleTgl.isChecked = false
                    moveCameraToLocationPoint(
                        Point(
                            bankMapObject.latitude,
                            bankMapObject.longitude
                        ), CHOOSED_OBJECT_ZOOM
                    )
                }
            }
            bottomDialog.show(childFragmentManager, "map-object-details-dialog")
        }
    }

    override fun onItemClick(mapObject: MapLocations.MapObject) {
        showMapObjectDetails(mapObject, true)
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 123
        const val CODE_REGION_ID = 1231

        const val DEFAULT_LOCATION_ZOOM = 7.0F
        const val CHOOSED_LOCATION_ZOOM = 13.0F
        const val CHOOSED_OBJECT_ZOOM = 30.0F
        const val USER_LOCATION_ZOOM = 16.0F

        const val DEFAULT_USER_LOCATION_RADIUS = 1000.0
    }

    override fun onMapTap(p0: Map, p1: Point) {
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        viewModel.createPoint(p1.latitude, p1.longitude)
        findNavController().navigate(
            R.id.action_mapFragment_to_createPointBottomDialog
        )
    }
}

private class Filter(
    private val allToggle: ToggleOnlyProgrammaticallyWrapper,
    private val friendsToggle: ToggleOnlyProgrammaticallyWrapper,
    private val notesToggle: ToggleOnlyProgrammaticallyWrapper,
) {

    init {
        setChecked(allToggle, true)
        setChecked(friendsToggle, false)
        setChecked(notesToggle, false)
    }

    private val toggles = listOf(allToggle, friendsToggle, notesToggle)

    fun isAllChecked(): Boolean = isChecked(allToggle)
    fun isFriendsChecked(): Boolean = isChecked(friendsToggle)
    fun isNotesChecked(): Boolean = isChecked(notesToggle)

    fun toggleAll(isChecked: Boolean = !isAllChecked()) {
        if (isLastChecked(allToggle)) {
            setChecked(allToggle, true)
            return
        }
        setChecked(allToggle, isChecked)
        if (isChecked) {
            uncheckAllExcept(allToggle)
        }
    }

    fun toggleFriends(isChecked: Boolean = !isFriendsChecked()) {
        toggle(friendsToggle, isChecked)
    }

    fun toggleNotes(isChecked: Boolean = !isNotesChecked()) {
        toggle(notesToggle, isChecked)
    }

    private fun toggle(toggle: ToggleOnlyProgrammaticallyWrapper, isChecked: Boolean) {
        setChecked(toggle, isChecked)
        if (isChecked) {
            uncheckAllExcept(toggle)
        } else if (isLastChecked(toggle)) {
            toggleAll(true)
        }
    }

    private fun uncheckAllExcept(toggle: ToggleOnlyProgrammaticallyWrapper) {
        toggles.forEach {
            if (it.toggleButton.id != toggle.toggleButton.id) {
                setChecked(it, false)
            }
        }
    }

    private fun isLastChecked(toggle: ToggleOnlyProgrammaticallyWrapper): Boolean {
        toggles.forEach {
            if (it.toggleButton.id != toggle.toggleButton.id) {
                if (isChecked(it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun setChecked(toggle: ToggleOnlyProgrammaticallyWrapper, isChecked: Boolean) {
        toggle.toggleButton.tag = isChecked
        toggle.setCheckedWithoutListener(isChecked)
    }

    private fun isChecked(toggle: ToggleOnlyProgrammaticallyWrapper) =
        toggle.toggleButton.tag as Boolean
}

/**
 * класс для вывода иконок маркеров на карту
 */
private class ClusterImageProvider(private val clusterSize: Int, private val context: Context) :
    ImageProvider() {

    private val FONT_SIZE = 15f
    private val MARGIN_SIZE = 3f
    private val STROKE_SIZE = 3f

    override fun getId(): String {
        return "text_$clusterSize"
    }

    override fun getImage(): Bitmap {
        val metrics = DisplayMetrics()
        val manager = context.getSystemService(WindowManager::class.java)
        manager!!.defaultDisplay.getMetrics(metrics)
        val textPaint = Paint()
        textPaint.apply {
            textSize = FONT_SIZE * metrics.density
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
            isAntiAlias = true
            color = Color.WHITE
        }
        val widthF: Float = textPaint.measureText(clusterSize.toString())
        val textMetrics: Paint.FontMetrics = textPaint.fontMetrics
        val heightF: Float =
            Math.abs(textMetrics.bottom) + Math.abs(textMetrics.top)
        val textRadius =
            Math.sqrt(widthF * widthF + heightF * heightF.toDouble()).toFloat() / 2
        val internalRadius: Float = textRadius + MARGIN_SIZE * metrics.density
        val scaleCoefficient = when (clusterSize) {
            in 1..10 -> 1.0F
            in 11..25 -> 1.2F
            in 26..49 -> 1.4F
            in 50..74 -> 1.6F
            else -> 1.8F
        }
        val externalRadius: Float =
            (internalRadius + STROKE_SIZE * metrics.density) * scaleCoefficient
        val width = (2 * externalRadius + 0.5).toInt()
        val widthHalf = (width / 2).toFloat()
        val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val backgroundPaint = Paint()
        backgroundPaint.isAntiAlias = true
        backgroundPaint.color =
            context.resources.getColor(R.color.fragment__bank_map__section_bgr)
        canvas.drawCircle(widthHalf, widthHalf, externalRadius - 0.5F, backgroundPaint)
        backgroundPaint.color =
            context.resources.getColor(R.color.fragment__bank_map__section_bgr)
        canvas.drawCircle(widthHalf, widthHalf, internalRadius, backgroundPaint)
        canvas.drawText(
            clusterSize.toString(),
            widthHalf,
            widthHalf - (textMetrics.ascent + textMetrics.descent) / 2,
            textPaint
        )
        return bitmap
    }
}
