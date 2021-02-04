import java.io.File;
import java.util.stream.IntStream;

public abstract class Filter {
    int[] filter = {
            1, 4, 6, 4, 1,
            4, 16, 24, 16, 4,
            6, 24, 36, 24, 6,
            4, 16, 24, 16, 4,
            1, 4, 6, 4, 1};
    int filterWidth = 5;
    int width;
    int height;
    int sum;
    int pixelIndexOffset ;
    int centerOffsetX ;
    int centerOffsetY;

    Filter(int X, int Y) {
        this.width = X;
        this.height = Y;
        this.pixelIndexOffset = width - filterWidth;
        this.centerOffsetX = filterWidth / 2;
        this.centerOffsetY = filter.length / filterWidth / 2;
        this.sum = IntStream.of(filter).sum();
    }

    abstract int[] applyFilter(int[] pixels);
}
