Classes already tested and passed:

**Dashboard empty**
**Dashboard partially full**
**Dashboard totally full**

- __First__ method "getMatrixScheme" test that the matrix's copy is correct
- __Second__ method "setDieOnCell" test that we can place the die on a cell
- this method does not care about the restriction of the cell but does 
- only check row and column, if the parameters are not right it throws 
- an exception


**PlacementCheck empty**
**PlacementCheck partially full**
**PlacementCheck totally full**

- __First__ method "isEmpty" test if the matrix scheme is empty
- __Second__ method "checkDiceColor" test the colour of two dice, it turns
- true if the dice has the same colour
- __Third__ method "checkDiceNumber" test the number of two dice, it turns
- true if the dice has the same number
- __Fourth__ method "allowedNeighbours" test if we can place a die on a cell
- it test if our neighbour has the same colour or the same number, it
- return true if we can place
- __Fifth__ method "nearBy" test if the cell has any neighbours ( left, 
- right, up, down and diagonals)
- __Sixth__ method "genericCheck" test all the previous method


**Cell empty**
**Cell full**

- __First__ method "cellTest" test if a cell is empty, if I placed a die and
- if I try to place a die on a occupied cell, in this case it throws an exception


**DiceBag empty**
**DiceBag full**

- __First__ method "enoughDiceForEachColour" test if in one bag the are 18
- dice for each colour
- __Second__ method "goodExtraction" test the extraction of some dice from the bag


**Die empty**
**Die full**

- __First__ method "numberFromOneToSix" try to set the number of some dice,
- if I can't set the number it throws exception
- __Second__ method "colourTest" create some dice with color attributes and
- check if colour is the same at run time execution


**Player empty**
**Player full**

-__First__ method "playerTest" test every method in Player.java, test number of
- matrixScheme token, test getNickname method and test useToken, if you want to use
- token and you haven't enough it throws an exception


**RoundTrack empty**
**RoundTrack full**

-__First__ method "roundTrackTest" test every method in RoundTrack.java, it create a
- RoundTrack object which represents the round track, test the builder and the getter







