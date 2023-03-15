package im.zgr.pushservice.app.content

import android.os.Bundle
import android.widget.TextView
import com.google.android.youtube.player.*
import im.zgr.pushservice.app.AppBasic
import im.zgr.pushservice.app.R

open class VideoActivity : ContentActivity() {

    private val titleView: TextView by lazy { findViewById(R.id.titleView) }
    private val textView: TextView by lazy { findViewById(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }

    override fun start() {
        setData(titleView, intent.getStringExtra(AppBasic.titleName))
        setData(textView, intent.getStringExtra(AppBasic.textName))
    }

    class VideoFragment: YouTubePlayerFragment() {

        private val uri: String by lazy { activity.intent.getStringExtra(AppBasic.urlName) ?: "" }
        private val id = "AIzaSyAa1OrsF0pXXafqSxNuSCHIezePC1Bnw5w"
        init {

            initialize(id, object: YouTubePlayer.OnInitializedListener {

                override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                    p1?.loadVideo(uri)
                    p1?.play()
                }

                override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                    p0.toString()
                }

            })
        }

    }

}
