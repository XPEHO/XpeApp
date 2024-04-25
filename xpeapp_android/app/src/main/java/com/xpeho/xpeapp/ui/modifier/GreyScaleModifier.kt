package com.xpeho.xpeapp.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

data class GreyScaleModifier(val saturation: Float): ModifierNodeElement<GreyScaleModifierNode>() {
    override fun create(): GreyScaleModifierNode = GreyScaleModifierNode(saturation)
    override fun update(node: GreyScaleModifierNode) {
        node.saturation = saturation
    }
    override fun InspectorInfo.inspectableProperties() {
        properties["saturation"] = saturation
    }
}

class GreyScaleModifierNode(var saturation: Float) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        val saturationMatrix = ColorMatrix().apply { setToSaturation(saturation) }
        val saturationFilter = ColorFilter.colorMatrix(saturationMatrix)
        val paint = Paint().apply {
            colorFilter = saturationFilter
        }
        drawIntoCanvas {
            it.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
            drawContent()
            it.restore()
        }
    }
}

fun Modifier.greyScale(saturation: Float) = this then GreyScaleModifier(saturation)