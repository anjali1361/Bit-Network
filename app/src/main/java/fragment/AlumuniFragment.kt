package fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuItemCompat.getActionView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import userInfo.MainActivity
import com.anjali.loginapp.R
import com.anjali.loginapp.util.ConnectionManager
import registration.User2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recycler_profile_user_single_row.view.*
import userInfo.AlumuniInfoActivity
import java.util.*

@Suppress("UNREACHABLE_CODE", "NAME_SHADOWING")
class AlumuniFragment : Fragment() {

    lateinit var Auth: FirebaseAuth
    lateinit var Store: FirebaseFirestore
    lateinit var recyclerList: RecyclerView
    lateinit var userId: String
    val documentReference = FirebaseFirestore.getInstance().collection("alumuni")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alumuni, container, false)


        recyclerList = view.findViewById(R.id.recyclerList)

        Auth = FirebaseAuth.getInstance()
        Store = FirebaseFirestore.getInstance()

        userId = Auth.currentUser?.uid.toString()

        if (!ConnectionManager().checkConnectivity(activity as Context)){
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

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

                    val user = it.toObject(User2::class.java)

                    adapter.add(UserItem5(user))

                    adapter.setOnItemClickListener { item, view ->

                        val userItem = item as UserItem5
                        val intent = Intent(context, AlumuniInfoActivity::class.java)
                        intent.putExtra(
                            USER_KEY,
                            userItem.user
                        )//we hava to extend the class User to parceable
                        startActivity(intent)
                    }
//                    recyclerList.adapter = adapter
                }

        }

            .addOnFailureListener{exception ->
                Log.d("AlumuniFragment","Getting error:",exception)

            }
    }
}


//@Suppress("NAME_SHADOWING")
class UserItem5(val user: User2): Item<GroupieViewHolder>(){// our class is child class of Item class of groupie

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
