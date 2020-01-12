package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;


public class AndroidNameResolver implements FullyQualifiedNameResolver {

    @NonNull
    @Override
    public String resolveName(@NonNull String name) {
        try {
            String tmpName = "android.widget." + name;
            Class.forName(tmpName);
            return tmpName;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            String tmpName = "android.view." + name;
            Class.forName(tmpName);
            return tmpName;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
