package com.warehouse.warehousegroup.remote

import com.warehouse.warehousegroup.model.PriceModel
import com.warehouse.warehousegroup.model.SearchModel
import com.warehouse.warehousegroup.model.UserModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("newuser.json")
    fun getUserID(): Observable<Response<UserModel>>

    @GET("search.json")
    fun getSearchList(@Query("Start") start: String,
                      @Query("Limit") limit: String,
                      @Query("MachineID") machineId: String,
                      @Query("Branch") branch: String,
                      @Query("Search") search: String,
                      @Query("Screen") screen: String,
                      @Query("UserID") userId: String
    ): Observable<Response<SearchModel>>

    @GET("price.json")
    fun getPriceList(@Query("Barcode") barcode: String,
                     @Query("MachineID") machineId: String,
                     @Query("UserID") userId: String,
                     @Query("Branch") branch: String,
                     @Query("DontSave") doNotSave: String
    ): Observable<Response<PriceModel>>
}