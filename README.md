# Photoshop
Repository for the Google Mentorship for Fall Term of 2018-2019.
## What is this program?
This is a photo editor that allows for multiple users to edit the image concurrently on the same network. Simple operations like drawing lines and rectangles are supported, along with more complicated features like blurring a section and making the image darker/lighter. Checkout a video of the program in use: [YouTube Demo](https://www.youtube.com/watch?v=AaUzGyxngZs)

## How do I run this program?  

### Prereqs:
1. Java 11 or later 
2. Maven
3. Internet Connection (for downloading dependencies)

### Directions to run the program
Download the repository and change your terminal directory into it. Run the following instructions in the terminal. 
* Manually: 
	1. Make the jar: ```mvn clean compile assembly:single```
	2. Run the jar: 
	```java -jar target/Photoshop-1.0-jar-with-dependencies.jar```
* Build script (runs the commands defined in "manually"):
	1. ```./run.sh```

## How do I use the program?
Look at the demo [here](https://www.youtube.com/watch?v=AaUzGyxngZs).  
Watch our presentation [here](https://www.youtube.com/watch?v=tADZawDH_kc)

## Reflection
This program was our first experience with going through the full software development cycle. In school, projects were limited
since we only have three weeks; however, in this program we had four months and have the help of a mentor.

### Improvements:
1. We started the program by creating an outline: the technologies we looked at and reasoning behind choosing one over the other;
a high level description of how the code pieces together and function; and a timeline. We should have made this document a living
one. We were focused on the actual coding part, and this caused the documentation to fall short and made it hard to understand
how the code fits together. A UML diagram would definitely be helpful to see what everyone is working on. Additionally, the timeline
was difficult to follow. We had to balance school work, extracurriculars, and learn the technology (multithreading, networking, model view controller pattner).
The dates should be more feasible to prevent a messy code base from forming and stressing over missing or broken features.

2. The code base at the end of the project was messy and hard to debug. Javadocs were missing for many functions and classes, making
it hard to understand what each of us wrote. Model view controller wasn't enforced heavily and resulted in a single class doing all
three at once – we did learn how it works in the end. In our timeline for the project, we should allocate time for refactoring the 
code and making sure everyone understands it. 

3. Last but not least, we need to work on our teamwork. It was difficult to divide up the code in such a way that one didn't 
depend on another. Again, a UML diagram would be helpful so we can clearly see who can work on what without breaking the code.
In addition, a UML diagram will help us understand how our code interacts with each other and no one will be left behind.

### What we learned:
1. Creating a program is not just writing code and making an outline. Everyone needs to know what they are doing and understand
the framework and ideas being used. Although we did not focus on this, graphics design is still an important part. We ended the program
with a live presentation, which we only do for final projects. Having to explain code to others who have no idea
what we are making is just as important as everything else.

2. Computer science ideas in general. Multithreading was a difficult idea to get used to since it is not
covered in APCS and we had to learn it through online resource and our mentor. We went over race conditions and synchronization using
locks and the synchronized keyword. In addition, we learned how to use the model view controller pattern to better organize
our code. Another notable topic we covered was networking using streams and tcp. 

3. Better time management. We met weekly at Google after school for an hour to go over any issues we have. In addition, we 
had to code during the school week and the weekends. Having to balance work from school, extracurriculars and this program,
we developed better time management skills. 

These are just a few of the many things we learned from this program. Although we didn't make fully 
fleshed-out program at the end, we did have fun working with each other and our mentor. It was a great learning experience
in tandem with Advanced Placement Computer Science. 

## Members
- Mentor: Nick Chavez 
- Devin Lin
- Jackson Zou
- Derek Leung
- William Cao
