Classes already tested and passed:

**Dashboard empty**
**Dashboard partially full**
**Dashboard totally full**

- __First__ method "getMatrixScheme" tests that the matrix's copy is correct
- __Second__ method "setDieOnCell" tests that we can place the die on a cell
- this method does not care about the restriction of the cell but does 
- only check row and column, if the parameters are not right it throws 
- an exception


**PlacementCheck empty**
**PlacementCheck partially full**
**PlacementCheck totally full**

- __First__ method "isEmpty" tests if the matrix scheme is empty
- __Second__ method "checkDiceColor" tests the colour of two dice, it returns
- true if the dice have the same colour
- __Third__ method "checkDiceNumber" tests the number of two dice, it returns
- true if the dice have the same value
- __Fourth__ method "allowedNeighbours" tests if we can place a die on a cell
- it tests if our neighbours have the same colour or the same number, it
- returns true if we can place the die
- __Fifth__ method "nearBy" tests if the cell has any neighbours ( left, 
- right, up, down and diagonals)
- __Sixth__ method "genericCheck" tests all the previous method


**Cell empty**
**Cell full**

- __First__ method "cellTest" tests if a cell is empty, if I placed a die and
- if I try to place a die on a occupied cell, in this case it throws an exception


**DiceBag empty**
**DiceBag full**

- __First__ method "enoughDiceForEachColour" tests if in one bag the are 18
- dice for each colour
- __Second__ method "goodExtraction" tests the extraction of some dice from the bag


**Die empty**
**Die full**

- __First__ method "numberFromOneToSix" try to set the number of some dice,
- if I can't set the number it throws exception
- __Second__ method "colourTest" create some dice with color attributes and
- check if colour is the same at run time execution


**Player empty**
**Player full**

-__First__ method "playerTest" tests every method in Player.java, tests number of
- matrixScheme token, tests getNickname method and tests useToken, if you want to use
- token and you haven't enough it throws an exception


**RoundTrack empty**
**RoundTrack full**

-__First__ method "roundTrackTest" tests every method in RoundTrack.java, it creates a
- RoundTrack object which represents the round track, tests the builder and the getter

**Restriction Tests**
- __ColorTest__: Checks if the color restrictions are set correctly and if the
restriction check method works correctly (returning false if the die has a color
 different from the one of the restrictions, else returning true)
- __ValueTest__: Checks if the value restrictions are set correctly and if the
restriction check method works correctly (returning false if the die
has a value different from the one of the restrictions, else returning true)
- __GenericTest__: Checks everything already tested in the first two tests and also 
checks that the restrictionCheck method of NoRestriction returns true

**GameBoard Test**
- __GameBoardTest__: Checks if the constructor works correctly, making sure that
dice bag, players, public achievements, tools and draft pool are set correctly.
The removeDieFromDraftPool method is tested too.







