import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.LinkedList;

public class Paint extends JPanel {

    private BufferedImage buffer;
    private BufferedImage gridBuffer[] = new BufferedImage[13];
    private Block gridBlocks[][];
    private Logic logic;
    private States.game stateGame = States.game.PLAYNG;

    Point[] vertices = {
            new Point(20, 4),   // Arriba
            new Point(16, 16),  // Arriba-Izquierda
            new Point(4, 20),   // Izquierda
            new Point(16, 24),  // Abajo-Izquierda
            new Point(20, 36),  // Abajo
            new Point(24, 24),  // Abajo-Derecha
            new Point(36, 20),  // Derecha
            new Point(24, 16)   // Arriba-Derecha
    };
    /**
     * 1. Initial
     * 2. Number
     * - 0
     * - 1
     * - 2
     * - 3
     * - 4
     * - 5
     * - 6
     * - 7
     * - 8
     * 3. Flag
     * 4. Mine
     * 5. Mine Exploded
    */

    private States.dificulty dificulty;
    private float randomLevel = 0.1f;

    private int rows = 1, cols = 1;
    private int width_bufferGrid = 40, height_bufferGrid = 40;

    public Graphics graphics;

    public Paint(Dimension size) {
        setSize(size);
        buffer = new BufferedImage(size.width, size.height, BufferedImage.TRANSLUCENT);
        graphics = (Graphics) buffer.getGraphics();
        graphics.setColor(Color.black);

        stateGame = States.game.PLAYNG;
        initGridBuffer();
        getBufferImage();
        setGridValues();
    }

    @Override
    public void setSize(Dimension size) {
        buffer = new BufferedImage(size.width, size.height, BufferedImage.TRANSLUCENT);
        super.setSize(size);

        stateGame = States.game.PLAYNG;
        initGridBuffer();
        getBufferImage();
        setGridValues();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBlocks(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public void setDificulty(){

        int numberOfBlocks = rows * cols;

        if (numberOfBlocks > 400){
            dificulty = States.dificulty.HARD;
        }else if(numberOfBlocks > 225){
            dificulty = States.dificulty.MEDIUM;
        }else{
            dificulty = States.dificulty.EASY;
        }

        switch (dificulty){
            case EASY:{
                randomLevel = 0.1F;
                break;
            }
            case MEDIUM:{
                randomLevel = 0.12F;
                break;
            }
            case HARD:{
                randomLevel = 0.15F;
                break;
            }
        }
    }

    public void putPixel(int x, int y, Color c) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, c.getRGB());
        }
    }

    public void putPixel(int x, int y, boolean invertY){
        if(invertY){
            y = buffer.getHeight() - y;
        }
        putPixel(x,y,graphics.getColor());
    }

    public void putPixel(int x, int y, Color c, boolean invertY) {
        if(invertY){
            y = buffer.getHeight() - y;
        }
        putPixel(x,y,c);
    }

    public void initGridBuffer(){
        rows = getSize().width / width_bufferGrid;
        cols = getSize().height / height_bufferGrid;
        gridBlocks = new Block[rows][cols];

        for(int i = 0; i < gridBuffer.length; i++){
            gridBuffer[i] = new BufferedImage(width_bufferGrid, height_bufferGrid, BufferedImage.TRANSLUCENT);
        }

        setDificulty();
    }

    public void setGridValues(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                gridBlocks[i][j] = new Block(new Point(i * width_bufferGrid, j * height_bufferGrid));
            }
        }

        setBlockValue();
    }

    public void setBlockValue(){

        if(gridBlocks == null){
            return;
        }

        logic = new Logic(new int[rows][cols], randomLevel);

        int[][] values = logic.generateValues();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                gridBlocks[i][j].nearBombNumber = values[i][j];

                switch (gridBlocks[i][j].nearBombNumber){
                    case -1:{
                        gridBlocks[i][j].mineSweeper = States.mineSweeper.MINE;
                        break;
                    }
                    case 0:{
                        gridBlocks[i][j].mineSweeper = States.mineSweeper.EMPTY;
                        break;
                    }
                    case 1,2,3,4,5,6,7,8: {
                        gridBlocks[i][j].mineSweeper = States.mineSweeper.NEARMINE;
                        break;
                    }
                }
            }
        }

    }

    public void setBufferBackground(BufferedImage image, Color color){
        for(int i = 0; i < width_bufferGrid; i++){
            for(int j = 0; j < height_bufferGrid; j++){
                image.setRGB(i, j, color.getRGB());
            }
        }
    }

    public void setBufferGridPixel(int x, int y, BufferedImage image, Color color){
        image.setRGB(x, y, color.getRGB());
    }

    public void getBufferImage(){
        setBufferBackground(gridBuffer[0], new Color(240, 240, 240)); // Para el número 0 (fondo gris claro)
        setBufferBackground(gridBuffer[1], new Color(255, 220, 220)); // Para el número 1 (fondo rosa claro)
        setBufferBackground(gridBuffer[2], new Color(220, 220, 255)); // Para el número 2 (fondo azul claro)
        setBufferBackground(gridBuffer[3], new Color(200, 200, 255)); // Para el número 3 (fondo azul medio)
        setBufferBackground(gridBuffer[4], new Color(180, 180, 255)); // Para el número 4 (fondo azul claro)
        setBufferBackground(gridBuffer[5], new Color(160, 200, 200)); // Para el número 5 (fondo cyan claro)
        setBufferBackground(gridBuffer[6], new Color(140, 180, 180)); // Para el número 6 (fondo cyan medio)
        setBufferBackground(gridBuffer[7], new Color(120, 160, 160)); // Para el número 7 (fondo cyan oscuro)
        setBufferBackground(gridBuffer[8], new Color(100, 140, 140)); // Para el número 8 (fondo cyan oscuro)

        setBufferBackground(gridBuffer[9],Color.LIGHT_GRAY); // Initial

        setBufferBackground(gridBuffer[10],Color.BLACK);     // Bomb
        drawBombCirc(gridBuffer[10],20,20,10, new Color(73, 83, 32));

        setBufferBackground(gridBuffer[11],Color.ORANGE);    // Flag

        setBufferBackground(gridBuffer[12],Color.RED);       // Bomb Exploted
        drawBombHex(gridBuffer[12], Color.ORANGE);

        drawNumberByBomb(Numbers.ONE, gridBuffer[1]);
        drawNumberByBomb(Numbers.TWO, gridBuffer[2]);
        drawNumberByBomb(Numbers.THREE, gridBuffer[3]);
        drawNumberByBomb(Numbers.FOUR, gridBuffer[4]);
        drawNumberByBomb(Numbers.FIVE, gridBuffer[5]);
        drawNumberByBomb(Numbers.SIX, gridBuffer[6]);
        drawNumberByBomb(Numbers.SEVEN, gridBuffer[7]);
        drawNumberByBomb(Numbers.EIGHT, gridBuffer[8]);
        drawNumberByBomb(Numbers.FLAG, gridBuffer[11]);

    }

    public void drawNumberByBomb(Numbers number, BufferedImage image){

        int pixelSize = 3;
        int margin = 3;

        for (int i = 0; i < number.getPixels().length; i++) {
            for (int j = 0; j < number.getPixels()[i].length; j++) {

                if (number.getPixels()[i][j] > 0) {

                    int x = j * pixelSize;
                    int y = i * pixelSize;

                    // Dibujar un bloque de píxeles en la posición (x, y)
                    for (int k = 0; k < pixelSize; k++) {
                        for (int l = 0; l < pixelSize; l++) {
                            setBufferGridPixel(x + k + margin, y + l + margin, image, Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    public void drawBombHex(BufferedImage image, Color color){
        if (vertices.length < 3) {
            return;
        }

        for (int i = 0; i < vertices.length - 1; i++) {
            drawLine(image, vertices[i], vertices[i + 1], color);
        }

        drawLine(image, vertices[vertices.length - 1], vertices[0], color);

        floodFill(image, 20,20, Color.ORANGE);
    }

    public void drawLine(BufferedImage bf, Point p0, Point p1, Color c){
        int x0 = p0.x;
        int y0 = p0.y;
        int x1 = p1.x;
        int y1 = p1.y;

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        byte sx = (byte)(x0 < x1 ? 1 : -1);
        byte sy = (byte)(y0 < y1 ? 1 : -1);

        int err = dx - dy;
        int err2;

        int x = x0;
        int y = y0;

        while (x != x1 || y != y1) {
            setBufferGridPixel(x, y, bf, c);
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }

    }

    public void floodFill(BufferedImage bf, int x, int y, Color fillColor) {

        int targetColor = bf.getRGB(x, y);

        if (targetColor == fillColor.getRGB()) {
            return;
        }

        LinkedList<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            // Point p = queue.pop();
            int px = p.x;
            int py = p.y;

            if (!isInsideBlock(px, py) || bf.getRGB(px, py) != targetColor) {
                continue;
            }

            setBufferGridPixel(px, py, bf, fillColor);

            queue.add(new Point(px + 1, py));
            queue.add(new Point(px - 1, py));
            queue.add(new Point(px, py + 1));
            queue.add(new Point(px, py - 1));
        }

        repaint();
    }

    public boolean isInsideBlock(int x, int y){
        return x >= 0 && y >= 0 && x < width_bufferGrid && y < height_bufferGrid;
    }

    public void drawBombCirc( BufferedImage image, int xc, int yc, int radius, Color color){
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;

        drawCirclePointsOctant(image, xc, yc, x, y, color);

        while (x <= y) {
            x++;
            if (p > 0) {
                y--;
                p = p + 4 * (x - y) + 10;
            } else {
                p = p + 4 * x + 6;
            }
            drawCirclePointsOctant(image,xc, yc, x, y, color);
        }

        floodFill(image, xc, yc, color);
    }

    private void drawCirclePointsOctant(BufferedImage bf, int xc, int yc, int x, int y, Color color) {
        drawCirclePointsQuadrant(bf, xc, yc, x, y, color);
        drawCirclePointsQuadrant(bf, xc, yc, y, x, color);
    }

    private void drawCirclePointsQuadrant(BufferedImage bf, int xc, int yc, int x, int y, Color color){
        setBufferGridPixel(xc + x, yc + y, bf, color); // Cuadrante 1
        setBufferGridPixel(xc - x, yc + y, bf, color); // Cuadrante 2
        setBufferGridPixel(xc + x, yc - y, bf, color); // Cuadrante 3
        setBufferGridPixel(xc - x, yc - y, bf, color); // Cuadrante 4
        
    }

    private void paintBlocks(Graphics g){
        for(int i = 0; i < gridBlocks.length; i++){
            for(int j = 0; j < gridBlocks[i].length; j++){
                Block block = gridBlocks[i][j];

                switch(block.cell){
                    case COVERED -> g.drawImage(gridBuffer[9], block.x, block.y, this);
                    case FLAGGED -> g.drawImage(gridBuffer[11], block.x, block.y, this);
                    case EXPLOTED -> g.drawImage(gridBuffer[12], block.x, block.y, this);
                }

                if (block.cell != States.cell.UNCOVERED){
                    continue;
                }

                if(block.mineSweeper == States.mineSweeper.MINE){
                    g.drawImage(gridBuffer[10], block.x, block.y,this);
                    continue;
                }

                switch (block.nearBombNumber){
                    case 0 -> g.drawImage(gridBuffer[0], block.x, block.y, this);
                    case 1 -> g.drawImage(gridBuffer[1], block.x, block.y, this);
                    case 2 -> g.drawImage(gridBuffer[2], block.x, block.y, this);
                    case 3 -> g.drawImage(gridBuffer[3], block.x, block.y, this);
                    case 4 -> g.drawImage(gridBuffer[4], block.x, block.y, this);
                    case 5 -> g.drawImage(gridBuffer[5], block.x, block.y, this);
                    case 6 -> g.drawImage(gridBuffer[6], block.x, block.y, this);
                    case 7 -> g.drawImage(gridBuffer[7], block.x, block.y, this);
                    case 8 -> g.drawImage(gridBuffer[8], block.x, block.y, this);
                }
            }
        }
    }

    public void onFlagBlock(int width, int heigth){
        int x = width / width_bufferGrid;
        int y = heigth / height_bufferGrid;

        if (x < 0 || y < 0 || x >= rows || y >= cols){
            return;
        }

        if (gridBlocks[x][y].cell == States.cell.UNCOVERED){
            return;
        }

        if(gridBlocks[x][y].cell != States.cell.FLAGGED){
            gridBlocks[x][y].cell = States.cell.FLAGGED;
        }else{
            gridBlocks[x][y].cell = States.cell.COVERED;
        }

        isWin();

    }

    public void onClickBlock(int width, int heigth){

        int x = width / width_bufferGrid;
        int y = heigth / height_bufferGrid;

        if (x < 0 || y < 0 || x >= rows || y >= cols){
            return;
        }

        if (gridBlocks[x][y].cell == States.cell.UNCOVERED){
            return;
        }

        if (gridBlocks[x][y].cell == States.cell.FLAGGED){
            return;
        }

        onUncoveredBlock(x,y);
        isWin();
    }

    private void onUncoveredBlock(int x, int y){
        switch(gridBlocks[x][y].mineSweeper){
            case EMPTY: {
                floodFillBlock(x,y);
                return;
            }
            case MINE:
            {
                stateGame = States.game.LOST;
                break;
            }
        }

        gridBlocks[x][y].cell = States.cell.UNCOVERED;
    }

    public void floodFillBlock(int x, int y){
        if (gridBlocks[x][y].cell == States.cell.UNCOVERED) {
            return;
        }

        int[][] neighbors = {
                {-1,-1},{0,-1},{1,-1},
                {-1, 0},       {1, 0},
                {-1, 1},{0, 1},{1, 1}
        };

        // Recorre todos los bloques adyacentes
        for (int[] neighbor : neighbors) {
            int newX = x + neighbor[0];
            int newY = y + neighbor[1];

            // Verifica si las coordenadas están dentro de los límites del tablero
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                gridBlocks[x][y].cell = States.cell.UNCOVERED;
                if(gridBlocks[x][y].mineSweeper == States.mineSweeper.EMPTY){
                    floodFillBlock(newX, newY);
                }
            }
        }
    }

    public boolean didLose(){
        return stateGame == States.game.LOST;
    }

    public boolean didWin(){
        return stateGame == States.game.WON;
    }

    public void onLose(int xP, int yP){
        if(stateGame != States.game.LOST){
            return;
        }

        int x = xP / width_bufferGrid;
        int y = yP / height_bufferGrid;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                gridBlocks[i][j].cell = States.cell.UNCOVERED;
            }
        }

        gridBlocks[x][y].cell = States.cell.EXPLOTED;
        repaint();
    }

    public void isWin(){

        if(stateGame == States.game.LOST){
            return;
        }

        int sumBlocks = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(isFinalStateCorrect(i,j)){
                    sumBlocks += 1;
                }else{
                    return;
                }
            }
        }

        if(sumBlocks == cols * rows){
            stateGame = States.game.WON;
        }
    }

    public boolean isFinalStateCorrect(int i, int j){
        if(gridBlocks[i][j].mineSweeper == States.mineSweeper.MINE && gridBlocks[i][j].cell == States.cell.FLAGGED){
            return true;
        }

        if (gridBlocks[i][j].mineSweeper != States.mineSweeper.MINE && gridBlocks[i][j].cell == States.cell.UNCOVERED) {
            return true;
        }

        return false;
    }

}
