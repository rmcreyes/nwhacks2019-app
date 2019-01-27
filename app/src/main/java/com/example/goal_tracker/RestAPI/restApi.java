package com.example.goal_tracker;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface restApi {

    @GET("/user/{userID}")
    Call<User> getUser(@Path("userID") int userID);

    @GET("/tasks/{taskID}")
    Call<Task> getTasks(@Path("taskID") int taskID);

    @GET("/goals/{goalID}")
    Call<Goal> getGoals(@Path("goalID") int goalID);

    @GET("/user/{username}")
    Call<ID> getUserID(@Path("username") String username);

    @POST("/user/{username}")
    Call<ID> postUser(@Path("username") String username);

    @POST("/user/{userID}/goals/{goalID}")
    Call<ID> postGoalTask(@Path("userID") int userID, @Body Task task );

    @POST("/user/{userID}/goal")
    Call<ID> postGoal(@Path("userID") int userID, @Body GoalChange goal);

    @POST("/user/{userID}/task")
    Call<ID> postTask(@Path("userID") int userID, @Body Task task);

    @PUT("/tasks/taskID")
    Call<ID> putTask(@Path("userID") int userID, @Body Task task);

    @PUT("/goals/goalID")
    Call<ID> putGoal(@Path("goalID") int goalID, @Body GoalChange goal);

}
