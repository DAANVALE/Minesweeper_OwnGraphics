import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Paint extends JPanel {

    private BufferedImage buffer;
    private BufferedImage gridBuffer[] = new BufferedImage[13];
    private Block gridBlocks[][];
    private Logic logic;
    private States.game stateGame = States.game.PLAYNG;

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

    public void putPixel(int x, int y){
        putPixel(x,y,graphics.getColor());
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

    public void setBufferPixel(BufferedImage image, Color color){
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
        setBufferPixel(gridBuffer[0], new Color(240, 240, 240)); // Para el número 0 (fondo gris claro)
        setBufferPixel(gridBuffer[1], new Color(255, 220, 220)); // Para el número 1 (fondo rosa claro)
        setBufferPixel(gridBuffer[2], new Color(220, 220, 255)); // Para el número 2 (fondo azul claro)
        setBufferPixel(gridBuffer[3], new Color(200, 200, 255)); // Para el número 3 (fondo azul medio)
        setBufferPixel(gridBuffer[4], new Color(180, 180, 255)); // Para el número 4 (fondo azul claro)
        setBufferPixel(gridBuffer[5], new Color(160, 200, 200)); // Para el número 5 (fondo cyan claro)
        setBufferPixel(gridBuffer[6], new Color(140, 180, 180)); // Para el número 6 (fondo cyan medio)
        setBufferPixel(gridBuffer[7], new Color(120, 160, 160)); // Para el número 7 (fondo cyan oscuro)
        setBufferPixel(gridBuffer[8], new Color(100, 140, 140)); // Para el número 8 (fondo cyan oscuro)

        setBufferPixel(gridBuffer[9],Color.LIGHT_GRAY); // Initial
        setBufferPixel(gridBuffer[10],Color.BLACK);     // Bomb
        setBufferPixel(gridBuffer[11],Color.ORANGE);    // Flag
        setBufferPixel(gridBuffer[12],Color.RED);       // Bomb Exploted

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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBlocks(g);
        g.drawImage(buffer, 0, 0, this);
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

        // Array para almacenar las coordenadas de los bloques adyacentes
        int[][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

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
