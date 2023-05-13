package com.example.students.features.main.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import com.example.students.databinding.LayoutMapObjectDetailsBottomDefaultBinding
import com.example.students.features.main.map.presentation.model.MapLocations
import com.example.students.utils.PermissionsUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog : BottomSheetDialogFragment() {

    companion object {
        val KEY_BUNDLE_MAP_OBJECT_BOTTOM_DIALOG = "key_bundle_map_object_bottom_dialog"
        val KEY_BUNDLE_MAP_OBJECT_INITIATOR_BOTTOM_DIALOG =
            "key_bundle_map_object_initiator_bottom_dialog"
    }

    interface BottomDialogListener {
        fun onPrepareTheShorterWayClick(mapObject: MapLocations.MapObject)
        fun onShowOnMapClick(mapObject: MapLocations.MapObject)
    }

    var mapObject: MapLocations.MapObject? = null
    var listener: BottomDialogListener? = null
    private var callFromList: Boolean = false

    //    header
    private lateinit var iconView: ImageView
    private lateinit var titleView: TextView
    private lateinit var addressView: TextView
    private lateinit var distanceView: TextView
    private lateinit var workHourView: TextView
    private lateinit var notWorkView: TextView
    private lateinit var setWayBtn: TextView
    private lateinit var showOnMapBtn: TextView

    //    services
    private lateinit var serviceLayout: View
    private lateinit var serviceLayoutRecycler: RecyclerView

    //    currency
    private lateinit var currencyLayoutAtm: View
    private lateinit var currencyLayoutAtmKzt: LinearLayout
    private lateinit var currencyLayoutAtmUsd: LinearLayout
    private lateinit var currencyLayoutAtmEur: LinearLayout

    //    schedule
    private lateinit var scheduleLayout: View
    private lateinit var scheduleMondayTitle: TextView
    private lateinit var scheduleMonday: TextView
    private lateinit var scheduleTuesdayTitle: TextView
    private lateinit var scheduleTuesday: TextView
    private lateinit var scheduleWednesdayTitle: TextView
    private lateinit var scheduleWednesday: TextView
    private lateinit var scheduleThursdayTitle: TextView
    private lateinit var scheduleThursday: TextView
    private lateinit var scheduleFridayTitle: TextView
    private lateinit var scheduleFriday: TextView
    private lateinit var scheduleSaturdayTitle: TextView
    private lateinit var scheduleSaturday: TextView
    private lateinit var scheduleSundayTitle: TextView
    private lateinit var scheduleSunday: TextView
    private lateinit var scheduleHolidayTitle: TextView
    private lateinit var scheduleHoliday: TextView

    private lateinit var binding: LayoutMapObjectDetailsBottomDefaultBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callFromList = arguments?.getBoolean(KEY_BUNDLE_MAP_OBJECT_INITIATOR_BOTTOM_DIALOG, false)!!
        mapObject = arguments?.getParcelable(KEY_BUNDLE_MAP_OBJECT_BOTTOM_DIALOG)
        binding = LayoutMapObjectDetailsBottomDefaultBinding.inflate(LayoutInflater.from(context), container, false)

        binding.root.let { view ->
            initViews(view)
            mapObject?.let {

                fillCommonViews()
                when (mapObject?.type) {
                    MapLocations.Type.Friend -> {
                        fillFriendDetails()
                    }
                    MapLocations.Type.Note -> {
                        fillNoteDetails()
                    }
                    else -> {
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutSheetBottomDetailsHeader.bankMapObjectDetailsCloseIv.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onStart() {
        super.onStart()
        val bottomSheet: View? =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        bottomSheetBehavior.halfExpandedRatio = 0.0001f
        bottomSheetBehavior.peekHeight = 1
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    /**
     * заполнение полей спец для типа банкомат
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun fillNoteDetails() {
        iconView.setImageDrawable(resources.getDrawable(R.drawable.ic_bank_object_note, null))
//        serviceLayoutAtm.visibility = View.VISIBLE
        mapObject?.let {

        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun fillFriendDetails() {
        iconView.setImageDrawable(
            resources.getDrawable(
                R.drawable.icv_person,
                null
            )
        )
    }


    /**
     * иннициализация вью элементов
     */
    private fun initViews(view: View) {
//        header
        iconView = view.findViewById(R.id.bank_map_object_details_iv)
        titleView = view.findViewById(R.id.bank_map_object_details_name_tv)
        addressView = view.findViewById(R.id.bank_map_object_details_address_tv)
        distanceView = view.findViewById(R.id.bank_map_object_details_distance_tv)
        setWayBtn = view.findViewById(R.id.bank_map_object_details_btn_set_the_way)
        showOnMapBtn = view.findViewById(R.id.bank_map_object_details_btn_show_on_map)
    }

    /**
     * заполнение полей общих для всех типов
     */
    private fun fillCommonViews() {
        mapObject?.let { mapObject ->
            titleView.text = mapObject.name
            addressView.text = mapObject.address
            //header
            if (PermissionsUtils.checkComplexLocationPermissions(requireActivity())) {
                distanceView.text = mapObject.distance.toInt().toString()
                distanceView.visibility = View.VISIBLE
            }

            if (callFromList) {
                showOnMapBtn.visibility = View.VISIBLE

            }
            setWayBtn.setOnClickListener {

                if (!PermissionsUtils.isSystemLocationEnabled(requireContext())
                    || !PermissionsUtils.isSystemLocationServiceEnabled(requireContext())
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.map_location_permissions_toast_for_way),
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                listener?.onPrepareTheShorterWayClick(mapObject)
                dismiss()
                }
            }

            showOnMapBtn.setOnClickListener {
                listener?.onShowOnMapClick(mapObject)
                dismiss()
            }
        }
    }
}
