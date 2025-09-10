/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.rabc.vo;

import cn.hutool.core.lang.tree.TreeNodeConfig;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class DiagramNode {

    public static final TreeNodeConfig DIAGRAM_CONFIG = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    /**
     * id
     */
    private Integer id;

    /**
     * 上级id
     */
    private Integer pid;

    /**
     * 节点名称
     */
    private String label;

    public static DiagramNode newRootNode(String nodeName){
        DiagramNode rootNode = new DiagramNode();
		rootNode.setPid(-1);
		rootNode.setId(0);
		rootNode.setLabel(nodeName);
        return rootNode;
    }
}
