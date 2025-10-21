package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.service.CrudService
import okhttp3.Call

open class BaseRepository<T,ID> (

    private val getAll : suspend () -> retrofit2.Response<List<T>>,
    private val getById: suspend (ID) -> retrofit2.Response<T>,
    private val create : suspend (T) -> retrofit2.Response<T>,
    private val update : suspend (ID,T) -> retrofit2.Response<T>,
    private val delete : suspend (ID) -> retrofit2.Response<Void>
) : CrudService <T,ID> {

    override suspend fun listar() : Result<List<T>> = safeCall { getAll() }
    override suspend fun obtener(id: ID) : Result<T> = safeCall { getById(id)}
    override suspend fun crear(entity : T) : Result<T> = safeCall {create(entity)}
    override suspend fun actualizar(id: ID, entity: T) : Result<T> = safeCall { update(id,entity)}
    override suspend fun eliminar(id: ID): Result<Boolean> = try {
        val response = delete(id)
        if (response.isSuccessful) {
            Result.success(true)
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


    private suspend fun <R> safeCall(call: suspend () -> retrofit2.Response<R>): Result<R> = try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Exception("Respuesta vacía"))
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
