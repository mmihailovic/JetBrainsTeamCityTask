package service;

import java.util.List;

public interface RepositoryService {
    /**
     * This method returns a list of all files that were changed independently in the specified local and remote branches
     * after their latest common commit
     * @param remoteBranch name of the remote branch
     * @param localBranch name of the local branch
     * @return List of {@link String} values representing the paths of changed files
     */
    List<String> findAllFilesChangedInBothBranchesFromLatestCommonCommit(String remoteBranch, String localBranch);
}
