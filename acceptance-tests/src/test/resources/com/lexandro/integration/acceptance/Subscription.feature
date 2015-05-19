@imaginarium
Feature: AppDirect Subscription API endpoints tests


  Scenario: First
    Given we have a subscription create event
    When I call create subscription endpoint version 1
    Then returns ok status


