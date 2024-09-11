package com.anshok.subzy.util.extensions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.anshok.subzy.R

class DottedCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var numberOfDots = 30
    private var dotRadius = 5f
    private var circleRadius = 100f
    private var dotColor = ContextCompat.getColor(context, R.color.Gray_62)
    private var startAngle = 0.0
    private var endAngle = 360.0

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = dotColor
    }

    fun setCircleParams(
        numberOfDots: Int,
        dotRadius: Float,
        circleRadius: Float,
        dotColor: Int,
        startAngle: Double,   // Угол начала
        endAngle: Double      // Угол конца
    ) {
        this.numberOfDots = numberOfDots
        this.dotRadius = dotRadius
        this.circleRadius = circleRadius
        this.dotColor = dotColor
        this.startAngle = startAngle
        this.endAngle = endAngle
        paint.color = dotColor
        invalidate() // Перерисовать view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        // Преобразуем углы в радианы
        val startAngleRad = Math.toRadians(startAngle)
        val endAngleRad = Math.toRadians(endAngle)
        val angleRange = endAngleRad - startAngleRad

        for (i in 0 until numberOfDots) {
            val angle = startAngleRad + (angleRange / numberOfDots) * i
            val x = centerX + circleRadius * Math.cos(angle).toFloat()
            val y = centerY + circleRadius * Math.sin(angle).toFloat()
            canvas.drawCircle(x, y, dotRadius, paint)
        }
    }
}