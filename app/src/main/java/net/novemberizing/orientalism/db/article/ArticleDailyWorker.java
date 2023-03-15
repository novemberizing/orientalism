package net.novemberizing.orientalism.db.article;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ArticleDailyWorker extends ListenableWorker {
    private static final String Tag = "ArticleDailyWorker";

    public static class Future implements ListenableFuture<Result> {
        private static final String Tag = "ArticleDailyWorker.Future";

        private Result result = null;
        private Runnable listener = null;
        private Executor executor = null;

        public void set(Result result) {
            Log.e(Tag, "set(result)");      // TODO: REMOVE THIS
            synchronized (this){
                this.result = result;
                if(executor != null && listener != null) {
                    executor.execute(listener);
                }
            }
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {
            Log.e(Tag, "addListener(Runnable listener, Executor executor)");    // TODO: REMOVE THIS
            synchronized (this) {
                if(result != null) {
                    executor.execute(listener);;
                } else {
                    this.listener = listener;
                    this.executor = executor;
                }
            }
        }

        @Override
        public boolean cancel(boolean b) {
            Log.e(Tag, "cancel(b) => implement this");          // TODO: REMOVE THIS
            return false;
        }

        @Override
        public boolean isCancelled() {
            Log.e(Tag, "isCancelled() => implement this");      // TODO: REMOVE THIS
            return false;
        }

        @Override
        public boolean isDone() {
            Log.e(Tag, "isDone() => implement this => " + Boolean.toString(result != null));    // TODO: REMOVE THIS
            return result != null;
        }

        @Override
        public Result get() throws ExecutionException, InterruptedException {
            Log.e(Tag, "get() => implement this");              // TODO: REMOVE THIS
            return result;
        }

        @Override
        public Result get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            Log.e(Tag, "get(l, timeUnit) => implement this");   // TODO: REMOVE THIS
            return result;
        }
    }

    /**
     * @param context   The application {@link Context}
     * @param params    Parameters to setup the internal state of this worker
     */
    public ArticleDailyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Log.e(Tag, "startWork()");  // TODO: CHANGE Log.w
        Future future = new Future();

        ArticleRepository.sync(articles -> future.set(Result.success()));

        return future;
    }
}
