package pl.nosystems.android.layouter.glide;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;

import static java.util.Objects.requireNonNull;


public class GlideLayouterRequestBuilder<RawDataType> {
    private Callable<RawDataType> dataProvider;
    private ViewGroup target;
    private FunctionThatThrows<RawDataType, ViewHierarchyElement> decoder;


    @NonNull
    public GlideLayouterRequestBuilder<RawDataType> from(@NonNull Callable<RawDataType> dataProvider) {
        this.dataProvider = requireNonNull(dataProvider);
        return this;
    }

    @NonNull
    public GlideLayouterRequestBuilder<RawDataType> into(@NonNull ViewGroup viewGroup) {
        this.target = requireNonNull(viewGroup);
        return this;
    }

    @NonNull
    public <NewRawDataType> GlideLayouterRequestBuilder<NewRawDataType> withRawDataTransformator(
            @NonNull final FunctionThatThrows<RawDataType, NewRawDataType> transformator
    ) {

        GlideLayouterRequestBuilder<NewRawDataType> objectGlideLayouterRequestBuilder = new GlideLayouterRequestBuilder<>();
        objectGlideLayouterRequestBuilder.target = target;
        objectGlideLayouterRequestBuilder.dataProvider = new Callable<NewRawDataType>() {
            @Override
            public NewRawDataType call() {
                try {
                    return transformator.apply(GlideLayouterRequestBuilder.this.dataProvider.call());
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        };

        return objectGlideLayouterRequestBuilder;
    }

    @NonNull
    public GlideLayouterRequestBuilder<RawDataType> withRawDataDecoder(@NonNull FunctionThatThrows<RawDataType, ViewHierarchyElement> decoder) {
        this.decoder = requireNonNull(decoder);
        return this;
    }

    @NonNull
    public GlideLayouterRequest build() {
        Callable<ViewHierarchyElement> provider = new Callable<ViewHierarchyElement>() {
            @Override
            public ViewHierarchyElement call() {
                try {

                    return decoder.apply(dataProvider.call());
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        };
        return new GlideLayouterRequest(provider, target) {
        };
    }
}
