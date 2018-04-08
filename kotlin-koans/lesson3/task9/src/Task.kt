// Return customers who have more undelivered orders than delivered
fun Shop.getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> {
    return customers
            .filter {
                val (delivered, notDelivered) = it.orders.partition { it.isDelivered }

                delivered.size < notDelivered.size
            }
            .toSet()
}
