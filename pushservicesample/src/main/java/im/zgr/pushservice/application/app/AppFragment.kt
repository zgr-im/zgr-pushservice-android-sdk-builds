package im.zgr.pushservice.application.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.utils.SettingsImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class AppFragment: Fragment() {

    lateinit var viewModel: AppModel

    val act: AppAct
        get() = requireActivity() as AppAct

    val sdk: NotificationSdk
        get() = NotificationSdk.getInstance(requireContext())

    val settings: SettingsImpl
        get() = SettingsImpl(requireContext())

    fun setTitle(v: String) {
        act.supportActionBar?.title = v
    }

    private fun addMenu(menuProvider: MenuProvider) =
        requireActivity().addMenuProvider(menuProvider)

    private fun removeMenu(menuProvider: MenuProvider) =
        requireActivity().removeMenuProvider(menuProvider)

    var menuItems: Menu? = null
    private var menuProvider: MenuProvider? = null
    fun setMenu(@MenuRes res: Int, action: (item: Int)->Unit) {
        menuProvider = object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                menuInflater.inflate(res, menu).also { menuItems = menu }
            override fun onMenuItemSelected(menuItem: MenuItem) =
                action(menuItem.itemId).run { false }
        }
    }

    override fun onResume() {
        super.onResume()
        menuProvider?.apply(this::addMenu)
    }

    override fun onPause() {
        menuProvider?.apply(this::removeMenu)
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AppModel().apply {
            wait.observe(viewLifecycleOwner, act::wait)
        }
    }

    fun moveTo(directions: NavDirections) =
        findNavController().navigate(directions)

    fun moveUp() =
        findNavController().navigateUp()

    fun <T> setResult(name: String, v: T) =
        findNavController().previousBackStackEntry
            ?.savedStateHandle?.set(name, v)

    fun <T> getResult(name: String, result: (v: T) -> Unit) =
        findNavController().currentBackStackEntry
            ?.savedStateHandle?.getLiveData<T>(name)
            ?.observe(viewLifecycleOwner, result)

    fun say(v: String?) = act.say(v)
    fun say(t: Throwable) = say(t.message ?: t.toString())
    fun open(url: String?) = url?.let {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun delayed(delay: Long, v: ()-> Unit) =
        Handler(Looper.getMainLooper()).postDelayed(v, delay)

}