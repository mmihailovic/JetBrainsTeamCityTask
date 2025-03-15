package dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class GitHubFileDTO {
    @SerializedName("filename")
    private String filePath;
}
