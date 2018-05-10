package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.exception.NotValidNumberException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictionFactoryTest {

    @Test
    void getRestriction() throws NotValidNumberException {

        AbstractRestriction restrictionFactory = new RestrictionFactory();

        Restriction restriction1 = new RestrictionFactory().getRestriction(RestrictionEnum.RED_R);
        System.out.println(restriction1.toString());

        Restriction restriction2 = new RestrictionFactory().getRestriction(RestrictionEnum.FOUR_R);
        System.out.println(restriction2.toString());

        Restriction restriction3 = new RestrictionFactory().getRestriction(RestrictionEnum.NO_R);
        System.out.println(restriction3.toString());

    }
}