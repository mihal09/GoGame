package server;

import sample.Stone;

import java.util.ArrayList;

public class Controller {
    Board board;
    Field koKilled;
    Field koKiller;
    ColorEnum currentPlayer;

    public Controller(int size){
        board = new Board(size);
        koKilled = null;
        koKilled = null;
        currentPlayer = ColorEnum.BLACK;
    }

    public boolean isMoveLegal(int x, int y){
        boolean result = isMoveLegal(x,y,currentPlayer);
        if(result){
            currentPlayer = currentPlayer == ColorEnum.BLACK ? ColorEnum.WHITE : ColorEnum.BLACK;
        }
        return result;
    }

    public boolean isMoveLegal(int x, int y, ColorEnum color){
        System.out.println("zaczynam sprawdzac legalnosc");
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

        //

        ColorEnum enemyColor = currentPlayer == ColorEnum.BLACK ? ColorEnum.WHITE : ColorEnum.BLACK;
        ArrayList<StoneGroup> deadEnemyPlayerGroups = findDeadGroups(groupsCopy, enemyColor);
        ArrayList<StoneGroup> deadCurrentPlayerGroups = findDeadGroups(groupsCopy, currentPlayer);

        Field temporaryKoKiller = null;
        Field temporaryKoKilled = null;

        if(deadCurrentPlayerGroups.size() > 0){ //if current player's group
            System.out.println(deadCurrentPlayerGroups.size());
            if(deadEnemyPlayerGroups.size() > 0){ //if enemy's group died,


                if(deadEnemyPlayerGroups.size() == 1){
                    if(deadEnemyPlayerGroups.get(0).size() == 1){
                        Field dyingStone = deadEnemyPlayerGroups.get(0).getStones().get(0);
                        if(currentStone.equals(koKilled) && dyingStone.equals(koKiller)){ //KO
                            System.out.print("KO1");
                            System.out.println(koKilled);
                            System.out.println(koKiller);
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
                        if(deadEnemyPlayerGroups.get(0).size() == 1){
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
                        deadStone.setColor(ColorEnum.EMPTY);
                    }
                    groupsCopy.remove(deadGroup);
                }
            }
        }


        koKilled = temporaryKoKilled ;
        koKiller = temporaryKoKiller;
        groupsCopy.add(newGroup);
        board.setGroups(groupsCopy);


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
}
