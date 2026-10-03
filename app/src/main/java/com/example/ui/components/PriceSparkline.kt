package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedTax

@Composable
fun PriceSparkline(
    history: List<Int>,
    modifier: Modifier = Modifier,
    height: Dp = 36.dp,
    width: Dp = 80.dp,
    isPositive: Boolean = true
) {
    if (history.size < 2) return

    val lineColor = if (isPositive) FutGreenProfit else FutRedTax
    val fillColor = lineColor.copy(alpha = 0.15f)

    Canvas(modifier = modifier.width(width).height(height)) {
        val minPrice = (history.minOrNull() ?: 0).toFloat()
        val maxPrice = (history.maxOrNull() ?: 1).toFloat()
        val range = (maxPrice - minPrice).coerceAtLeast(1f)

        val w = size.width
        val h = size.height
        val stepX = w / (history.size - 1)

        val path = Path()
        val fillPath = Path()

        val points = history.mapIndexed { index, price ->
            val normalizedY = 1f - ((price - minPrice) / range)
            Offset(x = index * stepX, y = (normalizedY * (h - 6)) + 3)
        }

        path.moveTo(points.first().x, points.first().y)
        fillPath.moveTo(points.first().x, points.first().y)

        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
            fillPath.lineTo(points[i].x, points[i].y)
        }

        fillPath.lineTo(points.last().x, h)
        fillPath.lineTo(points.first().x, h)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor, Color.Transparent),
                startY = 0f,
                endY = h
            )
        )

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
