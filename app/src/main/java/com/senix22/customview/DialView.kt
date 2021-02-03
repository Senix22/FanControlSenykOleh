package com.senix22.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import kotlin.math.cos
import kotlin.math.sin

class DialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private var SELECTION_COUNT = 4
    }

    private var width = 0f
    private var height = 0f
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        textSize = 40f
    }
    private var dialPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }
    private var radius = 0f
    private var activeSelection = 0

    private val tempLabel = StringBuffer(8)
    private val tempResult = FloatArray(2)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()
        radius = (Math.min(width, height) / 2.0 * 0.8).toFloat()
    }

    private fun computeXYForPosition(position: Int, radius: Float): FloatArray {
        val result = tempResult
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + (position * (Math.PI / 4.0))
        result[0] = (radius * cos(angle) + (width / 2.0)).toFloat()
        result[1] = (radius * sin(angle) + (height / 2.0)).toFloat()
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { c ->
            c.drawCircle(width / 2, height / 2, radius, dialPaint)
            val labelRadius = radius + 20
            val label = tempLabel
            for (i in 0 until SELECTION_COUNT) {
                val xyData = computeXYForPosition(i, labelRadius)
                label.setLength(0)
                label.append(i)
                c.drawText(label, 0, label.length, xyData[0], xyData[1], textPaint)
            }

            val markerRadius = radius - 35
            val xyData = computeXYForPosition(activeSelection, markerRadius)
            canvas.drawCircle(xyData[0], xyData[1], 20f, textPaint)


        }
    }

    init {
        setOnClickListener {
            activeSelection = (activeSelection + 1) % SELECTION_COUNT
            if (activeSelection >= 1) {
                dialPaint.color = Color.GREEN
                BorderImageView.delta +=2
            } else {
                BorderImageView.delta = 0
                dialPaint.color = Color.GRAY
            }
            invalidate()
        }
    }
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putInt("activeSelection", activeSelection)
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }
    override fun onRestoreInstanceState(state: Parcelable?) {
        var viewState = state
        if (viewState is Bundle) {
            activeSelection = viewState.getInt("happinessState", activeSelection)
            viewState = viewState.getParcelable("superState")
        }
        super.onRestoreInstanceState(viewState)
    }
}

