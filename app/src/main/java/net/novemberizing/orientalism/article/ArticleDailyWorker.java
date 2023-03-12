package net.novemberizing.orientalism.article;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
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
            Log.e(Tag, "set(result)");
            synchronized (this){
                this.result = result;
                if(executor != null && listener != null) {
                    executor.execute(listener);
                }
            }
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {
            Log.e(Tag, "addListener(Runnable listener, Executor executor)");
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
            Log.e(Tag, "cancel(b) => implement this");
            return false;
        }

        @Override
        public boolean isCancelled() {
            Log.e(Tag, "isCancelled() => implement this");
            return false;
        }

        @Override
        public boolean isDone() {
            Log.e(Tag, "isDone() => implement this => " + Boolean.toString(result != null));
            return result != null;
        }

        @Override
        public Result get() throws ExecutionException, InterruptedException {
            Log.e(Tag, "get() => implement this");
            return result;
        }

        @Override
        public Result get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            Log.e(Tag, "get(l, timeUnit) => implement this");
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
        Log.e(Tag, "startWork()");
        Future future = new Future();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // throw new RuntimeException(e);
                }
                future.set(Result.success());
                Log.e(Tag, "future is set");
            }
        });
        thread.start();
        return future;
    }
}
