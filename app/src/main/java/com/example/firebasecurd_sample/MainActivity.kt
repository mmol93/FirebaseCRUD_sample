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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.mainRecycler.layoutManager = LinearLayoutManager(this)
        binder.mainRecycler.setHasFixedSize(true)

        userList = arrayListOf<User>()
        getFirebaseData()
        Log.d("test", "currentFocus: ${this.currentFocus}")
    }

    fun getFirebaseData(){
        // path: Realtime Database에서 (1번 사용할 경우) 최상위 항목의 이름
        databaseRef = FirebaseDatabase.getInstance().getReference("teacher")
        // addValueEventListener: 데이터베이스의 값이 달라진 경우 해당 함수 호출
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    // User 클래스에 있는 데이터 변수 = 키
                    // 즉, getValue를 이용하여 Hash 형태의 데이터를 가져와서 리스트에 넣음
                    val data = snapshot.getValue(User::class.java)
                    userList.add(data!!)
                    Log.d("test", "test: $data")

                    binder.mainRecycler.adapter = MainAdapter(userList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}