package com.lexandro.integration.service;

import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.SubscriptionCreateEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JaxbXmlServiceTest {

    private JaxbXmlService jaxbXmlService;
    private String subscrioptionOrderXml;

    @Before
    public void setUp() throws Exception {
        jaxbXmlService = new JaxbXmlService();
    }

    @Test
    public void shouldUnmarshallSubscriptionOrder() throws Exception {
        // given
        givenWeHaveASubscriptionOrderXmlEvent();
        // when
        SubscriptionCreateEvent result = jaxbXmlService.toObject(subscrioptionOrderXml, SubscriptionCreateEvent.class);
        // then
        // not checking all fields, just demonstrating...
        assertEquals(EventType.SUBSCRIPTION_ORDER, result.getEventType());
        assertEquals("https://www.appdirect.com/finishprocure?token=dummyOrder", result.getReturnUrl());
        assertEquals("test-email+creator@appdirect.com", result.getCreator().getEmail());
    }

    private void givenWeHaveASubscriptionOrderXmlEvent() {
        subscrioptionOrderXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<event xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                "    <type>SUBSCRIPTION_ORDER</type>\n" +
                "    <marketplace>\n" +
                "        <baseUrl>https://acme.appdirect.com</baseUrl>\n" +
                "        <partner>ACME</partner>\n" +
                "    </marketplace>\n" +
                "    <flag>STATELESS</flag>\n" +
                "    <creator>\n" +
                "        <email>test-email+creator@appdirect.com</email>\n" +
                "        <firstName>DummyCreatorFirst</firstName>\n" +
                "        <language>fr</language>\n" +
                "        <lastName>DummyCreatorLast</lastName>\n" +
                "        <openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId>\n" +
                "        <uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid>\n" +
                "    </creator>\n" +
                "    <payload>\n" +
                "        <company>\n" +
                "            <country>CA</country>\n" +
                "            <email>company-email@example.com</email>\n" +
                "            <name>Example Company Name</name>\n" +
                "            <phoneNumber>415-555-1212</phoneNumber>\n" +
                "            <uuid>d15bb36e-5fb5-11e0-8c3c-00262d2cda03</uuid>\n" +
                "            <website>http://www.example.com</website>\n" +
                "        </company>\n" +
                "        <configuration>\n" +
                "            <entry>\n" +
                "                <key>domain</key>\n" +
                "                <value>mydomain</value>\n" +
                "            </entry>\n" +
                "        </configuration>\n" +
                "        <order>\n" +
                "            <editionCode>BASIC</editionCode>\n" +
                "            <pricingDuration>MONTHLY</pricingDuration>\n" +
                "            <item>\n" +
                "                <quantity>10</quantity>\n" +
                "                <unit>USER</unit>\n" +
                "            </item>\n" +
                "            <item>\n" +
                "                <quantity>15</quantity>\n" +
                "                <unit>MEGABYTE</unit>\n" +
                "            </item>\n" +
                "        </order>\n" +
                "    </payload>\n" +
                "    <returnUrl>https://www.appdirect.com/finishprocure?token=dummyOrder</returnUrl>\n" +
                "</event>";
    }


}