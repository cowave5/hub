<template>
  <div class="top-right-btn">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? $t('commons.button.search_hide') : $t('commons.button.search_show')" placement="top">
        <el-button size="mini" circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" :content="$t('commons.button.refresh')" placement="top">
        <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" :content="$t('commons.button.select_column')" placement="top" v-if="cols">
        <el-button size="mini" circle icon="el-icon-menu" @click="showColumn()" />
      </el-tooltip>
    </el-row>
    <el-dialog v-drag :visible.sync="open" append-to-body>
      <el-transfer :titles="[$t('commons.button.show'), $t('commons.button.hide')]"
        v-model="value" :data="localizedColumns" @change="dataChange">
      </el-transfer>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "RightToolbar",
  data() {
    return {
      // 显隐数据
      value: [],
      // 是否显示弹出层
      open: false,
    };
  },
  computed: {
    localizedColumns() {
      if (this.cols) {
        return this.cols.map(item => ({
          ...item,
          label: this.$t(`${item.label}`)
        }));
      } else {
        return []
      }
    }
  },
  props: {
    showSearch: {
      type: Boolean,
      default: true,
    },
    cols: {
      type: Array,
    },
  },
  created() {
    // 显隐列初始默认隐藏列
    for (let item in this.cols) {
      if (this.cols[item].show === false) {
        this.value.push(parseInt(item));
      }
    }
  },
  methods: {
    // 搜索
    toggleSearch() {
      this.$emit("update:showSearch", !this.showSearch);
    },
    // 刷新
    refresh() {
      this.$emit("queryTable");
    },
    // 右侧列表元素变化
    dataChange(data) {
      for (let item in this.cols) {
        const key = this.cols[item].key;
        this.cols[item].show = !data.includes(key);
      }
    },
    // 打开显隐列dialog
    showColumn() {
      this.open = true;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-transfer__button {
  border-radius: 50%;
  padding: 12px;
  display: block;
  margin-left: 0px;
}
::v-deep .el-transfer__button:first-child {
  margin-bottom: 10px;
}
</style>
