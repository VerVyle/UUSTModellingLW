package com.vervyle.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.vervyle.lab1.ui.theme.UUSTModellingLWTheme

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UUSTModellingLWTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

const val LIST_SIZE = 10

@Composable
fun Main(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.generator.lambda = 10F
        val poissonList = List(LIST_SIZE) { viewModel.getPoisson() }
        val exponentialList = List(LIST_SIZE) { viewModel.getExpVal() }
        item {
            Text(
                text = run {
                    "random exponential value generation (lambda = " +
                            "${viewModel.generator.lambda})"
                },
                modifier = modifier
            )
            Text(
                text = run {
                    var mean = 0.0
                    exponentialList.forEach {
                        mean += it
                    }
                    mean /= exponentialList.size
                    "generated list is "
                        .plus(
                            exponentialList.map { it.toString() }.toString()
                        )
                        .plus("\n mean is ").plus(mean)
                        .plus(
                            "\n with lambda equal " +
                                    "${viewModel.generator.lambda}"
                        )
                },
                modifier = modifier
            )
            Text(
                text = run {
                    // TODO: create test for sample
                    "Testing the sample"
                },
                modifier = modifier
            )
//            AndroidView(
//                factory = { context ->
//                    val view = LayoutInflater
//                        .from(context)
//                        .inflate(R.layout.barchart, null, false) as BarChart
//
//                    view
//                },
//            )
        }
        item {
            HorizontalDivider()
        }
        item {
            Text(
                text = run {
                    "poisson random value generation (lambda = " +
                            "${viewModel.generator.lambda})"
                },
                modifier = modifier
            )
            Text(
                text = run {
                    var mean = 0.0
                    poissonList.forEach {
                        mean += it
                    }
                    mean /= poissonList.size
                    "generated list is "
                        .plus(
                            poissonList.map { it.toString() }.toString()
                        )
                        .plus("\n mean is ").plus(mean)
                        .plus(
                            "\n with lambda equal " +
                                    "${viewModel.generator.lambda}"
                        )
                },
                modifier = modifier
            )
            Text(
                    text = run {
                        // TODO: create test for sample
                        "Testing the sample"
                    },
            modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    UUSTModellingLWTheme {
        Main(MainViewModel())
    }
}