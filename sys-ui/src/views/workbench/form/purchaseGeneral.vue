<template>
  <el-dialog :title="title" :visible.sync="visible" width="65%" style="max-height: 80vh;">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px" size="mini" style="float: right; width: 65%">
      <el-form-item label="采购清单" prop="content">
        <el-input v-model="form.content" type="textarea" placeholder="" :rows="8" disabled="disabled" style="width: 80%"/>
      </el-form-item>
      <el-form-item label="总金额" prop="total">
        <el-input v-model="form.money" placeholder="" disabled="disabled" style="width: 80%"/>
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input v-model="form.applyUserName" placeholder="" clearable disabled="disabled" style="width: 80%"/>
      </el-form-item>
      <el-form-item label="部门意见" prop="manageComment">
        <el-input v-model="form.manageComment" type="textarea" placeholder="..." style="width: 80%" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="财务意见" prop="manageComment">
        <el-input v-model="form.financeComment" type="textarea" placeholder="..." style="width: 80%" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="审批结果">
        <el-radio-group v-model="generalResult" :disabled="task.complete">
          <el-radio :key="true" :label="true">同意</el-radio>
          <el-radio :key="false" :label="false">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审批意见" prop="financeComment">
        <el-input v-model="form.generalComment" type="textarea" placeholder="..." style="width: 80%" :disabled="task.complete"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="!task.complete" type="primary" @click="handleAgree">提交</el-button>
      <el-button v-if="!task.complete" @click="cancel">取消</el-button>
    </div>
    <div id="ibox-content" class="col-sm-4">
      <div id="vertical-timeline" class="vertical-container dark-timeline">
        <div v-for="(item, index) in taskRecords" :key="index" class="vertical-timeline-block">
          <div class="vertical-timeline-icon lazur-bg"><i class="fa fa-briefcase"></i></div>
          <div v-if="item.taskType === 'userTask'" class="vertical-timeline-content">
            <h4><b>{{ item.taskName }} </b> ({{ item.assigneeName }})</h4>
            <p>- {{ item.comment }}</p>
            <span class="vertical-date">开始: <small>{{ item.startTime }}</small></span><br>
            <span class="vertical-date">结束: <small>{{ item.endTime }}</small></span>
          </div>
          <div v-if="item.taskType === 'endEvent'" class="vertical-timeline-content">
            <h4><b>结束: </b><span class="vertical-date"><small>{{ item.endTime }}</small></span></h4>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {taskComplete, taskRecords} from "@/api/workbench/task";
import {infoLeave} from "@/api/workbench/leave";
import {getDeptUsersByCode} from "@/api/system/dept";
import {instanceJump} from "@/api/system/flow/instance";
import {infoPurchase} from "@/api/workbench/purchase";

export default {
  dicts: [],
  data() {
    return {
      visible: false,
      title: '',
      task: {},
      form: {},
      data: {},
      variables: {},
      taskRecords: [],
      // 参数
      generalResult: true
    };
  },
  computed: {
    rules() {
      return {

      };
    }
  },
  methods: {
    show(row) {
      this.title = row.processName + ' | ' + row.taskName;
      this.task = row;
      this.getInfo(row.businessKey);
      this.getRecords(row.taskId);
      this.visible = true;
    },
    getInfo(id) {
      infoPurchase(id).then( response => {
        this.form = response.data;
        this.form.manageComment = this.form.processVariables.manageComment;
        this.form.financeComment = this.form.processVariables.financeComment;
        if(this.task.complete){
          this.form.generalComment = this.form.processVariables.generalComment;
          this.form.generalResult = this.form.processVariables.generalResult;
        }else{
          this.form.generalResult = true;
        }
      });
    },
    getRecords(id) {
      taskRecords(id).then( response => {
        this.taskRecords = response.data;
      });
    },
    /** 取消 */
    cancel() {
      this.visible = false;
    },
    /** 提交 */
    handleAgree(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.data.taskId = this.task.taskId;
          if(this.generalResult){
            this.data.comment = "同意";
          }else{
            this.data.comment = "拒绝";
          }
          this.variables.generalComment = this.form.generalComment;
          this.variables.generalResult = this.generalResult;
          this.data.variables = this.variables;
          taskComplete(this.data).then( response => {
            this.visible = false;
            this.$emit('ok');
            if(this.generalResult){
              this.$modal.msgSuccess("采购已同意");
            }else{
              this.$modal.msgSuccess("采购已拒绝");
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped>
#vertical-timeline {
  position: relative;
  padding: 0;
  margin-bottom: 2em;
}

#vertical-timeline::before {
  content: '';
  position: absolute;
  top: 0;
  left: 18px;
  height: 100%;
  width: 4px;
  background: #f1f1f1;
}

.lazur-bg {
  background-color: #9cb1b2;
  color: #ffffff;
}

.col-sm-4 {
  width: 30%;
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}

.vertical-container {
  width: 90%;
  max-width: 1170px;
  margin: 0 auto;
}

.vertical-container::after {
  content: '';
  display: table;
  clear: both;
}

.vertical-timeline-content {
  float: right;
  width: 85%;
  padding: 1em;
  position: relative;
  background: white;
  border-radius: 0.25em;
}

#vertical-timeline.light-timeline:before {
  background: #e7eaec;
}

.dark-timeline .vertical-timeline-content:before {
  border-color: transparent #f5f5f5 transparent transparent;
}

.dark-timeline.center-orientation .vertical-timeline-content:before {
  border-color: transparent transparent transparent #f5f5f5;
}

.dark-timeline .vertical-timeline-block:nth-child(2n) .vertical-timeline-content:before,  .dark-timeline.center-orientation .vertical-timeline-block:nth-child(2n) .vertical-timeline-content:before {
  border-color: transparent #f5f5f5 transparent transparent;
}

.dark-timeline .vertical-timeline-content, .dark-timeline.center-orientation .vertical-timeline-content {
  background: #f5f5f5;
}

.vertical-timeline-block {
  position: relative;
  margin: 2em 0;
  height: 150px;
}

.vertical-timeline-block:after {
  content: "";
  display: table;
  clear: both;
}

.vertical-timeline-block:first-child {
  margin-top: 0;
}

.vertical-timeline-block:last-child {
  margin-bottom: 0;
}

.vertical-timeline-icon {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  font-size: 16px;
  border: 3px solid #f1f1f1;
  text-align: center;
}

.vertical-timeline-icon i {
  display: block;
  width: 24px;
  height: 24px;
  position: relative;
  left: 50%;
  top: 50%;
  margin-left: -12px;
  margin-top: -9px;
}

.vertical-timeline-content:after {
  content: "";
  display: table;
  clear: both;
}

.vertical-timeline-content h2 {
  font-weight: 400;
  margin-top: 4px;
}

.vertical-timeline-content p {
  margin: 0.5em 0;
  line-height: 1.5;
}

.vertical-timeline-content .vertical-date {
  font-weight: 400;
}

.vertical-date small {
  color: #1ab394;
  font-weight: 400;
}

.vertical-timeline-content::before {
  content: '';
  position: absolute;
  top: 16px;
  right: 100%;
  height: 0;
  width: 0;
  border: 7px solid transparent;
  border-right: 7px solid white;
}
</style>
