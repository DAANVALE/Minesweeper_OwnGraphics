import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Block {

    public int x,y;

    public States.mineSweeper mineSweeper;
    public States.cell cell;
    public int nearBombNumber = 0;

    public Block(Point position){
        mineSweeper = States.mineSweeper.EMPTY;
        nearBombNumber = 0;
        x = position.x;
        y = position.y;
    }

}
