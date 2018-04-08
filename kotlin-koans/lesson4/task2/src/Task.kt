class LazyProperty(val initializer: () -> Int) {
    var isSet = false
    val lazy: Int = 0
        get() {
            if (!isSet) {
                isSet = true
                field = initializer()
            }

            return field
        }
}
