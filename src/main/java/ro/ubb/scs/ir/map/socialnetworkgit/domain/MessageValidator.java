package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

public class MessageValidator implements Validator<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message> {
    public void validate(ro.ubb.scs.ir.map.socialnetworkgit.domain.Message entity) throws ValidationException {
        if(entity.getMessage() == "")
            throw new ValidationException("Message must not be null!");
        if(entity.getTo() == null)
            throw new ValidationException("Receiver must not be null!");
    }
}
