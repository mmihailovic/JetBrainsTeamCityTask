package dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class GitHubAllChangesDTO {
    @SerializedName("files")
    private List<GitHubFileDTO> changedFiles;
}
