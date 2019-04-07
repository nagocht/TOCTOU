import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */  
    private RandomAccessFile file;

    /**
     * Creates a Wallet object
     *
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet () throws Exception {
	    this.file = new RandomAccessFile(new File("wallet.txt"), "rw");
    }

    /**
     * Gets the wallet balance. 
     *
     * @return                   The content of the wallet file as an integer
     */
    public int getBalance() throws IOException {
        this.file.seek(0);
        return Integer.parseInt(this.file.readLine());
    }

    /**
     * Sets a new balance in the wallet
     *
     * @param  newBalance          new balance to write in the wallet
     */
    public void setBalance(int newBalance) throws Exception {
	this.file.setLength(0);
	String str = Integer.valueOf(newBalance).toString()+'\n'; 
	this.file.writeBytes(str); 
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
	this.file.close();
    }

    public void safeWithdraw(int valueToWithdraw) throws Exception {
        FileLock lock = null;
        try {
            lock = this.file.getChannel().tryLock();
            if (lock!=null){
                int currentBalance = getBalance();
                int newBalance = currentBalance - valueToWithdraw;
                if (newBalance < 0){
                    throw new IllegalStateException("Not enough credit.");
                }
                setBalance(newBalance);
            } else {
                throw new IllegalStateException("Wallet already in use.");
            }

        } catch (OverlappingFileLockException e){
            // probably only possible using multi-threading
            throw new IllegalStateException("Wallet already in use.");
        } finally {
            if (lock!=null){
                lock.release();
            }
        }
    }
}
