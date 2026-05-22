package com.erluxman.focuslauncher.service.lobby

object RouletteShuffler {

    /**
     * Returns a deterministic, stable-per-seed shuffled list of `pool`. Used to rotate the
     * dock's user-slot apps daily without changing them mid-day. Caller should pass the
     * day-of-year (or epoch-days) as seed.
     */
    fun <T> shuffle(pool: List<T>, seed: Long): List<T> {
        if (pool.size <= 1) return pool
        val r = java.util.Random(seed)
        val mutable = ArrayList(pool)
        for (i in mutable.size - 1 downTo 1) {
            val j = r.nextInt(i + 1)
            val tmp = mutable[i]
            mutable[i] = mutable[j]
            mutable[j] = tmp
        }
        return mutable
    }
}
