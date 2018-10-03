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

> **Software maintenance** is the process of modifying a software system after delivery.

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

> **Technical Debt** is the extra develoment work that arises when code that is easy to implement in the short run is used instead of applying the best overall solution.

Software maintenance is not evolution. The latter implies some major changes in the architecture of the system that are not really covered by the maintenance. Evolution in itself may arise during every step (development, maintenance) or simply when the project becomes too complex to maintain.

There are multiple evolution types :
* Manual : the programmer does it
* Generic : write it broadly enough that evolution is fast
* Generation : automatic tools
* Transformation : transform a code into another (can also use auto tools)
* Configuration : make the program broad enough that we can modify it with a configuration file

> **Software Evolution** is all the programming activities that intend to generate a new software version from an earlier operational version.
>* **Static Evolution** are changes applied manually by a human programmer. When part of the software gets adapted or replaced and the product is redeployed.
>* **Dynamic Evolution** are changes applied *automatically at runtime* to adapt the new needs of the software system. For example, self-adaptive systems, metaprogramming, context-oriented programming, ...

The **eight laws of Software Evolution**, proposed around '74 by an empirical study, states that there is no such thing as a 'finished' program and recognize software evolution. They seem to be generally applicable to large systems but there is no proof whether they apply to other types of software (smaller ones, open-source projects, ...)
1. **Continuing change** : if it stays alive, it will continue to grow.
1. **Increasing complexity** : a program that stays alive will become more complex unless work is done against that.
1. **Self regulation**
1. **Conservation of organisational stability**
1. **Conservation of familiarity**
1. **Continuing growth** : a program must have new functionalities over time to continue to satisfy the user.
1. **Declining quality** : a program will be percieved as declining in quality over time unless work is done against that.
1. **Feedback system**

#### Choosing the right paradigm

It is an important step before developping because there are now so many language possibilities that they can offer many improvements to code maintenance.
* Object-oriented programming (benefits with inheritance to reuse and extend the code)
* Event-driven programming may be better when we focus on triggers and dynamic events (user inputs, actions)
* Service-oriented programming promotes software reuse with services as interfaces that can improve modularity.
* ...

---
## Domain Modeling

> Trying to model what the domain is about before we start implementing.

*Example of the assembly line that build more product with less input resources. This **Economies of Scale** saves us time and material with the help of technology*

We can apply the product lines principles to software to build for a **family of systems** sharing **common features** in a certain **domain**.

We hereby talk about **economies of scope** that also reduces the overall cost with the use of technology.

#### Domain Analysis
We will focus our analysis on **features**. We need to find what is **common** and what is **variable** to create a **product family**.

> A **feature** is a user-visible aspect or charasteristic of your application.

#### Feature Modelling

> A **feature model** is a hierarchy-arranged set of features (typically a tree-like diagram) with a defined notation (mandatory, optional, composition, alternative, ...)

* **Common features** are the mandatory (plain 'o') leaves in the tree. They appear in every configuration (see below)
* **Variabilities** are the optional features (empty 'o'). They appear in some configurations (see below)

> A **configuration** is the selected variables, an instance, of the model. It is deemed **valid** iif it respects the semantics imposed by the relationships and constraints. The model is **inconsistend** if it has no valid configuration. Two models are **equivalent** if they have the same set of configurations.

**Anomalies** in the diagram are things that should not be there :
* Redundancy if semantic information is modelled in multiple ways
* Inconsistence if there are contradictions within the model.
