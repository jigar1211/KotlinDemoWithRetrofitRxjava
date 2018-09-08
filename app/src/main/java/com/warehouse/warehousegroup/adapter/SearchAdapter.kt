package com.warehouse.warehousegroup.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import product.warehouse.com.warehouse.R
import com.warehouse.warehousegroup.model.Result
import com.warehouse.warehousegroup.view.ProductDetail

class SearchAdapter(val activity: Activity, private val searchModel:List<Result>): RecyclerView.Adapter<SearchAdapter.SearchListingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListingViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_search_list, parent, false)
        return SearchListingViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchModel.size
    }

    override fun onBindViewHolder(holder: SearchListingViewHolder, position: Int) {
        val item = searchModel[position]
        holder.bind(item)
    }

   inner class SearchListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(activity, ProductDetail::class.java)
            intent.putExtra("barcode",searchModel[0].Products[0].Barcode)
            activity.startActivity(intent)
        }

        val tvName: TextView = itemView.findViewById(R.id.tvName)
      init {
          tvName.setOnClickListener(this)
      }

        fun bind(item: Result?) {

            with(item) {
                tvName.text = (item?.Description)


            }
        }

    }
}