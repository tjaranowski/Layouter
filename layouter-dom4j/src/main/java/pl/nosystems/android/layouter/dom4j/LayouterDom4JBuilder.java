package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

public class LayouterDom4JBuilder {

    @NonNull
    public static LayouterDom4JBuilder instance() {
        return new LayouterDom4JBuilder();
    }

    @NonNull
    public LayouterDom4J build() {
        return new LayouterDom4J();
    }
}
