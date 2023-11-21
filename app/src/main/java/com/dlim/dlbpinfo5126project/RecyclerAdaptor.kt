package com.dlim.dlbpinfo5126project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdaptor(private val dataSet: List<Docs>) :
    RecyclerView.Adapter<RecyclerAdaptor.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewHeadline: TextView
        var textViewAuthor: TextView
        var textViewPubDate: TextView
        var textViewWebURL: TextView

        init {
            textViewHeadline = view.findViewById(R.id.textViewHeadLine)
            textViewAuthor = view.findViewById(R.id.textViewAuthor)
            textViewPubDate = view.findViewById(R.id.textViewPubDate)
            textViewWebURL = view.findViewById(R.id.textViewWebURL)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item_card_layout, viewGroup, false)

        val lp = view.layoutParams
        lp.height = 450
        view.layoutParams = lp

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewHeadline.text = dataSet[position].headline.main
        viewHolder.textViewAuthor.text = dataSet[position].byline.original
        viewHolder.textViewPubDate.text = dataSet[position].pub_date
        viewHolder.textViewWebURL.text = dataSet[position].web_url
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataSet.size
    }
}