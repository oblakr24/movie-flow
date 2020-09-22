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
import com.rokoblak.flowtest.ui.main.movies.MoviesViewModel
import com.rokoblak.flowtest.ui.main.ui.MoviesAdapter
import com.rokoblak.flowtest.ui.main.ui.getNetworkLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

class MainFragment : BaseFragment<MainFragmentBinding>() {

    override val layoutId: Int = R.layout.main_fragment

    private val moviesVm: MoviesViewModel by viewModels()
    private val adapter = MoviesAdapter(this::onFavouriteClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        lifecycleScope.launchWhenResumed {
            binding.input.afterTextChangedFlow()
                .debounce(200)
                .collect {
                    moviesVm.updateQuery(it.toString())
                }
        }
        lifecycleScope.launchWhenResumed {
            binding.switchNew.toggleChangedFLow()
                .debounce(200)
                .collect { toggled ->
                    moviesVm.updateToggle(toggled)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moviesVm.queried.observe(this, {
            adapter.submitList(it)
        })

        getNetworkLiveData().observe(this, { available ->
            binding.message.isVisible = !available
        })
    }

    private fun onFavouriteClicked(movieEntity: MovieEntity) {
        moviesVm.setFavourite(movieEntity)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}