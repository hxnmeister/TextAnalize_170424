package com.spring_project.TextAnalize_170424.controllers;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.core.credential.AzureKeyCredential;
import com.spring_project.TextAnalize_170424.models.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1")
@RestController
public class TextAnalysisController {
    @Value("${azure.cognitive.services.key}")
    private String azureApiKey;

    @Value("${azure.cognitive.services.endpoint}")
    private String azureEndpoint;

    @PostMapping(value = "/analyze-comment")
    public ResponseEntity<?> analyzeComment(@RequestBody Comment comment) {
        try {
            TextAnalyticsClient textAnalyticsClient = new TextAnalyticsClientBuilder()
                    .credential(new AzureKeyCredential(azureApiKey))
                    .endpoint(azureEndpoint)
                    .buildClient();

            DocumentSentiment documentSentiment = textAnalyticsClient.analyzeSentiment(comment.getText());

            return new ResponseEntity<>(documentSentiment.getSentences(), HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
