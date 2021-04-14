package edu.coe.hughes.contentresolvercontacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter


class MainActivity : AppCompatActivity() {
    var cursor: Cursor? = null
    var listView: ListView? = null
    var button: Button? = null
    var personName: EditText?= null
    var searchName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PackageManager.PERMISSION_GRANTED)
        }


        listView = findViewById(R.id.ListView)
        button = findViewById(R.id.Button)
        button!!.setOnClickListener(View.OnClickListener { // calling of getContacts()
            contacts
        })
        personName = findViewById(R.id.PersonName)
        personName!!.addTextChangedListener(object:TextWatcher {

           override fun afterTextChanged(s: Editable) {
               Log.i("MAIN", "Text changed")
               searchName = personName!!.text.toString()
               contacts
            }

            override fun beforeTextChanged(s: CharSequence, start:Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start:Int, before: Int, count: Int) {}
        });
    }

    // create cursor and query the data
    val contacts: Unit
        get() {
            // create cursor and query the data

            val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            //val cursor = contentResolver.query(uri, null, null, null, null)
            val cursor = contentResolver.query(uri, null, "DISPLAY_NAME = '$searchName'", null, null)

            val data = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val to = intArrayOf(android.R.id.text1, android.R.id.text2)

            val adapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, data, to, 1)
            listView!!.adapter = adapter

        }
}
