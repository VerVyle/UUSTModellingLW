package com.vervyle.test

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.utils.MPPointF


class MainActivity : ComponentActivity() {
    private var chart: BarChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY:SeekBar? = null
    private var tvX: TextView? = null
    private var tvY:android.widget.TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.barchart)

        title = "BarChartActivity"

        tvX = findViewById<TextView>(R.id.tvXMax)
        tvY = findViewById<TextView>(R.id.tvYMax)

        seekBarX = findViewById<SeekBar>(R.id.seekBar1)
        seekBarY = findViewById<SeekBar>(R.id.seekBar2)

        //seekBarY.setOnSeekBarChangeListener(this)
        //seekBarX.setOnSeekBarChangeListener(this)

        chart = findViewById<BarChart>(R.id.chart1)
        //chart.setOnChartValueSelectedListener(this)

        chart?.setDrawBarShadow(false)
        chart?.setDrawValueAboveBar(true)

        chart?.getDescription()?.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart?.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart?.setPinchZoom(false)

        chart?.setDrawGridBackground(false)

        // chart.setDrawYLabels(false);
        //val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(chart)

        val xAxis = chart!!.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        //xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        //xAxis.setValueFormatter(xAxisFormatter)

        //val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis = chart!!.getAxisLeft()
        //leftAxis.typeface = tfLight
        leftAxis.setLabelCount(8, false)
       // leftAxis.setValueFormatter(custom)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis = chart!!.getAxisRight()
        rightAxis.setDrawGridLines(false)
        //rightAxis.typeface = tfLight
        rightAxis.setLabelCount(8, false)
       // rightAxis.setValueFormatter(custom)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val l = chart!!.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f

        //val mv: XYMarkerView = XYMarkerView(this, xAxisFormatter)
        //mv.setChartView(chart) // For bounds control
        //chart.setMarker(mv) // Set the marker to the chart

        // setting data
        seekBarY!!.setProgress(50)
        seekBarX!!.setProgress(12)

        // chart.setDrawLegend(false);
    }

    private fun setData(count: Int, range: Float) {
        val start = 1f

        val values = ArrayList<BarEntry>()

        var i = start.toInt()
        while (i < start + count) {
            val `val` = (Math.random() * (range + 1)).toFloat()

            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), `val`, resources.getDrawable(R.drawable.ic_launcher_foreground)))
            } else {
                values.add(BarEntry(i.toFloat(), `val`))
            }
            i++
        }

        val set1: BarDataSet

        if (chart!!.data != null &&
            chart!!.data.dataSetCount > 0
        ) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "The year 2017")

            set1.setDrawIcons(false)

            val startColor1 = Color.Red
            val startColor2 = Color.Red
            val startColor3 = Color.Red
            val startColor4 = Color.Red
            val startColor5 = Color.Red
            val endColor1 = Color.Red
            val endColor2 = Color.Red
            val endColor3 = Color.Red
            val endColor4 = Color.Red
            val endColor5 = Color.Red

//            val gradientFills: MutableList<Fill> = ArrayList<Fill>()
//            gradientFills.add(Fill(startColor1, endColor1))
//            gradientFills.add(Fill(startColor2, endColor2))
//            gradientFills.add(Fill(startColor3, endColor3))
//            gradientFills.add(Fill(startColor4, endColor4))
//            gradientFills.add(Fill(startColor5, endColor5))

            //set1.setFills(gradientFills)

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            //data.setValueTypeface(tfLight)
            data.barWidth = 0.9f

            chart!!.data = data
        }
    }
//
//    fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        tvX!!.text = seekBarX!!.progress.toString()
//        tvY.setText(seekBarY.getProgress().toString())
//
//        setData(seekBarX!!.progress, seekBarY.getProgress().toFloat())
//        chart!!.invalidate()
//    }
//
//    protected fun saveToGallery() {
//        saveToGallery(chart, "BarChartActivity")
//    }
//
//    fun onStartTrackingTouch(seekBar: SeekBar?) {}
//
//    fun onStopTrackingTouch(seekBar: SeekBar?) {}
//
//    private val onValueSelectedRectF = RectF()
//
//    fun onValueSelected(e: Map.Entry<*, *>?, h: Highlight?) {
//        if (e == null) return
//
//        val bounds = onValueSelectedRectF
//        chart!!.getBarBounds(e as BarEntry?, bounds)
//        val position = chart!!.getPosition(e, AxisDependency.LEFT)
//
//        Log.i("bounds", bounds.toString())
//        Log.i("position", position.toString())
//
//        Log.i(
//            "x-index",
//            "low: " + chart!!.lowestVisibleX + ", high: "
//                    + chart!!.highestVisibleX
//        )
//
//        MPPointF.recycleInstance(position)
//    }
}