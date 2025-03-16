package service.impl;

import repository.*;
import service.RepositoryService;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryServiceImpl implements RepositoryService {
    private Repository localRepository;
    private Repository remoteRepository;

    public RepositoryServiceImpl(AbstractLocalRepository localRepository, AbstractRemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    @Override
    public List<String> findAllFilesChangedInBothBranchesFromLatestCommonCommit(String remoteBranch, String localBranch) {
        List<String> commitsFromRemoteBranch = remoteRepository.findAllCommits(remoteBranch);
        List<String> commitsFromLocalBranch = localRepository.findAllCommits(localBranch);

        if(commitsFromRemoteBranch.isEmpty() || commitsFromLocalBranch.isEmpty()) {
            Logger.getGlobal().log(Level.SEVERE,"There are no commits on some of the specified branches!");
            return new ArrayList<>();
        }

        String latestCommonCommit = findLatestCommonCommit(commitsFromRemoteBranch, commitsFromLocalBranch);

        if(latestCommonCommit == null) {
            Logger.getGlobal().log(Level.SEVERE, "Latest common commit not found!");
            return new ArrayList<>();
        }

        String latestCommitOnLocalBranch = commitsFromLocalBranch.get(0);
        String latestCommitOnRemoteBranch = commitsFromRemoteBranch.get(0);

        List<String> filesChangedOnLocalBranch = localRepository
                .findAllChangedFiles(latestCommitOnLocalBranch, latestCommonCommit);

        // Storing elements of the list in a Hashset for efficient comparison of two lists
        Set<String> changedFiles = new HashSet<>(filesChangedOnLocalBranch);

        List<String> filesChangedOnRemoteBranch = remoteRepository
                .findAllChangedFiles(latestCommitOnRemoteBranch, latestCommonCommit);

        List<String> filesChangedInBothBranches = new LinkedList<>();

        for(String file: filesChangedOnRemoteBranch) {
            if(changedFiles.contains(file)) {
                filesChangedInBothBranches.add(file);
            }
        }

        return filesChangedInBothBranches;
    }

    private String findLatestCommonCommit(List<String> branchACommits, List<String> branchBCommits) {
        // Storing elements of the list in a Hashset for efficient search
        Set<String> commits = new HashSet<>(branchACommits);

        for(String commit: branchBCommits) {
            if(commits.contains(commit)) {
                return commit;
            }
        }

        return null;
    }
}
