<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'sys-ui',
  data() {
    return {
      url: 'https://gitlab.cowave.com/commons/demo/demo-sys/sys-ui'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
