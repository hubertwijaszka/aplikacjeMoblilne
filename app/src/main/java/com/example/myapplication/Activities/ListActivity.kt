package com.example.myapplication.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.myapplication.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.Model.Ranking

import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import org.json.JSONArray
import java.net.URL

public class ListActivity : AppCompatActivity() {

    companion object {
        fun setAdapter(con: Context) {
            adapter = ListAdapter(
                con,
                rankingList
            )
        }
         var rankingList = ArrayList<Ranking>()
        lateinit var adapter : ListAdapter
        var preferences: SharedPreferences? = null
    }

    fun loadList(jsonString :String?){
        val temporaryRankings =ArrayList<Ranking>()
        val jsons= JSONArray(jsonString?.split("html>")?.get(2))
        for (i in 0..(jsons.length() - 1)) {
            val item = jsons.getJSONArray(i)
            temporaryRankings.add(
                Ranking(
                    item.get(1).toString(),
                    item.get(2).toString().toInt()
                )
            )
        }
        rankingList.clear()
        rankingList.addAll(temporaryRankings)
        adapter.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setAdapter(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        backToMainButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val internetData= InternetData(this)
        internetData.execute()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        listView.adapter = adapter
    }

    class InternetData(private var activity: ListActivity?): AsyncTask<String, String, String>(){

        override fun doInBackground(vararg p0: String?): String {
            try{
            val text= URL("http://hufiecgniezno.pl/br/record.php?f=get").readText()
                val editor = preferences!!.edit()
                editor.putString("jsonRanking", text)
                editor.apply()
                return text;
            }catch(e:Exception){
                val text= preferences!!.getString("jsonRanking","null")
                return text
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            activity?.loadList(result)
        }
    }


}
