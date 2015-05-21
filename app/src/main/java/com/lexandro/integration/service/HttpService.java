package com.lexandro.integration.service;

import java.io.IOException;

public interface HttpService {

    String get(String url) throws IOException;
}
