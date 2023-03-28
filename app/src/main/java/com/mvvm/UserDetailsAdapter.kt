package com.mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.appcomponent.domain.model.response.UserDetailsData
import com.mvvm.databinding.ItemUserDetailsBinding

class UserDetailsAdapter : RecyclerView.Adapter<UserDetailsAdapter.UserDetailsVH>() {

   private var userDetailsList= arrayListOf<UserDetailsData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserDetailsVH {

        val binding= ItemUserDetailsBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return UserDetailsVH(binding)
     }

    override fun onBindViewHolder(
        holder: UserDetailsVH,
        position: Int
    ) {
       holder.bind(userDetailsList[position])

    }

    override fun getItemCount(): Int {
        return userDetailsList.size
    }


    fun setData(userDetailsDataList: List<UserDetailsData>) {
        this.userDetailsList.apply {
            clear()
            addAll(userDetailsDataList)
        }
        notifyDataSetChanged()
    }


    inner class UserDetailsVH( private val binding: ItemUserDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind( userDetailsData: UserDetailsData) {
            binding.tvName.text=userDetailsData.name
            binding.tvUserName.text=userDetailsData.username
            binding.tvEmail.text=userDetailsData.email
            binding.tvWebsite.text=userDetailsData.website
            binding.tvCompany.text=userDetailsData.company.name
        }

    }
}


