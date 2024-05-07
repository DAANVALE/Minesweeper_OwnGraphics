public enum Numbers {
    ONE(new int[][]{
            {0, 1, 0},
            {1, 1, 0},
            {0, 1, 0},
            {0, 1, 0},
            {1, 1, 1}
    }),
    TWO(new int[][]{
            {1, 1, 1},
            {0, 0, 1},
            {1, 1, 1},
            {1, 0, 0},
            {1, 1, 1}
    }),
    THREE(new int[][]{
            {1, 1, 1},
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 1},
            {1, 1, 1}
    }),
    FOUR(new int[][]{
            {1, 0, 1},
            {1, 0, 1},
            {1, 1, 1},
            {0, 0, 1},
            {0, 0, 1}
    }),
    FIVE(new int[][]{
            {1, 1, 1},
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 1},
            {1, 1, 1}
    }),
    SIX(new int[][]{
            {1, 1, 1},
            {1, 0, 0},
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
    }),
    SEVEN(new int[][]{
            {1, 1, 1},
            {0, 0, 1},
            {0, 1, 0},
            {1, 0, 0},
            {1, 0, 0}
    }),
    EIGHT(new int[][]{
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
    });

    private final int[][] pixels;

    Numbers(int[][] pixels) {
        this.pixels = pixels;
    }

    public int[][] getPixels() {
        return pixels;
    }
}