import java.util.stream.IntStream;

public class GaussFilter extends Filter {


    GaussFilter(int X, int Y) {
        super(X, Y);
    }

    @Override
    public int[] applyFilter(int[] pixels) {
        int output[] = new int[width * height];
        int h = height - filter.length / filterWidth + 1;
        int w = width - filterWidth + 1;

        for ( int y = 0 ; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                     filterIndex < filter.length;
                     pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
                        int col = pixels[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels seperately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }
        return output;
    }
}
