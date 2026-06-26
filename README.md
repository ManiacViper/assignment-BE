# ICE Take-home assignment - Senior Fullstack Engineer – Finance - Backend

Imagine you work for a company that offers an intermediary service and you are part of the team responsible for
calculating commissions for clients.

- You will receive a request that contains a non empty list of services we provide
- Each element in the list will contain
  - an identifier, a positive integer
  - total amount of money, that cannot be greater than 1,000,000
- Each request is associated with a specific client
- Our company will charge a percentage of the total amount of each element from the list
- In order to calculate the rate associated with a specific element on the list we will use the amount.

For example:

Given this commission
| amount              | rate       |
|---------------------|------------|
| 0 - 1,000           |   10 %     |
| 1,000 - 3,000       |    5%      |
| 3,000 - 1,000,000   |    1%      |

and this request:

- (1) 900
- (2) 2000
- (3) 4000

the commissions will be:

- (1) 90
- (2) 100
- (3) 40
