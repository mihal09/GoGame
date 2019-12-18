package server.player;

import server.enums.ColorEnum;

public interface BotMoveStrategy {
    void move(ColorEnum color);
}
