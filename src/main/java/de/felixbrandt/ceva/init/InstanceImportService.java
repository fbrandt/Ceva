package de.felixbrandt.ceva.init;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import de.felixbrandt.ceva.config.InstanceFile;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Instance;

/**
 * Import problem instances into database.
 */
public class InstanceImportService
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final SessionHandler session_handler;
  private final boolean id_by_name;

  public static void run (final SessionHandler session_handler,
          final List<InstanceFile> instance_files, final boolean id_by_name)
  {
    final InstanceImportService service = new InstanceImportService(
            session_handler, id_by_name);
    service.importInstanceFiles(instance_files);
  }

  public InstanceImportService(final SessionHandler handler,
          final boolean id_by_name)
  {
    session_handler = handler;
    this.id_by_name = id_by_name;
  }

  public final void importInstanceFiles (
          final List<InstanceFile> instance_files)
  {
    for (final InstanceFile instance_file : instance_files) {
      LOGGER.debug("check file " + instance_file.getFilename() + " for import");
      importInstanceFile(instance_file);
    }
  }

  public final Instance importInstanceFile (final InstanceFile instance_file)
  {
    if (instance_file.isReadable()) {
      if (!instanceExists(instance_file)) {
        final Instance instance = createInstance(instance_file);
        session_handler.getSession().save(instance);
        session_handler.saveAndBegin();

        LOGGER.info("instance " + instance.getName() + " imported");

        return instance;
      } else {
        LOGGER.debug(
                "instance " + instance_file.getFilename() + " already exists");
      }
    } else {
      LOGGER.warn("instance " + instance_file.getFilename() + " not readable");
    }

    return null;
  }

  public final Instance createInstance (final InstanceFile instance_file)
  {
    Instance instance = loadInstance(instance_file.getFilename());

    instance.setName(instance_file.getFilename());
    instance.setChecksum(instance_file.getHash());
    instance.setContent(instance_file.getContent());

    return instance;

  }

  public final Instance loadInstance (final String name)
  {
    if (id_by_name) {
      final Query query = session_handler.getSession()
              .createQuery("from Instance where name = :name");
      query.setParameter("name", name);

      if (query.list().size() > 0) {
        LOGGER.info("Updating instance {}", name);
        return (Instance) query.list().iterator().next();
      }
    }

    return new Instance();
  }

  public final boolean instanceExists (final InstanceFile instance_file)
  {
    final Query query = session_handler.getSession()
            .createQuery("from Instance where checksum = :checksum");
    query.setParameter("checksum", instance_file.getHash());

    final boolean exists = query.list().size() > 0;

    return exists;
  }
}
