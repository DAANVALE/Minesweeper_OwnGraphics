import java.awt.Point;

public class Block {

    public int x,y;

    public States.mineSweeper mineSweeper = States.mineSweeper.EMPTY;
    public States.cell cell;
    public int nearBombNumber = 0;

    public Block(Point position){
        cell = States.cell.COVERED;
        nearBombNumber = 0;
        x = position.x;
        y = position.y;
    }

}
