# Software Maintenance & Evolution
#### LINGI2252 - Université Catholique de Louvain
* Professor Kim Mens
* Academic Year 2018-2019, Q1
---

## Introduction

### Software Maintenance
Software fails because of **preventable** mistakes, but companies don't put enough effort (money and time) before-hand to prevent those mistakes. History shows us that a lot of previous project resulted in billions of dollars of losses due to a lack of software quality assurance.

Maintenance is about **keeping the code clean** to ensure that future additions will not reduce the readability, usability, performances, ... Programmers do not like to maintain but that's actually where we need a lot of manpower.

The expected life of a system is about **7 years**. On average, a programmer spends half his time on maintenance.

> **Software maintenance** : the process of modifying a software system after delivery.

We have different types of maintenance :
* Adaptive : modify the system to adapt a changed environment
* Corrective : fixing problems occuring in production
* Perfective : improving performances, maintanability or other attributes of a computer program
* Preventive : correct a code without changing the current behavior to prevent future failures

Perfecting the system apparently is the biggest part of them all.

The main causes for maintenance problems are :
* Poor documentation
* Poor software quality
* Lack of knowledge about the system and its domain
* Ineffectiveness of the maintenance team

To sum it up, **it's all about the code quality**. If you code well from the start, the maintenance will cost less time ($$).

Softwares become less and less efficient because of ageing :
* Maintenance activities
* Ignorant surgery and architectural erosion
* Inflexibility from the start
* Insufficient or inconsistent documentation
* Deadline pressure
* Duplicated functionality
* Lack of modularity

Change is inevitable due to the environment and the (new) use, but that very change induces **technical debt**. Fixing things the wrong way only reports and increase future technical debt.

> **Technical Debt**: the extra develoment work that arises when code that is easy to implement in the short run is used instead of applying the best overall solution.

Software maintenance is not evolution. The latter implies some major changes in the architecture of the system that are not really covered by the maintenance. Evolution in itself may arise during every step (development, maintenance) or simply when the project becomes too complex to maintain.

There are multiple evolution types :
* Manual : the programmer does it
* Generic : write it broadly enough that evolution is fast
* Generation : automatic tools
* Transformation : transform a code into another (can also use auto tools)
* Configuration : make the program broad enough that we can modify it with a configuration file
