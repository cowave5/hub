<template>
  <div
    class="contextmenuContainer"
    v-if="isShow"
    :style="{ left: left + 'px', top: top + 'px' }"
  >
    <template v-if="isHasActiveElements">
      <div
        class="item"
        :class="{ disabled: !canMoveLevel }"
        @click="exec('moveUp')"
      >
        上移一层
      </div>
      <div
        class="item"
        :class="{ disabled: !canMoveLevel }"
        @click="exec('moveDown')"
      >
        下移一层
      </div>
      <div
        class="item"
        :class="{ disabled: !canMoveLevel }"
        @click="exec('moveTop')"
      >
        置于顶层
      </div>
      <div
        class="item"
        :class="{ disabled: !canMoveLevel }"
        @click="exec('moveBottom')"
      >
        置于底层
      </div>
      <div class="splitLine"></div>
      <div class="item danger" @click="exec('del')">删除</div>
      <div class="item" @click="exec('copy')">复制</div>
      <div
        class="item"
        :class="{ disabled: groupStatus === 'disabled' }"
        @click="exec(groupStatus)"
      >
        {{ groupBtnText }}
      </div>
    </template>
    <template v-else>
      <div class="item" @click="exec('selectAll')">全部选中</div>
      <div class="item" @click="exec('backToCenter')">回到中心</div>
      <div class="item" @click="exec('fit')">显示全部</div>
      <div class="item" @click="exec('resetZoom')">重置缩放</div>
    </template>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

export default {
  props :{
    app: {
      type: Object
    }
  },
  data() {
    return {
      isShow: false,
      left: 0,
      top: 0,
      isHasActiveElements: false,
      canMoveLevel: false,
      groupStatus: 'disabled',
    }
  },
  watch: {
    groupStatus(newValue, oldValue) {
      switch (newValue) {
        case 'disabled':
          this.groupBtnText = '编组'
          break
        case 'dogroup':
          this.groupBtnText = '编组'
          break
        case 'ungroup':
          this.groupBtnText = '取消编组'
          break
        default:
          break
      }
    },
  },
  mounted() {
    this.init();
  },
  methods: {
    init(){
      this.app.on('contextmenu', this.show)
      document.body.addEventListener('click', this.hide)
    },
    hide() {
      this.isShow = false
      this.left = 0
      this.top = 0
    },
    exec(command) {
      switch (command) {
        case 'moveUp':
          this.app.moveUpCurrentElement()
          break
        case 'moveDown':
          this.app.moveDownCurrentElement()
          break
        case 'moveTop':
          this.app.moveTopCurrentElement()
          break
        case 'moveBottom':
          this.app.moveBottomCurrentElement()
          break
        case 'del':
          this.app.deleteCurrentElements()
          break
        case 'copy':
          this.app.copyPasteCurrentElements()
          break
        case 'selectAll':
          this.app.selectAll()
          break
        case 'backToCenter':
          this.app.scrollToCenter()
          break
        case 'fit':
          this.app.fit()
          break
        case 'resetZoom':
          this.app.setZoom(1)
        case 'dogroup':
          this.app.dogroup()
          break
        case 'ungroup':
          this.app.ungroup()
          break
        default:
          break
      }
    },
    show(e, activeElements) {
      this.isHasActiveElements = activeElements.length > 0
      this.canMoveLevel = activeElements.length === 1
      this.left = e.clientX + 10
      this.top = e.clientY + 10
      this.isShow = true
      this.handleGroup(activeElements)
    },
    handleGroup(activeElements) {
      let isGroup = true
      activeElements.forEach(item => {
        if (!item.hasGroup()) {
          isGroup = false
        }
      })
      if (isGroup) {
        this.groupStatus = 'ungroup'
      } else if (activeElements.length > 1) {
        this.groupStatus = 'dogroup'
      }
    },
  },
}


</script>

<style lang="scss" scoped>
.contextmenuContainer {
  position: fixed;
  width: 161px;
  background: #fff;
  box-shadow: 0 4px 12px 0 hsla(0, 0%, 69%, 0.5);
  border-radius: 4px;
  padding-top: 16px;
  padding-bottom: 16px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #1a1a1a;

  .splitLine {
    height: 1px;
    background-color: #f5f5f5;
    margin: 5px 0;
  }

  .item {
    height: 28px;
    line-height: 28px;
    padding-left: 16px;
    cursor: pointer;
    &.danger {
      color: #f56c6c;
    }
    &:hover {
      background: #f5f5f5;
    }
    &.disabled {
      color: grey;
      cursor: not-allowed;
      &:hover {
        background: #fff;
      }
    }
  }
}
</style>
