package net.novemberizing.orientalism.db.article;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ArticleDailyWorker extends ListenableWorker {

    public static class Future implements ListenableFuture<Result> {
        private Result result = null;
        private Runnable listener = null;
        private Executor executor = null;

        public void set(Result result) {
            synchronized (this){
                this.result = result;
                if(executor != null && listener != null) {
                    executor.execute(listener);
                }
            }
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {
            synchronized (this) {
                if(result != null) {
                    executor.execute(listener);
                } else {
                    this.listener = listener;
                    this.executor = executor;
                }
            }
        }

        @Override
        public boolean cancel(boolean b) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return result != null;
        }

        @Override
        public Result get() throws ExecutionException, InterruptedException {
            return result;
        }

        @Override
        public Result get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
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
        Future future = new Future();

        ArticleRepository.refreshSync(articles -> future.set(Result.success()));

        return future;
    }
}
