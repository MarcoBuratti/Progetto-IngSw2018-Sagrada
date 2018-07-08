# Socket data exchange protocol

Messages from client to Server and the "Update massage" are encoded using standard JSON format (key/value).
The other message are send using string standard format.
### Welcome message
Message sent from server to client to welcome the player and ask for username.
*<"welcome">CR*

### Login message
Message sent from client to server to communicate username.
*<{"You have logged in as:": "username"}>CR*

### Relogin message
Message sent from server to client to notify the login process has been completed successfully and communicate the player ID.
*<{"You have logged in again as: ": "username"}>CR*

### Failed login message
Message sent from server to client to ask for insert username again because the login process has failed.
*<{"This nickname has been already used! Please try again": "failed_login"}>CR*

### Game start message
Message sent from server to client to notify the game start, communicating the list of users playing the same game through a JSON file.
*<{"The game has started!"}>CR*

### Game end message
Message sent from server to client to notify the game end, communicating the players' scores and results through a JSON file.
*<{"You win!", "You lose!", "Player + Score"}>CR*

### Online again message
Message sent from client to server to notify the user is online again after a disconnection.
*<{"nickname + “has reconnected!"}>CR*

### Private Achievement message
Message sent from Server to client to notify the player of his private achievement
*<{"Your private achievement is:  + privateAchievementColor"}>*

### Reconnected message
Message sent from server to client to notify the user has been reconnected to the game and is now able to play.
*<{"reconnected"}>CR*

### Scheme Message
Message sent from Server to Client to ask the scheme
*<{"schemes. + schemes"}>CR*

### Second Scheme Message
Message sent from Server to Client to notify the scheme chosen
*<{"You have chosen the following scheme: " + scheme name + "Please wait, the game will start soon."}>CR*

### Third Scheme Message
Message sent from Server to Client to notify the scheme chosen after the timer deadline
*<{“Too late! Your scheme is: " + schemaDiDefault + "The game has already started!"}>CR*

### Elaboration Message
Message sent from Server to Client to tell him that Server is processing the move
*<{"Trying to make the move ..."}>CR*

### First disconnection Message
Message sent from Server to Client to the game has ended
*<{"Connection expired.", “Terminate.”}>CR*
 
### Second disconnection message
Message sent from Server to Client to tell him that he has been disconnected
*<{“You’ve been disconnected successfully.”}>CR*

### Third disconnection message
Message sent from Server to Client to tell that someone has disconnected from server
*<{nickname + “has disconnected from the server.”}>CR*

### Fourth disconnection message
Message sent from Client to Server to tell that player wants to leave the game
*<{"\quit"}>*

### Gameboard Update
Message sent from Server to Client to update the gameboard
*<{Private Achievement "Tool":"Extracted Tool" + "Public Achievement":"Extracted Achievement" + "RoundTrack":"Extracted Die" + "DraftPool":"DraftPool die" + Player + FavourTokens + Dashboard}>CR*

### Going through
This message is used to notify the player has chosen to go through without placing dice or using tool cards.
*<{"playerID": "ID", "type_playerMove": "GoThrough"}>CR*

### Placing a die
This message is used to notify the player has chosen to place a die on his own dashboard.
*<{"playerID": "ID", "type_playerMove": "PlaceDie", "Die": "Die_Index", "Row": "Row_Index", "Column": "Column_Index"}>CR*

### Using a tool card
This message is used to notify the player has chosen to use a tool card.
*<{"playerID": "ID", "type_playerMove": "UseTool", "Tool": "Tool_Name", "arguments.."}>CR*

### First Turn Message
Message sent from Server to Client to tell him that it's not his turn
*<{"It's not your turn. Please wait."}>CR*

### Second Turn Message
Message sent from Server to Client to tell him that move is incorrect
*<{“Your move is incorrect.”}>CR*

### Third Turn Message
Message sent from Server to Client to tell him that it's turn of....
*<{"It's " + nickname del currentPlayer + "'s turn. Please wait."}>CR*

### Fourth Turn Message
Message sent from Server to Client to tell him that it's his turn
*<{"It's your turn!Please make your move." }>CR*

### Fifth Turn Message
Message sent from Server to Client to tell him that his turn has ended
*<{nicknameCurrentPlayer + “’s turn has ended.”}>CR*

### Sixth Turn Message
Message sent from Server to Client to tell him that the time is over
*<{“The time is over!”}>CR*

### Seventh Turn Message
Message sent from Server to Client to tell him that tool usage succeeded
*<{“The selected tool has been used successfully"}>CR*

### Eighth Turn Message
Message sent from Server to Client to tell him that placement move succeeded
*<{“The die has been placed on the selected cell."}>CR*

### Ninth Turn Message
Message sent from Server to Client to tell him that he can't use the tool
*<{"You don't have enough favour tokens left to use this tool!"}>CR*

### Tenth Turn Message
Message sent from Server to Client to tell him to complete the move afrte using a tool card
*<{"Please complete your move:"}>CR*

### Tool double communication Message
Message sent from Server to Client to tell him that player cannot place the die
*<{"You cannot place this die anyway!"}>CR*

### Tool double communication Message
Message sent from Server to Client to tell him that the move is incorrect
*<{"Try again placing the die!"}>CR*

### Tool double communication Message
Message sent from Server to Client to tell him that by the way the player cannot place the die
*<{"You cannot place the die anymore!"}>CR*