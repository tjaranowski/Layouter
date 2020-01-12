package pl.nosystems.android.layouter.core;

import androidx.annotation.NonNull;


public class LayouterBuilder {

    @NonNull
    public static LayouterBuilder instance() {
        return new LayouterBuilder();
    }

    @NonNull
    public Layouter build() {
        return new Layouter();
    }
}
