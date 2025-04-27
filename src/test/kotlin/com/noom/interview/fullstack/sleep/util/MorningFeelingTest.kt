package com.noom.interview.fullstack.sleep.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MorningFeelingTest {
    @Test
    fun `should create MorningFeelinEnum with BAD, OK, GOOD values`() {
        assertEquals(MorningFeeling.BAD.toString(), "BAD")
        assertEquals(MorningFeeling.OK.toString(), "OK")
        assertEquals(MorningFeeling.GOOD.toString(), "GOOD")
    }
}