package repository.impl;

import dto.GitHubAllChangesDTO;
import dto.GitHubCommitDTO;
import dto.GitHubFileDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.DefaultUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRemoteRepositoryTest {
    private static final String BRANCH_NAME = "branch";
    private static final String HEAD_COMMIT = "abc124";
    private static final String BASE_COMMIT = "abc123";

    @InjectMocks
    private GitHubRemoteRepository gitHubRemoteRepository;

    @Mock
    private GitHubAllChangesDTO gitHubAllChangesDTO;

    @Mock
    private DefaultUtils utils;

    @Mock
    private GitHubCommitDTO gitHubCommitDTO;

    @Mock
    private GitHubFileDTO gitHubFileDTO;

    @Test
    void test_findAllCommits() throws URISyntaxException, IOException, InterruptedException {
        when(utils.fetchData(anyString(), eq(GitHubCommitDTO[].class), any())).thenReturn(new GitHubCommitDTO[] {gitHubCommitDTO});

        List<String> commits = gitHubRemoteRepository.findAllCommits(BRANCH_NAME);

        assertEquals(1, commits.size());
    }

    @Test
    void test_findAllCommits_Exception() throws URISyntaxException, IOException, InterruptedException {
        when(utils.fetchData(anyString(), eq(GitHubCommitDTO[].class), any())).thenThrow(IOException.class);

        List<String> commits = gitHubRemoteRepository.findAllCommits(BRANCH_NAME);

        assertEquals(0, commits.size());
    }

    @Test
    void test_findAllChangedFiles_HeadEqualsToBase() {
        List<String> changedFiles = gitHubRemoteRepository.findAllChangedFiles(BASE_COMMIT, BASE_COMMIT);
        assertEquals(0, changedFiles.size());
    }

    @Test
    void test_findAllChangedFiles_Exception() throws URISyntaxException, IOException, InterruptedException {
        when(utils.fetchData(anyString(), eq(GitHubAllChangesDTO.class), any())).thenThrow(IOException.class);
        List<String> changedFiles = gitHubRemoteRepository.findAllChangedFiles(HEAD_COMMIT, BASE_COMMIT);
        assertEquals(0, changedFiles.size());
    }

    @Test
    void test_findAllChangedFiles_ChangedFilesNull() throws URISyntaxException, IOException, InterruptedException {
        when(gitHubAllChangesDTO.getChangedFiles()).thenReturn(null);
        when(utils.fetchData(anyString(), eq(GitHubAllChangesDTO.class), any())).thenReturn(gitHubAllChangesDTO);

        List<String> changedFiles = gitHubRemoteRepository.findAllChangedFiles(HEAD_COMMIT, BASE_COMMIT);

        assertEquals(0, changedFiles.size());
        verify(gitHubAllChangesDTO, times(1)).getChangedFiles();
    }

    @Test
    void test_findAllChangedFiles_ChangedFilesNotNull() throws URISyntaxException, IOException, InterruptedException {
        when(gitHubAllChangesDTO.getChangedFiles()).thenReturn(List.of(gitHubFileDTO));
        when(utils.fetchData(anyString(), eq(GitHubAllChangesDTO.class), any())).thenReturn(gitHubAllChangesDTO);

        List<String> changedFiles = gitHubRemoteRepository.findAllChangedFiles(HEAD_COMMIT, BASE_COMMIT);

        assertEquals(1, changedFiles.size());
        verify(gitHubAllChangesDTO, times(2)).getChangedFiles();
    }

}