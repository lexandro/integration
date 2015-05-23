package com.lexandro.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SpringHttpService implements HttpService {

    public static final String DEFAULT_CHARSET = "UTF-8";

    @Override
    public String get(String url) throws IOException {
        HttpClient client = null;
        HttpGet request;
        HttpResponse response = null;
        try {
            client = HttpClientBuilder
                    .create()
                    .build();
            //
            request = new HttpGet(url);
            response = client.execute(request);
            //
            String eventXml = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            log.info("Returned eventXml is a {}", eventXml);
            return eventXml;
        } finally {
            // This trick was found in http://bit.ly/1IS297B
            if (response != null) {
                HttpClientUtils.closeQuietly(response);
            }
            //
            if (client != null) {
                HttpClientUtils.closeQuietly(client);
            }
        }

    }
}
