package com.agri.myapp

data class Products(
    var productId: String? = null,
    var productName:String? = null,
    var productPrice:String? = null,
    var productCharge:String? = null,
    var productMobile:String? = null,
    var productType:String? = null,
    var productCount:String? = null,
    var productImage:String? = "",
    var Email:String? = null
) {
    constructor() : this(null, null, null, null,  null,null, null,  "",null)
}

