package sample;

public class Board extends BoardAbstract {
    public static final int offset = 40;
    public Board(int size){
        this.size = size;
        addStones();
    }

    protected void clearStones(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                stones[i][j].setColor(ColorEnum.EMPTY);
            }
        }
    }

    public void addStones(){
        stones = new Stone[size][size];
        // creating fields
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                stones[i][j] = new Stone(i*(Stone.radius+offset)+Stone.radius, j*(Stone.radius+offset)+Stone.radius, ColorEnum.EMPTY);
            }
        }
    }
}
