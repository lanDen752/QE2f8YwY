// 代码生成时间: 2025-09-17 13:03:09
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class BatchFileRenamer {

    private SessionFactory sessionFactory;

    public BatchFileRenamer() {
        // Initialize Hibernate SessionFactory
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * Renames files in a directory based on a naming pattern.
     * @param directoryPath Path to the directory containing files to be renamed.
     * @param namingPattern Regex pattern to match file names.
     * @param newNameFormat Format for the new names.
     * @throws IOException If an I/O error occurs.
     */
    public void renameFiles(String directoryPath, String namingPattern, String newNameFormat) throws IOException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Retrieve file list from database
            List<FileRecord> files = session.createQuery("FROM FileRecord", FileRecord.class).list();

            Path directory = Paths.get(directoryPath);
            Pattern pattern = Pattern.compile(namingPattern);

            for (FileRecord fileRecord : files) {
                File file = new File(directory + File.separator + fileRecord.getFileName());
                if (file.exists()) {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.find()) {
                        String newName = String.format(newNameFormat, matcher.group(1));
                        File newFile = new File(directory + File.separator + newName);
                        if (!file.renameTo(newFile)) {
                            throw new IOException("Failed to rename file: " + file.getName());
                        }
                        // Update file name in database
                        fileRecord.setFileName(newName);
                        session.update(fileRecord);
                    }
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Represents a file record in the database.
     */
    public static class FileRecord {

        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public static void main(String[] args) {
        try {
            BatchFileRenamer batchFileRenamer = new BatchFileRenamer();
            // Example usage: rename files in 'files' directory with naming pattern '(\d+)\..*'
            // to new name format 'file_$1.ext'
            batchFileRenamer.renameFiles("files", "(\d+)\..*", "file_$1.ext");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
