package com.cowave.sys.meter.domain.torna.dto;

import com.cowave.sys.meter.domain.torna.bean.User;
import lombok.Data;

/**
 * @author thc
 */
@Data
public class UpdateDocFolderDTO {

    private Long id;
    private String name;
    private Long parentId;
    private User user;

}
