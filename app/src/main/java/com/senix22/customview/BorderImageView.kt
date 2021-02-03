package com.senix22.customview


import android.content.Context
import android.content.Intent.getIntent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class BorderImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private var rotationDegrees = 0
    companion object{
        var delta = 0
    }
    override fun onDraw(canvas: Canvas) {
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas.rotate(rotation(delta).toFloat())
        canvas.translate((-width / 2).toFloat(), (-height / 2).toFloat())
        postInvalidateOnAnimation()
        super.onDraw(canvas)
    }

     private fun rotation(delta:Int): Int {
        rotationDegrees = (rotationDegrees + delta) % 360
        return rotationDegrees

    }
}
