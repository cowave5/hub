<template>
  <el-tooltip content="系统消息" placement="top" trigger="click">
    <el-badge v-if="noticeCount > 0" :value="displayValue" :max="99" >
      <el-popover placement="bottom" :width="400" trigger="click">
        <template #reference>
          <svg-icon icon-class="notification"/>
        </template>
      </el-popover>
    </el-badge>
    <template v-else>
      <el-popover placement="bottom" :width="400" trigger="click">
        <template #reference>
          <svg-icon icon-class="notification" />
        </template>
      </el-popover>
    </template>
  </el-tooltip>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {

    }
  },
  computed: {
    ...mapGetters([
      'noticeCount'
    ]),
    displayValue() {
      return this.$store.state.noticeSocket.noticeCount > 99 ? '99+' : this.$store.state.noticeSocket.noticeCount
    }
  },
  methods: {

  }
}
</script>

<style >
.el-badge__content.is-fixed {
    position: absolute;
    top: 10px;
    right: 18px;
    -webkit-transform: translateY(-50%) translateX(100%);
    transform: translateY(-50%) translateX(100%);
}

.el-badge__content {
    background-color: #ff4949;
    border-radius: 10px;
    color: #FFFFFF;
    font-size: 12px;
    height: 18px;
    line-height: 18px;
    padding: 0 6px;
    text-align: center;
    white-space: nowrap;
    border: 1px solid #FFFFFF;
}
</style>
