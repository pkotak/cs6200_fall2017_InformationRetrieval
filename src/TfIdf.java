import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Paarth Kotak
 */
public class TfIdf {
    private static HashMap<String, Integer> documentWordTotal;
    private static HashMap<String, List<Posting>> invertedIndex;
    private static Query queryObj;
    private static List<Result> results;

    /**
     * @param query1 Query object
     * @param invertedIndex1 inverted index
     * @param documentWordTotal1 document map and its related frequencies
     * @return Results object -> Document_id, tfidf score, query_id
     */
    public static List<Result> getResult(Query query1, HashMap<String, List<Posting>> invertedIndex1,
                                         HashMap<String, Integer> documentWordTotal1){
        documentWordTotal = documentWordTotal1;
        invertedIndex = invertedIndex1;
        queryObj = query1;
        results = new ArrayList<>();

        for(String term : queryObj.query().split(" ")){
            List<Posting> postings = invertedIndex.get(term);
            for(Posting p: postings) {
                double tf = calculateTf(p.docID(), p.termFrequency());
                double idf = calculateIdf(postings.size());
                double score = tf*idf;
                results.add(new Result1(p.docID(),score,query1.queryID()));
            }
        }

        return results;
    }

    /**
     * @param docID  Document ID
     * @param termFrequency Number of times term occurs in a document
     * @return term frequency of term in document
     */
    private static double calculateTf(String docID, int termFrequency){
        int totalDocWords = documentWordTotal.get(docID);
        return (double) termFrequency/totalDocWords;
    }

    /**
     * @param docsWithTerm  Number of documents that contain term
     * @return idf
     */
    private static double calculateIdf(int docsWithTerm){
        double totalDocuments = documentWordTotal.size();
        return Math.log(totalDocuments/(double) docsWithTerm);
    }

}