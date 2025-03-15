package de.arjmandi.navvistask.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import de.arjmandi.navvistask.ui.number_list.NumberListScreen
import de.arjmandi.navvistask.ui.number_list.NumberViewModel
import de.arjmandi.navvistask.ui.theme.NavvisTaskTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NumberViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavvisTaskTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        NumberListScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}