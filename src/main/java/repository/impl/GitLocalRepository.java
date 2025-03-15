package repository.impl;

import lombok.AllArgsConstructor;
import repository.AbstractLocalRepository;
import utils.Utils;

import java.util.List;

@AllArgsConstructor
public class GitLocalRepository extends AbstractLocalRepository {
    private String localRepoPath;

    @Override
    public List<String> findAllCommits(String branch) {
        try {
            return Utils.executeGitCommand(localRepoPath, "git", "log", "--format=format:%H", branch);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<String> findAllChangedFiles(String head, String base) {
        try {
            return Utils.executeGitCommand(localRepoPath, "git", "diff", "--name-only", head, base);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
