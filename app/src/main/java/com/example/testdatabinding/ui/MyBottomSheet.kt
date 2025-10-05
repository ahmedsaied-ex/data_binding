package com.example.testdatabinding.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.testdatabinding.MyOpject
import com.example.testdatabinding.R
import com.example.testdatabinding.data.view_model.MapViewModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MyBottomSheet : BottomSheetDialogFragment() {

    private val mapStateViewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false)
        val editButton = view.findViewById<Button>(R.id.btn_edit)
        val  dismissButton= view.findViewById<Button>(R.id.btn_dismiss)


        editButton.setOnClickListener {
            mapStateViewModel.enableEditing()
            dismiss()
        }


        dismissButton.setOnClickListener {

            Log.d("TAG_132", "dismiss: ${MyOpject.isAdjustable}")
            dismiss()
        }


        return view
    }


}