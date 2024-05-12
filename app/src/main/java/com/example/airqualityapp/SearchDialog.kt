package com.example.airqualityapp

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment


class SearchDialog : AppCompatDialogFragment() {

    private var currentCity: String = "Wrocław"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Enter city name!")

        val input = EditText(activity)
        input.inputType = InputType.TYPE_CLASS_TEXT
        this.currentCity = input.toString()

        builder.setView(input)
        builder.setPositiveButton("OK",null)

        return builder.create()
    }
    fun getCurrentCity(): String = this.currentCity
}

