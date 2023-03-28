package com.mvvm.appcomponent.util

import com.google.gson.Gson
import com.mvvm.appcomponent.domain.model.response.BaseErrorResponse
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.HttpException

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(
            Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map {
                Resource.Success(it)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            val error = if (t is HttpException)
                getErrorMessage(t.response()?.errorBody())
            else
                BaseErrorResponse(0, "Something went wrong !!", false, Any())
            query().map { Resource.Error(error, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)

}

fun <RequestType> networkBoundResourceWithoutDb(
    fetch: suspend () -> RequestType
) = flow {

    emit(Resource.Loading(null))
    try {
        emit(Resource.Success(fetch.invoke()))
    } catch (t: Throwable) {
        val error = if (t is HttpException)
            getErrorMessage(t.response()?.errorBody())
        else{
            BaseErrorResponse(0, t.message.toString(), false, Any())
        }

        emit(Resource.Error(error, null))
    }
}

fun getErrorMessage(errorBody: ResponseBody?): BaseErrorResponse {

    if (errorBody == null)
        return BaseErrorResponse(0, "Server not reachable", false,Any())

    val baseErrorResponse = Gson().fromJson(errorBody.charStream(), BaseErrorResponse::class.java)
    if (baseErrorResponse.code == 500)
        return BaseErrorResponse(500, "Something went wrong !!", false,Any())
    return baseErrorResponse
}