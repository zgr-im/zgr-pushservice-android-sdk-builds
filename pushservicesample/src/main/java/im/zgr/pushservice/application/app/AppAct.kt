package im.zgr.pushservice.application.app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import im.zgr.pushservice.NotificationActivity
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.databinding.LayoutActivityBinding

open class AppAct: NotificationActivity() {

    private val binding: LayoutActivityBinding by lazy {
        LayoutActivityBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0F
        setContentView(binding.root)
    }

    protected fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(binding.frame.id, fragment).commit()
    }

    fun wait(v: Boolean) {
        binding.loading.isVisible = v
    }

    override fun onSupportNavigateUp(): Boolean {
        val nav = findNavController(R.id.nav)
        return nav.navigateUp() || super.onSupportNavigateUp()
    }

}
