package com.example.unitconverter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unitconverter.R
import java.lang.NumberFormatException

class MassViewModel: ViewModel() {
    private val _scale:MutableLiveData<Int> = MutableLiveData(R.string.kilo)

    val scale: LiveData<Int>
        get() = _scale

    fun setScale(value:Int){
        _scale.value = value
    }
    private  val _mass : MutableLiveData<String> = MutableLiveData("")

    val mass : LiveData<String>
        get() = _mass

    fun getMassAsFloat(): Float = (_mass.value?: "").let{
            return try{
                it.toFloat()
            } catch (e: NumberFormatException){
                Float.NaN
            }
    }

    fun setMass(value: String){
        _mass.value = value
    }
    fun convert() = getMassAsFloat().let{
            if (!it.isNaN())
                if (_scale.value == R.string.kilo)
                    it * 2.2046F
                else
                    it / 2.2046F
            else
                Float.NaN
    }

}