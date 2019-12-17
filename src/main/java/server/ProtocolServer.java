package server;

import server.board.Board;

import java.io.PrintWriter;


public class ProtocolServer {
    private final PrintWriter output;


    public ProtocolServer(PrintWriter output) {
        this.output = output;
        System.out.println("New protocol created.");
    }

    /**
     * Sends "START_GAME" message to a client.
     */
    public void startGame(Board board) {
        output.println("START_GAME " + board.getSize());
        System.out.println("startGame has been send.");
    }

    void sendBoard(Board board){
        String message = "";
        for (int x=0; x<board.getSize(); x++){
            for (int y=0; y<board.getSize(); y++){
                char field = ' ';
                switch ( board.getStone(x,y).getColor()){
                    case BLACK:
                        field = 'B';
                        break;
                    case WHITE:
                        field = 'W';
                        break;
                    case EMPTY:
                        field = 'E';
                        break;
                    case EMPTY_BLACK:
                        field = '1';
                        break;
                    case EMPTY_WHITE:
                        field = '2';
                        break;
                }
                message += field;
            }
        }
        output.println("BOARD " + message);
        System.out.println("sendBoard has been send.");
        System.out.println(message);
    }

    /**
     * Sends "VALID_MOVE" message to a client.
     */
    void validMove(int x, int y, String color) {
        output.println("VALID_MOVE " + x + " " + y + " " + color);
        System.out.println("validMove has been send.");
    }

    void startAgreeing(){
        output.println("START_AGREEING");
        System.out.println("Started agreeing");
    }

    void territoryDisagreement(){
        output.println("TERRITORY_DISAGREEMENT");
        System.out.println("territoryDisagreement");
    }

    public void sendScores(int scoreBlack, int scoreWhite) {
        output.println("SCORE" + " " + scoreBlack + " " + scoreWhite);
        System.out.println("GAME FINISHED SENDING SCORESS");
    }

    public void surrender(String playerColor){
        output.println("SURRENDER" + " " + playerColor);
    }

    /**
     * Sends "INVALID_MOVE" message to a client.
     */
    void invalidMove() {
        output.println("INVALID_MOVE");
        System.out.println("invalidMove has been send.");
    }


}
