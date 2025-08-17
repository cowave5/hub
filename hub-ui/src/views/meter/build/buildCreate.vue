<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix steps">
        <el-steps :active="currentStep" finish-status="success" class="inline-steps">
          <el-step title="构建方式" @click.native="handleStepClick(0)"></el-step>
          <el-step title="基础信息" @click.native="handleStepClick(1)"></el-step>
          <el-step title="构建流程" @click.native="handleStepClick(2)"></el-step>
          <el-step title="发布操作" @click.native="handleStepClick(3)"></el-step>
          <el-step title="其它配置" @click.native="handleStepClick(4)"></el-step>
        </el-steps>
      </div>

    </el-card>
  </div>
</template>

<script>
export default {
  name: "BuildCreate",
  data() {
    return {
      currentStep: 1,

    }
  },
  methods: {
    handleStepClick(index){
      this.currentStep = index;
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-step.is-success ~ .el-step .el-step__line {
  background-color: #67c23a;
}

.steps {
  margin: 20px auto 0;

  ::v-deep .el-step {
    height: 100%;

    .el-step__line {
      background-color: rgba(0, 0, 0, 0.15);
      margin-right: 10px !important;
      margin-left: 30px !important;
    }

    .el-step__icon {
      font-size: 16px;
      border: 1px solid;

      .el-step__icon-inner {
        font-weight: unset !important;
      }
    }

    .el-step__head.is-success {
      color: #1890ff;
      border-color: #1890ff;
    }

    .is-success .el-step__icon.is-text {
      border: none;
      background-color: #d2ecec;
      color: #1890ff;
    }

    .is-process .el-step__icon.is-text {
      background: #1890ff;
      color: #fff;
    }

    .el-step__head.is-process {
      color: #1890ff;
      border-color: #1890ff;
    }

    .el-step__title.is-process {
      color: #1890ff;
    }

    .el-step__title.is-success {
      color: #2da56a;
    }
  }
}
</style>
