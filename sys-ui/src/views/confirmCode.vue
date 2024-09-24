<template>
  <div class="get-confirm-code">
    <el-button style="width: 100%" :disabled="buttondDisabled" @click="handleButtonClick" type="text">{{timeKeepingActive?`已发送 ${count}s`:'获取验证码'}}</el-button>
  </div>
</template>

<script>
export default {
  name: 'confirmCode',
  props: {
    seconds: {  //获取验证码按钮的秒数
      type: Number,
      default: 60,
    },
    timeKeeping: {    //计时
      type: Boolean,
      required: true,
    }
  },
  data () {
    return {
      timeKeepingActive: false,
      count: 0,
      buttondDisabled: false,
    }
  },
  methods: {
    handleButtonClick(){    //点击事件抛出给父组件
      this.$emit('getConfirmCodeClick')
    }
  },
  watch: {
    timeKeeping: {
      handler(newValue){
        if(newValue === true){    //false变为了true，启动计时器
          this.count = this.seconds    //秒数
          this.buttondDisabled = true    //按钮禁用
          this.timeKeepingActive = true    //界面秒数倒计时界面显示
          let t = setInterval(() => {
            if (this.count <= 0){
              clearTimeout(t)    //时间到了就清除计时器
              this.buttondDisabled = false    //按钮禁用
              this.timeKeepingActive = false    //界面秒数倒计时界面显示
              this.$emit('timeKeepingDone')    //抛出即使完成时间
            }
            this.count--;
          }, 1000)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.get-confirm-code {
  min-width: 70px;
}
</style>
