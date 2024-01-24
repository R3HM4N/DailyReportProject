package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PageInfo {
    List<UserDTO> userDTOS;
    private int pageNumber;
    private long totalElements;
    private boolean hasNext;
}
