package com.example.myapplication.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.myapplication.Model.User
import com.example.myapplication.R
import com.example.myapplication.SQL.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var computerNo = Random.nextInt(0, 20)
    var currentUser :String=String()
    var steps = 0
    var myNo = 0
    var preferences: SharedPreferences? = null
    var alert: AlertDialog? = null
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)
        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        currentUser = preferences!!.getString("CURRENTUSER", null)
        var best : Int=databaseHelper.getUserPoints(currentUser)
        if(best==0) best=99999
        //val best : Int=0
        if(best != 99999) {
            bestResaultTextView.text = "Najlepszy wynik: $best"
        } else {
            bestResaultTextView.text = "Nie ma jeszcze najlepszego wyniku"
        }

        newGameButton.setOnClickListener {
            startNewGame()
        }
        seeRankingList.setOnClickListener(){
            startRankingList()
        }
        logoutButton.setOnClickListener(){
            logout();
        }

        shootButton.setOnClickListener {
            if(numberEditText.text.toString().compareTo("") != 0) {

                try {
                    myNo = numberEditText.text.toString().toInt()
                    if (myNo < 0 || myNo > 20) {
                        resaultTextView.text = "Podaj liczbę z zakresu [0;20]"
                        numberEditText.setText("")
                    } else {
                        steps += 1
                        var points=0
                        if (myNo == computerNo) {
                            if(steps == 1 ) { points=5}
                            else if(steps<5){points =3}
                            else if(steps<7){ points=2}
                            else {points=1}


                            resaultTextView.text = "Wygrałeś w $steps próbach"
                            val b = checkTheBest()
                            sendResult(steps)
                            showAllert(b,points)
                        } else {
                            if(steps==10){
                                resaultTextView.text = "przegrałeś"
                                showAllert(3,points)
                            }
                            if (myNo > computerNo){resaultTextView.text = "Podaj mniejszą liczbę niż $myNo. Liczba prób: $steps"}
                            else{resaultTextView.text = "Podaj większą liczbę niż $myNo. Liczba prób: $steps"}

                        }
                    }
                } catch (e : NumberFormatException) {
                    resaultTextView.text = "Podaj liczbę z zakresu [0;20]"
                }
                catch(e:Exception){
                    System.out.println(e.stackTrace)
                }

            }
            numberEditText.setText("")
        }

        startNewGame()
    }
    private fun logout(){
        val editor = preferences!!.edit()
        editor.putString("CURRENTUSER", null)
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun sendResult(points:Int){
        val internetData= InternetData(this)
        internetData.execute(points.toString(),currentUser)
    }
    private fun showAllert(b: Int, points:Int) {
        if (b==1) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Wygrałeś w $steps krokach i poprawiłeś najlepszy wynik, zdobyłeś $points punktów")
            builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    alert!!.cancel()
                    startNewGame()
                }

            })
            alert = builder.create()
            alert!!.show()
        } else if(b==0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Wygrałeś w $steps krokach, zdobyłeś $points punktów")
            builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    alert!!.cancel()
                    startNewGame()
                }

            })
            alert = builder.create()
            alert!!.show()
        }
        else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Przegrałeś, zdobyłeś 0 punktów")
            builder.setPositiveButton("Ok :(", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    alert!!.cancel()
                    startNewGame()
                }

            })
            alert = builder.create()
            alert!!.show()

        }
    }

    private fun startNewGame() {
        steps = 0
        myNo = 0
        computerNo = Random.nextInt(0, 20)
        resaultTextView.text = "Podaj liczbę"
        Toast.makeText(applicationContext, "Wylosowano nową liczbę", Toast.LENGTH_SHORT).show()
    }
    private fun startRankingList() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    private fun checkTheBest(): Int {
        var points= databaseHelper.getUserPoints(currentUser)
        if(points==0) points=9999
        if (steps < points ) {
            bestResaultTextView.text = "Najlepszy wynik: $steps"
            databaseHelper.updateUser(steps,currentUser)
            return 1
        }
        return 0
    }
    class InternetData(private var activity: MainActivity?): AsyncTask<String, String, String>(){
        override fun doInBackground(vararg p0: String?): String {
           try{ val text= URL("http://hufiecgniezno.pl/br/record.php?f=add&id=$p0(1)&r=$p0(0)").readText()
            System.out.println(text);}
           catch(e:Exception){}
            return "aaa";
        }
    }
}
