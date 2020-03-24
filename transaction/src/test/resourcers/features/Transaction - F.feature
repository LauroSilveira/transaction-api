Feature: Transaction F

  Scenario Outline: Transaction f example
  Given A transaction that is stored in  our system
  When I check the "<status>" from CLIENT channel
    And the transaction date is greater than today
  Then  The system returns the "<status>" 'FUTURE'
    And the "<amount>"  substracting the fee
    Examples:
      | reference  | status    | amount  |
      | 12334 	   | FUTURE    | 190.20  |