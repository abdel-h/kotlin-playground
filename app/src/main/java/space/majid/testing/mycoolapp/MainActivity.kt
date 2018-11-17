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
import java.io.File
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    companion object {
        const val STATE_MESSAGES = "MainActivity.messages"
    }

    private val gson = Gson()

    private var messages: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
