package com.example.bodima.models


data class Foods(
    var foodId: String? = null,
    var foodName:String? = null,
    var foodPrice:String? = null,
    var foodDescription:String? = null,
    var foodMobile:String? = null,
    var foodStartTime:String? = null,
    var foodMeridiumStart:String? = null,
    var foodEndTime:String? = null,
    var foodMeridiumEnd:String? = null,
    var foodAddress:String? = null,
    var foodCategory:String? = null,
    var foodType:String? = null,
    var foodImage:String? = "",
    var foodEmail:String? = null
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null, null, "",null)
}

