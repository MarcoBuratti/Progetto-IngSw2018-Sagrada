package it.polimi.ingsw.server.model;

public class FakeDashboard extends Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;

    /**
     * Creates a FakeDashboard Object using Dashboard's constructor.
     * @param schemeName the name of the scheme
     */
    FakeDashboard ( String schemeName ) {
        super( schemeName );
    }

    /**
     * Compares two schemes, checking if all the cells having the same position have the same restrictions.
     * @param dashboard the Dashboard the player wants to compare to the FakeDashboard
     * @return a boolean
     */
    boolean equalsScheme( Dashboard dashboard ) {
        for (int row = 0; row < ROW; row++)
            for (int col = 0; col < COLUMN; col++)
                if (!super.getMatrixScheme()[row][col].cellEquals(dashboard.getMatrixScheme()[row][col]))
                    return false;
        return true;
    }

}
