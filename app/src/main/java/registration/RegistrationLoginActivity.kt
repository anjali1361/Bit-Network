package registration

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.anjali.loginapp.R
import kotlinx.android.synthetic.main.activity_registration_login.*
import kotlinx.android.synthetic.main.dialog.view.*


class RegistrationLoginActivity : AppCompatActivity() {

    lateinit var register1:Button
    lateinit var login1:Button
    lateinit var toolbar: Toolbar

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_login)

        toolbar = findViewById(R.id.toolbar)

        setUpToolbar()

        register1 = findViewById(R.id.register1)
        login1=findViewById(R.id.login1)

        register1.setOnClickListener{
            val view = LayoutInflater.from(this).inflate(R.layout.dialog,null)
            val dialog = AlertDialog.Builder(this).setView(view).setTitle("Registration form")
            val mAlertDialog = dialog.show()
            view.alumuni.setOnClickListener{
                val intent = Intent(this@RegistrationLoginActivity, AlumuniActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.startup.setOnClickListener{
                val intent = Intent(this@RegistrationLoginActivity, StartupActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.teacher.setOnClickListener{
                val intent = Intent(this@RegistrationLoginActivity, TeachersActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.nonteacher.setOnClickListener{
                val intent = Intent(this@RegistrationLoginActivity, NonTeachersActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.seniors.setOnClickListener{
                val intent = Intent(this@RegistrationLoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.exit.setOnClickListener{
               mAlertDialog.dismiss()
            }


        }
        login1.setOnClickListener{
            val intent = Intent(this@RegistrationLoginActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Authentication"
    }
}
