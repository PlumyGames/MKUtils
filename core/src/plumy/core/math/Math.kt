package plumy.core.math

import arc.math.Angles
import arc.math.Mathf
import arc.math.geom.Position
import arc.math.geom.Vec2
import kotlin.math.exp
import kotlin.math.ln

val Float.isZero: Boolean
    get() = Mathf.zero(this)
val Double.isZero: Boolean
    get() = Mathf.zero(this)
val Float.clamp: Float
    get() = this.coerceIn(0f, 1f)

operator fun Vec2.component1(): Float = x
operator fun Vec2.component2(): Float = y
val Degree.radian: Radian
    get() = this * Mathf.degreesToRadians
val Radian.degree: Degree
    get() = this * Mathf.radiansToDegrees
val Power2: FUNC = {
    it * it
}
val Power3: FUNC = {
    it * it * it
}
val Log2: FUNC = Mathf::log2
val Log3: FUNC = {
    Mathf.log(3f, it)
}

fun PowerGen(power: Float): FUNC = {
    Mathf.pow(it, power)
}

fun LogGen(base: Float): FUNC = {
    Mathf.log(base, it)
}

fun ExpGen(base: Float): FUNC = {
    Mathf.pow(base, it)
}

fun ExpLogGen(base: Float): Pair<FUNC, FUNC> =
    Pair(ExpGen(base), LogGen(base))

val LogE: FUNC = ::ln
val ExpE: FUNC = ::exp
fun toAngle(x: Float, y: Float): Float =
    Angles.angle(x, y)

fun toAngle(x: Int, y: Int): Float =
    Angles.angle(x.toFloat(), y.toFloat())

fun toAngle(x1: Int, y1: Int, x2: Int, y2: Int): Float =
    Angles.angle(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())

fun toAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float =
    Angles.angle(x1, y1, x2, y2)

fun toAngle(x: Double, y: Double): Float =
    Angles.angle(x.toFloat(), y.toFloat())

fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    val x = x1 - x2
    val y = y1 - y2
    return Mathf.sqrt(x * x + y * y)
}

fun distance2(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    val x = x1 - x2
    val y = y1 - y2
    return x * x + y * y
}
/**
 * @return this
 */
fun Vec2.normal(): Vec2 {
    this.set(y, -x)
    return this
}
/**
 * @return this
 */
fun Vec2.normal(factor: Float): Vec2 {
    this.set(factor * y, -factor * x)
    return this
}
/**
 * @param target degree of target angle
 * @param speed the rotation speed
 * @return this
 */
fun Vec2.approachAngle(target: Float, speed: Float): Vec2 =
    setAngle(
        Angles.moveToward(
            angle(),
            target,
            speed
        )
    )
/**
 * @param targetX X of target position
 * @param targetY Y of target position
 * @param speed the rotation speed
 * @return this
 */
fun Vec2.approachAngle(targetX: Float, targetY: Float, speed: Float): Vec2 =
    setAngle(
        Angles.moveToward(
            angle(),
            Angles.angle(x, y, targetX, targetY),
            speed
        )
    )

fun Vec2.approachLen(targetLen: Float, speed: Float): Vec2 =
    setLength(
        Mathf.approach(len(), targetLen, speed)
    )

fun Vec2.set(x: Short, y: Short): Vec2 =
    this.set(x.toFloat(), y.toFloat())

fun Vec2.set(x: Int, y: Int): Vec2 =
    this.set(x.toFloat(), y.toFloat())

fun Vec2.minus(x: Int, y: Int) = apply {
    this.x -= x.toFloat()
    this.y -= y.toFloat()
}

fun Vec2.minus(x: Short, y: Short) = apply {
    this.x -= x.toFloat()
    this.y -= y.toFloat()
}

fun Vec2.minus(x: Float, y: Float) = apply {
    this.x -= x
    this.y -= y
}

fun Vec2.minus(p: Position) = apply {
    this.x = p.x
    this.y = p.y
}

fun Vec2.div(b: Float) = apply {
    x /= b
    y /= b
}

operator fun Vec2.plusAssign(b: Float) {
    x += b
    y += b
}

operator fun Vec2.minusAssign(b: Float) {
    x -= b
    y -= b
}

operator fun Vec2.plusAssign(b: Vec2) {
    x += b.x
    y += b.y
}

operator fun Vec2.minusAssign(b: Vec2) {
    x -= b.x
    y -= b.y
}

operator fun Vec2.plusAssign(b: Position) {
    x += b.x
    y += b.y
}

operator fun Vec2.minusAssign(b: Position) {
    x -= b.x
    y -= b.y
}

operator fun Vec2.timesAssign(b: Float) {
    x *= b
    y *= b
}

operator fun Vec2.divAssign(b: Float) {
    x /= b
    y /= b
}

private val temp = Vec2()
fun isDiagonalTo(
    x1: Float, y1: Float, x2: Float, y2: Float,
): Boolean {
    val angle = temp.set(x1, y1).minus(x2, y2).angle()
    return (angle % 45f).isZero && !(angle % 90f).isZero
}

fun isDiagonalTo(
    x1: Int, y1: Int, x2: Int, y2: Int,
): Boolean {
    val angle = temp.set(x1, y1).minus(x2, y2).angle()
    return (angle % 45f).isZero && !(angle % 90f).isZero
}

val Float.sqr: Float
    get() = this * this
val Float.cube: Float
    get() = this * this * this
val Int.sqr: Int
    get() = this * this
val Int.cube: Int
    get() = this * this * this
val Int.sqrf: Float
    get() = (this * this).toFloat()
val Int.cubef: Float
    get() = (this * this * this).toFloat()

fun nextBoolean(): Boolean = Mathf.rand.nextBoolean()