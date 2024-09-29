<template>
  <el-dialog :title="title" :visible.sync="visible" width="75%" style="max-height: 90vh;">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px" size="mini" style="float: right; width: 65%">
      <el-form-item label="请假人" prop="applyUserName">
        <el-input v-model="form.applyUserName" placeholder="" clearable style="width: 80%" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="请假类型" prop="leaveType">
        <el-select v-model="form.leaveType" placeholder="" style="width: 80%" :disabled="task.complete">
          <el-option v-for="dict in dict.type.leave" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="beginTime">
        <el-date-picker v-model="form.beginTime" clearable style="width: 80%" :disabled="task.complete"
                        value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder=""/>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="form.endTime" clearable style="width: 80%" :disabled="task.complete"
                        value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder=""/>
      </el-form-item>
      <el-form-item label="请假原因" prop="reason">
        <el-input v-model="form.reason" placeholder="" type="textarea" clearable style="width: 80%" :disabled="task.complete"/>
      </el-form-item>
      <el-form-item v-if="!task.complete" label="审批人" prop="deptApprover">
        <el-select v-model="form.deptApprover" placeholder="" style="width: 80%">
          <el-option v-for="item in approverOptions" :key="item.userId" :label="item.userName" :value="item.userId"/>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="!task.complete" type="primary" @click="handleAgree">重新提交</el-button>
      <el-button v-if="!task.complete" type="danger" @click="handleReject">撤销</el-button>
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
import {taskComplete, taskRecords} from "@/api/flow/task";
import {editLeave, infoLeave} from "@/api/flow/leave";
import {getDeptCandidatesByCode} from "@/api/system/dept";
import {instanceJump} from "@/api/flow/manage/instance";
import {getUserCandidates} from "@/api/system/user";

export default {
  dicts: ['leave'],
  data() {
    return {
      visible: false,
      title: '',
      task: {},
      form: {},
      data: {},
      variables: {},
      taskRecords: [],
      // 审批人选项
      approverOptions: [],
      // 参数
      deptAapproveResult: true
    };
  },
  computed: {
    rules() {
      return {
        beginTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        reason: [
          { required: true, message: "请假原因不能为空", trigger: "blur" }
        ],
        deptApprover: [
          { required: true, message: "审批人不能为空", trigger: "blur" }
        ],
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
      infoLeave(id).then( response => {
        this.form = response.data;
        if(this.task.complete){
          this.form.deptComment = this.form.processVariables.deptComment;
          this.form.deptAapproveResult = this.form.processVariables.deptAapproveResult;
        }else{
          this.form.deptAapproveResult = true;
        }
        getUserCandidates().then(resp => {
          if (resp.data && resp.data.length > 0) {
            this.approverOptions = resp.data;
            this.form.deptApprover = resp.data[0].userId;
          }
        });
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
    /** 重新提交 */
    handleAgree(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.data.taskId = this.task.taskId;
          this.data.comment = "重新提交";
          this.variables.deptReapply = true;
          this.data.variables = this.variables;
          editLeave(this.form).then( resp => {
            taskComplete(this.data).then( response => {
              this.visible = false;
              this.$emit('ok');
              this.$modal.msgSuccess("请假已重新提交");
            });
          });
        }
      });
    },
    /** 撤销 */
    handleReject(){
      this.data.taskId = this.task.taskId;
      this.data.comment = "撤销";
      this.variables.deptReapply = false;
      this.data.variables = this.variables;
      taskComplete(this.data).then( response => {
        this.visible = false;
        this.$emit('ok');
        this.$modal.msgSuccess("请假已撤销");
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
