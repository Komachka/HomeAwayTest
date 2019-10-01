package com.kstor.homeawaytest.utils

import org.hamcrest.core.SubstringMatcher

class StringContainsIgnoringCase(substring: String) : SubstringMatcher(substring) {
    override fun relationship(): String {
        return "containing (ignore case)"
    }

    override fun evalSubstringOf(string: String?): Boolean {
        return string?.toLowerCase()?.contains(substring.toLowerCase()) ?: false
    }

    companion object {
        fun containsStringIgnoreCase(substring: String) = StringContainsIgnoringCase(substring)
    }
}
