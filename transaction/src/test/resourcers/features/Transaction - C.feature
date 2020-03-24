Feature: Transaction example C

  Scenario Outline: Transaction example C
    Given A new transaction that is stored in our system
    When I check the "<status>" from INTERNAL channel
    And the transaction date  is before today
    Then The systems returns the status 'SETTLED'
    And the "<amount>"
    And the "<fee>" value
    Examples:
      | reference  | status    | amount  |  fee  |
      | 12334 	   | INTERNAL  | 193.38  |  3.18 |