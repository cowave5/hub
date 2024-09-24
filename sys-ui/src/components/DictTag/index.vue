<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value) && $i18n.locale==='zh'">
        <el-tag :disable-transitions="true" :key="item.value" :index="index"
          :type="item.raw.css === undefined ? 'primary' : item.raw.css">
          {{ item.label }}
        </el-tag>
      </template>
      <template v-if="values.includes(item.value) && $i18n.locale==='en'">
        <el-tag :disable-transitions="true" :key="item.value" :index="index"
                :type="item.raw.css === undefined ? 'primary' : item.raw.css">
          {{ item.labelEn }}
        </el-tag>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
  },
  computed: {
    values() {
      if (this.value !== null && typeof this.value !== 'undefined') {
        return Array.isArray(this.value) ? this.value : [this.value];
      } else {
        return [];
      }
    },
  },
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
