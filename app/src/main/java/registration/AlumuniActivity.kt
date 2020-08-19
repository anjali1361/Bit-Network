package registration

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anjali.loginapp.Main2Activity
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hbb20.CountryCodePicker
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class AlumuniActivity : AppCompatActivity(){

    lateinit var Name: EditText
    lateinit var Email: EditText
    lateinit var Password: EditText
    lateinit var Mobile: EditText
    lateinit var batch: EditText
    lateinit var Profession: EditText
    lateinit var branch: EditText
    lateinit var history: EditText
    lateinit var memories: EditText
    lateinit var Register: Button
    lateinit var Address: EditText
    lateinit var City: EditText
    lateinit var State: EditText
    lateinit var Country: EditText
    lateinit var fill: TextView
    lateinit var dob: EditText
    lateinit var Zipcode: EditText
    lateinit var upload: TextView
    lateinit var uploadPhoto: ImageView
    lateinit var ccp:CountryCodePicker
    lateinit var Auth: FirebaseAuth//Auth is an object instance of firebase
    lateinit var Store: FirebaseFirestore
    lateinit var progressBar: ProgressBar
    lateinit var userId: String
    var downloadedUrl: String = ""
    lateinit var toolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumuni)

        Name = findViewById(R.id.name)
        Email = findViewById(R.id.email)
        Password = findViewById(R.id.Password)
        Mobile = findViewById(R.id.mobile)
        batch = findViewById(R.id.batch)
        Profession = findViewById(R.id.profession)
        history = findViewById(R.id.history)
        Register = findViewById(R.id.Register)
        branch = findViewById(R.id.branch)
        Address=findViewById(R.id.homeAddress)
        City=findViewById(R.id.city)
        State=findViewById(R.id.state)
        dob=findViewById(R.id.dob)
        Country=findViewById(R.id.country)
        Zipcode=findViewById(R.id.zipcode)
        upload=findViewById(R.id.upload)
        memories=findViewById(R.id.memories)
        uploadPhoto=findViewById(R.id.uploadPhoto)
        fill = findViewById(R.id.fill)
        progressBar = findViewById(R.id.progressBar)
        Store = FirebaseFirestore.getInstance()
        Auth = FirebaseAuth.getInstance()
        toolbar = findViewById(R.id.toolbar)
        ccp = findViewById(R.id.ccp)

        userId = Auth.currentUser?.uid.toString()

        setUpToolbar()

        if (Auth.currentUser != null) {//user has an account by checking if the current user object is present
            val intent = Intent(this@AlumuniActivity, Main2Activity::class.java)
            startActivity(intent)
            finish()
        }


        Register.setOnClickListener {
            val Email = Email.text.toString()
            val Password = Password.text.toString()
            val name = Name.text.toString()
            val CCP = ccp.selectedCountryCode
            val Phone = Mobile.text.toString()
            val Batch = batch.text.toString()
            val profession = Profession.text.toString()
            val History = history.text.toString()
            val Memories = memories.text.toString()
            val address = Address.text.toString()
            val city = City.text.toString()
            val state = State.text.toString()
            val country = Country.text.toString()
            val zipcode = Zipcode.text.toString()
            val DOB = dob.text.toString()
            val Branch = branch.text.toString()




            if (!Email.equals("") && !Password.equals("") && !name.equals("") && !Phone.equals("") && !CCP.equals("") && !Batch.equals("") && !profession.equals("") && !History.equals("") && !address.equals("")&& !city.equals("") && !state.equals("") && !country.equals("") && !Memories.equals("") && !zipcode.equals("") && !DOB.equals("") && !Branch.equals("") ) {
                if (Password.length < 6) {
                    Toast.makeText(
                        this@AlumuniActivity,
                        "Password Must Be Of >= 6 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    progressBar.visibility = View.VISIBLE


                    Auth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(this) {//addOnCompleteListener mthod is used to verify whether the task is done successfully or not
                                task ->
                            progressBar.visibility = View.GONE

                            if (task.isSuccessful) {


                                Toast.makeText(
                                    this@AlumuniActivity,
                                    "User Created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                //saving or retrieving of userid of current user that is regestering by using current instance of firebase authentication Auth


                                uploadImageToFirebase()

                                verifyEmail()



                                val intent = Intent(this@AlumuniActivity, Main2Activity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@AlumuniActivity,
                                    "Error! = " + (task.exception),
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                            }

                        }

                }

            } else {
                Toast.makeText(this@AlumuniActivity, "Enter All Credentials", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        upload.setOnClickListener {

            Log.d(
                "AlumuniActivity",
                "try to show photo selector"
            )//use of implicit intent to launch gallery
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                0
            )// the constant requestcode is used in our next phase to verify what intent our result is coming from

        }

    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("AlumuniActivity", "photo was selected")

            selectedPhotoUri = data.data

            //the below code is only meant to view the data also done by picasso
            val bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
            )//now we have access to the bitmap of the photo selected
//
//            uploadPhoto.setImageBitmap(bitmap)
//            uploadPhoto.alpha = 0f

            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadPhoto.setBackgroundDrawable(bitmapDrawable)
        }
    }
    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()//uuid  refers a unique id
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener { it ->
            Log.d("AlumuniActivity", "image uploaded successfully: ${it.metadata?.path}")

            //to access the image url created in the storage section of firebase

            ref.downloadUrl.addOnSuccessListener() {
                it.toString()   //it refers to the url
                Log.d("AlumuniActivity", "File Location: $it")
                downloadedUrl = it.toString()

                saveUser(downloadedUrl)


            }
        }

    }


    private fun verifyEmail() {
        val mAuth = Auth
        val mUser = mAuth.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@AlumuniActivity,
                    "Verification email is sent to" + mUser.email,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@AlumuniActivity,
                    "Fail to send verification email ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun saveUser(imageUrl: String) {
        userId = Auth.currentUser!!.uid

        val Documentreference =Store.collection("alumuni").document(userId)

        val user = User2(
            Name.text.toString(),
            Email.text.toString(),
            Password.text.toString(),
            Mobile.text.toString(),
            batch.text.toString(),
            Profession.text.toString(),
            history.text.toString(),
            Address.text.toString(),
            City.text.toString(),
            State.text.toString(),
            Country.text.toString(),
            dob.text.toString(),
            Zipcode.text.toString(),
            imageUrl,
            memories.text.toString(),
            ccp.selectedCountryCode,
            branch.text.toString()

        )
        Documentreference.set(user).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("AlumuniActivity", "user saved to firebase firestore")
            } else {
                Log.d("AlumuniActivity", "user not saved = " + (task.exception))

            }

        }
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Alumuni Registration"
    }

}

@Parcelize
class User2(val fname:String, val email:String, val password:String, val mobile:String, val batch:String, val profession:String, val history:String,  val homeAddress:String, val city:String, val state:String, val country:String, val dob:String, val zipcode:String,val imageUrl: String, val memories:String,val ccp:String, val branch:String):
    Parcelable {
    //hre parceable means wrapping up of object to send it from one activity to another which is not done without using experimental true in build gradle file module app
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "","","")
}

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var resetPassword: TextView
    lateinit var email3: EditText
    lateinit var resetLink: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var Login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setUpToolbar()

        resetPassword = findViewById(R.id.resetPassword)
        resetLink = findViewById(R.id.resetLink)
        email3 = findViewById(R.id.email3)
        mAuth = FirebaseAuth.getInstance()
        Login = findViewById(R.id.Login)

        resetLink.setOnClickListener{
            val Email3 = email3.text.toString()
            if(!Email3.equals("")){
                mAuth.sendPasswordResetEmail(Email3).addOnSuccessListener(this){
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Reset Link Sent To Your Email",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(
                        this@ForgotPasswordActivity,
                        LoginActivity::class.java
                    )

                    startActivity(intent)
                    finish()

                }.addOnFailureListener(this){
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Error! Reset Link Is Not Sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(
                        this@ForgotPasswordActivity,
                        LoginActivity::class.java
                    )

                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Email is required",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
        Login.setOnClickListener{
            val intent = Intent(
                this@ForgotPasswordActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "change password"
    }
}