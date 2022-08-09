package im.zgr.pushservice.app

import android.widget.Toast
import androidx.fragment.app.Fragment

open class AppFragment: Fragment() {

    fun say(s: String) = Toast.makeText(
        requireContext(), s, Toast.LENGTH_SHORT
    ).show()

}