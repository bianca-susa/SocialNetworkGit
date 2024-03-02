package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import ro.ubb.scs.ir.map.socialnetworkgit.validator.*;
public class PrietenieValidator implements Validator<ro.ubb.scs.ir.map.socialnetworkgit.domain.Prietenie>
{
    @Override
    public void validate(ro.ubb.scs.ir.map.socialnetworkgit.domain.Prietenie entity) throws ValidationException {
        if(entity.getId().getRight().getId() == entity.getId().getLeft().getId())
            throw new ValidationException("Friends must be different!");
        if(entity.getId().getLeft().getId() == null || entity.getId().getRight().getId()==null)
            throw new ValidationException("Can't befriend null!");
    }
}
