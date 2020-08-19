package userInfo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anjali.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import fragment.NonTeachingFragment
import registration.User3

class NonTeacherInfoActivity : AppCompatActivity() {

    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var mobile: TextView
    lateinit var holding: TextView
    lateinit var homeAddress: TextView
    lateinit var city: TextView
    lateinit var ccp:TextView
    lateinit var state: TextView
    lateinit var country: TextView
    lateinit var dob: TextView
    lateinit var zipcode: TextView
    lateinit var Auth: FirebaseAuth
    lateinit var userId: String
    lateinit var profile_image: CircleImageView
    lateinit var Store: FirebaseFirestore
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_non_teacher_info)


        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        mobile = findViewById(R.id.mobile)
        holding=findViewById(R.id.holding)
        homeAddress=findViewById(R.id.homeAddress)
        city=findViewById(R.id.city)
        state=findViewById(R.id.state)
        country=findViewById(R.id.country)
        dob=findViewById(R.id.dob)
        zipcode=findViewById(R.id.zipcode)
        ccp=findViewById(R.id.ccp)
        toolbar = findViewById(R.id.toolbar)
        profile_image=findViewById(R.id.profile_image)

        Store = FirebaseFirestore.getInstance()
        Auth = FirebaseAuth.getInstance()

        val user = intent.getParcelableExtra<User3>(
            NonTeachingFragment.USER_KEY)
        setUpToolbar()
        supportActionBar?.title = user.fname


        userId = Auth.currentUser!!.uid//getting userid



        val documentReference = Store
            .collection("nonTeachers").document(userId)//var contains reference to the data that is present in the user's collection

        documentReference.addSnapshotListener { documentSnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
            if (documentSnapshot != null) {

                name.text=user.fname
                mobile.text=user.mobile
                email.text=user.email
                dob.text=user.dob
                holding.text = user.holding
                homeAddress.text=user.homeAddress
                city.text=user.city
                state.text = user.state
                country.text=user.country
                zipcode.text=user.zipcode
                ccp.text=user.ccp
                Picasso.get().load(user.imageUrl).into(profile_image)


            }


        }

    }


    fun setUpToolbar(){
        setSupportActionBar(toolbar)

    }
}













