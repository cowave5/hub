package com.cowave.sys.job.domain.constant;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum TaskTypeEnum implements EnumVal<Void> {

    /**
     * BEAN
     */
    BEAN("BEAN", false, null, null),

    /**
     * GROOVY
     */
    GROOVY("GROOVY", false, null, null),

    /**
     * SHELL
     */
    SHELL("SHELL", true, "bash", ".sh"),

    /**
     * PYTHON
     */
    PYTHON("PYTHON", true, "python", ".py"),

    /**
     * PHP
     */
    PHP("PHP", true, "php", ".php"),

    /**
     * NODEJS
     */
    NODEJS("NODEJS", true, "node", ".js"),

    /**
     * POWERSHELL
     */
    POWERSHELL("POWERSHELL", true, "powershell", ".ps1");

    private final String desc;
    private final boolean isScript;
    private final String cmd;
    private final String suffix;

    TaskTypeEnum(String desc, boolean isScript, String cmd, String suffix) {
        this.desc = desc;
        this.isScript = isScript;
        this.cmd = cmd;
        this.suffix = suffix;
    }

    public boolean isScript() {
        return isScript;
    }
}
