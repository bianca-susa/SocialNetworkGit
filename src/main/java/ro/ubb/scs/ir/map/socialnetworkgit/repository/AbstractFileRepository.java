package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.io.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E>
{
    String filename;

    public AbstractFileRepository(Validator<E> validator, String filename) {
        super(validator);
        this.filename = filename;
        load_from_file();
    }

    private void load_from_file(){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String newLine = reader.readLine();
            while (newLine != null) {
                //System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
                newLine = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    @Override
    //public E save(E entity)
    public Optional<E> save(E entity)
    {
        Optional<E> result = super.save(entity);
        if(result != null)
            writeToFile(entity);
        return result;
    }

    protected void writeToFile(E entity)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {

            writer.write(createEntityAsString(entity));
            writer.newLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
