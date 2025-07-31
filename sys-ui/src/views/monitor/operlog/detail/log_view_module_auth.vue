<template>
  <el-dialog title="鉴权管理日志" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="info" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="操作人：">{{ info.access.accessUserName }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作时间：">{{ info.opTime }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="访问ip：">{{ info.ip }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作结果：">
            <template v-for="item in success_failed">
              <span v-if="info.opStatus === item.value">{{ $t(item.label) }}</span>
            </template>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="访问路径：">{{ info.url }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="操作描述：">{{ info.opDesc }} </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { success_failed } from '@/utils/constants';
export default {
  dicts: [],
  data() {
    return {
      success_failed: success_failed,
      visible: false,
      info: {}
    };
  },
  methods: {
    show(info) {
      this.info = info;
      this.visible = true;
    }
  }
};
</script>
