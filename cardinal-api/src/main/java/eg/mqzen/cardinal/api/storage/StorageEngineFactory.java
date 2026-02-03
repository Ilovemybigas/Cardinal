package eg.mqzen.cardinal.api.storage;


/**
 * Factory for creating storage engines based on configuration
 */
public interface StorageEngineFactory<D> {

    StorageEngine create(StorageConfig config) throws StorageException;

    StorageEngine createFromYaml(D yaml) throws StorageException;

}