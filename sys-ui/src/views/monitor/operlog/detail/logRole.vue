<template>
  <el-dialog title="角色管理日志" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="log" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="操作人：">{{ log.userName }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作结果：">{{ log.logStatus === 1 ? '成功' : '失败'  }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="访问ip：">{{ log.ip }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="访问时间：">{{ log.logTime }}</el-form-item>
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
          <el-form-item v-if="log.actionCode === 'readonly'" label="操作内容：">
            <span v-if="log.logContent.req.readOnly === 1">设置角色[{{ log.logContent.req.roleName }}]为只读</span>
            <span v-if="log.logContent.req.readOnly === 0">取消角色[{{ log.logContent.req.roleName }}]为只读</span>
          </el-form-item>

          <el-form-item v-if="log.actionCode === 'add'" label="角色信息：">
            <el-form ref="form-add" :model="log.logContent.req" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="角色名称：">{{ log.logContent.req.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ log.logContent.req.roleCode }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ log.logContent.req.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>

          <el-form-item v-if="log.actionCode === 'edit'" label="修改前：">
            <el-form ref="form-before" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="角色名称：">{{ log.logContent.resp.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ log.logContent.resp.roleCode }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ log.logContent.resp.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>

          <el-form-item v-if="log.actionCode === 'edit'" label="修改后：">
            <el-form ref="form-after" :model="log.logContent.req" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="角色名称：">{{ log.logContent.req.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ log.logContent.req.roleCode }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ log.logContent.req.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="log.actionCode === 'delete'">
        <el-table :data="log.logContent.resp" :header-cell-style="{'text-align':'center'}">
          <el-table-column prop="roleName" align="center" label="角色名称"/>
          <el-table-column prop="roleCode" align="center" label="角色编码"/>
          <el-table-column prop="remark" align="center" label="备注"/>
        </el-table>
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
