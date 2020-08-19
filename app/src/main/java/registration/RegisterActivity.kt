

package registration

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.anjali.loginapp.Main2Activity
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hbb20.CountryCodePicker
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*



import java.util.*

class RegisterActivity : AppCompatActivity() {

    lateinit var Name: EditText
    lateinit var Email: EditText
    lateinit var Password: EditText
    lateinit var Mobile: EditText
    lateinit var branch: EditText
    lateinit var batch: EditText
    lateinit var club: EditText
    lateinit var hoby:EditText
    lateinit var Register: Button
    lateinit var homeAddress:EditText
    lateinit var City:EditText
    lateinit var State:EditText
    lateinit var Country:EditText
    lateinit var ccp:CountryCodePicker
    lateinit var fill: TextView
    lateinit var dob:EditText
    lateinit var Zipcode:EditText
    lateinit var upload:TextView
    lateinit var upload_photo: ImageView
    lateinit var Auth: FirebaseAuth//Auth is an object instance of firebase
    lateinit var Store: FirebaseFirestore
    lateinit var progressBar: ProgressBar
    lateinit var interest:EditText
    lateinit var userId: String
    var downloadedUrl: String = ""
    lateinit var toolbar: Toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        Name = findViewById(R.id.name)
        Email = findViewById(R.id.email)
        Password = findViewById(R.id.Password)
        Mobile = findViewById(R.id.mobile)
        branch = findViewById(R.id.branch)
        batch = findViewById(R.id.batch)
        club = findViewById(R.id.club)
        Register = findViewById(R.id.Register)
        homeAddress=findViewById(R.id.homeAddress)
        City=findViewById(R.id.city)
        State=findViewById(R.id.state)
        dob=findViewById(R.id.dob)
        Country=findViewById(R.id.country)
        Zipcode=findViewById(R.id.zipcode)
        upload=findViewById(R.id.upload)
        hoby=findViewById(R.id.hoby)
        ccp=findViewById(R.id.ccp)
        toolbar = findViewById(R.id.toolbar)
        interest=findViewById(R.id.interest)
        fill = findViewById(R.id.fill)
        upload_photo = findViewById(R.id.uploadPhoto)
        progressBar = findViewById(R.id.progressBar)
        Store = FirebaseFirestore.getInstance()
        Auth = FirebaseAuth.getInstance()//to get the current instance of database from firebase show that we can perform various operation on it or it is the firebase authentication service reference which we will use to authenticate and create a new user

        setUpToolbar()



        if (Auth.currentUser != null) {//user has an account by checking if the current user object is present
            val intent = Intent(this@RegisterActivity, Main2Activity::class.java)
            startActivity(intent)
            finish()
        }

        Register.setOnClickListener {
            val Email = Email.text.toString()
            val Password = Password.text.toString()
            val name = Name.text.toString()
            val Phone = Mobile.text.toString()
            val Club = club.text.toString()
            val Batch = batch.text.toString()
            val Branch = branch.text.toString()
            val Hoby = hoby.text.toString()
            val HomeAddress = homeAddress.text.toString()
            val city = City.text.toString()
            val state = State.text.toString()
            val country = Country.text.toString()
            val zipcode = Zipcode.text.toString()
            val DOB = dob.text.toString()
            val Interest = interest.text.toString()
            val CCP=ccp.selectedCountryCode



            if (!Email.equals("") && !Password.equals("") && !name.equals("") && !Phone.equals("") && !Club.equals("") && !Batch.equals("") && !Branch.equals("") && !Hoby.equals("") && !HomeAddress.equals("")&& !city.equals("") && !state.equals("") && !country.equals("") && !Branch.equals("") && !zipcode.equals("") && !DOB.equals("") && !Interest.equals("") && !CCP.equals("")) {
                if (Password.length < 6) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password Must Be Of >= 6 Characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    progressBar.visibility = View.VISIBLE

                    //to register the user in firebase

                    Auth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(this) {//addOnCompleteListener mthod is used to verify whether the task is done successfully or not
                                task ->
                            progressBar.visibility = View.GONE

                            if (task.isSuccessful) {


                                Toast.makeText(
                                    this@RegisterActivity,
                                    "User Created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                //saving or retrieving of userid of current user that is regestering by using current instance of firebase authentication Auth


                                uploadImageToFirebase()

                                verifyEmail()

                                val intent = Intent(this@RegisterActivity, Main2Activity::class.java)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Error! = " + (task.exception),
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                            }

                        }

                }

            } else {
                Toast.makeText(this@RegisterActivity, "Enter All Credentials", Toast.LENGTH_SHORT)
                    .show()
            }

        }


        upload.setOnClickListener {

            Log.d(
                "RegisterActivity",
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
            Log.d("RegisterActivity", "photo was selected")

            selectedPhotoUri = data.data

            //the below code is only meant to view the data also done by picasso
            val bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
            )//now we have access to the bitmap of the photo selected

            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadPhoto.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()//uuid  refers a unique id
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener { it ->
            Log.d("RegisterActivity", "image uploaded successfully: ${it.metadata?.path}")

            //to access the image url created in the storage section of firebase

            ref.downloadUrl.addOnSuccessListener() {
                it.toString()   //it refers to the url
                Log.d("RegisterActivity", "File Location: $it")
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
                    this@RegisterActivity,
                    "Verification email is sent to" + mUser.email,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Fail to send verification email ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun saveUser(imageUrl: String) {
        userId = Auth.currentUser!!.uid

        val Documentreference =Store.collection("users").document(userId)

        val user = User(
            Name.text.toString(),
            Email.text.toString(),
            Password.text.toString(),
            Mobile.text.toString(),
            branch.text.toString(),
            club.text.toString(),
            hoby.text.toString(),
            homeAddress.text.toString(),
            City.text.toString(),
            State.text.toString(),
            Country.text.toString(),
            dob.text.toString(),
            Zipcode.text.toString(),
            imageUrl,
            batch.text.toString(),
            interest.text.toString(),
            ccp.selectedCountryCode


        )
        Documentreference.set(user).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("RegisterActivity", "user saved to firebase firestore")
            } else {
                Log.d("RegisterActivity", "user not saved = " + (task.exception))

            }

        }
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Seniors Registration"
    }

}
@Parcelize
class User(var fname:String, val email:String, val password:String, val mobile:String, val branch:String, val club:String, val hoby:String, val homeAddress:String, val city:String, val state:String, val country:String, val dob:String, val zipcode:String, val imageUrl: String, val batch:String, val interest:String, val ccp:String): Parcelable{//hre parceable means wrapping up of object to send it from one activity to another which is not done without using experimental true in build gradle file module app
constructor(): this("","","","","","","","","","","","","","","","","")

}
