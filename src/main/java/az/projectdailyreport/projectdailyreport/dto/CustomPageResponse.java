package az.projectdailyreport.projectdailyreport.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomPageResponse {

    private List<UserDTO> content;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
}
