package pl.nosystems.android.layouter.glide;

public interface FunctionThatThrows<I, O> {

    O apply(I input) throws Throwable;
}
