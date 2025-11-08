package com.example.moviecatalog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.CollectionModel
import com.example.moviecatalog.databinding.CollectionScreenBinding
import com.example.moviecatalog.logic.util.CollectionsManager
import com.squareup.picasso.Picasso

class CollectionScreenActivity : AppCompatActivity() {
    private lateinit var binding: CollectionScreenBinding
    private val collectionsManager = CollectionsManager()
    private var collections: List<CollectionModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CollectionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCollections()
        setOnMainScrenClick()
        setOnProfileScreenClick()
    }

    private fun loadCollections() {
        collections = collectionsManager.getCollectionsWithDefaults(this)
        setupCollectionsList()
    }

    private fun setupCollectionsList() {
        val adapter = createCollectionsAdapter()
        binding.collectionsList.adapter = adapter
    }

    private fun createCollectionsAdapter(): ArrayAdapter<CollectionModel> {
        return object : ArrayAdapter<CollectionModel>(
            this,
            R.layout.collection_in_collection_screen,
            R.id.collection_name,
            collections
        ) {
            @SuppressLint("DiscouragedApi")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(
                    R.layout.collection_in_collection_screen,
                    parent,
                    false
                )

                val collection = getItem(position)!!
                setupCollectionView(view, collection)
                return view
            }
        }
    }

    private fun setupCollectionView(view: View, collection: CollectionModel) {
        val avatar = view.findViewById<ImageView>(R.id.collection_avatar)
        val name = view.findViewById<TextView>(R.id.collection_name)

        loadCollectionAvatar(avatar, collection)
        name.text = collection.name
    }

    @SuppressLint("DiscouragedApi")
    private fun loadCollectionAvatar(avatar: ImageView, collection: CollectionModel) {
        if (collection.avatarUrl.startsWith("android.resource://")) {
            val resourceId = resources.getIdentifier(
                collection.avatarUrl.substringAfterLast("/"),
                "drawable",
                packageName
            )
            avatar.setImageResource(resourceId)
        } else {
            Picasso.get().load(collection.avatarUrl).into(avatar)
        }
    }

    private fun setOnMainScrenClick() {
        binding.mainTextInCollectionScreen.setOnClickListener {
            navigateToMainScreen()
        }
        binding.collectionScreenTv.setOnClickListener {
            navigateToMainScreen()
        }
    }

    private fun setOnProfileScreenClick() {
        binding.profileTextInCollectionScreen.setOnClickListener {
            navigateProfileScreen()
        }
        binding.collectionScreenProfile.setOnClickListener {
            navigateProfileScreen()
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateProfileScreen() {
        val intent = Intent(this, ProfileScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}