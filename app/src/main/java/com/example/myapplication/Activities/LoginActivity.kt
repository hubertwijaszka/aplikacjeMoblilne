package com.example.myapplication.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.example.myapplication.InputValidation
import com.example.myapplication.R
import com.example.myapplication.SQL.DatabaseHelper


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@LoginActivity

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var textInputLayoutLogin: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout

    private lateinit var textInputEditTextLogin: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText

    private lateinit var appCompatButtonLogin: AppCompatButton

    private lateinit var textViewLinkRegister: AppCompatTextView

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    var preferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // hiding the action bar
        supportActionBar!!.hide()

        // initializing the views
        initViews()

        // initializing the listeners
        initListeners()

        // initializing the objects
        initObjects()

    }

    /**
     * This method is to initialize views
     */
    private fun initViews() {

        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView

        textInputLayoutLogin = findViewById<View>(R.id.textInputLayoutLogin) as TextInputLayout
        textInputLayoutPassword = findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout

        textInputEditTextLogin = findViewById<View>(R.id.textInputEditTextLogin) as TextInputEditText
        textInputEditTextPassword = findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText

        appCompatButtonLogin = findViewById<View>(R.id.appCompatButtonLogin) as AppCompatButton

        textViewLinkRegister = findViewById<View>(R.id.textViewLinkRegister) as AppCompatTextView

    }

    /**
     * This method is to initialize listeners
     */
    private fun initListeners() {

        appCompatButtonLogin!!.setOnClickListener(this)
        textViewLinkRegister!!.setOnClickListener(this)
    }

    /**
     * This method is to initialize objects to be used
     */
    private fun initObjects() {
        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        databaseHelper = DatabaseHelper(activity)
        inputValidation = InputValidation(activity)

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonLogin -> verifyFromSQLite()
            R.id.textViewLinkRegister -> {
                // Navigate to RegisterActivity
                val intentRegister = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intentRegister)
            }
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private fun verifyFromSQLite() {

        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextLogin!!, textInputLayoutLogin!!, getString(R.string.error_message_email))) {
            return
        }

        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextPassword!!, textInputLayoutPassword!!, getString(R.string.error_message_email))) {
            return
        }

        if (databaseHelper!!.checkUser(textInputEditTextLogin!!.text.toString().trim { it <= ' ' }, textInputEditTextPassword!!.text.toString().trim { it <= ' ' })) {

            val editor = preferences!!.edit()
            editor.putString("CURRENTUSER", textInputEditTextLogin!!.text.toString().trim { it <= ' ' })
            val accountsIntent = Intent(activity, MainActivity::class.java)
            accountsIntent.putExtra("CURRENTUSER", textInputEditTextLogin!!.text.toString().trim { it <= ' ' })
            editor.apply()
            emptyInputEditText()
            startActivity(accountsIntent)


        } else {

            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView!!, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        textInputEditTextLogin!!.text = null
        textInputEditTextPassword!!.text = null
    }
}