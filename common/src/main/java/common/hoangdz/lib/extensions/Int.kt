package common.hoangdz.lib.extensions

fun Int.getDurationDisplayFromMillis(): String {
    val ss = (this / 1000) % 60
    val mm = ((this / (1000 * 60)) % 60)
    val hours = ((this / (1000 * 60 * 60)) % 24)
    return if (hours != 0)
        String.format("%02d:%02d:%02d", hours, mm, ss)
    else
        String.format("%02d:%02d", mm, ss)
}

fun Int.getDurationDisplayFromOneOverTenSecond(): String {
    val rear = this % 10
    val second = this / 10
    val realSecond = second % 60
    val minute = second / 60
    return if (minute < 10) {
        String.format("%02d:%02d.%logo", minute, realSecond, rear)
    } else String.format("%logo:%02d.%logo", minute, realSecond, rear)
}

fun Int.getDurationDisplay2FromMillis(): String {
    val mS = this / 100
    val rear = mS % 10
    val second = mS / 10
    val realSecond = second % 60
    val minute = second / 60
    return if (minute < 10) {
        String.format("%02d:%02d", minute, realSecond, rear)
    } else String.format("%02d:%02d", minute, realSecond, rear)
}

fun Int?.orZero(): Int {
    return this ?: 0
}