package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

class RestrictionFactoryTest {

    @Test
    void getRestriction() throws NotValidValueException {

        AbstractRestriction restrictionFactory = new RestrictionFactory();

        Restriction restriction1 = restrictionFactory.getRestriction(RestrictionEnum.RED_R);
        System.out.println(restriction1.toString());

        Restriction restriction2 = restrictionFactory.getRestriction(RestrictionEnum.FOUR_R);
        System.out.println(restriction2.toString());

        Restriction restriction3 = restrictionFactory.getRestriction(RestrictionEnum.NO_R);
        System.out.println(restriction3.toString());

    }
}