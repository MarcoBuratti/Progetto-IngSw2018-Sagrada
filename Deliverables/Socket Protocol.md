# Socket Mode data exchange protocol
### Welcome message
Message sent from server to client to welcome the player and ask for username.
*"welcome"CR*

### Login message
Message sent from client to server to communicate username.
*"login"<username>CR*

### Successful login message
Message sent from server to client to notify the login process has been completed successfully and communicate the player ID.
*"successful_login"<playerID>CR*

### Failed login message
Message sent from server to client to ask for insert username again because the login process has failed.
*"failed_login"CR*

### Game start message
Message sent from server to client to notify the game start, communicating the list of users playing the same game through a JSON file.
*"game_start"<json_file>CR*

### Game end message
Message sent from server to client to notify the game end, communicating the players' scores and results through a JSON file.
*"game_end"<json_file>CR*

### Online again message
Message sent from client to server to notify the user is online again after a disconnection.
*"online_again"CR*

### Reconnected message
Message sent from server to client to notify the user has been reconnected to the game and is now able to play.
*"reconnected"CR*

### Game Update message
Message sent from server to client to notify changes on the game board through a JSON file.
*"game_update"<json_file>CR*

### Player moves messages
Message sent from client to server to notify a game move.
The playerID argument must specify the player making the move,
the moveID specifies which kind of move is done and other arguments depend on the kind of move.
*"player_move"<playerID><move_name><arguments..>CR*

##### Going through
This message is used to notify the player has chosen to go through without placing dice or using tool cards.
*"player_move"<playerID>"gothrough"CR*

##### Placing a die
This message is used to notify the player has chosen to place a die on his own dashboard.
*"player_move"<playerID>"setdie"<die><row><column>CR*

##### Using a tool card
This message is used to notify the player has chosen to use a tool card.
*"player_move"<playerID>"usetool"<tool><arguments..>CR*
