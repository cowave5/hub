<template>
  <el-dialog title="日志详情" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="log" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="日志分类：">{{ log.groupName }}/{{ log.groupName }}/{{ log.actionName }}</el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="操作人：">{{ log.userName }}</el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="操作结果：">{{ log.logStatus === 1 ? '成功' : '失败'  }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="访问时间：">{{ log.logTime }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="访问ip：">{{ log.ip }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="访问路径：">{{ log.url }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="操作描述：">{{ log.logDesc }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="操作内容：">{{ log.logContent }} </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { info } from "@/api/monitor/operlog";
export default {
  data() {
    return {
      visible: false,
      log: {}
    };
  },
  methods: {
    show(id) {
      this.getDetail(id);
      this.visible = true;
    },
    getDetail(id) {
      info(id).then(res => {
        this.log = res.data;
      });
    },
  }
};
</script>
