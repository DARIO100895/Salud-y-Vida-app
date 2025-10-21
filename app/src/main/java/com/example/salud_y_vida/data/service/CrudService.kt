package com.example.salud_y_vida.data.service

interface CrudService<T,ID> {

    suspend fun listar() : Result<List<T>>
    suspend fun obtener(id: ID) : Result<T>
    suspend fun crear(entity: T) : Result<T>
    suspend fun actualizar(id: ID, entity: T) : Result<T>
    suspend fun eliminar(id: ID) : Result<Boolean>
}