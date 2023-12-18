package com.example.decloitrevalentin.data

import com.example.decloitrevalentin.list.Task
import retrofit2.Response
import retrofit2.http.GET

interface UserWebService {
    @GET("/sync/v9/user/")
    suspend fun fetchUser(): Response<User>

}


interface TasksWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>
}