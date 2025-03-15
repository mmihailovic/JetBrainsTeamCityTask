package repository;

import java.util.List;

public interface Repository {
    /**
     * This method returns a list of all commits for the specified branch
     * @param branch branch name
     * @return List of {@link String} values representing SHAs of commits
     */
    List<String> findAllCommits(String branch);

    /**
     * This method returns all the files that were changed when comparing two commits
     * @param head head commit
     * @param base base commit
     * @return List of {@link String} values representing the paths of changed files
     */
    List<String> findAllChangedFiles(String head, String base);
}
