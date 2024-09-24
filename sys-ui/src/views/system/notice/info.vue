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

    <el-table v-if="notice.noticeStatus > 0" title="已读情况" :data="reads" :show-header="false" style="margin-top: 15px;">
      <el-table-column prop="userName"   align="left" width="160"/>
      <el-table-column prop="readStatus" align="left" width="60">
        <template slot-scope="{row: {readStatus}}">
          <span v-if="readStatus < 10">未读</span>
          <span v-if="readStatus >= 10">已读</span>
        </template>
      </el-table-column>
      <el-table-column prop="readTime" align="left" width="160"/>
      <el-table-column prop="readBack" align="left"/>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </el-dialog>
</template>
<script>
import {getNotice, getReads} from "@/api/system/notice";
export default {
  dicts: ['notice_type', 'notice_level'],
  data() {
    return {
      visible: false,
      notice: {},
      reads: [],
      noticeId: undefined,
      queryParams: {
        page: 1,
        pageSize: 10
      },
      total: 0,
    };
  },
  methods: {
    show(id) {
      this.noticeId = id;
      this.queryParams.noticeId = id;
      this.getDetail();
    },
    getDetail() {
      getNotice(this.noticeId).then(rsp => {
        this.notice = rsp.data;
        this.visible = true;
        if(this.notice.noticeStatus > 0){
          this.getList();
        }
      });
    },
    getList(){
      getReads(this.queryParams).then(resp => {
        this.reads = resp.data.list;
        this.total = resp.data.total;
      });
    }
  }
};
</script>

<style>
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
