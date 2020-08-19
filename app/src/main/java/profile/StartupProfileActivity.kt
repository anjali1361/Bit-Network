package profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Guideline
import com.anjali.loginapp.Main2Activity
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.startup_update.view.*
import registration.LoginActivity

class StartupProfileActivity : AppCompatActivity() {

    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var mobile: TextView
    lateinit var batch: TextView
    lateinit var branch: TextView
    lateinit var field: TextView
    lateinit var startupName: TextView
    lateinit var address: TextView
    lateinit var city: TextView
    lateinit var state: TextView
    lateinit var country: TextView
    lateinit var about: TextView
    lateinit var dob: TextView
    lateinit var zipcode: TextView
    lateinit var inspiration: TextView
    lateinit var Udetails: Button
    lateinit var profile_image:CircleImageView
    lateinit var ccp:TextView
    lateinit var Auth: FirebaseAuth//Auth is an object instance of firebase
    lateinit var Store: FirebaseFirestore
    lateinit var userId: String
    lateinit var toolbar: Toolbar
    var Name:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_profile)

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        mobile = findViewById(R.id.mobile)
        batch = findViewById(R.id.batch)
        startupName = findViewById(R.id.startupName)
        branch = findViewById(R.id.branch)
        address=findViewById(R.id.address)
        city=findViewById(R.id.city)
        state=findViewById(R.id.state)
        dob=findViewById(R.id.dob)
        country=findViewById(R.id.country)
        zipcode=findViewById(R.id.zipcode)
        field=findViewById(R.id.field)
        inspiration=findViewById(R.id.inspiration)
        about=findViewById(R.id.about)
        profile_image=findViewById(R.id.profile_image)
        Store = FirebaseFirestore.getInstance()
        Auth = FirebaseAuth.getInstance()
        toolbar = findViewById(R.id.toolbar)
        Udetails=findViewById(R.id.Udetails)
        ccp = findViewById(R.id.ccp)

        userId = Auth.currentUser?.uid.toString()


        setUpToolbar()

        val documentReference = Store.collection("startup").document(userId)
        documentReference.addSnapshotListener(this
        ) { documentSnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
            if (documentSnapshot != null) {
                name.setText(documentSnapshot.getString("fname"))
                email.text = documentSnapshot.getString("email")
                mobile.setText(documentSnapshot.getString("mobile"))
                address.setText(documentSnapshot.getString("homeAddress"))
                city.setText(documentSnapshot.getString("city"))
                state.setText(documentSnapshot.getString("state"))
                country.setText(documentSnapshot.getString("country"))
                batch.setText(documentSnapshot.getString("batch"))
                branch.setText(documentSnapshot.getString("branch"))
                zipcode.setText(documentSnapshot.getString("zipcode"))
                ccp.setText(documentSnapshot.getString("ccp"))
                field.setText(documentSnapshot.getString("field"))
                inspiration.setText(documentSnapshot.getString("inspiration"))
                dob.setText(documentSnapshot.getString("dob"))
                Picasso.get().load(documentSnapshot.getString("imageUrl")).into(profile_image)
                about.setText(documentSnapshot.getString("about"))
                startupName.setText(documentSnapshot.getString("startupname"))

                Name = name.text.toString()

            }
        }

        Udetails.setOnClickListener{

            if(Name?.isBlank()!!){

                Toast.makeText(this,"You Are Not Eligible Updated Anything",Toast.LENGTH_SHORT).show()

            }

            else {

                val view = LayoutInflater.from(this).inflate(R.layout.startup_update, null)
                val dialog =
                    AlertDialog.Builder(this).setView(view).setTitle("Update Any Credentials ")


                val mAlertDialog = dialog.show()

                view.ok1.setOnClickListener {
                    val Mobile = view.umobile1.text.toString()
                    val Startupname = view.ustartupname1.text.toString()
                    val CCP = view.uccp1.selectedCountryCode
                    val Address = view.uaddress1.text.toString()
                    val City = view.ucity1.text.toString()
                    val State = view.ustate1.text.toString()
                    val Country = view.ucountry1.text.toString()
                    val Zipcode = view.uzipcode1.text.toString()
                    val About = view.uabout1.text.toString()
                    val Branch = view.ubranch1.text.toString()
                    val Batch = view.ubatch1.text.toString()


                    if (!Mobile.equals("") && !CCP.equals("")) {
                        documentReference.update("mobile", Mobile)
                        documentReference.update("ccp", CCP)
                    }
                    if (!Startupname.equals("") && !CCP.equals("")) {
                        documentReference.update("startupname", Startupname)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !Address.equals("")) {
                        documentReference.update("homeAddress", Address)
                        documentReference.update("ccp", CCP)
                    }
                    if (!City.equals("") && !CCP.equals("")) {
                        documentReference.update("city", City)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !State.equals("")) {
                        documentReference.update("state", State)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !Country.equals("")) {
                        documentReference.update("country", Country)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !Zipcode.equals("")) {
                        documentReference.update("zipcode", Zipcode)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !Branch.equals("")) {
                        documentReference.update("branch", Branch)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !Batch.equals("")) {
                        documentReference.update("batch", Batch)
                        documentReference.update("ccp", CCP)
                    }
                    if (!CCP.equals("") && !About.equals("")) {
                        documentReference.update("about", About)
                        documentReference.update("ccp", CCP)
                    }
                    mAlertDialog.dismiss()
                    Toast.makeText(
                        this@StartupProfileActivity,
                        "Information Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                view.exit1.setOnClickListener {
                    mAlertDialog.dismiss()
                }

            }


        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when( item.itemId) {

            R.id.menu_signout ->{
                Auth.signOut()
                val intent = Intent(this@StartupProfileActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            android.R.id.home ->{
                val intent = Intent(this@StartupProfileActivity,
                    Main2Activity::class.java)
                startActivity(intent)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "View Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.signout,menu)
        return super.onCreateOptionsMenu(menu)
    }
}


