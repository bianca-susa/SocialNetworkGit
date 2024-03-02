package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

public class CerereValidator implements Validator<ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere> {
    public void validate(ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere entity) throws ValidationException {
        if(entity.getStatus().isEmpty())
            throw new ValidationException("Status must not be empty!");
    }
}
