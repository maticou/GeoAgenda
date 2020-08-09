package com.example.geoagenda.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.geoagenda.MainActivity
import com.example.geoagenda.R
import com.example.geoagenda.ui.Category.Category
import com.example.geoagenda.ui.Category.CategoryViewAdapter
import com.example.geoagenda.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*


class CategoriesFragment : Fragment(), CategoryViewAdapter.OnCategoryItemClickListener {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var auth: FirebaseAuth
    private var categoryList = ArrayList<Category>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel =
                ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_categories, container, false)

        getCategories()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //println(categoryList.size)
        recycler_view.adapter = CategoryViewAdapter(categoryList, this)
        recycler_view.layoutManager = GridLayoutManager(this.context,2)
        recycler_view.setHasFixedSize(true)
    }
    override fun onItemClick(categories: Category, position: Int) {
        val fragment = CategoriesFragment()
        val arguments = Bundle()
        arguments.putString("categ",categories.id )
        fragment.setArguments(arguments)
        val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
        Log.d("categoria",categories.title)
        transaction.replace(R.id.category_container,HomeFragment.newInstance(categories.id))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getCategories(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReferenceFromUrl("https://mementos-da7d9.firebaseio.com/")

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val rootRef = myRef.child(user?.uid.toString()).child("Categorias")

        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val values = dataSnapshot.children

                categoryList.clear()

                values.forEach {
                    val data = it.value as HashMap<String, String>

                    val newCat = Category(data.get("id").toString(),
                        data.get("title").toString(),
                        data.get("image").toString())

                    //println(newReminder)
                    categoryList.add(newCat)
                }

                recycler_view.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
