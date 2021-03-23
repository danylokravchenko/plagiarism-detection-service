package kravchenko.danylo.plagiarism.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kravchenko.danylo.plagiarism.dto.PlagiarismRequestItem;
import kravchenko.danylo.plagiarism.dto.PlagiarismResponseItem;
import kravchenko.danylo.plagiarism.dto.PlagiarismReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.corn.httpclient.HttpClient;
import net.sf.corn.httpclient.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlagiarismService {

    @Value("${remoteUrl}")
    private String remoteUrl;

    /*
     * make a remote request to plagiarism server
     */
    private List<PlagiarismResponseItem> makeRequest(final List<PlagiarismRequestItem> items) {
        try {
            HttpClient client = new HttpClient(new URI(remoteUrl));
            client.setContentType("application/json");
            client.setAcceptedType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(items);
            HttpResponse response = client.sendData(HttpClient.HTTP_METHOD.POST, jsonString);
            if (response.hasError()) {
                log.error("Error while making request: " + response.getMessage());
            }
            String result = response.getData();
            return objectMapper.readValue(result, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Error while communicating with plagiarism server: " + e.toString());
        }
        return new ArrayList<>();
    }

    /*
     * Split text into chunks
     */
    private List<String> splitText(final String text) {
        if (text.length() <= 550) {
            return new ArrayList<>(){{add(text);}};
        }
        List<String> result = new ArrayList<>();
        int startIndex = 0;
        int endIndex = 551;
        while(endIndex <= text.length()) {
            int searchStartIndex = endIndex-80;
            // substring where to search for the end of the input sentence
            String dotToFind = text.substring(searchStartIndex, endIndex);
            // index of the dot
            int sentenceEnd = dotToFind.indexOf(".");
            if (sentenceEnd == -1) {
                if (endIndex+100 < text.length()) {
                    endIndex += 100;
                } else {
                    endIndex += text.length()-endIndex;
                }
                continue;
            }
            // append substring of [startIndex; found index of the end of the input sentence] chunk
            result.add(text.substring(startIndex, searchStartIndex + sentenceEnd));

            startIndex = searchStartIndex + sentenceEnd + 1;
            endIndex = startIndex+500;
        }

        return result;
    }

    /*
     * create all possible pairs `text_a:text_b`
     */
    private List<PlagiarismRequestItem> createTextPairs(final List<String> textsA, final List<String> textsB) {
        List<PlagiarismRequestItem> result = new ArrayList<>();

        for (String textA : textsA) {
            for (String textB : textsB) {
                result.add(PlagiarismRequestItem.builder()
                        .textA(textA)
                        .textB(textB)
                        .build());
            }
        }
        return result;
    }

    /*
     * Highlight found plagiarism
     */
    private PlagiarismReview highlightPlagiarism(final List<PlagiarismResponseItem> items, final String textA, final String textB) {
        PlagiarismReview result = new PlagiarismReview();

        double plagiarismLevel = items.stream()
                .filter(PlagiarismResponseItem::isPlagiarism)
                .map(item -> {
                    int startIndexTextA = textA.indexOf(item.getTextA());
                    int endIndexTextA = startIndexTextA + item.getTextA().length();
                    int startIndexTextB = textB.indexOf(item.getTextB());
                    int endIndexTextB = startIndexTextB + item.getTextB().length();

                    result.addHighlight(startIndexTextA, endIndexTextA, startIndexTextB, endIndexTextB, item.getAccuracy());

                    return item.getAccuracy();
                }).mapToDouble(i -> i).average()
                .orElse(0);

        result.setPlagiarismLevel(plagiarismLevel);
        return result;
    }

    /*
     * Analyze 2 texts on plagiarism
     */
    public PlagiarismReview analyzePlagiarism(final PlagiarismRequestItem item) {
        // split big texts to small ones and pass them to plagiarism server
        if (item.getTextA().length() > 500 || item.getTextB().length() > 500) {
            List<String> textA = splitText(item.getTextA());
            List<String> textB = splitText(item.getTextB());
            List<PlagiarismRequestItem> items = createTextPairs(textA, textB);
            List<PlagiarismResponseItem> plagiarismResult = makeRequest(items);
            return highlightPlagiarism(plagiarismResult, item.getTextA(), item.getTextB());
        } else {
            // both texts are small
            List<PlagiarismRequestItem> items = new ArrayList<>(){};
            items.add(PlagiarismRequestItem.builder()
                    .textA(item.getTextA())
                    .textB(item.getTextB())
                    .build());
            List<PlagiarismResponseItem> plagiarismResult = makeRequest(items);
            return highlightPlagiarism(plagiarismResult, item.getTextA(), item.getTextB());
        }
    }
}
