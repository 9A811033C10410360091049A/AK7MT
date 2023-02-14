package com.tktzlabs.AK7MT.db

class Trades {
    var id = 0
    var type: String
    var amount: String
    var price: String

    internal constructor(type: String, amount: String, price: String) {
        this.type = type
        this.amount = amount
        this.price = price
    }

    internal constructor(id: Int, type: String, amount: String, price: String) {
        this.id = id
        this.type = type
        this.amount = amount
        this.price = price
    }
}