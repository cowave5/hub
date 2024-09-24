<template>
  <el-dialog :title="notice.noticeTitle" :visible.sync="visible" width="900px" top="5vh">
    <div style="position: absolute; left: 20px; top: 50px; color: #9b9595; font-size: small;">
      <template v-for="item in dict.type.notice_type">
        <span v-if="notice.noticeType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
        <span v-if="notice.noticeType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
      </template>
      /
      <template v-for="item in dict.type.notice_level">
        <span v-if="notice.noticeLevel === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
        <span v-if="notice.noticeLevel === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
      </template>
      <span style="padding-left: 40px;">创建人：{{ notice.createUserName }}</span>
      <span style="padding-left: 40px;">发布时间：{{ notice.publishTime }}</span>
      <span v-if="notice.noticeStatus > 0" style="padding-left: 40px;">已读统计：{{ notice.statRead }}/{{ notice.statTotal }}</span>
    </div>
    <editor v-model="notice.content" :min-height="80" :readOnly="true"/>
    <el-input v-model="readBack"  placeholder="反馈意见..." style="margin-top: 20px;">
      <el-button slot="append" type="primary" @click="handleReadBack">提交</el-button>
    </el-input>
  </el-dialog>
</template>
<script>
import {getNotice, msgBack} from "@/api/system/notice";
export default {
  dicts: ['notice_type', 'notice_level'],
  data() {
    return {
      visible: false,
      notice: {},
      noticeId: undefined,
      readBack: undefined
    };
  },
  methods: {
    show(row) {
      this.noticeId = row.noticeId;
      this.readBack = row.readBack;
      this.getDetail();
    },
    getDetail() {
      getNotice(this.noticeId).then(rsp => {
        this.notice = rsp.data;
        this.visible = true;
      });
    },
    handleReadBack(){
      msgBack(this.noticeId, this.readBack).then(rsp => {});
    }
  }
};
</script>

<style>
.el-input-group__append button.el-button {
  background-color: transparent;
  color: inherit;
  border: 0;
}

.el-input-group__append button.el-button:hover {
  background-color: #66b1ff;
  color: #fff;
  border: 0;
}

.editor, .ql-toolbar {
  white-space: pre-wrap !important;
  line-height: normal !important;
}
.quill-img {
  display: none;
}
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}
</style>
