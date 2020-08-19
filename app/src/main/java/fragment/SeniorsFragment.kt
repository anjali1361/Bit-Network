package fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import userInfo.MainActivity
import com.anjali.loginapp.R
import registration.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recycler_profile_user_single_row.view.*
import registration.User3

/**
 * A simple [Fragment] subclass.
 */
@Suppress("NAME_SHADOWING")
class SeniorsFragment : Fragment() {

    lateinit var Auth: FirebaseAuth
    lateinit var Store: FirebaseFirestore
    lateinit var recyclerList: RecyclerView
    lateinit var userId: String

    val documentReference = FirebaseFirestore.getInstance().collection("users")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_seniors, container, false)

        recyclerList = view.findViewById(R.id.recyclerList)


        Auth = FirebaseAuth.getInstance()
        Store = FirebaseFirestore.getInstance()

        userId = Auth.currentUser?.uid.toString()

        fetchUser()

        return view
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }


    private fun fetchUser() {



        documentReference.orderBy("fname").get().addOnSuccessListener{


            val adapter = GroupAdapter<GroupieViewHolder>()

            recyclerList.adapter = adapter


            it.forEach {

                Log.d(
                    "AlumuniFragment",
                    "$it"
                )


                val user = it.toObject(User::class.java)


                adapter.add(UserItem(user))
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra(
                        USER_KEY,
                        userItem.user
                    )//we hava to extend the class User to parceable
                    startActivity(intent)
                }
                recyclerList.adapter = adapter
            }

        }
            .addOnFailureListener{exception ->
                Log.d("SeniorsFragment","Getting error:",exception)

            }

    }
}


//@Suppress("NAME_SHADOWING")
class UserItem(val user: User): Item<GroupieViewHolder>(){// our class is child class of Item class of groupie

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //will be called in our list for each user object later on....
        viewHolder.itemView.accountHolder.text = user.fname
        viewHolder.itemView.General.text = user.branch
        viewHolder.itemView.General2.text = user.batch
        Picasso.get().load(user.imageUrl).into(viewHolder.itemView.profile_image)


    }
    override fun getLayout(): Int {
        return R.layout.recycler_profile_user_single_row
    }



}
//class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    fun setUserName(Username :String){
//        val accountHolder = itemView.accountHolder
//        accountHolder.setText(Username)
//    }
//}
