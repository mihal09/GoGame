package server.player;

import server.enums.ColorEnum;

public abstract class Player extends Thread {
        public ColorEnum color;
        public volatile boolean alive;

        /**
         * Changes alive value to false in order to stop the thread
         */
        void kill() {
            alive = false;
        }
}
