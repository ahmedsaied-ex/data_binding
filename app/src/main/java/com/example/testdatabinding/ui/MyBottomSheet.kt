package com.example.test_trip_logic.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.testdatabinding.MyOpject
import com.example.testdatabinding.R

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MyBottomSheet : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false)
        val editButton = view.findViewById<Button>(R.id.btn_edit)
        val  dismissButton= view.findViewById<Button>(R.id.btn_dismiss)

        editButton.setOnClickListener {

            MyOpject.isAdjustable=true
            Log.d("TAG_132", "onCreateView: ${MyOpject.isAdjustable}")
            dismiss()
        }

        dismissButton.setOnClickListener {

            Log.d("TAG_132", "dismiss: ${MyOpject.isAdjustable}")
            dismiss()
        }


        return view
    }


}