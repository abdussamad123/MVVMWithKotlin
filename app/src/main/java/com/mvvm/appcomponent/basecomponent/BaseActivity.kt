package com.mvvm.appcomponent.basecomponent

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.mvvm.appcomponent.util.AppUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


abstract class BaseActivity<VB: ViewBinding>(
    private val bindingInflater: (inflator:LayoutInflater) -> VB
) : AppCompatActivity(){

    private var _binding : VB? = null
    val binding : VB
    get() = _binding as VB

    private val progress: AlertDialog? by lazy {
        AppUtils.getProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingInflater.invoke(layoutInflater)
        if(_binding == null){
            throw IllegalArgumentException("Binding cannot be null")
        }
        setContentView(binding.root)
    }
    suspend fun <T>collectLatestLifecycleFlow(flow: Flow<T>,collect: suspend (T) -> Unit) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }

    fun showProgress() {
        progress?.show()
    }

    fun hideProgress() {
        progress?.dismiss()
    }
}