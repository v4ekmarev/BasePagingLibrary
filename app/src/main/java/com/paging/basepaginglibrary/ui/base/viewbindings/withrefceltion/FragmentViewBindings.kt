package com.justcashback.ims_justcash.ui.base.viewbindings.withrefceltion

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.paging.basepaginglibrary.ui.base.viewbindings.noreflection.ViewBindingProperty
import com.justcashback.ims_justcash.ui.base.viewbindings.noreflection.viewBinding

@PublishedApi
internal class FragmentViewBinder<T : ViewBinding>(private val viewBindingClass: Class<T>) {

    /**
     * Cache static method `ViewBinding.bind(View)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("bind", View::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    @Suppress("UNCHECKED_CAST")
    fun bind(fragment: Fragment): T {
        return bindViewMethod(null, fragment.requireView()) as T
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class FragmentInflateViewBinder<T : ViewBinding>(
    private val viewBindingClass: Class<T>
) {

    /**
     * Cache static method `ViewBinding.inflate(LayoutInflater)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("inflate", LayoutInflater::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    @Suppress("UNCHECKED_CAST")
    fun bind(fragment: Fragment): T {
        return bindViewMethod(null, fragment.layoutInflater) as T
    }
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param T Class of expected [ViewBinding] result class
 */
public inline fun <reified T : ViewBinding> Fragment.viewBinding(
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<Fragment, T> = when (createMethod) {
    CreateMethod.BIND -> viewBinding(FragmentViewBinder(T::class.java)::bind)
    CreateMethod.INFLATE -> viewBinding(FragmentInflateViewBinder(T::class.java)::bind)
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param viewBindingClass Class of expected [ViewBinding] result class
 */
public fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<Fragment, T> = when (createMethod) {
    CreateMethod.BIND ->  viewBinding(FragmentViewBinder(viewBindingClass)::bind)
    CreateMethod.INFLATE ->  viewBinding(FragmentInflateViewBinder(viewBindingClass)::bind)
}
