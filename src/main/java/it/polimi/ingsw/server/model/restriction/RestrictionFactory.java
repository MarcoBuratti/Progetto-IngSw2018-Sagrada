package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.Arrays;
import java.util.List;

public class RestrictionFactory implements AbstractRestriction {

    private List<RestrictionEnum> restriction = Arrays.asList(RestrictionEnum.values());
    private List<Color> colors = Arrays.asList(Color.values());

    public Restriction getRestriction(RestrictionEnum restrictionCell) throws NotValidValueException {

        if (restriction.indexOf(restrictionCell) == 0)
            return new NoRestriction();
        else if (restriction.indexOf(restrictionCell) < 7)
            return new ValueRestriction(restriction.indexOf(restrictionCell));
        else
            return new ColorRestriction(colors.get(restriction.indexOf(restrictionCell) - 7));

    }
}
