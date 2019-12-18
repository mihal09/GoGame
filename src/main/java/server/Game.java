package server;

import server.board.Board;
import server.board.Field;
import server.enums.ColorEnum;
import server.enums.GameState;
import server.player.BotMoveStrategy;
import server.player.HumanPlayer;
import server.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private ServerSocket listener;
    private LogicController logicController;
    private List<Player> players;
    private int moveCount;
    private GameState gameState;
    boolean whitePassed, blackPassed;
    boolean whiteAgreed, blackAgreed;
    private int size;
    boolean withBot;

    private static Game instance;

    public static Game getInstance() throws IOException {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void initialise(int size, boolean withBot,  ServerSocket listener) throws IOException {
        this.listener = listener;
        this.size = size;
        this.withBot = withBot;
        reset();
    }

    private void reset() throws IOException {
        System.out.println("RESET");
        logicController = new LogicController(size);
        moveCount = 0;
        gameState = GameState.BEFORE_START;
        addPlayers(withBot);
        runPlayers();
        gameState = GameState.MOVING;
        whitePassed = blackPassed = false;
    }

    private void addPlayers(boolean withBot) throws IOException {
        players = new ArrayList<>();
        players.add(new HumanPlayer(ColorEnum.BLACK, listener.accept()));
        if(!withBot)
            players.add(new HumanPlayer(ColorEnum.WHITE, listener.accept()));
        else
            players.add(new BotPlayer(ColorEnum.WHITE, this, new RandomStrategy()));
    }

    private void runPlayers() {
        for (Player player : players) {
            player.start();
        }
    }

    public LogicController getLogicController() {return logicController;}

    public void handleCommand(String command){ // MOVE 3 5 BLACK
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
            if(logicController.getCurrentPlayer().toString().equals(color)){
                boolean isValid = logicController.isMoveLegal(x,y, colorEnum);
                if(isValid){
                    System.out.println("VALID MOVE");
                    moveCount++;
                    for(Player player : players){
                        if(player instanceof HumanPlayer){
                            ((HumanPlayer) player).protocol.validMove(x,y,color);
                            ((HumanPlayer) player).protocol.sendBoard(logicController.getBoard());
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
            String words[] = command.split(" ");
            String surrenderingPlayer = words[1];
            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.surrender(surrenderingPlayer);
                }
            }
        }
        else if(command.startsWith("PASS")){
            if(!gameState.equals(GameState.MOVING))
                return;
            String words[] = command.split(" "); //PASS BLACK
            String color = words[1];
            if(!logicController.getCurrentPlayer().toString().equals(color)) // NOT HIS TURN
                return;
            if(color.equals("BLACK"))
                blackPassed = true;
            else if(color.equals("WHITE"))
                whitePassed = true;
            logicController.changePlayer();
            territoryAgreementStart();

        }
        else if(command.startsWith("MARK_TERRITORY")){ // MARK_TERRITORY 3 4 BLACK
            if(!gameState.equals(GameState.AGREEING))
                return;
            String words[] = command.split(" ");
            int x = Integer.parseInt(words[1]);
            int y = Integer.parseInt(words[2]);
            String color = words[3];
            Field stone = logicController.getBoard().getField(x,y);
            if(stone.getColor().equals(ColorEnum.EMPTY) || stone.getColor().equals(ColorEnum.EMPTY_BLACK) || stone.getColor().equals(ColorEnum.EMPTY_WHITE)){
                if(color.equals("BLACK"))
                    stone.setColor(ColorEnum.EMPTY_BLACK);
                else if(color.equals("WHITE"))
                    stone.setColor(ColorEnum.EMPTY_WHITE);
                else if(color.equals("EMPTY"))
                    stone.setColor(ColorEnum.EMPTY);
                for(Player player : players){
                    if(player instanceof HumanPlayer){
                        ((HumanPlayer) player).protocol.sendBoard(logicController.getBoard());
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
                logicController.setCurrentPlayer(ColorEnum.BLACK);
            else if(color.equals("WHITE"))
                logicController.setCurrentPlayer(ColorEnum.WHITE);

            logicController.getBoard().removeTerritory();
            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.territoryDisagreement();
                    ((HumanPlayer) player).protocol.sendBoard(logicController.getBoard());
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
            int [] scores =  logicController.getScore();
            System.out.println("CALCULATING SCORE");

            for(Player player : players){
                if(player instanceof HumanPlayer){
                    ((HumanPlayer) player).protocol.sendScores(scores[0], scores[1]);
                }
            }
        }
    }

    public class RandomStrategy implements BotMoveStrategy {
        public void move(ColorEnum color){
            boolean hasMoved = false;
            Board board = logicController.getBoard();
            for(int i = 0; i< board.getSize(); i++){
                for(int j = 0; j< board.getSize(); j++){
                    if(board.getField(i,j).getColor().equals(ColorEnum.EMPTY)){
                        boolean isValid = logicController.isMoveLegal(i,j,color);
                        if(isValid){
                            hasMoved = true;
                            for(Player player : players){
                                if(player instanceof HumanPlayer){
                                    ((HumanPlayer) player).protocol.validMove(i,j, color.toString());
                                    ((HumanPlayer) player).protocol.sendBoard(logicController.getBoard());
                                }
                            }
                            break;
                        }
                    }
                }
            }

            if(!hasMoved){
                System.out.println("NIE MAM RUCHU");
                whitePassed = true;
                territoryAgreementStart();
                logicController.changePlayer();
            }
        }
    }
    public class BotPlayer extends Player {
        private Game game;
        private BotMoveStrategy botMoveStrategy;

        BotPlayer(ColorEnum color, Game game, BotMoveStrategy botMoveStrategy) {
            alive = true;
            this.game = game;
            this.color = color;
            this.botMoveStrategy = botMoveStrategy;
        }

        @Override
        public void run() {
            while (alive) {
                synchronized (this) {
                    if (game.getLogicController().getCurrentPlayer().equals(this.color) || blackPassed) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                                logicController.changePlayer();
                            }
                            else if(gameState.equals(GameState.MOVING)) {
                                move();
                            }
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                            System.out.println("BLĄD W: " + color);
                        }
                    }
                }
            }
        }


        public void move(){
            botMoveStrategy.move(color);
        }
    }

}
