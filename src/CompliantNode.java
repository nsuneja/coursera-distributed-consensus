import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.spi.TransactionalWriter;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
    final double minFolloweePct = 0.0;
    final double maxFolloweePct = 55.0;
    Set<Transaction> possibleTransactionSet;
    int numOfFollowees;
    int curRoundNumber;
    int maxRounds;

    public CompliantNode(double p_graph, double p_malicious,
                         double p_txDistribution, int numRounds) {
        possibleTransactionSet = new HashSet<Transaction>();
        numOfFollowees = 0;
        maxRounds = numRounds;
        curRoundNumber = 0;
    }

    public void setFollowees(boolean[] followees) {
        for (int i = 0; i < followees.length; i++) {
            if (followees[i]) {
                numOfFollowees++;
            }
        }
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        for (Transaction t: pendingTransactions) {
            possibleTransactionSet.add(t);
        }
    }

    public Set<Transaction> sendToFollowers() {
        curRoundNumber++;
        return possibleTransactionSet;
    }

    private boolean isTransactionGood(Transaction t,
                              Map<Transaction, Integer> voteCountMap) {
        double threshold = minFolloweePct +
             ((double)(maxFolloweePct - minFolloweePct)/(maxRounds - 1)) * (curRoundNumber - 1);
        //System.out.println(curRoundNumber + "," + threshold);
        return (voteCountMap.get(t)/(double)numOfFollowees) * 100.0 >= threshold;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        Map<Transaction, Integer> voteCountMap = new HashMap<Transaction, Integer>();
        Set<Transaction> updatedSet = new HashSet<Transaction>();

        for (Candidate candidate: candidates) {
            if (voteCountMap.containsKey(candidate.tx)) {
                Integer voteCount = voteCountMap.get(candidate.tx);
                voteCount++;
                voteCountMap.put(candidate.tx, voteCount);
            } else {
                voteCountMap.put(candidate.tx, 1);
            }
        }

        for (Transaction t: possibleTransactionSet) {
            // Check if this transaction was agreed upon by "min/max FolloweePct"
            // percentage of nodes.
            if (voteCountMap.containsKey(t) && isTransactionGood(t, voteCountMap)) {
                // Keep the transaction.
                updatedSet.add(t);
            }
        }

        for (Transaction t: voteCountMap.keySet()) {
            if (isTransactionGood(t, voteCountMap)) {
                updatedSet.add(t);
            }
        }

        possibleTransactionSet = updatedSet;
    }
}
