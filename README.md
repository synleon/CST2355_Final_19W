# CST2335 Graphical Interface Programming
### Code repo for CST2355 final project
#### Overview
This is a group project for groups of 4 members (all in the same lab section). If you would like to be assigned a group, send an email to your lab instructor.  If you have chosen partners yourself, then please email the names of the group to your lab instructor.  You must work in the group to which you are assigned.  Use your assigned group number WITH your names in labeling submissions.  Be sure to work through the Group Activity Worksheet together to exchange contact information amongst your group (Algonquin student email addresses at a minimum). You should also determine who would be working on what part of the project. Your group may choose a name for your group if you like.  All work must be the work of the group members and ONLY the group members:
-	**If a member submits plagiarized work, the whole group will be charged.**
    - **Double-check each other’s work to ensure sources are cited within program comments.**
-	Each person will be responsible for their own part of the project and graded on this separately
-	A portion of the grade will come from an evaluation of your participation as a group member

#### Purpose
The Project is assigned to give you experience in: 
-	Developing software in a group environment. 
- Dividing workload to meet deadlines. 
- Designing modular software that allows for that division. 
- Learning from the work of others 

#### Requirements
Here is a list of the requirements for the final project:
1.	Each Activity must have a ListView to present items. Selecting an item from the ListView must show detailed information about the item selected.
2.	Each activity must have at least 1 progress bar and at least 1 button.
3.	Each activity must have at least 1 edit text with appropriate text input method and at least 1 Toast, Snackbar, and custom dialog notification.
4.	The software must have 1 different activity written by each person in your group. The activity must be accessible by selecting a graphical icon from a Toolbar.
5.	Each Activity must use a fragment somewhere in its graphical interface.
6.	Each activity must have a help menu item that displays a dialog with the author’s name, Activity version number, and instructions for how to use the interface.
7.	There must be at least 1 other language supported by your Activity. If you are not bilingual, then you must support both British and American English (words like colour, color, neighbour, neighbor, etc). If you know a language other than English, then you can support that language in your application and don’t need to support American English.
8.	The items listed in the ListView must be stored by the application so that appear the next time the application is launched. The user must be able to add and delete items, which would then also be stored in a database.
9.	Each activity must use an AsyncTask to retrieve data from an http server.
10.	Each activity must use SharedPreferences to save something about the application for use the next time the application is launched.
11.	All activities must be integrated into a single working application, on a single device or emulator.
12.	The interfaces must look professional, with GUI elements properly laid out and aligned. 
13.	The functions and variables you write must be properly documented using JavaDoc comments.

#### ***Milestones***
Bonus marks will be awarded for displaying correct functionality by the following dates:

| Milestone \# and date      | Requirements implemented \# | Bonus Marks available |
| :------------------------- | :-------------------------- | :-------------------- |
| \#1 – Thursday Nov 15, 2018| 1, 2, 3, 11, 13             | 1                     |
| \#2 – Thursday Nov 22, 2018| 4, 6, 7, 9, 11, 13          | 1                     |
| \#3 – Thursday Nov 29, 2018| 5, 8, 10, 11, 12, 13        | 1                     |

#### Beginning Steps
- Create a new GitHub repository from one of the group members’ accounts. That group member must then invite the other group members to contribute. This is done by clicking on the “Settings” tab in Github, then click “Collaborators” on the left side menu, and search the group member names to add them to the project. Other team members should then clone that project to their computer and start making branches for their work. ***You will not be able to integrate your work if you do not start by first cloning the project!***
   - As early as possible:
   - Decide who will work on which application.
   - Determine the additional tasks and decide who will take on each, for example: Technical Lead, Action Bar, Project Management and Communication Lead, Code Custodian, Documentation, Test Plan Integrator, Integration Tester, and any others you can determine
   - **Discuss and document a code-freeze date for the group project**, I recommend **72 hours before the actual due date** so final code files can be merged into the project in preparation for upload to Blackboard.
- Attempt to write your own code on your own branch and then merge that branch often (after each requirement is finished). Don’t try to merge the code only on the last week.

#### Grading Guide
- Grading in 3 parts – Read each one carefully:
- Group activity worksheet (5%)
   - By Match 1st – Please fill in the Excel file “CST2335_FinalProject_Team_Activity_Workbook.xlxs” and submit on Brightspace, under Final Project. Everyone in the group should submit the file. 
- Each student is graded on his or her application separately (85%) 
   - Week of March 25 - 29 – demonstrate the Milestone 1 requirements for bonus
   - Week of April 1 - 5 – demonstrate the Milestone 2 requirements for bonus
   - Week of April 8 - 12 – demonstrate the Milestone 3 requirements for bonus
   - Week of April 15 - 19 – Project demonstration during your scheduled lab sessions. Arrange a single submission of the group deliverable by one of the group members on behalf of the entire group. The professor will ask to see all of the required items. You must be in the lab in person. Code submitted on Brightspace will not be marked. 
- Each group must submit their final code as a record of what was finished at the end of the project. (10%)
   - based on the average of your team members peer review
   - Note: If you do not submit an individual Self and Peer form your self-evaluation becomes zero. If a team member does not submit their self and peer form, the other team members will not be penalized.

#### The Applications
Each of the applications (as they are intended) requires similar programming techniques.  Each application takes information from the user, and stores it in a database.  Each application also provides functionality to summarize or analyze the whole body of data entered into the application.  Beyond that you are free to get creative.
##### Merriam Webster Dictionary
- The user can search for words to get a list of definitions. The URL is: https://www.dictionaryapi.com/api/v1/references/sd3/xml/pasta?key=4556541c-b8ed-4674-9620-b6cba447184f
- To change the search term, you have to change the text between “xml/” and “?key”. The example above is searching for the definition of “pasta”. If you change the word, you will get a different definition. Don’t forget to use URLEncoder.encode( string, “UTF-8”) to encode your search word.
- The user can save the definition for local viewing. The user then can see a list of saved words. The user can also delete an item from the saved words.
- Your application should save the last item searched to display the next time the application is launched.

##### News Feed
- Create an application that reads news stories from a News Crawler site: https://webhose.io. I’ve created an account with the token: 86940a5c-b094-4465-942e-81ce096fe5c9 
- The user should be able to enter a search term. Your application will call the web server to retrieve a list of articles that match the term. Your application should create a list of titles that are retrieved in the results. Clicking on a title should load the rest of the news article, and a link to the article. Clicking on the link should go to a web page Intent with the link text sent in the Intent.
- There should be a button on the article to save this article to your device for later viewing. From the list of saved articles, you should be able to delete an item from your list.
- Your application should save the last topic that was searched to display the next time the application is launched.

##### Flight Status tracker
- You should sign up for a free API key here: https://aviation-edge.com/subscribe/signup.php?level=1
- The user can enter an airport code and retrieve flights departing or arriving from the airport. From the list of flights, the user can select one of them to see the location, speed, altitude of the flight, and status of the flight.
- The user can save the flight number for later updates. The user can then delete the flight from the list of saved flights

##### New York Times Article Search
- Create an application that reads news stories from the New York Times website.
- The user should be able to enter a search term. Your application will call the web server to retrieve a list of articles that match the term. Your application should create a list of titles that are retrieved in the results. Clicking on a title should load the rest of the news article, and a link to the article. Clicking on the link should go to a web page Intent with the link text sent in the Intent.
- There should be a button on the article to save this article to your device for later viewing. From the list of saved articles, you should be able to delete an item from your list.
- Your application should save the last topic that was searched to display the next time the application is launched.
