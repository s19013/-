//@file:JvmName("HowToControlFragment1_3Kt")

package com.example.gobblet5.HowToOperateFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gobblet5.R


class HowToOperateFragment1_2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_how_to_operate1_2, container, false)
    }
}