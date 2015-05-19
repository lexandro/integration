@imaginarium
Feature: AppDirect Subscription API endpoints tests


  Scenario: First
    Given the app started
    When I call create subscription endpoint
    Then returns status OK

