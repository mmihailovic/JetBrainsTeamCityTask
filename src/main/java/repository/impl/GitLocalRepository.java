package repository.impl;

import repository.AbstractLocalRepository;
import utils.Utils;

import java.util.List;

public class GitLocalRepository extends AbstractLocalRepository {
    private String localRepoPath;
    private Utils utils;

    public GitLocalRepository(String localRepoPath, Utils utils) {
        this.localRepoPath = localRepoPath;
        this.utils = utils;
    }

    @Override
    public List<String> findAllCommits(String branch) {
        try {
            return utils.executeCommand(localRepoPath, "git", "log", "--format=format:%H", branch);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<String> findAllChangedFiles(String head, String base) {
        try {
            return utils.executeCommand(localRepoPath, "git", "diff", "--name-only", head, base);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
