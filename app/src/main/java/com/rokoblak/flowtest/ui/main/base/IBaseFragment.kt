package com.rokoblak.flowtest.ui.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


interface IBaseFragment<T: ViewDataBinding> {

    /**
     * Override to set layout resource id
     */
    val layoutId: Int

    var _binding: T?

    val binding: T
        get() = _binding ?: throw IllegalAccessError("View binding must be accessed within the valid state of fragment's lifecycle")

    fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        return _binding?.root
    }

    fun cleanupBinding() {
        _binding?.unbind()
        _binding = null
    }

}

inline fun <T: ViewDataBinding, R> IBaseFragment<T>.withBinding(block: T.() -> R): R {
    return binding.let(block)
}