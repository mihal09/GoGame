package server;


import server.board.Board;
import server.board.Field;
import server.board.StoneGroup;
import server.enums.ColorEnum;

import java.util.ArrayList;

public class LogicController {
    private Board board;
    Field koKilled;
    Field koKiller;


    private ColorEnum currentPlayer;
    int whiteScore, blackScore;

    public LogicController(int size){
        board = new Board(size);
        koKilled = null;
        koKilled = null;
        currentPlayer = ColorEnum.BLACK;
        whiteScore = blackScore = 0;
    }


    public boolean isMoveLegal(int x, int y, ColorEnum color){
        if(!currentPlayer.equals(color))
            return false;
        if(!board.isEmpty(x,y))
            return false;

        Field currentStone = board.getField(x,y);
        currentStone.setColor(color);

        ArrayList<StoneGroup> groupsCopy = new ArrayList<StoneGroup>(board.getGroups());


        StoneGroup newGroup = new StoneGroup(board, color);
        ArrayList<StoneGroup> adjacentGroups = new ArrayList<StoneGroup>();
        newGroup.addStone(currentStone);



        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if((i==0 && j == 0) || (i!=0 && j!=0))
                    continue;
                int newX = x+i;
                int newY = y+j;

                if(board.isSameColor(newX,newY, color)){
                    StoneGroup group = findGroupWithStone(groupsCopy, board.getField(newX,newY));
                    if(!adjacentGroups.contains(group)){
                        adjacentGroups.add(group);
                    }
                }
            }
        }

        if(adjacentGroups.size() > 0) {
            for (StoneGroup group : adjacentGroups) {
                if(group.getStones().size()>0) {
                    for (Field stone : group.getStones()) {
                        newGroup.addStone(stone);
                    }
                }
                groupsCopy.remove(group);
            }
        }
        groupsCopy.add(newGroup);

        //

        ColorEnum enemyColor = currentPlayer == ColorEnum.BLACK ? ColorEnum.WHITE : ColorEnum.BLACK;
        ArrayList<StoneGroup> deadEnemyPlayerGroups = findDeadGroups(groupsCopy, enemyColor);
        ArrayList<StoneGroup> deadCurrentPlayerGroups = findDeadGroups(groupsCopy, currentPlayer);

        Field temporaryKoKiller = null;
        Field temporaryKoKilled = null;


        if(deadCurrentPlayerGroups.size() > 0){ //if current player's group
            if(deadEnemyPlayerGroups.size() > 0){ //if enemy's group died,

                if(deadEnemyPlayerGroups.size() == 1){
                    if(deadEnemyPlayerGroups.get(0).getSize() == 1){
                        Field dyingStone = deadEnemyPlayerGroups.get(0).getStones().get(0);
                        if(currentStone.equals(koKilled) && dyingStone.equals(koKiller)){ //KO
                            currentStone.setColor(ColorEnum.EMPTY);
                            return false;
                        }
                        temporaryKoKilled = dyingStone;
                        temporaryKoKiller = currentStone;
                    }
                }
                for(StoneGroup deadGroup : deadEnemyPlayerGroups){
                    System.out.println("ZABIJAM TU");
                    for(Field deadStone : deadGroup.getStones()){
                        if(deadGroup.getColor().equals(ColorEnum.BLACK))
                            whiteScore++;
                        else
                            blackScore++;
                        deadStone.setColor(ColorEnum.EMPTY);
                    }
                    groupsCopy.remove(deadGroup);
                }
            }
            else{ //suicide
                System.out.println("SUICIDE");
                currentStone.setColor(ColorEnum.EMPTY);
                return false;
            }
        }
        else{
            if(deadEnemyPlayerGroups.size() > 0){
                if(deadEnemyPlayerGroups.size() == 1){
                    if(deadEnemyPlayerGroups.size() == 1){
                        if(deadEnemyPlayerGroups.get(0).getSize() == 1){
                            Field dyingStone = deadEnemyPlayerGroups.get(0).getStones().get(0);
                            if(currentStone.equals(koKilled) && dyingStone.equals(koKiller)){ //KO
                                System.out.print("KO2");
                                System.out.println(koKilled);
                                System.out.println(koKiller);
                                currentStone.setColor(ColorEnum.EMPTY);
                                return false;
                            }
                            temporaryKoKilled = dyingStone;
                            temporaryKoKiller = currentStone;
                        }
                    }
                }
                for(StoneGroup deadGroup : deadEnemyPlayerGroups){
                    System.out.println("ZABIJAM");
                    for(Field deadStone : deadGroup.getStones()){
                        if(deadGroup.getColor().equals(ColorEnum.BLACK))
                            whiteScore++;
                        else
                            blackScore++;
                        deadStone.setColor(ColorEnum.EMPTY);
                    }
                    groupsCopy.remove(deadGroup);
                }
            }
        }


        koKilled = temporaryKoKilled ;
        koKiller = temporaryKoKiller;
        board.setGroups(groupsCopy);

        changePlayer();

        return true;
    }

    public StoneGroup findGroupWithStone(ArrayList<StoneGroup> groupsList, Field stone){
        for( StoneGroup stoneGroup : groupsList){
            if(stoneGroup.containsStone(stone))
                return stoneGroup;
        }
        return null;
    }

    public ArrayList<StoneGroup> findDeadGroups(ArrayList<StoneGroup> groupsList, ColorEnum color){
        ArrayList<StoneGroup> deadGroups = new ArrayList<StoneGroup>();
        for (StoneGroup group : groupsList){
            if(group.getColor().equals(color) && !group.hasBreath())
                deadGroups.add(group);
        }
        return deadGroups;
    }

    public Board getBoard(){
        return board;
    }

    public ColorEnum getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ColorEnum currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void changePlayer(){
        currentPlayer = currentPlayer == ColorEnum.BLACK ? ColorEnum.WHITE : ColorEnum.BLACK;
    }

    public int[] getScore() {
        int [] scores = new int[2];
        scores[0] = blackScore;
        scores[1] = whiteScore;

        for(int x = 0; x < board.getSize(); x++){
            for(int y = 0; y < board.getSize(); y++){
                ColorEnum color = board.getField(x,y).getColor();
                if(color.equals(ColorEnum.EMPTY_BLACK))
                    scores[0]++;
                else if(color.equals(ColorEnum.EMPTY_WHITE))
                    scores[1]++;
            }
        }

        return scores;
    }
}
