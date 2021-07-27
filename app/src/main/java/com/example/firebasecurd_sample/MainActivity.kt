package com.example.firebasecurd_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasecurd_sample.Adaptor.MainAdapter
import com.example.firebasecurd_sample.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.example.firebasecurd_sample.User

class MainActivity : AppCompatActivity() {
    private lateinit var databaseRef : DatabaseReference
    private lateinit var userList : ArrayList<User>
    private lateinit var binder : ActivityMainBinding
    private lateinit var keyList : MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        keyList = mutableListOf<String>()

        binder.mainRecycler.layoutManager = LinearLayoutManager(this)

        userList = arrayListOf<User>()
        databaseRef = FirebaseDatabase.getInstance().getReference("students")

        getFirebaseKeys()
        getFirebaseData(keyList)
    }
    fun getFirebaseKeys(){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    keyList.clear()
                    // "students 하위에 있는 키만 가져온다"
                    snapshot.children.forEach {
                        keyList.add(it.key.toString())
                    }
                    Log.d("MainActivity", "keys: $keyList")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "getFirebaseKeys: canceled")
            }
        })
    }

    fun getFirebaseData(keyList : MutableList<String>){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    userList.clear()
                    // 각각의 키를 이용하여 각 항목에 접근하고 데이터를 가져온다
                    keyList.forEach {
                        val data = snapshot.child(it).getValue(User::class.java)
                        userList.add(data!!)
                    }
                    Log.d("MainActivity", "userList: $userList")
                    binder.mainRecycler.adapter = MainAdapter(userList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "getFirebaseData: canceled")
            }
        })
    }
}