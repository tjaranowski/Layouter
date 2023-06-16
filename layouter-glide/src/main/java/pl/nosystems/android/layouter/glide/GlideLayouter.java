package pl.nosystems.android.layouter.glide;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import pl.nosystems.android.layouter.core.LayouterBuilder;
import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementReconstructor;

public class GlideLayouter {
    @NonNull
    private final List<ViewHierarchyElementReconstructor> reconstructors = new ArrayList<>();
    @NonNull
    private final Handler mainThreadHandler;
    @NonNull
    private final Executor executor;

    GlideLayouter(@NonNull List<ViewHierarchyElementReconstructor> reconstructorList) {
        mainThreadHandler = new Handler(Looper.getMainLooper());
        executor = Executors.newSingleThreadExecutor();
        this.reconstructors.addAll(reconstructorList);
    }

    public void queueRequest(@NonNull final pl.nosystems.android.layouter.glide.GlideLayouterRequest request) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    final ViewHierarchyElement apply = request.getDataProvider().call();

                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final ViewGroup target = request.getTarget();
                            final View built = LayouterBuilder.instance().build()
                                    .parseElementIntoView(apply, reconstructors, target, target.getContext());
                            final Consumer<View> doneCallback = request.getDoneCallback();

                            if(doneCallback != null) {
                                doneCallback.accept(built);
                            }
                        }
                    });

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @NonNull
    public <RawDataType> GlideLayouterRequestBuilder<RawDataType> startBuildingRequestFrom(
            @NonNull Callable<RawDataType> dataProvider) {
        GlideLayouterRequestBuilder<RawDataType> builder = new GlideLayouterRequestBuilder<>();
        builder.from(dataProvider);
        return builder;
    }

}
