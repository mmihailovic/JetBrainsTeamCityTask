package service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.AbstractLocalRepository;
import repository.AbstractRemoteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceImplTest {
    private static final String LOCAL_BRANCH = "branchB";
    private static final String REMOTE_BRANCH = "branchA";
    private static final String[] COMMITS = {"abc123", "abc124", "abc125"};
    private static final String[] FILES = {"file1", "file2"};

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @Mock
    private AbstractLocalRepository localRepository;

    @Mock
    private AbstractRemoteRepository remoteRepository;

    @Test
    void test_LocalBranchNoCommits() {
        when(remoteRepository.findAllCommits(anyString())).thenReturn(List.of());
        when(localRepository.findAllCommits(anyString())).thenReturn(List.of());

        List<String> changedFiles = repositoryService
                .findAllFilesChangedInBothBranchesFromLatestCommonCommit(REMOTE_BRANCH, LOCAL_BRANCH);

        assertEquals(0, changedFiles.size());
        verify(localRepository, never()).findAllChangedFiles(anyString(), anyString());
        verify(remoteRepository, never()).findAllChangedFiles(anyString(), anyString());
    }

    @Test
    void test_LatestCommonCommitNotFound() {
        when(remoteRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[0]));
        when(localRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[1]));

        List<String> changedFiles = repositoryService
                .findAllFilesChangedInBothBranchesFromLatestCommonCommit(REMOTE_BRANCH, LOCAL_BRANCH);

        verify(localRepository, never()).findAllChangedFiles(anyString(), anyString());
        verify(remoteRepository, never()).findAllChangedFiles(anyString(), anyString());
        assertEquals(0, changedFiles.size());
    }

    @Test
    void test_LatestCommonCommitFound_WithoutChanges() {
        when(remoteRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[0]));
        when(localRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[0]));
        when(remoteRepository.findAllChangedFiles(COMMITS[0], COMMITS[0])).thenReturn(List.of());
        when(localRepository.findAllChangedFiles(COMMITS[0], COMMITS[0])).thenReturn(List.of());

        List<String> changedFiles = repositoryService
                .findAllFilesChangedInBothBranchesFromLatestCommonCommit(REMOTE_BRANCH, LOCAL_BRANCH);

        verify(localRepository).findAllChangedFiles(COMMITS[0], COMMITS[0]);
        verify(remoteRepository).findAllChangedFiles(COMMITS[0], COMMITS[0]);
        assertEquals(0, changedFiles.size());
    }

    @Test
    void test_LatestCommonCommitFound_WithChanges() {
        when(remoteRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[1], COMMITS[0]));
        when(localRepository.findAllCommits(anyString())).thenReturn(List.of(COMMITS[2], COMMITS[0]));
        when(remoteRepository.findAllChangedFiles(COMMITS[1], COMMITS[0])).thenReturn(List.of(FILES));
        when(localRepository.findAllChangedFiles(COMMITS[2], COMMITS[0])).thenReturn(List.of(FILES[0]));

        List<String> changedFiles = repositoryService
                .findAllFilesChangedInBothBranchesFromLatestCommonCommit(REMOTE_BRANCH, LOCAL_BRANCH);

        verify(localRepository).findAllChangedFiles(COMMITS[2], COMMITS[0]);
        verify(remoteRepository).findAllChangedFiles(COMMITS[1], COMMITS[0]);
        assertEquals(1, changedFiles.size());
        assertEquals(FILES[0], changedFiles.get(0));
    }
}