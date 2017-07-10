import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
    Set<Transaction> possibleTransactionSet;

    public CompliantNode(double p_graph, double p_malicious,
                         double p_txDistribution, int numRounds) {
        possibleTransactionSet = new HashSet<Transaction>();
    }

    public void setFollowees(boolean[] followees) {
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        for (Transaction t: pendingTransactions) {
            possibleTransactionSet.add(t);
        }
    }

    public Set<Transaction> sendToFollowers() {
        return possibleTransactionSet;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        for (Candidate candidate: candidates) {
            possibleTransactionSet.add(candidate.tx);
        }
    }
}
