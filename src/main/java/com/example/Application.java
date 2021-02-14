package com.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.Commuter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static java.lang.System.*;

/*
You can simply run this app, code to change is in Train
 */
public class Application {

    private static final Path FILE_NAME = Path.of("../token.txt");

    public static void main(String[] args) throws IOException {
        final HashMap<Object, Object> valueMap = new HashMap<>();

        Train train = new Train();
        out.println("A train has arrived");
        for (int i = 0; i < 3; i++) {
            out.println("- A passenger steps into the train");
            train.addCommuter(new Commuter());
        }
        out.println("The train departs with " + train.getPassengers() + " passengers");
        if(3 != train.getPassengers()) {
            out.println("... That doesn't seem right!");
        }
        valueMap.put("trainPassengers", train.getPassengers());

        sendMessageToService(valueMap);
    }

    private static void sendMessageToService(HashMap<Object, Object> valueMap) throws IOException {
        out.println();
        String token = Files.readString(FILE_NAME, StandardCharsets.UTF_8);
        if(token == null || token.length() != 66) {
            err.println("Token was not found. Make sure you have a token.txt in the root of your exercises folder, with subfolders for each exercise.");
            exit(-1);
        }

        final String exerciseName = Path.of("").toAbsolutePath().getFileName().toString();
        if(exerciseName == null || !exerciseName.contains("_")) {
            err.println("Could not verify exercise name. Make sure the folder name matches the name used by the github repository.");
            exit(-1);
        }

        String apiUrl = "https://deep-dive-java-course.herokuapp.com/api/exercise/" + exerciseName;
        apiUrl = apiUrl.replace("https://deep-dive-java-course.herokuapp.com", "http://localhost:8080");

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);


        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(valueMap), headers);
        ResponseEntity<String> result = restTemplate.postForEntity(apiUrl, entity, String.class);
        if(result.getStatusCode().equals(HttpStatus.OK)){
            out.println("Submission completed and accepted");
        } else {
            err.println("Submitted but refused: " + result.getStatusCode());
            exit(-1);
        }
    }
}
