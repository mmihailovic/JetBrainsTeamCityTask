This project is my solution for the task for project **Improvements of managing infrastructure code in TeamCity**.

### Key components
- **Repository** interface defines the methods for finding all commits and all changed files comparing two commits in the repository.
  Abstract classes **AbstractLocalRepository** and **AbstractRemoteRepository** is implemented to separate local and remote repositories.
  The concrete implementations **GitLocalRepository** and **GitHubRemoteRepository** inherit the abstract classes, providing specific
  implementations for the methods defined in the **Repository** interface.
  This approach follows the **Strategy Pattern**.
- **RepositoryService** interface provides method for finding all files with the same path that were changed in both local and remote branches
  since their latest common commit. Concrete implementation of this interface is **RepositoryServiceImpl** and it uses methods from **Repository**
  interface for finding that changes. Since this method finds files that were changed on both **local** and **remote** branches, **RepositoryServiceImpl**
  requires two instances of **Repository** interface: one which extend **AbstractLocalRepository** and one which extend **AbstractRemoteRepository**, ensuring
  that we have one local and one remote repository to handle both branches appropriately.
- **Utils** interface defines the methods for fetching data from API and executing commands.
