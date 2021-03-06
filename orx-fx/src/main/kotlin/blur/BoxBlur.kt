package org.openrndr.extra.fx.blur

import org.openrndr.draw.*
import org.openrndr.extra.fx.filterFragmentCode

import org.openrndr.math.Vector2

/**
 * BoxBlur implemented as a separable filter
 */
class BoxBlur : Filter(Shader.createFromCode(Filter.filterVertexCode,
        filterFragmentCode("blur/box-blur.frag"))) {

    data class ColorBufferDescription(val width: Int, val height: Int, val contentScale: Double, val format: ColorFormat, val type: ColorType)

    /**
     * The sample window, default is 5
     */
    var window: Int by parameters

    /**
     * Spread multiplier, default is 1.0
     */
    var spread: Double by parameters

    /**
     * Post-blur gain, default is 1.0
     */
    var gain: Double by parameters

    private var intermediateCache = mutableMapOf<ApproximateGaussianBlur.ColorBufferDescription, ColorBuffer>()

    init {
        window = 5
        spread = 1.0
        gain = 1.0
    }

    override fun apply(source: Array<ColorBuffer>, target: Array<ColorBuffer>) {
        val intermediateDescription = ApproximateGaussianBlur.ColorBufferDescription(target[0].width, target[0].height, target[0].contentScale, target[0].format, target[0].type)
        val intermediate = intermediateCache.getOrPut(intermediateDescription) {
            colorBuffer(target[0].width, target[0].height, target[0].contentScale, target[0].format, target[0].type)
        }

        intermediate.let {
            parameters["blurDirection"] = Vector2(1.0, 0.0)
            super.apply(source, arrayOf(it))

            parameters["blurDirection"] = Vector2(0.0, 1.0)
            super.apply(arrayOf(it), target)
        }
    }
}