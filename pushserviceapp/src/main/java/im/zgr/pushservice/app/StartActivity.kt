package im.zgr.pushservice.app

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

abstract class StartActivity : AppCompatActivity() {

    protected val logoView: ImageView by lazy { findViewById(R.id.logo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

}
