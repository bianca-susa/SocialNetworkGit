package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.util.List;

public class UtilizatorFileRepository extends ro.ubb.scs.ir.map.socialnetworkgit.repository.AbstractFileRepository<Long, Utilizator>
{
    public UtilizatorFileRepository(String filename, Validator<Utilizator> validator)
    {
        super(validator, filename);
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        Utilizator user = new Utilizator(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId() + ";" + entity.getFirst_name() + ";" + entity.getLast_name();
    }
}
