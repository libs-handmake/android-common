package com.hoangdz.libgdx_common.extensions

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import kotlin.math.acos
import kotlin.math.sqrt


/**
 * Created by HoangDepTrai on 15, July, 2022 at 4:27 PM
 */

private fun colorIntToHex(color: Int): String {
    return String.format("#%06X", 0xFFFFFF and color)
}

fun <T> Collection<T>.toGDXArray() = kotlin.run {
    val array = Array<T>(size)
    forEach {
        array.add(it)
    }
    return@run array
}

fun <T> kotlin.Array<out T>.toGDXArray() = kotlin.run {
    val array = Array<T>(size)
    forEach {
        array.add(it)
    }
    return@run array
}

fun Color.toColorHex() = colorIntToHex(
    android.graphics.Color.rgb(
        (r * 255).toInt(),
        (g * 255).toInt(),
        (b * 255).toInt()
    )
)

fun String.toGDXColor() = kotlin.run {
    val colorInt = android.graphics.Color.parseColor(this)
    return@run Color(
        (colorInt shr 16 and 0xFF) / 255f,
        (colorInt shr 8 and 0xFF) / 255f,
        (colorInt and 0xFF) / 255f,
        1f
    )
}

fun Vector2.getDistance() = kotlin.run {
    sqrt(x * x + y * y)
}

infix fun Vector2.distanceTo(other: Vector2) = (this vectorWith other).getDistance()

infix fun Vector2.centre(other: Vector2) = Vector2((x + other.x) / 2f, (y + other.y) / 2f)

infix fun Vector2.angleWith(vector2: Vector2): Float {
    return acos(cosOf(this, vector2))
}

fun cosOf(u: Vector2, v: Vector2): Float {
    return (u.x * v.x + u.y * v.y) / (u.getDistance() * v.getDistance())
}

fun Vector2.isSamePoint(other: Vector2) = x == other.x && y == other.y

infix fun Vector2.vectorWith(point: Vector2) = Vector2(x - point.x, y - point.y)

fun Vector2.isOf(a: Vector2, b: Vector2) = distanceTo(a) + distanceTo(b) == (a distanceTo b)

operator fun Vector2.minus(other: Vector2) = vectorWith(other)

operator fun Vector2.times(scalar: Float) = Vector2(x * scalar, y * scalar)

operator fun Vector2.plus(other: Vector2) = Vector2(x + other.x, y + other.y)

fun Bitmap.toGdxTexture(): Texture {
    val tex = Texture(width, height, Pixmap.Format.RGBA8888)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex.textureObjectHandle)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, this, 0)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    recycle()
    return tex
}

fun PolygonSpriteBatch.resetBlendingFunc() =
    setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

fun SpriteBatch.resetBlendingFunc() =
    setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)


