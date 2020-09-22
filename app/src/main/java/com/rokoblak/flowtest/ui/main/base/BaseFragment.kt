package com.rokoblak.flowtest.ui.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<T : ViewDataBinding> : Fragment(), IBaseFragment<T> {

    override var _binding: T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        return inflateBinding(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanupBinding()
    }
}