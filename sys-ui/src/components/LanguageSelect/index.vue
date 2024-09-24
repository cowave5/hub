<template>
  <el-dropdown trigger="click" @command="handleLanguage">
    <div>
      <svg-icon icon-class="language"  />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="language===item.value" :command="item.value">
        {{ item.label }}
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  data() {
    return {
      sizeOptions: [
        { label: '中文', value: 'zh' },
        { label: 'English', value: 'en' }
      ]
    }
  },
  computed: {
    language() {
      return this.$i18n.locale
    }
  },
  methods: {
    handleLanguage(language) {
      this.$i18n.locale = language;
    }
  }
}
</script>
