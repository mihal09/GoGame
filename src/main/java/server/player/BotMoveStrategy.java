package server.player;

import server.enums.ColorEnum;

public interface BotMoveStrategy {
    public void move(ColorEnum color);
}
