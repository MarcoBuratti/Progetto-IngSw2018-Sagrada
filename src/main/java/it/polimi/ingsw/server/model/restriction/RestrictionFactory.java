package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.Arrays;
import java.util.List;

public class RestrictionFactory extends AbstractRestriction {

    private List<RestrictionEnum> restriction = Arrays.asList(RestrictionEnum.values());
    private List<Color> colors = Arrays.asList(Color.values());

    public Restriction getRestriction(RestrictionEnum restrictionCell) throws NotValidValueException {

        if (0 < restriction.indexOf(restrictionCell) && restriction.indexOf(restrictionCell) < 7)
            return new ValueRestriction(restriction.indexOf(restrictionCell));
        else if (restrictionCell == RestrictionEnum.NO_R)
            return new NoRestriction();
        else
            return new ColorRestriction(colors.get(restriction.indexOf(restrictionCell) - 7));

    }
}
