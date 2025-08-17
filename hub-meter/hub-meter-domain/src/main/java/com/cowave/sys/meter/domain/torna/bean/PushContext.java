package com.cowave.sys.meter.domain.torna.bean;

import com.cowave.sys.meter.domain.torna.dto.DocInfoDTO;
import com.cowave.sys.meter.domain.torna.dto.DocMeta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PushContext {

    private String author;

    private List<DocMeta> docMetas;

    private List<DocInfoDTO> contentChangedDocs;
}
