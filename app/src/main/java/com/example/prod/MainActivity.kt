package com.example.prod

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import com.example.apis.NewsViewModel
import com.example.financedate.FinanceViewModel
import com.example.navigation.AppNavigation
import com.example.notesdata.NotesViewModel
import com.example.tickersapi.TickersViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {
    @Inject
    lateinit var newsViewModel: NewsViewModel
    @Inject
    lateinit var tickersViewModel: TickersViewModel
    @Inject
    lateinit var financeViewModel: FinanceViewModel
    @Inject
    lateinit var notesViewModel: NotesViewModel


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { notesViewModel.saveImageUri(it.toString()) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).appComponent.inject(this)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        fun openGallery() {
            pickImageLauncher.launch("image/*")
        }
        setContent {
            AppNavigation(tickersViewModel, newsViewModel, financeViewModel, notesViewModel,
                { openGallery() })
        }


    }
}

