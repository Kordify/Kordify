package world.anhgelus.kordify.main

object VersionHelper {
    var version: String? = null
        private set
    val regex = Regex("([^~]|>=)?([0-9].[0-9].[0-9])(-[a-z]+.?[0-9]*)?")

    fun init(v: String) {
        if (version != null) {
            throw IllegalArgumentException("The version was already set")
        }
        version = v
    }

    fun isCompatible(v: String): Boolean {
        val groups = regex.matchEntire(v)!!.groups
        if (groups[1] == null) {
            return v == version
        }
        return when(groups[1]!!.value) {
            "^" -> large(groups[2]!!.value)
            "~" -> tilde(groups[2]!!.value)
            ">=" -> up(groups[2]!!.value)
            else -> false
        }
    }

    private fun large(v: String): Boolean {
        val s = v.split(".")
        val rs = version!!.split(".")
        val major = s[0].toInt()
        val majorS = rs[0].toInt()
        val m = s[1].toInt()
        val ms = rs[1].toInt()
        val p = s[2].toInt()
        val ps = rs[2].toInt()
        if (major == 0) {
            return if (m == 0) {
                p == ps
            } else {
                m == ms && ps >= p
            }
        }
        if (major != majorS) {
            return false
        }
        return up(v)
    }

    private fun tilde(v: String): Boolean {
        val s = v.split(".")
        val rs = version!!.split(".")
        val major = s[0].toInt()
        val majorS = rs[0].toInt()
        val m = s[1].toInt()
        val ms = rs[1].toInt()
        val p = s[2].toInt()
        val ps = rs[2].toInt()
        if (major != majorS || m != ms) {
            return false
        }
        return p <= ps
    }

    private fun up(v: String): Boolean {
        val s = v.split(".")
        val rs = version!!.split(".")
        if (s[0].toInt() > rs[0].toInt()) {
            return false
        }
        if (s[1].toInt() > rs[1].toInt()) {
            return false
        }
        if (s[2].toInt() > rs[2].toInt()) {
            return false
        }
        return true
    }
}