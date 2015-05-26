package com.lexandro.integration.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({HttpClientBuilder.class, HttpGet.class, EntityUtils.class, SpringHttpService.class})
public class SpringHttpServiceTest {
    public static final String EVENT_XML = "eventxml";
    public static final String TEST_URL = "http://bbc.co.uk";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    //
    private SpringHttpService springHttpService;
    //
    @Mock
    private HttpClientBuilder httpClientBuilder;
    @Mock
    private CloseableHttpClient httpClient;
    @Mock
    private HttpGet httpGet;
    @Mock
    private CloseableHttpResponse httpResponse;
    @Mock
    private HttpEntity httpEntity;

    @Before
    public void setUp() throws Exception {
        springHttpService = new SpringHttpService();
        initMocks(this);
        //

        PowerMockito.mockStatic(HttpClientBuilder.class);

        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.build()).thenReturn(httpClient);
        PowerMockito.whenNew(HttpGet.class).withArguments(TEST_URL).thenReturn(httpGet);
        //
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpClient.execute(httpGet)).thenReturn(httpResponse);

        //
        PowerMockito.mockStatic(EntityUtils.class);
        PowerMockito.when(EntityUtils.toString(httpEntity, "UTF-8")).thenReturn(EVENT_XML);

    }

    @Test
    public void shouldReturnResultFromHttpGet() throws Exception {
        // when
        String actualResult = springHttpService.get(TEST_URL);
        // then
        assertEquals(EVENT_XML, actualResult);
    }

    @Test
    public void shouldCloseResources() throws Exception {
        // when
        springHttpService.get(TEST_URL);
        // then
        verify(httpClient).close();
    }

    @Test
    public void shouldNotAcceptEmptyUrl() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        springHttpService.get("");
        // then
    }

    @Test
    public void shouldNotAcceptNullUrl() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        springHttpService.get(null);
        // then
    }
}