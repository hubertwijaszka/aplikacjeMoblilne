package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myapplication.Model.Ranking

class ListAdapter(private val context: Context,
                  private val dataSource: ArrayList<Ranking>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_ranking, parent, false)

        val indexTextView = rowView.findViewById(R.id.indexText) as TextView


        val pointsTextView = rowView.findViewById(R.id.pointsText) as TextView
        val ranking = getItem(position) as Ranking


        indexTextView.text = ranking.index.toString()
        pointsTextView.text = ranking.points.toString()


        return rowView
    }
}
