package server;

import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private ServerSocket listener;
    private Controller controller;
    private List<Player> players;
    private int moveCount;
    private GameState gameState;
    boolean whitePassed, blackPassed;
    boolean whiteAgreed, blackAgreed;



    public Game(int size, boolean withBot,  ServerSocket listener) throws IOException {
        this.listener = listener;
        controller = new Controller(size);
        moveCount = 0;
        gameState = GameState.BEFORE_START;
        addPlayers(withBot);
        runPlayers();
        gameState = GameState.MOVING;
        whitePassed = blackPassed = false;
    }

    private void addPlayers(boolean withBot) throws IOException {
        players = new ArrayList<>();
        players.add(new HumanPlayer(ColorEnum.BLACK, listener.accept(), this));
        if(!withBot)
            players.add(new HumanPlayer(ColorEnum.WHITE, listener.accept(), this));
        else
            players.add(new BotPlayer(ColorEnum.WHITE, this));
    }

    private void runPlayers() {
        for (Player player : players) {
            player.start();
        }
    }

    public Controller getController() {return  controller;}

    void handleCommand(String command){ // MOVE 3 5 BLACK
        System.out.println("Command from a client: " + command);
        if (command.startsWith("MOVE")) {
            if(!gameState.equals(GameState.MOVING))
                return;
            blackPassed = whitePassed = false;
            String words[] = command.split(" ");
            int x = Integer.parseInt(words[1]);
            int y = Integer.parseInt(words[2]);
            String color = words[3];
            ColorEnum colorEnum = color.equals("BLACK") ? ColorEnum.BLACK : ColorEnum.WHITE ;
            if(controller.getCurrentPlayer().toString().equals(color)){
                boolean isValid = controller.isMoveLegal(x,y, colorEnum);
                if(isValid){
                    System.out.println("VALID MOVE");
                    moveCount++;
                    for(Player player : players){
                        if(player instanceof HumanPlayer){
                            ((HumanPlayer) player).protocol.validMove(x,y,color);
                            ((HumanPlayer) player).protocol.sendBoard(controller.getBoard());
                        }
                    }
                }
                else{
                    System.out.println("INVALID MOVE");
                    for(Player player : players){
                        if(player instanceof HumanPlayer){
                            ((HumanPlayer) player).protocol.invalidMove();
                        }
                    }
                }
            }
        }
        else if(command.startsWith("SURRENDER")){
            //TODO
            //KONIEC GRY
        }
        else if(command.startsWith("PASS")){
            if(!gameState.equals(GameState.MOVING))
                return;
            String words[] = command.split(" "); //PASS BLACK
            String color = words[1];
            if(!controller.getCurrentPlayer().toString().equals(color)) // NOT HIS TURN
                return;
            if(color.equals("BLACK"))
                blackPassed = true;
            else if(color.equals("WHITE"))
                whitePassed = true;
            controller.changePlayer();
            territoryAgreementStart();

        }
        else if(command.startsWith("MARK_TERRITORY")){ // MARK_TERRITORY 3 4 BLACK
            if(!gameState.equals(GameState.AGREEING))
                return;
            String words[] = command.split(" ");
            int x = Integer.parseInt(words[1]);
            int y = Integer.parseInt(words[2]);
            String color = words[3];
            Field stone = controller.getBoard().getField(x,y);
            if(stone.getColor().equals(ColorEnum.EMPTY) || stone.getColor().equals(ColorEnum.EMPTY_BLACK) || stone.getColor().equals(ColorEnum.EMPTY_WHITE)){
                if(color.equals("BLACK"))
                    stone.setColor(ColorEnum.EMPTY_BLACK);
                else if(color.equals("WHITE"))
                    stone.setColor(ColorEnum.EMPTY_WHITE);
                else if(color.equals("EMPTY"))
                    stone.setColor(ColorEnum.EMPTY);
                for(Player player : players){
                    if(player instanceof HumanPlayer){
                        ((HumanPlayer) player).protocol.sendBoard(controller.getBoard());
                    }
                }
            }
        }
        else if(command.startsWith("TERRITORY_AGREE")){
            if(!gameState.equals(GameState.AGREEING))
                return;
            String words[] = command.split(" ");
            String color = words[1];
            if(color.equals("BLACK"))
                blackAgreed = true;
            else if(color.equals("WHITE"))
                whiteAgreed = true;
            territoryAgreementFinish();
        }
        else if(command.startsWith("TERRITORY_DISAGREE")){
            if(!gameState.equals(GameState.AGREEING))
                return;

            whitePassed = blackPassed = whiteAgreed = blackAgreed = false;
            String words[] = command.split(" ");
            String color = words[1];
            gameState = GameState.MOVING;
            if(color.equals("BLACK"))
                controller.setCurrentPlayer(ColorEnum.BLACK);
            else if(color.equals("WHITE"))
                controller.setCurrentPlayer(ColorEnum.WHITE);

            controller.getBoard().removeTerritory();
            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.territoryDisagreement();
                    ((HumanPlayer) player).protocol.sendBoard(controller.getBoard());
                }
            }
        }

    }

    private void territoryAgreementStart(){
        if(blackPassed && whitePassed){
            System.out.println("STARTED AGREEING");
            gameState = GameState.AGREEING;
            whiteAgreed = blackAgreed = false;
            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.startAgreeing();
                }
            }
        }
    }

    private void territoryAgreementFinish() {
        if(whiteAgreed && blackAgreed){
            //TODO GAME END
            int [] scores =  controller.getScore();
            System.out.println("CALCULATING SCORE");

            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.sendScores(scores[0], scores[1]);
                }
            }
        }
    }

    public class BotPlayer extends Player {
        private Game game;

        BotPlayer(ColorEnum color, Game game) {
            alive = true;
            this.game = game;
            this.color = color;
        }

        @Override
        public void run() {
            while (alive) {
                synchronized (this) {

                    if (game.getController().getCurrentPlayer().equals(this.color) || blackPassed) {
                        try {
                            if(blackPassed){
                                if(!whitePassed){
                                    whitePassed = true;
                                    territoryAgreementStart();
                                }

                                if(blackAgreed && !whiteAgreed) {
                                    System.out.println("BIALY SIE ZGADZA");
                                    whiteAgreed = true;
                                    territoryAgreementFinish();
                                }
                                controller.changePlayer();
                            }
                            else if(gameState.equals(GameState.MOVING))
                                move();
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                            System.out.println("BLĄD W: " + color);
                        }
                    }
                }
            }
        }

        private void move(){
            Board board = controller.getBoard();
            for(int i = 0; i< board.getSize(); i++){
                for(int j = 0; j< board.getSize(); j++){
                    if(board.getField(i,j).getColor().equals(ColorEnum.EMPTY)){
                        boolean isValid = controller.isMoveLegal(i,j,color);
                        if(isValid){
                            for(Player player : players){
                                if(player instanceof HumanPlayer){
                                    ((HumanPlayer) player).protocol.validMove(i,j, color.toString());
                                    ((HumanPlayer) player).protocol.sendBoard(controller.getBoard());
                                }
                            }
                            break;
                        }
                    }
                }
            }

        }
    }

}
