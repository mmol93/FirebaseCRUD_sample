package com.example.firebasecurd_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
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

    // ActionBar 적용하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu!!.findItem(R.id.search)

        val searchView = item.actionView as SearchView
        searchView.queryHint = "Search Student's name"

        val searchListener = object : SearchView.OnQueryTextListener{
            // 서치뷰에서 텍스트를 입력하고 확인을 눌렀을 때
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchName(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        }

        // 서치뷰에 리스너 등록
        searchView.setOnQueryTextListener(searchListener)
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->

        }

        return true
    }

    // 서치뷰에서 사용될 query 형태로 검색
    fun searchName(name : String?){
        if (name != null){
            userList.clear()
            databaseRef.orderByChild("name").startAt(name).endAt("$name~")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // 하위 값을 하나씩 모두 가져온다
                        for (i in snapshot.children){
                            Log.d("MainActivity", "값: $i")
                            val data = i.getValue(User::class.java)
                            userList.add(data!!)
                        }
                        binder.mainRecycler.adapter = MainAdapter(userList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    // 한 단계 하위 데이터의 모든 key 값을 keyList라는 리스트 변수에 저장하는 함수
    fun getFirebaseKeys(){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    keyList.clear()
                    // "students 하위에 있는 키만 가져온다"
                    snapshot.children.forEach {
                        keyList.add(it.key.toString())
                    }
//                    Log.d("MainActivity", "keys: $keyList")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "getFirebaseKeys: canceled")
            }
        })
    }

    // 하위의 모든 키를 이용하여 각 키의 값을 가져와서 recyclerView를 만드는 함수
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
//                    Log.d("MainActivity", "userList: $userList")
                    binder.mainRecycler.adapter = MainAdapter(userList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "getFirebaseData: canceled")
            }
        })
    }
}