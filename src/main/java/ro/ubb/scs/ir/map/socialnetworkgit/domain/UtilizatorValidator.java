package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;

public class UtilizatorValidator implements Validator<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator>
{
    @Override
    public void validate(ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator entity) throws ValidationException
    {
        if(entity.getFirst_name().isEmpty()) throw new ValidationException("Invalid first name!");
        if(entity.getLast_name().isEmpty()) throw new ValidationException("Invalid last name!");
    }
}
