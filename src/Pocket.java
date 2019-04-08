import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;


public class Pocket {
    /**
     * The RandomAccessFile of the pocket file
     */
    private RandomAccessFile file;

    /**
     * Creates a Pocket object
     * 
     * A Pocket object interfaces with the pocket RandomAccessFile.
     */
    public Pocket () throws Exception {
        this.file = new RandomAccessFile(new File("pocket.txt"), "rw");
    }

    /**
     * Adds a product to the pocket.
     *
     * @param  product           product name to add to the pocket (e.g. "car")
     */
    public void safeAddProduct(String product) throws Exception {
        FileLock lock = null;
        try {
            lock = this.file.getChannel().lock();

            this.file.seek(this.file.length());
            this.file.writeBytes(product+'\n');

        } catch (OverlappingFileLockException e) {
            // probably only possible using multi-threading
            throw new IllegalStateException("Wallet already in use.");
        } finally {
            if (lock!=null){
                lock.release();
            }
        }
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
        this.file.close();
    }
}
