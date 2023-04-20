package com.fgr.githubuserfinder.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fgr.githubuserfinder.MainActivity
import com.fgr.githubuserfinder.R
import com.fgr.githubuserfinder.adapter.FavoriteUsersAdapter
import com.fgr.githubuserfinder.databinding.FragmentHomeBinding
import com.fgr.githubuserfinder.utils.HomeViewModelFactory
import com.fgr.githubuserfinder.viewmodel.HomeViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isDarkModeActiveState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setActionBarTitle("Github User's Search")


        val dataStore = requireContext().dataStore
        val factory: HomeViewModelFactory =
            HomeViewModelFactory.getInstance(requireContext(), dataStore = dataStore)
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        setupMenu(homeViewModel)

        binding.rvMyFavorite.layoutManager = LinearLayoutManager(requireContext())
        homeViewModel.listUsers.observe(requireActivity()) { newList ->
            if (!newList.isNullOrEmpty()) {
                binding.userNotFound.visibility = View.GONE
                binding.rvMyFavorite.visibility = View.VISIBLE
                binding.rvMyFavorite.adapter =
                    FavoriteUsersAdapter(requireContext(), newList.asReversed())
            } else {
                binding.userNotFound.visibility = View.VISIBLE
                binding.rvMyFavorite.visibility = View.GONE
            }
        }
    }

    private fun setupMenu(homeViewModel: HomeViewModel) {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onPrepareMenu(menu: Menu) {
                homeViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive ->
                    if (isDarkModeActiveState != isDarkModeActive) {
                        isDarkModeActiveState = isDarkModeActive
                        if (isDarkModeActive) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            menu.findItem(R.id.light).setIcon(R.drawable.ic_light_off)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            menu.findItem(R.id.light).setIcon(R.drawable.ic_light_on)
                        }
                    }
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> {
                        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                        binding.root.findNavController()
                            .navigate(R.id.action_homeFragment_to_searchFragment)
                        true
                    }
                    R.id.light -> {
                        homeViewModel.saveThemeSetting(!isDarkModeActiveState)
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }
}