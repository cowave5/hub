<template>
  <el-dialog :visible.sync="visible" width="900px" top="5vh">
    <template #title>
      <span class="el-dialog__title">操作日志权限：{{ info.scopeName }}</span>
    </template>
    <el-form ref="form" :model="form" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item>
            <el-radio-group v-model="form.scope">
              <el-radio label="personal">仅本人数据</el-radio>
              <el-radio label="dept">本部门数据</el-radio>
              <el-radio label="all">全部数据</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['hub:scope:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
  </el-dialog>
</template>
<script>
import {checkPermit} from "@/utils/permission";
import {updateScopeContent} from "@/api/system/scope";
export default {
  dicts: [],
  data() {
    return {
      visible: false,
      info: {},
      form: {}
    };
  },
  methods: {
    checkPermit,
    show(info) {
      this.info = info;
      this.form = info.scopeContent;
      this.visible = true;
    },
    cancel() {
      this.visible = false;
    },
    submitForm() {
      updateScopeContent(this.info.scopeId, this.form).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
        this.visible = false;
      });
    }
  }
};
</script>
