Feature: Registeration Page Scenarios

Background: Prerequisite criteria for Registeration Page
Given User enters the Life Customer portal url from Registeration page

@regression @login
Scenario: Verify the logout functionality
When user Clicks on logout button
Then user should be loggedout of the application succesfully