# Team 6458's FIRST POWER UP 2018

Welcome to Team 6458's code repository for the 2018 FIRST POWER UP
robotics competition.

## Resources
* [2018 FRC Control System](https://wpilib.screenstepslive.com/s/4485)
* [Command based programming](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/599732-what-is-command-based-programming)

## "This looks really scary"
Yes. It is certainly overwhelming, and total beginners are probably NOT going to
have the smoothest transition over.
You don't ever have to write code that gets accepted if it's beyond your
skill level.
However, you can always contribute in other
ways, such as reviewing code in pull requests. The more eyes, the better!

## Pull Request Reviewing
Reviewing others' pull requests is an **essential** part of the workflow
we have. Without reviews, bad code tends to get put in and malfunctions
and bugs occur. Last year, only **one** person wrote the entire robot's
systems, and the number of **human injuries** that nearly occurred were far
too frequent. To avoid damage to the robot and human injuries,
we need to make sure all possible sources of
error are filtered out first and foremost--especially before testing on the real thing.

## Installation
1. Ensure you have [Git](https://git-scm.com/) and [JDK **8**](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed.
2. Make a new directory somewhere, enter it, and start Git Bash in that directory. You can do this on Windows by right clicking in the File Explorer and clicking Git Bash Here.
3. Type `git clone https://github.com/chrislo27/Team6458-2018` and press Enter.
4. If you are using IntelliJ IDEA, navigate to File > New > Project from Existing Sources and select the build.gradle file. Uncheck "Create separate module per source set".
If you are using Eclipse, see below.

### Importing git project in Eclipse
1. Go to the toolbar on the top and select Window->Perspective->Open Perspective->Other...
2. Select "Git"
3. Near the top of the page, there are 3 buttons with GIT written on it, one has a blue curved arrow and is labeled "Clone a Git Repository and add the clone to this view" when your mouse hovers over it, click this button
4. Enter URI and credentials for GitHub since this is a private repository, click Next
5. Ensure that "master" and any additional branches you want are selected and click Next
6. Choose your desired directory and click Finish
7. Return to the Java view by clicking the button on the top right with the package symbol, a J and two circles
8. Goto and right click the "Package Explorer" and select "Import"
9. Select "Existing Gradle Project" under the folder "Gradle"
10. Set your root directory to where your project is stored and import
12. After you import it, right click build.gradle and go to Gradle and click refresh gradle project
13. To connect to the git repository, start Git Bash (see below), create a branch (see below) and check out the master branch (see below).

### How to start 
0. Navigate to the folder where the project is stored
1. Enter it
2. Right click in the folder and open Git Bash

### How to use Git Bash
Git commands are called by typing "git" and then the command. For example - "git pull". Options can be typed after the command using "--".
The important commands that all of you need to familiarize yourself with is branch, pull, fetch, reset, push and commit. Before I go into
those, let me explain how the repository works. There is a local and a remote repository. The remote repository of a branch is called 
"origin". The local copy of the remote repository is called "origin/branchname" (where branchname is the name of the branch). The copy 
that can be edited is called branchname (where branchname is the name of the branch).

## Follow these steps to practice the above commands -
0. You will need Bash open. Open Git Bash (see above)
1. To create a local branch type "git branch branchname" where branchname is the name of the branch. The format of the name should be 
"implement/feature" or "fix/BugName"
2. To push your branch to remote (create a remote branch), type "git push origin branchname" (where branchname is the name of the branch)
You will need a remote branch so other people can see what feature you are working on. You also need it for pull requests, when you wish
your code to be merged with the master branch.
3. You should be able to see your remote branch on GitHub, under the "branches" section under your "Code" section. People can view
your code here and you can use the website for pull requests. The owners will approve the merge or reject it. 
4. Now, attempt to merge your branch (the new feature you are developing) with the master branch (the base code from GitHub). Type "git checkout
master". Type "git pull". Type "git checkout yourbranchname" (where yourbranchname is the name of your branch).
5. Type "git merge master".
6. Push the branch to remote. The command had been mentioned above. Once you push, it should have updated.
7. To add a file, you will need to open Eclipse. Go to Git View. Go to branches. Open Local Folder. Double click your branch.
Checkout. Go to your package explorer. If it says your branch name next to you package folder, the process worked and you have 
successfully checked out your branch.
8. Now, add a class to the source code called TestClass, just like a normal Java project. No need to use Git Bash yet.
9. Try writing some dummy code (sysout and a main method). If the syntax doesn't compile, you have probably imported it as a git project
and not a gradle. Follow steps 8 - 12 from "### Importing git project in Eclipse" and then retry this step.
10. To add this code onto your branch, open Git Bash and type "git add -A". Format and capitalization is very important. 
11. To commit this to your remote branch, type "git commit -a -m"CommitMessage"" (where CommitMessage is a comment you leave within
quotes about this commit).
12. If you get an error saying "Fatal - unable to autodetect email adress", means your git account has not been synchronized.
13. Follow the instructions. (i.e. git config -- gloabl user.email "your email" and git config -- global user.name "your name"
14. Once you are done, push the branch to remote. Syntax was mentioned above. Check your branch on Git Hub after you are done.
15. Congratulations. You now know how to write code and push it into the repository through your branch. If you think code should be 
implemented on the robot, you can pull request from the website, where the codeowners will review and merge or reject your code. 
16. To delete your remote branch, use the Bash and type "git push origin :branchname" (where branchname is the name of the branch).
17. Remember to delete local after you delete remote (git branch -d branchname)(where branchname is the name of the branch)
18. To switch a branch, type "git branch". This should show you the current available branches. Switch to the branch by typing "git checkout
branchname" (where branchname is the name of the branch)
19. In case you really screw up, reset everything to the base code by typing "git fetch origin" and then type "git reset --hard origin/master"
20. If master code has been updated, you can update you code by typing "git pull".
 
### Keeping the `master` branch up to date
0. Have no unstaged changes.
1. `git checkout master && git pull --rebase origin master`
2. `git checkout <your old branch here>`

### To update your own branch
0. Have no unstaged changes.
1. Ensure the `master` branch is up to date (see above).
2. While checked out in your branch: `git rebase master`

### Submitting Changes
0. Do all your work on a new branch first, not master.
Do this by running this command with Git Bash INSIDE of the folder where
the `.git/` folder is: `git branch <branch name>`. The branch name should
clearly indicate it belongs to you.
1. After committing and pushing your work to `origin`, open a Pull Request. It is one of the tabs on the page.
2. Have a clear title and description.
3. Everyone will begin to review your work to see if it is fit for merging into the master branch. At least one of the [code owners](.github/CODEOWNERS) must approve it.
4. If everything looks good, the owner of this repository will merge it.

## Code Specifications
* Java **8** only. No other JVM languages please. (The `src/unused/` folder contains
unused code only for reference, and it should not be tampered with.)
  * Java 9 is not supported, unfortunately.
* Use `private static final Logger LOGGER` instances where logging is necessary. See
[SemiRobot.java](src/main/java/team6458/SemiRobot.java) for an example.
* [Null is bad.](https://en.wikipedia.org/wiki/Tony_Hoare#Apologies_and_retractions)
Avoid nulls wherever possible.
State all accepted inputs as nullable or non-null. The last thing that should
happen is that the robot locks up and crashes with a NullPointerException
due to poor practices.
  * If nulls are used in non-closed scope situations, you will have to
  justify its use. You will most likely be asked to rewrite it without null.

### Formatting
* Four spaces or tabs set to four space widths
* Correct indentations for all code
* Keep it neat, please. This won't be nitpicked to a T, but it shouldn't
look like a bomb went off.

### Javadocs
* **EVERY** Javadoc-able thing needs Javadoc documentation, unless it is:
  * A `private static final Logger LOGGER` instance in a class
  * An obvious getter/setter
  * Note that normal code should only be commented with normal comments
* All documentation must be grammatically correct English with no spelling errors
  * Note: text for `@tags` like `@param` or `@returns` do not need a full stop
* See [PlateAssignment.java](src/main/java/team6458/util/PlateAssignment.java) for a thorough example.
* Tip: write your documentation like a serial killer is out to get you.
That's what it feels like to stare at code weeks or days later sometimes.
Save yourself the hassle in the future and do it properly in the present.
