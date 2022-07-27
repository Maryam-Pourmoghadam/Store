
package com.example.store.data.network

import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.lang.Exception


abstract class BaseRepository {
    suspend fun <T:Any> safeApiCall(apiToBeCalled:suspend() ->Response<T>):NetworkResult<T>{
        return withContext(Dispatchers.IO){
            try {
                val response:Response<T> =apiToBeCalled()
                if (response.isSuccessful) {
                    NetworkResult.Success(data = response.body()!!)
                }
                else{
                    val errorResponse: ErrorResponse?=convertErrorBody(response.errorBody())
                    NetworkResult.Error( errorResponse?.message?:getErrorMessage(response.code()))
                }


            }
            catch (e:IOException){
                NetworkResult.Error("ارتباط با سرور قطع می باشد, از اتصال به شبکه اطمینان حاصل کنید")
            }catch (e:Exception){
                NetworkResult.Error(e.message)
            }

        }
    }

    private fun convertErrorBody(errorBody:ResponseBody?):ErrorResponse?{
        return try {
            errorBody?.source()?.let {
                val moshiAdapter=Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        }catch (e:Exception){
            null
        }
    }

    private fun getErrorMessage(resposeCode:Int):String{
       return when(resposeCode){
            401->"دسترسی به این درخواست غیر مجاز می باشد"
            403->"دسترسی از سمت سرور ممنوع می باشد"
            404->"درخواست مورد نظر یافت نشد"
            500->"مشکل داخلی برای سرور بوجود آمده است"
            503->"سرویس(سرور) در دسترس نمی باشد"
            504->"سرور دچار ارور تایم اوت شده است"
            400->"سینتکس درخواست مورد نظر صحیح نمی باشد"
            in 500..599->"مشکلی از سمت سرور رخ داده است"
            in 400..499->"پردازش در خواست شما ممکن نیست"
            in 300..399->"درخواست مورد نظر یافت نشد"
            else->"مشکلی رخ داده است"
        }
    }
}
