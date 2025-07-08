<template>
  <component :is="actualComponent" :key="tenantIndex" />
</template>

<script>
export default {
  name: 'DynamicIndex',
  data() {
    return {
      actualComponent: null
    };
  },
  computed: {
    tenantIndex() {
      return this.$store.getters.tenantIndex
    }
  },
  watch: {
    tenantIndex: {
      immediate: true,
      handler(newVal) {
        this.actualComponent = null;
        this.$nextTick(() => {
          this.actualComponent = require(`@/views/${newVal}.vue`).default;
        })
      }
    }
  }
};
</script>
