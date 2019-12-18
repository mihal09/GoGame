package server.enums;

public enum ColorEnum {
    BLACK {
        public String toString(){
            return "BLACK";
        }
    },
    WHITE {
        public String toString(){
            return "WHITE";
        }
    },
    EMPTY {
        public String toString(){
            return "EMPTY";
        }
    },
    EMPTY_BLACK {
        public String toString(){
            return "EMPTY_BLACK";
        }
    },
    EMPTY_WHITE {
        public String toString(){
            return "EMPTY_WHITE";
        }
    }
}
