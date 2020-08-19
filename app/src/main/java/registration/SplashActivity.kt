package registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.anjali.loginapp.R

class SplashActivity : AppCompatActivity() {

    lateinit var topAnim:Animation
    lateinit var bottomAnim:Animation
    lateinit var seniors:ImageView
    lateinit var alumuni:ImageView
    lateinit var startup:ImageView
    lateinit var Teachers:ImageView
    lateinit var allInOne:TextView
    lateinit var sideAnim:Animation
    lateinit var logo:ImageView

     var SPLASH_SCREEN = 5000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //to hide statusbar also in splash activity
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        topAnim = AnimationUtils.loadAnimation(this,
            R.anim.top_animation
        )
        bottomAnim = AnimationUtils.loadAnimation(this,
            R.anim.bottom_animation
        )
        sideAnim = AnimationUtils.loadAnimation(this,
            R.anim.side_animation
        )

        seniors=findViewById(R.id.seniors)
        Teachers=findViewById(R.id.Teachers)
        startup=findViewById(R.id.startup)
        alumuni=findViewById(R.id.alumuni)
        allInOne=findViewById(R.id.allInOne)
        logo = findViewById(R.id.logo)


      alumuni.startAnimation(topAnim)
        seniors.startAnimation(topAnim)
        startup.startAnimation(bottomAnim)
        Teachers.startAnimation(bottomAnim)
        allInOne.startAnimation(sideAnim)
        logo.startAnimation(sideAnim)


        Handler().postDelayed({
            val startAct= Intent(this@SplashActivity, RegistrationLoginActivity::class.java)
            startActivity(startAct)
            finish()
        },2000)


    }
}
