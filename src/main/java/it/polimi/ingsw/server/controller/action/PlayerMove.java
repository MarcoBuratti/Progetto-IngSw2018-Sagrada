package it.polimi.ingsw.server.controller.action;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerMove implements Serializable {
    private static final String PLACE_DIE = "PlaceDie";
    private static final String USE_TOOL = "UseTool";
    private static final String GO_THROUGH = "GoThrough";
    private String playerNickname;
    private String moveType;
    private ArrayList<Integer> intParameters;
    private Boolean twoReplace;
    private Boolean addOne;
    private Integer indexDie;
    private Integer extractedToolIndex;

    /**
     * Constructor used to create a new go through move.
     *
     * @param playerNickname the player's nickname
     * @param moveType       the type of the new move
     */
    public PlayerMove(String playerNickname, String moveType) {
        this.playerNickname = playerNickname;
        if (moveType.equals(GO_THROUGH))
            this.moveType = moveType;
        else
            throw new IllegalArgumentException();
    }

    /**
     * Constructor used for the 6th tool.
     *
     * @param playerNickname     the player's nickname
     * @param moveType           the type of the new move
     * @param extractedToolIndex the index of the tool chosen by the player
     */
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.extractedToolIndex = extractedToolIndex;
    }

    /**
     * Constructor used for the place die move or for the following tools: 1st, 3rd, 9th, 12th.
     *
     * @param playerNickname the player's nickname
     * @param moveType       the type of the new move
     * @param firstParameter an int representing the index of the selected die ( draft pool ) if it's a placement move or the index of the selected tool if it's a tool move
     * @param intParameters  a list of int parameters containing parameters used for the moves
     */
    public PlayerMove(String playerNickname, String moveType, int firstParameter, List<Integer> intParameters) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.intParameters = (ArrayList<Integer>) intParameters;
        switch (moveType) {
            case PLACE_DIE:
                this.indexDie = firstParameter;
                break;
            case USE_TOOL:
                this.extractedToolIndex = firstParameter;
                this.twoReplace = intParameters.size() > 4;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor used for the following tools: 2nd, 10th, 11th.
     *
     * @param playerNickname     the player's nickname
     * @param moveType           the type of the new move
     * @param extractedToolIndex the index of the tool chosen by the player
     * @param indexDie           an int representing the index of the selected die ( draft pool )
     * @param intParameters      a list of int parameters containing parameters used for the moves
     */
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie, List<Integer> intParameters) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.extractedToolIndex = extractedToolIndex;
        this.intParameters = (ArrayList<Integer>) intParameters;
        this.indexDie = indexDie;
    }

    /**
     * Constructor used for the 8th tool.
     *
     * @param playerNickname     the player's nickname
     * @param moveType           the type of the new move
     * @param extractedToolIndex the index of the tool chosen by the player
     * @param indexDie           an int representing the index of the selected die ( draft pool )
     * @param addOne             a boolean that specifies if the user wants to increase or decrease the value of the die
     */
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie, boolean addOne) {
        this.playerNickname = playerNickname;
        this.extractedToolIndex = extractedToolIndex;
        this.moveType = moveType;
        this.indexDie = indexDie;
        this.addOne = addOne;
    }

    /**
     * Constructor used for the following tools: 4th, 5th, 7th.
     *
     * @param playerNickname     the player's nickname
     * @param moveType           the type of the new move
     * @param extractedToolIndex the index of the tool chosen by the player
     * @param indexDie           an int representing the index of the selected die ( draft pool )
     */
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie) {
        this.playerNickname = playerNickname;
        this.extractedToolIndex = extractedToolIndex;
        this.moveType = moveType;
        this.indexDie = indexDie;
    }

    /**
     * A static method that allows the user to create a new PlayerMove using the right constructor.
     * It reads the parameters it needs from a JSON file through the method playerMoveReader.
     *
     * @return a new PlayerMove Object created by reading a JSON file
     */
    public static PlayerMove playerMoveConstructor() {
        try {
            JSONObject jsonObject = PlayerMoveParser.readMove();
            return playerMoveReader(jsonObject);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * It gets all the needed parameters from the jsonObject parameter and then selects the right constructor
     * for a new PlayerMove and returns it.
     *
     * @param jsonObject the JSONObject containing the parameters needed to construct the move
     * @return a new PlayerMove
     */
    public static PlayerMove playerMoveReader(JSONObject jsonObject) {

        String playerNickname = (String) jsonObject.get("playerID");
        String moveType = (String) jsonObject.get("type_playerMove");
        int dieIndex;
        ArrayList<Integer> coordinates;
        int otherKeysSize;


        switch (moveType) {
            case PLACE_DIE:
                dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                otherKeysSize = jsonObject.size() - 3;
                coordinates = new ArrayList<>();
                for (int i = 0; i < otherKeysSize; i++)
                    coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 2))));
                return new PlayerMove(playerNickname, moveType, dieIndex, coordinates);

            case USE_TOOL:
                int toolIndex = Integer.parseInt((String) jsonObject.get("toolIndex"));
                int extractedToolIndex = Integer.parseInt((String) jsonObject.get("extractedToolIndex"));
                switch (toolIndex) {

                    case 7:
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        String boolKey = ((String) jsonObject.get("Key2"));
                        boolean bool;
                        bool = (boolKey.equals("1"));
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex, bool);

                    case 0:
                    case 2:
                    case 8:
                    case 11:
                        otherKeysSize = jsonObject.size() - 4;
                        coordinates = new ArrayList<>();
                        for (int i = 0; i < otherKeysSize; i++) {
                            coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 1))));
                        }
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, coordinates);

                    case 1:
                    case 9:
                    case 10:
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        otherKeysSize = jsonObject.size() - 5;
                        coordinates = new ArrayList<>();
                        for (int i = 0; i < otherKeysSize; i++) {
                            coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 2))));
                        }
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex, coordinates);

                    case 3:
                    case 4:
                    case 6:
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex);

                    case 5:
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex);

                    default:
                        break;
                }
                throw new IllegalArgumentException();


            case GO_THROUGH:
                return new PlayerMove(playerNickname, moveType);

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the playerNickname attribute.
     *
     * @return a String representing the player's nickname
     */
    public String getPlayerNickname() {
        return this.playerNickname;
    }

    /**
     * Returns the value positioned at the "index" position of the intParameters attribute if it exists.
     *
     * @param index the index of the intParameters attribute the user wants to access
     * @return an Integer
     */
    public Integer getIntParameters(int index) {
        if (!intParameters.isEmpty() && index < intParameters.size()) {
            return intParameters.get(index);
        } else
            throw new IllegalArgumentException();
    }

    /**
     * Returns an Optional ( Boolean ) which specifies if the player wants to move one or two dice ( 11th tool ).
     *
     * @return an Optional, the twoReplace attribute
     */
    public Optional<Boolean> getTwoReplace() {
        return Optional.ofNullable(twoReplace);
    }

    /**
     * Rerturns an Optional ( Integer ) which specifies the index of the chosen die in the draft pool.
     *
     * @return an Optional, the indexDie attribute
     */
    public Optional<Integer> getIndexDie() {
        return Optional.ofNullable(indexDie);
    }

    /**
     * Returns an Optional ( Integer ) which specifies if the player wants to increase or decrease the value of the die ( 8th tool ).
     *
     * @return an Optional, the addOne attribute
     */
    public Optional<Boolean> getAddOne() {
        return Optional.ofNullable(addOne);
    }

    /**
     * Returns an Optional ( Integer ) which specifies the index of the selected tool ( from the three ones extracted ).
     *
     * @return an Optional, the extractedToolIndex attribute
     */
    public Optional<Integer> getExtractedToolIndex() {
        return Optional.ofNullable(extractedToolIndex);
    }

    /**
     * Returns the moveType attribute.
     *
     * @return a String representing the type of the move
     */
    public String getMoveType() {
        return moveType;
    }

}


