<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name: 'App',
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? this.$t(title) + "/" + this.$t('commons.menu.root') : this.$t('commons.menu.root')
      }
    }
  },
  created() {
    this.$store.dispatch('OpenNoticeSocket');
  },
  beforeDestroy() {
    this.$store.dispatch('CloseNoticeSocket');
  },
}
</script>
