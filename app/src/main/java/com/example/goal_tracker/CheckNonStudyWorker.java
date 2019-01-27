package com.example.goal_tracker;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.*;

public class CheckNonStudyWorker extends Worker {
    public CheckNonStudyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, compress the stored images.
        // In this example no parameters are passed; the task is
        // assumed to be "compress the whole library."


        // Indicate success or failure with your return value:
        return Result.success();

        // (Returning Result.retry() tells WorkManager to try this task again
        // later; Result.failure() says not to try again.)
    }
}
