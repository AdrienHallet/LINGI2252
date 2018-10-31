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

The idea is that we want to model our domain. In a product line, every product variant comes from the same basic architecture (example of the car, we always have a car body, wheels, ...).

There are three main phases :
1. **Context analysis** : define the boundaries of a domain under analysis. Thinking clearly about the clients, the users, the inputs and outputs of the software.
2. **Domain modelling** : describe the problem addressed by the software in the domain. What are the must-have and could-have features ? Define a lexicon to clearly state what we mean by using a term or another.
3. **Architecture modelling** : create the overall architecture to implement a solution to the problems in that domain.

#### Feature Modelling

> A **feature model** is a hierarchy-arranged set of features (typically a tree-like diagram) with a defined notation (mandatory, optional, composition, alternative, ...)

* **Common features** are the mandatory (plain 'o') leaves in the tree. They appear in every configuration (see below)
* **Variabilities** are the optional features (empty 'o'). They appear in some configurations (see below)

> A **configuration** is the selected variables, an instance, of the model. It is deemed **valid** iif it respects the semantics imposed by the relationships and constraints. The model is **inconsistend** if it has no valid configuration. Two models are **equivalent** if they have the same set of configurations.

**Anomalies** in the diagram are things that should not be there :
* Redundancy if semantic information is modelled in multiple ways
* Inconsistence if there are contradictions within the model.
* Dead features if they can never be selected in any way.
* False-optional features if the selection of the parent induces the selection of the false-optional child

## Software Reuse

> **Software Reuse** is the reapplication of a variety of kinds of knowledge about one system to another **in order to reduce the effort** of developing or maintaining that other system.

*A library is a good example of software reuse. Someone took the time to make it work correctly and packaging it in a library to ease its usage. A copy/paste is an example of a bad code reuse, but it still is.*

That's why reuse is good :
* We gain time (thus money)
* By using a library (part of software) that has been proved, we don't need to prove again that it works.
* Improves overall quality.

Choosing the right bit of code to reuse (for example, choosing a web framework) is therefore very important. We want it to be still alive. The number of users is a good metric of the quality of a library.

Note that we don't only reuse software code, we can also reuse the tests. Any artifact actually.

#### How Object Oriented Programming promotes software reuse

OOP's **abstraction mechanisms** :
* Encapsulation
* Information hiding
* Polymorphism
* Code sharing

Are claimed to promote better code because they allow **modularity**, **code sharing**, **design reuse** which minimises maintenance costs. Obviously, having a complete **hierarchy of classes** helps you reuse the classes across different projects and recycle methods/attributes with the help of *self, this* keywords.

The **super** keyword is statically bounded. Were it dynamic (like *self*) that it would create a kind of infinite loop in the recursive calls when looking up for the context.

*Now we overview other useful OOP features like the **abstract classes**, **abstract methods** and some other.*

Multiple inheritance, while allowing for more modularity, reduces the readability of the code. You don't really know where your code is executed. What if the same method is defined in both superclasses ? What about conflicts ? In java, interfaces solve the problem by allowing multiple inheritance without any code.

## Code Refactoring

> **Refactoring** is a software transformation that preserves the external behavior of the program but improves the internal structure of the program, typically with the purpose of making the software easier to understand and modify.

There are many advantages to refactoring.
* Improve design
* Counter code decay
* Increase comprehensibility
* Find bugs
* Create robust code
* Increase productivity

And the overall satisfaction to work on a clean code. You **should refactor whenever its possible**. Do not postpone refactor, although it can be impossible due to deadlines. Postponing refactoring adds to the technical debt.

#### Refactor Methods
1. **Extract method:** When a fragment of code can be grouped (e.g.: a big conditional statement), turn it into a method.
2. **Inline method:** When a method body is as clear as its signature, remove the method and use the body instead. (e.g.: a one-liner that is understandable)
3. **Inline temp:** Instead of using a temporary variable, it may sometimes be useful to just use it as a one-liner. But it depends on who will read the code and how easy the code is to read in general.
4. **Temp to Query:** A temporary variable (e.g.: a dispatch of prices) can be replaced with a method call to ease readabilit and remove redundancy in a method.
5. **Explaining variables:** When you have a complex code, it can be useful to add temporary variables with meaningful names to help understand the code.
6. **Method to Method Object:** It's like extract method but with a variable. So you create an object that can use a method call replacing the method extraction.

## Bad code smells
A bad smell in your code indicates that it is time to refacor it (it's about **when** to modify it, not **how**). Those smells allows us to identify what needs to be changed, and are kind of a recipe book to help us (indication, not order !) choose the right refactoring pattern.

The goal is to get a more *habitable* code, which is a code in which you feel at home even when you haven't written it yourself. It is easy to read and to change, which is why software needs to be habitable (it always changes).

### Classifications of bad smells
1. **Code duplication** is the top crime, the worst problem. It should be eliminated using various techniques when possible.
2. **Large Piece of code** : class, methode, parameter list,... Should be extracted and arranged toegether in logical ways. For long parameters lists, it is normal with functional programming, but should be avoided by creating objects in POO.
3. **Lack of loose coupling or cohesion**. Coupling is the degree to which the component depend on each other, and cohésion is the degree to which the element is a module belong together. Tight coupling and low cohesions are bad smells. Pair of classes that know too much about each other's private details should be separated and *data clumps* (data items in lots of places) should be made into their own objects.
4. **Too much or too little delegation**. If a client asks for something, and the "chain of asking" goes trough lots of other entities, we might want to move the object closer. Also, if some delegation is only about calling something else, it should be removed (secretary example).
5. **Non Object-Oriented control or data structures**. Switch statements often cause duplication and can be avoided using *polymorphism*. Also, a reluctance to use classes instead of primitive types is bad, since the difference is hard to defined in OO.
6. **Other : comments**. Comments are a good thing to have, but a bad smell when a long comment tries to explain bad code (they are used as a deodorant). When removing the bad smell in the code, the comment often become superfluous : the name of a method usually is enough "comment" on it. Good comments often explains **why** something was done a particular way, or when you don't know exactly what to do.
