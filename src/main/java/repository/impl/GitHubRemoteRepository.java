package repository.impl;

import dto.GitHubAllChangesDTO;
import dto.GitHubCommitDTO;
import dto.GitHubFileDTO;
import repository.AbstractRemoteRepository;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitHubRemoteRepository extends AbstractRemoteRepository {
    private static final String GITHUB_ALL_COMMITS_FOR_BRANCH_ROUTE = "https://api.github.com/repos/%s/%s/commits?sha=%s";
    private static final String GITHUB_ALL_DIFFERENCES_ROUTE = "https://api.github.com/repos/%s/%s/compare/%s...%s";
    private String owner;
    private String repo;
    private String accessToken;

    public GitHubRemoteRepository(String owner, String repo, String accessToken) {
        this.owner = owner;
        this.repo = repo;
        this.accessToken = accessToken;
    }

    @Override
    public List<String> findAllCommits(String branch) {
        try {
            String route = String.format(GITHUB_ALL_COMMITS_FOR_BRANCH_ROUTE, owner, repo, branch);

            GitHubCommitDTO[] commits = Utils.fetchData(route, GitHubCommitDTO[].class, accessToken);

            return Arrays.stream(commits)
                    .map(GitHubCommitDTO::getSha)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<String> findAllChangedFiles(String head, String base) {
        try {
            if(head.equals(base)) {
                return new ArrayList<>();
            }

            String route = String.format(GITHUB_ALL_DIFFERENCES_ROUTE, owner, repo, base, head);

            GitHubAllChangesDTO changes = Utils.fetchData(route, GitHubAllChangesDTO.class, accessToken);

            if(changes.getChangedFiles() == null) {
                return List.of();
            }

            return changes.getChangedFiles()
                    .stream()
                    .map(GitHubFileDTO::getFilePath)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
