package repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Utils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitLocalRepositoryTest {
    private static final String HEAD_COMMIT = "abc124";
    private static final String BASE_COMMIT = "abc123";
    private static final String BRANCH = "branch";
    private static final String FILE = "FILE";

    @InjectMocks
    private GitLocalRepository gitLocalRepository;

    @Mock
    private Utils utils;

    @Test
    void test_findAllCommits() throws IOException {
        when(utils.executeCommand(any(), any(String[].class))).thenReturn(List.of(HEAD_COMMIT));

        List<String> commits = gitLocalRepository.findAllCommits(BRANCH);
        assertEquals(1, commits.size());
        assertEquals(HEAD_COMMIT, commits.get(0));
    }

    @Test
    void test_findAllCommits_Exception() throws IOException {
        when(utils.executeCommand(any(), any(String[].class))).thenThrow(IOException.class);

        List<String> commits = gitLocalRepository.findAllCommits(BRANCH);

        assertEquals(0, commits.size());
    }

    @Test
    void test_findAllChangedFiles() throws IOException {
        when(utils.executeCommand(any(), any(String[].class))).thenReturn(List.of(FILE));

        List<String> changedFiles = gitLocalRepository.findAllChangedFiles(HEAD_COMMIT, BASE_COMMIT);
        assertEquals(1, changedFiles.size());
        assertEquals(FILE, changedFiles.get(0));
    }

    @Test
    void test_findAllChangedFiles_Exception() throws IOException {
        when(utils.executeCommand(any(), any(String[].class))).thenThrow(IOException.class);

        List<String> changedFiles = gitLocalRepository.findAllChangedFiles(HEAD_COMMIT, BASE_COMMIT);

        assertEquals(0, changedFiles.size());
    }

}