package com.example.expandablelist

import android.annotation.SuppressLint
import android.content.res.loader.ResourcesLoader
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.expandablelist.ui.theme.ExpandableListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by lazy { AuthorViewModel() }
        setContent {
            ExpandableListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(viewModel)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorExpandableList(viewModel: AuthorViewModel, navController: NavController) {

    val response = viewModel.authorList.collectAsState()
    val authorList = response.value

    var expandableState by remember {
        mutableIntStateOf(-1)
    }

    LaunchedEffect(Unit) {
        viewModel.getAuthorList()
    }

    when (authorList) {
        is AuthorViewModel.ApiState.Success -> {

            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Top app bar")
                        },
                    )
                }
            ) {
                LazyColumn {
                    itemsIndexed(authorList.data) { index, item ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                            shape = MaterialTheme.shapes.medium,
                            onClick = {
                                expandableState = if (expandableState == index) -1 else index
                            }) {
                            Column() {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .wrapContentHeight()
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .crossfade(true)
                                            .data(item.downloadUrl)
                                            .build(),
                                        modifier = Modifier.size(50.dp),
                                        contentScale = ContentScale.FillWidth,
                                        contentDescription = "Author image",
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    item.author?.let { it1 ->
                                        Text(
                                            text = it1,
                                            modifier = Modifier.weight(1f, true)
                                        )
                                    }
                                    IconButton(onClick = {
                                        expandableState = if (expandableState == index) -1 else index
                                    }) {
                                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "")
                                    }
                                    IconButton(onClick = {
                                        navController.navigate(Route.DetailsScreen.route + "/$index")
                                    }) {
                                        Icon(Icons.Rounded.ArrowForward, contentDescription = "")
                                    }
                                }
                            }
                            if (expandableState == index) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .crossfade(true)
                                        .scale(Scale.FIT)
                                        .data(item.downloadUrl)
                                        .build(),
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(20.dp),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
            }
        }

        is AuthorViewModel.ApiState.Error -> {
            Text(text = authorList.error)
        }

        is AuthorViewModel.ApiState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(viewModel: AuthorViewModel, selectedItemPos: Int, navController: NavController) {
    val response = viewModel.authorList.collectAsState()
    val selectedItem1 = response.value
    val selectedItem = (selectedItem1 as AuthorViewModel.ApiState.Success).data[selectedItemPos]

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Details Screen") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedItem.downloadUrl)
                    .crossfade(true)
                    .build(), contentDescription = "",
                modifier = Modifier.padding(10.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun Navigation(viewModel: AuthorViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.AuthorScreen.route) {
        composable(Route.AuthorScreen.route) {
            AuthorExpandableList(viewModel, navController)
        }
        composable(
            Route.DetailsScreen.route + "/{selectedItem}",
            arguments = listOf(navArgument("selectedItem") { type = NavType.IntType })
        ) { backstack ->
            backstack.arguments?.let {
                DetailsScreen(
                    viewModel,
                    it.getInt("selectedItem"),
                    navController
                )
            }
        }
    }
}


sealed class Route(val route: String) {
    object AuthorScreen : Route("AuthorScreen")
    object DetailsScreen : Route("DetailScreen")
}