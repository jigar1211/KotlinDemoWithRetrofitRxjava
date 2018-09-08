package com.warehouse.warehousegroup.model



data class PriceModel(
        val MachineID: String,
        val Action: String,
        val ScanBarcode: String,
        val ScanID: String,
        val UserDescription: String,
        val Product: Product,
        val ProdQAT: String,
        val ScanDateTime: String,
        val Found: String,
        val UserID: String
)

data class Product(
        val Class0: String,
        val Price: Price,
        val Barcode: String,
        val ItemDescription: String,
        val DeptID: String,
        val SubClass: String,
        val Class0ID: String,
        val SubDeptID: String,
        val Description: String,
        val BranchPrice: String,
        val ItemCode: String,
        val SubDept: String,
        val ClassID: String,
        val ImageURL: String,
        val Dept: String,
        val SubClassID: String,
        val Class: String,
        val ProductKey: String
)

data class Price(
    val Price: String,
    val Type: String
)