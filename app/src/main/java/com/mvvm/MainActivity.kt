package com.mvvm

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.appcomponent.basecomponent.BaseActivity
import com.mvvm.appcomponent.util.Resource
import com.mvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val mainViewModel: MainViewModel by viewModels()
    private var userDetailsAdapter:UserDetailsAdapter?=null

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        mainViewModel.getUserDetails()
        binding.rvUser.layoutManager=LinearLayoutManager(this)
        userDetailsAdapter=UserDetailsAdapter()
        binding.rvUser.adapter=userDetailsAdapter

        getUserDetails()


    }

    private fun  getUserDetails(){
        lifecycleScope.launch {
            collectLatestLifecycleFlow(mainViewModel.userDetails){
                when(it){
                    is Resource.Loading -> {
                        showProgress()
                    }
                    is Resource.Success -> {
                        hideProgress()
                        it.data?.let { userListData ->
                           Log.d("UserListData", userListData.toString())
                            userDetailsAdapter?.setData(userListData)

                        }
                    }
                    is Resource.Error -> {
                        hideProgress()
                        it.error?.message?.let { errorMessage->
                            Toast.makeText(baseContext, "Error:- $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}