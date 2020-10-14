package com.rokoblak.flowtest.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rokoblak.flowtest.R
import com.rokoblak.flowtest.databinding.MainFragmentBinding
import com.rokoblak.flowtest.ui.main.base.BaseFragment
import com.rokoblak.flowtest.ui.main.movies.MovieEntity
import com.rokoblak.flowtest.ui.main.movies.MainViewModel
import com.rokoblak.flowtest.ui.main.ui.MoviesAdapter
import com.rokoblak.flowtest.ui.main.ui.getNetworkLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

class MainFragment : BaseFragment<MainFragmentBinding>() {

    override val layoutId: Int = R.layout.main_fragment

    private val viewModel: MainViewModel by viewModels()
    private val adapter = MoviesAdapter(this::onFavouriteClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        lifecycleScope.launchWhenResumed {
            binding.input.afterTextChangedFlow().debounce(400)
                .collect {
                    viewModel.updateQuery(it.toString())
                }
        }
        lifecycleScope.launchWhenResumed {
            binding.switchNew.toggleChangedFLow()
                .debounce(200)
                .collect { toggled ->
                    viewModel.updateToggle(toggled)
                }
        }

        viewModel.inputState.observe(viewLifecycleOwner, {
            binding.input.setText(it.query)
            binding.switchNew.isChecked = it.newToggled
        })

        viewModel.queried.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        getNetworkLiveData().observe(viewLifecycleOwner, { available ->
            binding.message.isVisible = !available
        })
    }

    private fun onFavouriteClicked(movieEntity: MovieEntity) {
        viewModel.setFavourite(movieEntity)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}