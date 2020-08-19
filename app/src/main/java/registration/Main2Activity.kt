package com.anjali.loginapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fragment.*
import kotlinx.android.synthetic.main.dialog.view.*
import profile.*
import registration.LoginActivity

class Main2Activity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var Auth:FirebaseAuth
    lateinit var fStore:FirebaseFirestore
    var previousMenuItem: MenuItem? = null
    lateinit var toolbar: Toolbar

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {//responcible for opening the 1st screen of the activity,hence openAlumuni fun is called here
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        navigationView=findViewById(R.id.navigationView)
        frameLayout=findViewById(R.id.frameLayout)
        toolbar=findViewById(R.id.toolbar)
        Auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

            setUpToolBar()
            openAlumuni()

        //defining working of hamburger ic_launcher_foreground
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@Main2Activity,drawerLayout,R.string.open_drawer,R.string.close_drawer)//ActionBarDrawerToggle is a class,the last two parameter defines the action of drawerToggle i.e.ic_launcher_foreground

        drawerLayout.addDrawerListener(actionBarDrawerToggle)//set click listener on toggle or whenever ic_launcher_foreground is pressed it gets notified about it and then perform some action,gives functionality to toggle,but to make it work we hve to make the home button also functional as toggle is placed in place of this only

        actionBarDrawerToggle.syncState()//synchronizing the state of toggle,this code is used to change the hamburger ic_launcher_foreground to a back arrow ic_launcher_foreground and vice versa,or performs animation

        //handling clicklistenet on itms of menu

        navigationView.setNavigationItemSelectedListener {//concept of lambda syntax

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable= true
            it.isChecked=true
            previousMenuItem = it

            when(it.itemId) {//it refers to the currently selected item
                R.id.alumuni -> {
                    openAlumuni()
                }
                R.id.startup -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
                        StartupFragment()
                    ).commit()
                    supportActionBar?.title = "Startup Network"
                    drawerLayout.closeDrawers()
                }
                R.id.teachers -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
                        TeachingFragment()
                    ).commit()
                    supportActionBar?.title = "Teaching Faculty Network"
                    drawerLayout.closeDrawers()
                }
                R.id.nonTeachers -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
                        NonTeachingFragment()
                    ).commit()
                    supportActionBar?.title = "Non Teaching Faculty Network"
                    drawerLayout.closeDrawers()
                }
                R.id.seniors -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
                        SeniorsFragment()
                    ).commit()
                    supportActionBar?.title = "Seniors Network"
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {

                    val view = LayoutInflater.from(this).inflate(R.layout.dialog,null)
                    val dialog = AlertDialog.Builder(this).setView(view).setTitle("User's Profile")
                    val mAlertDialog = dialog.show()
                    view.alumuni.setOnClickListener{
                        val intent = Intent(this@Main2Activity, AlumuniProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    view.startup.setOnClickListener{
                        val intent = Intent(this@Main2Activity, StartupProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    view.teacher.setOnClickListener{
                        val intent = Intent(this@Main2Activity, TeachersProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    view.nonteacher.setOnClickListener{
                        val intent = Intent(this@Main2Activity, NonteachersProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    view.seniors.setOnClickListener {
                        val intent = Intent(this@Main2Activity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    view.exit.setOnClickListener{
                        mAlertDialog.dismiss()
                    }
                    drawerLayout.closeDrawers()
                }
                R.id.signOut -> {

                    Auth.signOut()
                    val intent = Intent(this@Main2Activity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()


                }

            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bit Sindri Portal"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    fun openAlumuni(){
        val fragment = AlumuniFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title ="Alumuni Network"
        navigationView.setCheckedItem(R.id.alumuni)
    }

    override fun onBackPressed() {
     val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is AlumuniFragment -> openAlumuni()
            else->super.onBackPressed()
        }

    }
//handling the clicks of action bar
    @SuppressLint("WrongConstant")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      val id = item.itemId

      if(id == android.R.id.home){//since toggle is placed in place of action bar
        drawerLayout.openDrawer(GravityCompat.START)//to open drawer from the side where the screen starts
    }

    return super.onOptionsItemSelected(item)
    }

}
