Feature: Transaction A

  Scenario Outline: Invalid Transaction
    Given A transaction that is not stored in our system
    When I check the status from any "<channel>"
    Then The system returns the status INVALID
    Examples:
      | reference  | channel |
      | 12334 	   | CLIENT  |