package ro.ubb.scs.ir.map.socialnetworkgit.validator;

public interface Validator<E>
{
    void validate(E entity) throws ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;
}
