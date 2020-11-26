interface Slice<V>

interface A

val SL0: Slice<out A> = TODO()

fun <X> foo(s: Slice<X>): X? {
    if (s === SL0) {
        return <!INAPPLICABLE_CANDIDATE!>bar<!>(s) // works for some reason
    }
    return null
}

fun <Y> bar(w: Slice<Y>): Y? = null
