package com.example.nimbus.ui.screens

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nimbus.domain.PieChartData
import com.example.nimbus.ui.screens.ui.theme.NimbusTheme
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList
import com.example.nimbus.R
import com.example.nimbus.dto.Team.TeamTransferDTO
import com.example.nimbus.ui.components.CustomLoading
import com.example.nimbus.ui.components.TeamCard
import com.example.nimbus.ui.components.TopNavigation
import com.example.nimbus.ui.screens.fragments.drawFadingEdges
import com.example.nimbus.ui.theme.Orange300
import com.example.nimbus.ui.theme.Orange500
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.viewmodels.GlobalViewModel
import com.example.nimbus.ui.viewmodels.MyTeamsModelFactory
import com.example.nimbus.ui.viewmodels.MyTeamsScreenViewModel
import com.example.nimbus.ui.viewmodels.PredictionModelFactory
import com.example.nimbus.ui.viewmodels.PredictionUiState
import com.example.nimbus.ui.viewmodels.PredictionViewModel
import com.example.nimbus.utils.SharedPreferencesManager
import com.github.mikephil.charting.animation.Easing
import kotlinx.coroutines.launch
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter.Companion.factory

class PredictionAIActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            window.statusBarColor = getColor(R.color.gray_900)
            window.navigationBarColor = getColor(R.color.gray_700)
            
            NimbusTheme {
                val context = LocalContext.current
                val sharedPrefManager = SharedPreferencesManager(context)

                val globalViewModel: GlobalViewModel by viewModels {
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                }

                val viewModel: PredictionViewModel = viewModel(
                    factory = PredictionModelFactory(sharedPrefManager, globalViewModel)
                )

                val uiState by viewModel.uiState.collectAsState()

                Surface(color = colorResource(id = R.color.gray_900)) {
                    Scaffold(
                        topBar = {
                            TopNavigation(
                                selectedPage = 4,
                                globalViewModel = globalViewModel,
                                athletePage = 1,
                                onBackClick = { context.startActivity(Intent(context, MainActivity::class.java)) },
                                onMenuClick = { context.startActivity(Intent(context, MainActivity::class.java)) }
                            )
                        },
                        content = { paddingValues ->
                            if(uiState.isLoading) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CustomLoading()
                                }
                            } else if(uiState.forecast != null) {
                                Box(
                                    modifier = Modifier.padding(paddingValues)
                                ) {
                                    val pieChartData = listOf(
                                        PieChartData(
                                            uiState.adversary?.name,
                                            uiState.forecast?.challenger_win_percentage?.toFloat()
                                        ),
                                        PieChartData(
                                            globalViewModel.getSelectedTeam()?.name,
                                            uiState.forecast?.challenged_win_percentage?.toFloat()
                                        )
                                    )

                                    PieChart(pieChartData)
                                }
                            } else {
                                AllTeamsList(uiState, viewModel)
                            }
                        })
                }

            }
        }
    }

    @Composable
    fun PieChart(pieData: List<PieChartData>) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    AsyncImage(
                        model = "https://logodownload.org/wp-content/uploads/2019/06/golden-state-warriors-logo-8.png",
                        contentDescription = stringResource(id = R.string.challenger_logo, ""),
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "X",
                        fontFamily = catamaranFontFamily,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = colorResource(id = R.color.orange_500)
                    )

                    AsyncImage(
                        model = "https://logodownload.org/wp-content/uploads/2019/06/golden-state-warriors-logo-8.png",
                        contentDescription = stringResource(id = R.string.challenger_logo, ""),
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Seu time",
                    fontFamily = catamaranFontFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.orange_500)
                )

                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .size(320.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Crossfade(targetState = pieData) { pieChartData ->
                        AndroidView(factory = { context ->
                            PieChart(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                )
                                val customTypeface: Typeface? = ResourcesCompat.getFont(context, R.font.catamaran_black)
                                this.description.isEnabled = false

                                this.isDrawHoleEnabled = true
                                this.holeRadius = 75f
                                this.setHoleColor(0xff131313.toInt())
                                this.transparentCircleRadius = 0f

                                this.legend.isEnabled = false

                                this.centerText = pieData[0].value?.toInt().toString() + "%"
                                this.setCenterTextColor(0xFFff7425.toInt())
                                this.setCenterTextSize(55f)
                                this.setCenterTextTypeface(customTypeface)

                                this.setDrawEntryLabels(false)
                                this.setDrawSlicesUnderHole(true)

                                this.setUsePercentValues(true)

                                this.animateXY(300, 500, Easing.EaseInOutCubic)

                                // on below line we are specifying entry label color as white.
                                ContextCompat.getColor(context, R.color.orange_100)
                                //this.setEntryLabelColor(resources.getColor(R.color.white))
                            }
                        },
                            // on below line we are specifying modifier
                            // for it and specifying padding to it.
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(5.dp), update = {
                                // on below line we are calling update pie chart
                                // method and passing pie chart and list of data.
                                updatePieChartWithData(it, pieChartData)
                            })
                    }
                }

                Text(
                    text = "Time adversário",
                    fontFamily = catamaranFontFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.orange_300)
                )

                Text(
                    text = stringResource(id = R.string.win_probability, 10, pieData[0].value?.toInt().toString() + "%"),
                    fontFamily = catamaranFontFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.orange_100),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // on below line we are creating a update pie
// chart function to update data in pie chart.
    fun updatePieChartWithData(
        // on below line we are creating a variable
        // for pie chart and data for our list of data.
        chart: PieChart,
        data: List<PieChartData>
    ) {
        // on below line we are creating
        // array list for the entries.
        val entries = ArrayList<PieEntry>()

        // on below line we are running for loop for
        // passing data from list into entries list.
        for (i in data.indices) {
            val item = data[i]
            entries.add(PieEntry(item.value ?: 0.toFloat(), item.browserName ?: ""))
        }

        // on below line we are creating
        // a variable for pie data set.
        val ds = PieDataSet(entries, "")
        ds.setDrawValues(false)
        // on below line we are specifying color
        // int the array list from colors.
        ds.colors = arrayListOf(
            Orange500.toArgb(),
            Orange300.toArgb(),
        )
        // on below line we are specifying position for value
        ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

        // on below line we are specifying position for value inside the slice.
        ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

        // on below line we are specifying
        // slice space between two slices.
        ds.sliceSpace = 2f

        // on below line we are specifying text color
        ContextCompat.getColor(this, R.color.white)
        // ds.valueTextColor = resources.getColor(R.color.white)

        // on below line we are specifying
        // text size for value.
        ds.valueTextSize = 18f

        // on below line we are specifying type face as bold.
        ds.valueTypeface = Typeface.DEFAULT_BOLD

        // on below line we are creating
        // a variable for pie data
        val d = PieData(ds)

        // on below line we are setting this
        // pie data in chart data.
        chart.data = d

        // on below line we are
        // calling invalidate in chart.
        chart.invalidate()
    }
}

@Composable
fun AllTeamsList(uiState: PredictionUiState, viewModel: PredictionViewModel) {
    val scrollState = rememberLazyListState()
    Column {
        Text(text = "Selecione o time adversário")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(505.dp)
                .padding(top = 18.dp)
                .weight(weight = 1f)
                .drawFadingEdges(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = scrollState
        ) {
            items(items = uiState.teams.toList()) {
                TeamCard(
                    team = it,
                    onClick = {
                        viewModel.setAdversary(it)
                    }
                )
            }
        }
    }
}

