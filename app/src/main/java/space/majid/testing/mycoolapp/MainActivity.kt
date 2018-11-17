package com.example.g.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivityForResult
import com.google.gson.reflect.TypeToken




class MainActivity : AppCompatActivity() {
    companion object {
        const val STATE_MESSAGES = "MainActivity.messages"
    }

    private var messages: ArrayList<String> = arrayListOf()
    private var gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val m = sharedPreferences.getString("allMessages", null)
        messages = gson.fromJson(m, type)

        setContentView(R.layout.activity_main)
        updateMessageView()
        btn_send.setOnClickListener {
            startActivityForResult<ConfirmationActivity>(
                    1,
                    ConfirmationActivity.EXTRA_MESSAGE to new_message.text.toString()
            )
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            if( resultCode == Activity.RESULT_OK ) {
                val res = data?.getIntExtra(ConfirmationActivity.EXTRA_ISCONFIRMED, 0)
                when(res) {
                    1 -> {

                        val message = new_message.text.toString()
                        if(messages.size == 10) {
                            messages.removeAt(0)
                        }
                        messages.add(message)
                        updateMessageView()

                        // Save stuff
                        val sp = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                        with(sp.edit()) {
                            val json = gson.toJson(messages)
                            putString("allMessages", json)
                            apply()
                        }
                    }
                }
            }
        }
    }

    private fun updateMessageView() {
        txt_messages.text = messages
                            .reversed()
                            .joinToString("\n")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(STATE_MESSAGES, messages)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        messages = savedInstanceState.getStringArrayList(STATE_MESSAGES)
        updateMessageView()
    }
}
