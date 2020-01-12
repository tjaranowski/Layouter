package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

public interface FullyQualifiedNameResolver {

    @NonNull
    String resolveName(@NonNull String name);
}
