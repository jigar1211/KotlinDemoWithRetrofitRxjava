package com.warehouse.warehousegroup.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(
        @SerializedName("ProdQAT") val product: String,
        @SerializedName("UserID") val userId: String
) : Serializable


