package im.zgr.pushservice.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import im.zgr.pushservice.Preferences

class ConnectionFragment : Fragment() {

    private val act: View get() = requireView()

    private val endpointURL: AppCompatEditText
        get() = act.findViewById(R.id.endpointURL)

    private val applicationId: AppCompatEditText
        get() = act.findViewById(R.id.applicationId)

    private val apiKey: AppCompatEditText
        get() = act.findViewById(R.id.apiKey)

    private val apply: Button
        get() = act.findViewById(R.id.apply)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_connection,
            container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = Preferences(requireContext())
        endpointURL.setText(pref.endpointURL)
        applicationId.setText(pref.applicationId)
        apiKey.setText(pref.apiKey)
        apply.setOnClickListener {
            pref.endpointURL = endpointURL.text.toString()
            pref.applicationId = applicationId.text.toString()
            pref.apiKey = apiKey.text.toString()
            requireActivity().finish()
        }
    }

}
