package com.lexandro.integration.service.xml;

import com.lexandro.integration.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JaxbXmlServiceTest {

    private JaxbXmlService jaxbXmlService;
    private String dummyOrder;
    private String dummyChange;
    private String dummyCancel;
    private String dummyNotice;
    private String dummyAssign;
    private String dummyUnassign;

    @Before
    public void setUp() throws Exception {
        jaxbXmlService = new JaxbXmlService();

        initXmlMessages();
    }
    /*
      Just basic testing of unmarshalling
    */

    @Test
    public void shouldUnmarshallSubcriptionOrder() throws Exception {
        // when
        AbstractEvent actualResult = jaxbXmlService.toObject(dummyOrder, SubscriptionCreateEvent.class);
        // then
        assertEquals(EventType.SUBSCRIPTION_ORDER, actualResult.getEventType());
    }

    @Test
    public void shouldUnmarshallSubcriptionChange() throws Exception {
        // when
        AbstractEvent actualResult = jaxbXmlService.toObject(dummyChange, SubscriptionChangeEvent.class);
        // then
        assertEquals(EventType.SUBSCRIPTION_CHANGE, actualResult.getEventType());
    }

    @Test
    public void shouldUnmarshallSubcriptionCancel() throws Exception {
        // when
        AbstractEvent actualResult = jaxbXmlService.toObject(dummyCancel, SubscriptionCancelEvent.class);
        // then
        assertEquals(EventType.SUBSCRIPTION_CANCEL, actualResult.getEventType());
    }


    @Test
    public void shouldUnmarshallUserAssign() throws Exception {
        // when
        AbstractEvent actualResult = jaxbXmlService.toObject(dummyNotice, AssignUserEvent.class);
        // then
        assertEquals(EventType.USER_ASSIGNMENT, actualResult.getEventType());
    }

    @Test
    public void shouldUnmarshallUserUnAssign() throws Exception {
        // when
        AbstractEvent actualResult = jaxbXmlService.toObject(dummyUnassign, UnAssignUserEvent.class);
        // then
        assertEquals(EventType.USER_UNASSIGNMENT, actualResult.getEventType());
    }

    private void initXmlMessages() {
        dummyOrder = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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

        dummyChange = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<event xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                "    <type>SUBSCRIPTION_CHANGE</type>\n" +
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
                "        <account>\n" +
                "            <accountIdentifier>dummy-account</accountIdentifier>\n" +
                "            <status>ACTIVE</status>\n" +
                "        </account>\n" +
                "        <configuration/>\n" +
                "        <order>\n" +
                "            <editionCode>PREMIUM</editionCode>\n" +
                "            <pricingDuration>MONTHLY</pricingDuration>\n" +
                "            <item>\n" +
                "                <quantity>20</quantity>\n" +
                "                <unit>USER</unit>\n" +
                "            </item>\n" +
                "            <item>\n" +
                "                <quantity>15</quantity>\n" +
                "                <unit>MEGABYTE</unit>\n" +
                "            </item>\n" +
                "        </order>\n" +
                "    </payload>\n" +
                "    <returnUrl>https://www.appdirect.com/finishprocure?token=dummyChange</returnUrl>\n" +
                "</event>";

        dummyCancel = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<event xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                "    <type>SUBSCRIPTION_CANCEL</type>\n" +
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
                "        <account>\n" +
                "            <accountIdentifier>dummy-account</accountIdentifier>\n" +
                "            <status>ACTIVE</status>\n" +
                "        </account>\n" +
                "        <configuration/>\n" +
                "    </payload>\n" +
                "    <returnUrl>https://www.appdirect.com/finishcancel?token=dummyCancel</returnUrl>\n" +
                "</event>";

        dummyNotice = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<event xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                "    <type>SUBSCRIPTION_NOTICE</type>\n" +
                "    <marketplace>\n" +
                "        <baseUrl>https://acme.appdirect.com</baseUrl>\n" +
                "        <partner>ACME</partner>\n" +
                "    </marketplace>\n" +
                "    <flag>STATELESS</flag>\n" +
                "    <payload>\n" +
                "        <account>\n" +
                "            <accountIdentifier>dummy-account</accountIdentifier>\n" +
                "            <status>SUSPENDED</status>\n" +
                "        </account>\n" +
                "        <configuration/>\n" +
                "        <notice>\n" +
                "            <type>DEACTIVATED</type>\n" +
                "        </notice>\n" +
                "    </payload>\n" +
                "</event>";

        dummyAssign = "<event>\n" +
                "    <type>USER_ASSIGNMENT</type>\n" +
                "    <marketplace>\n" +
                "        <partner>ACME</partner>\n" +
                "        <baseUrl>https://www.acme-marketplace.com</baseUrl>\n" +
                "    </marketplace>\n" +
                "    <creator>\n" +
                "        <email>andy.sen@appdirect.com</email>\n" +
                "        <firstName>Andy</firstName>\n" +
                "        <lastName>Sen</lastName>\n" +
                "        <openId>https://www.acme-marketplace.com/openid/id/078e23c3-060f-47b4-b1a3-65c3cb2a283d</openId>\n" +
                "        <language>en</language>\n" +
                "    </creator>\n" +
                "    <payload>\n" +
                "        <account>\n" +
                "            <accountIdentifier>MY_ACCOUNT_IDENTIFIER</accountIdentifier>\n" +
                "            <status>FREE_TRIAL</status>\n" +
                "        </account>\n" +
                "        <user>\n" +
                "            <email>christophe.levesque@appdirect.com</email>\n" +
                "            <firstName>Christophe</firstName>\n" +
                "            <lastName>Levesque</lastName>\n" +
                "            <openId>https://www.acme-marketplace.com/openid/id/4a76c6c4-96e1-42a0-93e0-36af5fa374e8</openId>\n" +
                "            <language>fr</language>\n" +
                "            <attributes>\n" +
                "                <entry>\n" +
                "                    <key>favoriteColor</key>\n" +
                "                    <value>green</value>\n" +
                "                </entry>\n" +
                "                <entry>\n" +
                "                    <key>hourlyRate</key>\n" +
                "                    <value>40</value>\n" +
                "                </entry>\n" +
                "            </attributes>\n" +
                "        </user>\n" +
                "    </payload>\n" +
                "</event>";

        dummyUnassign = "<event>\n" +
                "    <type>USER_UNASSIGNMENT</type>\n" +
                "    <marketplace>\n" +
                "        <partner>ACME</partner>\n" +
                "        <baseUrl>https://www.acme-marketplace.com</baseUrl>\n" +
                "    </marketplace>\n" +
                "    <creator>\n" +
                "        <email>christophe.levesque@origonetworks.com</email>\n" +
                "        <firstName>Christophe</firstName>\n" +
                "        <lastName>Levesque</lastName>\n" +
                "        <openId>https://www.acme-marketplace.com/openid/id/078e23c3-060f-47b4-b1a3-65c3cb2a283d</openId>\n" +
                "    </creator>\n" +
                "    <payload>\n" +
                "        <account>\n" +
                "            <accountIdentifier>MY_ACCOUNT_IDENTIFIER</accountIdentifier>\n" +
                "            <status>ACTIVE</status>\n" +
                "        </account>\n" +
                "        <user>\n" +
                "            <email>christophe.levesque@appdirect.com</email>\n" +
                "            <firstName>Christophe</firstName>\n" +
                "            <lastName>Levesque</lastName>\n" +
                "            <openId>https://www.acme-marketplace.com/openid/id/4a76c6c4-96e1-42a0-93e0-36af5fa374e8</openId>\n" +
                "        </user>\n" +
                "    </payload>\n" +
                "</event>";
    }
}