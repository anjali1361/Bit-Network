package registration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anjali.loginapp.Main2Activity
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog.view.*

class LoginActivity : AppCompatActivity() {

    lateinit var email2:EditText
    lateinit var password2:EditText
    lateinit var login:Button
    lateinit var createnewAccount:TextView
    lateinit var loginAccount:TextView
    lateinit var forgotPassword:TextView
    lateinit var progressBar2: ProgressBar
    private lateinit var Auth:FirebaseAuth
    lateinit var toolbar: Toolbar

    private  val TAG = "LoginActivity"
//    var downloadedUrl: String = ""


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        toolbar = findViewById(R.id.toolbar)

         setUpToolbar()

         email2 = findViewById(R.id.email2)
         password2 =  findViewById(R.id.password2)
        login = findViewById(R.id.login1)
        createnewAccount = findViewById(R.id.createnewAccount)
        loginAccount = findViewById(R.id.loginAccount)
        forgotPassword = findViewById(R.id.forgotPassword)
        progressBar2 = findViewById(R.id.progressBar2)
        Auth = FirebaseAuth.getInstance()


        if (Auth.currentUser != null) {//user has an account by checking if the current user object is present
            val intent = Intent(this@LoginActivity, Main2Activity::class.java)
            startActivity(intent)
            finish()
        }


   login.setOnClickListener{
       val Email2:String = email2.text.toString()
       val Password2:String = password2.text.toString()

      if(!Email2.equals("") && !Password2.equals("")){

//          if(Password2.length<6){
//              Toast.makeText(this@LoginActivity,"Invalid Password",Toast.LENGTH_SHORT).show()
//          }else {
              progressBar2.visibility = View.VISIBLE

              //authentication of user

              Auth.signInWithEmailAndPassword(Email2, Password2)
                  .addOnCompleteListener(this) {//addOnCompleteListener mthod is used to verify whether the task is done successfully or not
                          task ->
                      progressBar2.visibility = View.GONE

                      if (task.isSuccessful) {
                          Toast.makeText(
                              this@LoginActivity,
                              "Logged In Successfully",
                              Toast.LENGTH_SHORT
                          ).show()
                          val intent = Intent(this@LoginActivity, Main2Activity::class.java)
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                          intent.putExtra("URL",downloadedUrl)
                          startActivity(intent)
                      } else {
                          Toast.makeText(
                              this@LoginActivity,
                              "Error! = " + (task.exception),
                              Toast.LENGTH_SHORT
                          ).show()
                          progressBar2.visibility = View.GONE
                      }

//                  }
          }
      }else{
          Toast.makeText(this@LoginActivity,"Enter all Details",Toast.LENGTH_SHORT).show()

      }


     }

        loginAccount.setOnClickListener{

            val view = LayoutInflater.from(this).inflate(R.layout.dialog,null)

            val dialog = AlertDialog.Builder(this).setView(view).setTitle("Registration form")

            val mAlertDialog = dialog.show()

            view.alumuni.setOnClickListener{
                val intent = Intent(this@LoginActivity,
                    AlumuniActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.startup.setOnClickListener{
                val intent = Intent(this@LoginActivity,
                    StartupActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.teacher.setOnClickListener{
                val intent = Intent(this@LoginActivity,
                    TeachersActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.nonteacher.setOnClickListener{
                val intent = Intent(this@LoginActivity,
                    NonTeachersActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.seniors.setOnClickListener{
                val intent = Intent(this@LoginActivity,
                    RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            view.exit.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(this@LoginActivity,
                ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Login"
    }
}
