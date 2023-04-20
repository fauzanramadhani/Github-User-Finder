package com.fgr.githubuserfinder.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fgr.githubuserfinder.MainActivity
import com.fgr.githubuserfinder.R
import com.fgr.githubuserfinder.adapter.SearchUsersAdapter
import com.fgr.githubuserfinder.viewmodel.SearchViewModel
import com.fgr.githubuserfinder.databinding.FragmentSearchBinding
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.utils.isNotEmpty


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchItem: SearchView
    private lateinit var arrayList: ArrayList<ListUsers>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setActionBarTitle("")
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchUsers.layoutManager = layoutManager
        arrayList = ArrayList()

        setupMenu()
    }

    private fun setupMenu() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        searchViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle back button when menu expanded
                menu.findItem(R.id.light).isVisible = false
                menu.findItem(R.id.search).expandActionView()
                menu.findItem(R.id.search)
                    .setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                        override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                            return true
                        }

                        override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                            binding.root.findNavController().popBackStack()
                            return true
                        }

                    })

                searchViewModel.searchText.observe(requireActivity()) { searchText ->
                    if (searchText.isNotEmpty()) {
                        searchItem.setQuery(searchText, false)
                        searchItem.isIconified = false
                    }
                }
                searchViewModel.listUsers.observe(requireActivity()) { listUser ->
                    if (isNotEmpty(listUser)) {
                        arrayList.clear()
                        binding.rvSearchUsers.adapter =
                            SearchUsersAdapter(requireContext(), arrayList)
                        arrayList.addAll(listUser)
                        binding.rvSearchUsers.visibility = View.VISIBLE
                        binding.userNotFound.visibility = View.GONE
                    } else {
                        binding.rvSearchUsers.visibility = View.GONE
                        binding.userNotFound.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)

                searchItem = menu.findItem(R.id.search).actionView as SearchView

                searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            binding.userNotFound.visibility = View.GONE
                            imm.hideSoftInputFromWindow(view?.windowToken, 0)
                            searchViewModel.searchUsers(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            if (newText.isNotEmpty()) {
                                searchViewModel.onSearchTextChange(newText)
                            }
                        }
                        return true
                    }

                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> {
                        true
                    }
                    R.id.light -> {
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}