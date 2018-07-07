package it.polimi.ingsw.util;


import it.polimi.ingsw.client.connection.ConnectionClient;

public enum TypeMove {

    CHOOSE_INDEX_DIE {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setIndexDash();
        }
    },

    PLACE_COORDINATES {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setRowColumn( true );
        }
    },

    GET_COORDINATES {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setRowColumn( false );
        }
    },

    CHOOSE_TOOL_INDEX {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setToolIndex();
        }
    },

    CHOOSE_PLUS_MIN {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setPlusMin();
        }
    },

    CHOOSE_DIE_NUM {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setDieNum();
        }
    },

    CHOOSE_ROUND_INDEX {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.setRoundTrackIndex();
        }
    },

    CHOOSE_SEND_MOVE {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.sendMove();
        }
    },

    CHOOSE_GO_ON {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.goOn();
        }
    },

    WAIT_MOVE {
        public void moveToDo(ConnectionClient connectionClient) {
            connectionClient.newPlaceMove();
        }
    };

    public abstract void moveToDo(ConnectionClient connectionClient);
}