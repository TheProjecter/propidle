package acceptance.steps;

import org.apache.lucene.index.IndexWriter;

import java.util.concurrent.Callable;

import com.googlecode.propidle.persistence.Transaction;

public class CloseTransaction implements Callable {
    private final IndexWriter indexWriter;
    private final Transaction transaction;
    private final Callable step;

    public CloseTransaction(IndexWriter indexWriter, Transaction transaction, Callable step) {
        this.indexWriter = indexWriter;
        this.transaction = transaction;
        this.step = step;
    }

    public Object call() throws Exception {
        Object result;
        try {
            result = step.call();
            indexWriter.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Did not commit transaction", e);
        }
        transaction.commit();
        return result;

    }
}
