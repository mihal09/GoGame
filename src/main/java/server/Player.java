package server;

public abstract class Player extends Thread {
        ColorEnum color;
        volatile boolean alive;

        /**
         * Changes alive value to false in order to stop the thread
         */
        void kill() {
            alive = false;
        }
}
