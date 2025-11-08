package com.example.moviecatalog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.CollectionModel
import com.example.moviecatalog.databinding.CollectionScreenBinding
import com.example.moviecatalog.logic.util.CollectionsManager
import com.squareup.picasso.Picasso

class CollectionScreenActivity : AppCompatActivity() {
    private lateinit var binding: CollectionScreenBinding
    private val collectionsManager = CollectionsManager()
    private var collections: List<CollectionModel> = emptyList()
    private val effects = Effects()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CollectionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindow()
        setupWindowInsest()
        loadCollections()
        setOnMainScrenClick()
        setOnProfileScreenClick()
        setOnCreateOrChangeListener()
    }

    private fun setupWindow() {
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        effects.hideSystemBars(window)
    }

    private fun setupWindowInsest() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
        val goToIcon = view.findViewById<ImageView>(R.id.go_to_collection)

        goToIcon.setOnClickListener {
            val intent = Intent(this, ChangeCollectionActivity::class.java)
            startActivity(intent)
        }

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

    private fun setOnCreateOrChangeListener() {
        binding.addIcon.setOnClickListener {
            val intent = Intent(this, CreateCollectionActivity::class.java)
            startActivity(intent)
        }
    }
}