package userInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import fragment.AlumuniFragment
import registration.User2

class AlumuniInfoActivity : AppCompatActivity() {


    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var mobile: TextView
    lateinit var branch: TextView
    lateinit var batch: TextView
    lateinit var memories: TextView
    lateinit var history: TextView
    lateinit var homeAddress: TextView
    lateinit var city: TextView
    lateinit var state: TextView
    lateinit var country: TextView
    lateinit var dob: TextView
    lateinit var proffession:TextView
    lateinit var ccp:TextView
    lateinit var zipcode: TextView
    lateinit var Auth: FirebaseAuth
    lateinit var userId: String
    lateinit var profile_image: CircleImageView
    lateinit var Store: FirebaseFirestore
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumuni_info)


        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        mobile = findViewById(R.id.mobile)
        branch = findViewById(R.id.branch)
        memories = findViewById(R.id.memories)
        batch = findViewById(R.id.batch)
        history=findViewById(R.id.history)
        homeAddress=findViewById(R.id.homeAddress)
        city=findViewById(R.id.city)
        state=findViewById(R.id.state)
        ccp=findViewById(R.id.ccp)
        country=findViewById(R.id.country)
        proffession=findViewById(R.id.proffession)
        dob=findViewById(R.id.dob)
        zipcode=findViewById(R.id.zipcode)
        profile_image=findViewById(R.id.profile_image)

        Store = FirebaseFirestore.getInstance()
        Auth = FirebaseAuth.getInstance()

        toolbar = findViewById(R.id.toolbar)

        val user = intent.getParcelableExtra<User2>(
            AlumuniFragment.USER_KEY)
        setUpToolbar()
        supportActionBar?.title = user.fname



        userId = Auth.currentUser!!.uid//getting userid


        //retrieving of data from firestore database and assigning to respective fields of profile

        val documentReference = Store
            .collection("alumuni").document(userId)//var contains reference to the data that is present in the user's collection

        documentReference.addSnapshotListener { documentSnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
            if (documentSnapshot != null) {

                name.text=user.fname
                mobile.text=user.mobile
                email.text=user.email
                dob.text=user.dob
                branch.text=user.branch
                batch.text=user.batch
                memories.text=user.memories
                history.text = user.history
                homeAddress.text=user.homeAddress
                proffession.text=user.profession
                city.text=user.city
                state.text = user.state
                country.text=user.country
                zipcode.text=user.zipcode
                Picasso.get().load(user.imageUrl).into(profile_image)
                ccp.text=user.ccp


            }


        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)

    }
}




