package com.fgr.githubuserfinder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fgr.githubuserfinder.adapter.SearchUsersAdapter
import com.fgr.githubuserfinder.data.DetailViewModel
import com.fgr.githubuserfinder.databinding.FragmentDetailUserRvBinding
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.utils.DetailActivityModelFactory

class DetailUserRvFragment : Fragment() {

    companion object {
        const val INDEX_POSITION = "index_position"
        const val USERNAME = "username"
    }

    private var _binding: FragmentDetailUserRvBinding? = null
    private val binding get() = _binding!!
    private lateinit var listFoll: ArrayList<ListUsers>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailUserRvBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchUsers.layoutManager = layoutManager
        val indexPosition = arguments?.getInt(INDEX_POSITION, 0)!!
        val userName = arguments?.getString(USERNAME) ?: "empty"
        listFoll = ArrayList()
        val factory = DetailActivityModelFactory(userName, indexPosition)
        val detailViewModel = ViewModelProvider(
            this,
            factory
        )[DetailViewModel::class.java]
        detailViewModel.isLoadingFollowers.observe(requireActivity()) { isLoadingFollowers ->
            if (isLoadingFollowers) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
        detailViewModel.isLoadingFollowing.observe(requireActivity()) {isLoadingFollowing ->
            if (isLoadingFollowing) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
        detailViewModel.followerList.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.rvSearchUsers.adapter = SearchUsersAdapter(requireContext(), it)
            }
        }
        detailViewModel.followingList.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.rvSearchUsers.adapter = SearchUsersAdapter(requireContext(), it)
            }
        }
    }
}