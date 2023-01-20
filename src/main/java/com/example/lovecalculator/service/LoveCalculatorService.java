package com.example.lovecalculator.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.lovecalculator.model.Result;

@Qualifier("loveCalculatorService")
@Service
public class LoveCalculatorService {
    private static final String LOVE_CAL_URL = "https://love-calculator.p.rapidapi.com/getPercentage";

    private static final String RESULT_ENTITY = "resultlist";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public Optional<Result> getResult(String fname, String sname) throws Exception {
        String apiKey = System.getenv("RAPIDAPI_KEY");

        String loveUri = UriComponentsBuilder
                .fromUriString(LOVE_CAL_URL)
                .queryParam("fname", fname)
                .queryParam("sname", sname)
                .toUriString();


        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(loveUri))
            .header("X-RapidAPI-Key", apiKey)
            .header("X-RapidAPI-Host", "love-calculator.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Result r = Result.create(response.body());

        if (r!=null) {
            save(r);
            return Optional.of(r);
        }
        return Optional.empty();
    }

    public void save(final Result r) {
        redisTemplate.opsForList()
            .leftPush(RESULT_ENTITY, r.getId());
        redisTemplate.opsForHash()
            .put(RESULT_ENTITY+"_Map", r.getId(), r);
    }


    public List<Result> findAll() {
        List<Object> fromResultList = redisTemplate.opsForList()
                    .range(RESULT_ENTITY, 0, -1);
        List<Result> listOfResults = redisTemplate.opsForHash()
                .multiGet(RESULT_ENTITY+"_Map", fromResultList)
                .stream()
                .filter(Result.class::isInstance)
                .map(Result.class::cast)
                .toList();

        return listOfResults;
    }
}
