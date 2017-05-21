Feature: On My Movies page show saved movies
    As user of Movies App I want see my movies as carousel, as list and as grid.

    @ScenarioId("F01")
    Scenario: Show my movies saved as carousel, as list and as grid
        Given I see My Movies page - F01
        When  I select navigation bottom to show movie as carousel
        Then  I want to see my movies saved as carousel
        When  I select navigation bottom to show movie as list
        Then  I want to see my movies saved as list
        When  I select navigation bottom to show movie as grid
        Then  I want to see my movies saved as grid
        When  I swipe to right my movies grid
        Then  I want to see my movies as list again
        When  I swipe to right my movies list
        Then  I want to see my movies as carousel again