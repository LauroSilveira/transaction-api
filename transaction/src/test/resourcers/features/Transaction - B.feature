Feature: Transaction B

  Scenario Outline: Transaction status
    Given A transaction that is stored in our system
    When I check the status from CLIENT or ATM "<channel>"
    And the transaction date is before today
    Then The system returns the status 'SETTLED'
    And the amount substracting the fee
    Examples:
      | reference  | channel |
      | 12334 	   | SETTLED  |