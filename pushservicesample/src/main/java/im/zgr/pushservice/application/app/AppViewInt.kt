package im.zgr.pushservice.application.app

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface AppViewInt<T: ViewBinding> {
    val binding: T
}