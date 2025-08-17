package com.cowave.sys.meter.infra.torna;

import com.cowave.sys.meter.domain.torna.dto.DocInfoDTO;

/**
 * @author thc
 */
public interface DocMd5Builder {

    /**
     * 生成文档md5
     *
     * @param docInfoDTO
     * @return
     */
    String buildMd5(DocInfoDTO docInfoDTO);

}
