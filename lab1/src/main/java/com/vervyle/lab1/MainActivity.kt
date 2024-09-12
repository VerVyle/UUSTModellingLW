package com.vervyle.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import com.vervyle.lab1.ui.theme.UUSTModellingLWTheme
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.apache.commons.math3.distribution.PoissonDistribution
import org.apache.commons.math3.util.BigReal
import java.math.BigDecimal
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

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

const val LIST_SIZE = 1000

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
        viewModel.generator.lambda = 10f
        val poissonList = List(LIST_SIZE) { viewModel.getPoisson() }
        viewModel.generator.lambda = 0.1f
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
                    val k = floor(1 + log(LIST_SIZE.toDouble(), 2.0)).toInt()
                    val bins = List(k) { index ->
                        index * exponentialList.max() / k
                    }
                    val mean = run {
                        var sum: Double = 0.0
                        exponentialList.forEach {
                            sum += it
                        }
                        sum / exponentialList.size
                    }
                    val observedFreq = MutableList(k) {
                        0
                    }
                    exponentialList.forEach { expVal ->
                        bins.indices.filter { it != k - 1 }.forEach { index ->
                            if (expVal >= bins[index] && expVal < bins[index + 1]) {
                                observedFreq[index]++
                            }
                        }
                    }
                    val expectedFreq =
                        DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq.indices.filter { it != expectedFreq.size - 1 }) {
                        val lowerBound = bins[i]
                        val upperBound = bins[i + 1]
                        expectedFreq[i] = exponentialList.size *
                                (ExponentialDistribution(mean)
                                    .cumulativeProbability(upperBound) -
                                        ExponentialDistribution(mean)
                                            .cumulativeProbability(lowerBound))
                    }
                    val expectedFreq2 =
                        DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq2.indices.filter { it != expectedFreq.size - 1 }) {
                        expectedFreq2[i] = exponentialList.size *
                                (1.0.minus(exp(-1.0 * bins[i + 1] / mean)))
                    }
                    "bins: ${bins.map { it.toString() }} \n"
                        .plus("observedFreq: ${observedFreq.map { it.toString() }} \n")
                        .plus("expectedFreq: ${expectedFreq.map { it.toString() }} \n")
                        .plus("expectedFreqMy: ${expectedFreq2.map { it.toString() }} \n")
                },
                modifier = modifier
            )
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
                    val k = floor(1 + log(LIST_SIZE.toDouble(), 2.0)).toInt()
                    val bins = List(k) { index ->
                        index * poissonList.max() / k
                    }
                    val mean = run {
                        var sum: Double = 0.0
                        poissonList.forEach {
                            sum += it
                        }
                        sum / poissonList.size
                    }
                    val lambda = mean
                    val observedFreq = MutableList(k) {
                        0
                    }
                    poissonList.forEach { poissonVal ->
                        bins.indices.filter { it != k - 1 }.forEach { index ->
                            if (poissonVal >= bins[index] && poissonVal < bins[index + 1]) {
                                observedFreq[index]++
                            }
                        }
                    }
                    val expectedFreq =
                        DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq.indices.filter { it != expectedFreq.size - 1 }) {
                        val lowerBound = bins[i]
                        val upperBound = bins[i + 1]
                        expectedFreq[i] = exponentialList.size *
                                (PoissonDistribution(mean)
                                    .cumulativeProbability(upperBound) -
                                        PoissonDistribution(mean)
                                            .cumulativeProbability(lowerBound))
                    }
                    // FIXME: try dif formula 
                    val expectedFreq2 =
                        DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq2.indices.filter { it != expectedFreq.size - 1 }) {
                        expectedFreq2[i] = run {
                            var expected = 0.0
                            for (j in bins[i]..<bins[i + 1]) {
                                expected += lambda.pow(j) * exp(-1.0 * lambda) / factorial(j) * exponentialList.size
                            }
                            expected
                        }
                    }
                    Log.d(
                        "Prob", "${
                            lambda.pow(13) * exp(-1.0 * lambda) / factorial(13) * exponentialList.size +
                                    lambda.pow(14) * exp(-1.0 * lambda) / factorial(14) * exponentialList.size
                        }"
                    )
                    "bins: ${bins.map { it.toString() }} \n"
                        .plus("observedFreq: ${observedFreq.map { it.toString() }} \n")
                        .plus("expectedFreq: ${expectedFreq.map { it.toString() }} \n")
                        .plus("expectedFreqMy: ${expectedFreq2.map { it.toString() }} \n")
                },
                modifier = modifier
            )
        }
    }
}

fun factorial(int: Int): Int {
    var fac = 1
    var buf = 1
    while (buf < int) {
        fac *= buf + 1
        buf++
    }
    return fac
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    UUSTModellingLWTheme {
        Main(MainViewModel())
    }
}