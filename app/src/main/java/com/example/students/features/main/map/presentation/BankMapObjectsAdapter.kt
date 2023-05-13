package com.example.students.features.main.map.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import com.example.students.features.main.map.presentation.model.MapLocations
import com.example.students.utils.PermissionsUtils
import com.example.students.utils.setSafeOnClickListener

class MapObjectsAdapter constructor(
    val context: Context,
) :
    RecyclerView.Adapter<MapObjectsAdapter.ViewHolder>() {
    private var mData: MutableList<MapLocations.MapObject> = mutableListOf()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onItemClick(mapObject: MapLocations.MapObject)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val bankMapObject = mData[position]

        holder.let { vHolder ->
            bankMapObject.let { mapObject ->
                val clickView: View =
                    vHolder.itemView.findViewById(R.id.bank_map_object_name_tv_click_view)
                clickView.setSafeOnClickListener { mClickListener?.onItemClick(mapObject) }
                var nameTv: TextView = vHolder.itemView.findViewById(R.id.bank_map_object_name_tv)
                nameTv.text = mapObject.name

                val address: TextView =
                    vHolder.itemView.findViewById(R.id.bank_map_object_address_tv)
                address.text = mapObject.address
                val distance: TextView =
                    vHolder.itemView.findViewById(R.id.bank_map_object_distance_tv)
                distance.visibility =
                    if (PermissionsUtils.checkComplexLocationPermissions(context)) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                distance.text = mapObject.distance.toInt().toString()

                val icon: ImageView = vHolder.itemView.findViewById(R.id.bank_map_object_iv)

                icon.setImageDrawable(
                    vHolder.itemView.context.resources.getDrawable(
                        when (mapObject.type) {
                            MapLocations.Type.Friend -> R.drawable.icv_person
                            MapLocations.Type.Note -> R.drawable.ic_bank_object_note
                            else -> {
                                R.drawable.ic_point_green_white_border_10dp
                            }
                        }
                    )
                )


            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size ?: 0
    }


    fun addListElements(list: List<MapLocations.MapObject>) {
        mData.clear()
        (mData as ArrayList<MapLocations.MapObject>).addAll(list)
        notifyDataSetChanged()
    }

    fun clearAllListElements() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun getItems(): List<MapLocations.MapObject> = mData.toList()

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    fun getItem(id: Int): MapLocations.MapObject {
        return mData.get(id) ?: MapLocations.MapObject()
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item__bank_objects_list, parent, false)
        return ViewHolder(view)
    }

}